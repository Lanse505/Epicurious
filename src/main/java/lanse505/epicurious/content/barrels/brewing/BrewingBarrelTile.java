package lanse505.epicurious.content.barrels.brewing;

import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.IItemStackQuery;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.block.BlockTileBase;
import com.hrznstudio.titanium.block.tile.TileActive;
import com.hrznstudio.titanium.block.tile.button.PosButton;
import com.hrznstudio.titanium.block.tile.fluid.PosFluidTank;
import com.hrznstudio.titanium.block.tile.inventory.PosInvHandler;
import com.hrznstudio.titanium.block.tile.inventory.SidedInvHandler;
import com.hrznstudio.titanium.block.tile.progress.PosProgressBar;
import com.hrznstudio.titanium.client.gui.addon.StateButtonAddon;
import com.hrznstudio.titanium.client.gui.addon.StateButtonInfo;
import lanse505.epicurious.Epicurious;
import lanse505.epicurious.content.ModBlocks;
import lanse505.epicurious.content.barrels.BarrelBlockBase;
import lanse505.epicurious.content.barrels.BarrelTileBase;
import lanse505.epicurious.core.network.BarrelButtonMessage;
import lanse505.epicurious.core.recipes.barrels.BrewingSerializableRecipe;
import lanse505.epicurious.utils.configs.EpicuriousConfigs;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.Collections;
import java.util.List;

public class BrewingBarrelTile extends BarrelTileBase {
    protected BrewingSerializableRecipe lastRecipe;
    @Save
    private PosFluidTank brewable;
    @Save
    private SidedInvHandler storage;
    @Save
    private PosFluidTank brewed;
    @Save
    private PosProgressBar bar;
    private PosButton button;

    public BrewingBarrelTile() {
        super(ModBlocks.brewingBarrelBlock);
        this.addTank(brewable = new PosFluidTank("brewable", EpicuriousConfigs.getInstance().getBarrels().getBrewing().brewingInputSize.get(), 0, 0).setTankAction(PosFluidTank.Action.FILL));
        this.addInventory(storage = (SidedInvHandler) new SidedInvHandler("storage", 5, 5, 3, 0).setTile(this).setRange(3, 1).setInputFilter((itemStack, integer) -> {
            if (!isSealed()) {
                return IItemStackQuery.ANYTHING.test(itemStack);
            }
            return false;
        }));
        this.addTank(brewed = new PosFluidTank("brewed", EpicuriousConfigs.getInstance().getBarrels().getBrewing().brewingOutputSize.get(), 10, 10).setTankAction(PosFluidTank.Action.DRAIN));
        this.addButton(button = new PosButton(15, 15, 14, 14) {
            @Override
            public List<IFactory<? extends IGuiAddon>> getGuiAddons() {
                //TODO: Add proper asset                                           //TODO: Add Proper Asset
                return Collections.singletonList(() -> new StateButtonAddon(button, new StateButtonInfo(0, AssetTypes.BUTTON_SIDENESS_DISABLED), new StateButtonInfo(1, AssetTypes.BUTTON_SIDENESS_ENABLED)) {
                    @Override
                    public int getState() {
                        return isSealed() ? 1 : 0;
                    }
                });
            }
        }.setId(0).setPredicate((playerEntity, compoundNBT) -> {
            this.updateSealedState(!this.getBlockState().get(BarrelBlockBase.SEALED));
            if (world != null && world.isRemote) {
                Epicurious.NETWORK.sendToNearby(world, pos, 1, new BarrelButtonMessage(pos, isSealed() ? 1 : 0));
            }
            markForUpdate();
        }));
        this.addProgressBar(
                bar = new PosProgressBar(20, 20, 0)
                .setCanIncrease(tileEntity -> lastRecipe != null && isSealed() && isRecipeValidForContents())
                .setBarDirection(PosProgressBar.BarDirection.HORIZONTAL_RIGHT)
                .setColor(DyeColor.LIGHT_BLUE)
                .setOnFinishWork(this::finishBrewing)
        );
        storage.setColor(DyeColor.BROWN);
    }

    @Override
    public void tick() {
        processBrewing();
    }

    protected void processBrewing() {
        checkRecipe();
        setBarValues();
        bar.tickBar();
    }

    private void checkRecipe() {
        if (lastRecipe == null || !isRecipeValidForContents()) {
            lastRecipe = this.world.getRecipeManager().getRecipes()
                            .stream().filter(iRecipe -> iRecipe.getType() == BrewingSerializableRecipe.SERIALIZER.getRecipeType())
                            .map(iRecipe -> (BrewingSerializableRecipe) iRecipe).filter(recipe -> recipe.isValid(brewable.getFluid(), storage)).findFirst().orElse(null);
        }
    }

    private void setBarValues() {
        if (lastRecipe != null && bar.getMaxProgress() != lastRecipe.ticks) {
            bar.setMaxProgress(lastRecipe.ticks);
        }
    }

    private void finishBrewing() {
        if (lastRecipe != null) {
            brewable.drain(lastRecipe.brewableFluid, IFluidHandler.FluidAction.EXECUTE);
            for (int i = 0; i < storage.getSlots(); i++) {
                ItemStack stack = storage.getStackInSlot(i);
                stack.shrink(1);
            }
            brewed.fill(lastRecipe.brewedFluid, IFluidHandler.FluidAction.EXECUTE);
        }
    }

    public boolean isRecipeValidForContents() {
        return lastRecipe.isValid(brewable.getFluid(), storage);
    }

    public PosFluidTank getBrewable() {
        return brewable;
    }

    public SidedInvHandler getStorage() {
        return storage;
    }

    public PosFluidTank getBrewed() {
        return brewed;
    }
}
