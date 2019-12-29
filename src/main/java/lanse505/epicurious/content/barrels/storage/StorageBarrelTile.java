package lanse505.epicurious.content.barrels.storage;

import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.IItemStackQuery;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.block.tile.button.PosButton;
import com.hrznstudio.titanium.block.tile.fluid.PosFluidTank;
import com.hrznstudio.titanium.block.tile.inventory.SidedInvHandler;
import com.hrznstudio.titanium.client.gui.addon.StateButtonAddon;
import com.hrznstudio.titanium.client.gui.addon.StateButtonInfo;
import lanse505.epicurious.Epicurious;
import lanse505.epicurious.content.ModBlocks;
import lanse505.epicurious.content.barrels.BarrelBlockBase;
import lanse505.epicurious.content.barrels.BarrelTileBase;
import lanse505.epicurious.core.network.BarrelButtonMessage;
import lanse505.epicurious.core.recipes.barrels.BiomeRainSerializableRecipe;
import lanse505.epicurious.utils.configs.EpicuriousConfigs;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.List;

public class StorageBarrelTile extends BarrelTileBase {
    private BiomeRainSerializableRecipe lastRecipe;

    @Save
    private SidedInvHandler storage;
    @Save
    private PosFluidTank tank;
    private PosButton button;

    public StorageBarrelTile() {
        super(ModBlocks.storageBarrelBlock);
        this.addInventory(storage = (SidedInvHandler) new SidedInvHandler("item_storage", 62, 25, 9, 0).setTile(this).setRange(3, 3).setInputFilter((itemStack, integer) -> {
            if (!isSealed()) {
                return IItemStackQuery.ANYTHING.test(itemStack);
            }
            return false;
        }));
        this.addTank(tank = new PosFluidTank("fluid_storage", 32000, 133, 23).setTile(this));
        this.addButton(button = new PosButton(-13, 1, 14, 14) {
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
        storage.setColor(DyeColor.BROWN);
    }

    @Override
    public void tick() {
        if (!this.isSealed()) {
            processRaining();
            this.markDirty();
        }
    }

    protected void processRaining() {
        Biome biome = this.world.getBiome(pos);
        if (!isSealed()) {
            if (EpicuriousConfigs.getInstance().getBarrels().canBarrelsFillWhileRaining.get() && world.isRaining() && world.canBlockSeeSky(pos)) {
                FluidStack fluidStack = new FluidStack(ForgeRegistries.FLUIDS.getValue(new ResourceLocation("minecraft:water")), 5);
                if (lastRecipe == null || !lastRecipe.isValid(biome)) {
                    lastRecipe = this.getWorld().getRecipeManager().getRecipes()
                            .stream().filter(iRecipe -> iRecipe.getType() == BiomeRainSerializableRecipe.SERIALIZER.getRecipeType())
                            .map(iRecipe -> (BiomeRainSerializableRecipe) iRecipe).filter(biomeRainSerializableRecipe -> biomeRainSerializableRecipe.isValid(biome)).findFirst().orElse(null);
                }
                if (EpicuriousConfigs.getInstance().getBarrels().shouldBarrelsRespectRainValueOfBiomes.get()) {
                    if (!biome.getPrecipitation().equals(Biome.RainType.NONE)) {
                        if (lastRecipe != null) {
                            fluidStack = lastRecipe.fluidStack.copy();
                        }
                        tank.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                    }
                } else {
                    if (lastRecipe != null) {
                        fluidStack = lastRecipe.fluidStack.copy();
                    }
                    tank.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                }
            }
        }
    }

    public SidedInvHandler getStorage() {
        return storage;
    }

    public PosFluidTank getTank() {
        return tank;
    }
}
