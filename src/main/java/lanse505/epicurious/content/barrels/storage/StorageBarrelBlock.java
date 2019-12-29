package lanse505.epicurious.content.barrels.storage;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.tile.fluid.PosFluidTank;
import lanse505.epicurious.Epicurious;
import lanse505.epicurious.content.ModBlocks;
import lanse505.epicurious.content.barrels.BarrelBlockBase;
import lanse505.epicurious.content.barrels.BarrelTileBase;
import lanse505.epicurious.utils.EpicuriousReferenceObjects;
import lanse505.epicurious.utils.configs.EpicuriousConfigs;
import net.java.games.input.Controller;
import net.java.games.input.Keyboard;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ClockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.fixes.LWJGL3KeyOptions;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.List;

public class StorageBarrelBlock extends BarrelBlockBase<StorageBarrelTile> {

    public StorageBarrelBlock() {
        super(StorageBarrelTile.class);
    }

    @Override
    public IFactory<StorageBarrelTile> getTileEntityFactory() {
        return StorageBarrelTile::new;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof StorageBarrelTile) {
            StorageBarrelTile storageBarrelTile = (StorageBarrelTile) te;
            if (stack.getTag() != null) {
                storageBarrelTile.getBlockState().with(SEALED, stack.getTag().getBoolean("Sealed"));
                storageBarrelTile.getStorage().deserializeNBT(stack.getTag().getCompound("BlockEntityTag").getCompound("items"));
                storageBarrelTile.getTank().readFromNBT(stack.getTag());
            } else {
                state.with(SEALED, false);
            }
        }
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        TileEntity tileEntity = getTE(worldIn, pos);
        if (tileEntity instanceof StorageBarrelTile) {
            StorageBarrelTile storageBarrelTile = (StorageBarrelTile) tileEntity;
            if (state.get(SEALED)) {
                ItemStack stack = new ItemStack(ModBlocks.storageBarrelBlock.asItem(), 1);
                CompoundNBT compoundNBT = new CompoundNBT();
                compoundNBT.put("BlockEntityTag", storageBarrelTile.getStorage().serializeNBT());
                compoundNBT.putBoolean("Sealed", state.get(SEALED));

                CompoundNBT tank = new CompoundNBT();
                storageBarrelTile.getTank().writeToNBT(tank);
                compoundNBT.put("Tank", tank);

                stack.setTag(compoundNBT);

                ItemEntity entity = new ItemEntity(worldIn, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), stack);
                entity.setDefaultPickupDelay();
                worldIn.addEntity(entity);
                return;
            } else {
                for (int i = 0; i < storageBarrelTile.getStorage().getSlots(); i++) {
                    ItemStack stacks = storageBarrelTile.getStorage().getStackInSlot(i);
                    player.inventory.addItemStackToInventory(stacks);
                }
                return;
            }
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult ray) {
        BarrelTileBase barrelTileBase = getTE(worldIn, pos);
        if (worldIn.isRemote || barrelTileBase == null) {
            return true;
        }

        if (barrelTileBase instanceof StorageBarrelTile) {
            StorageBarrelTile storageBarrelTile = (StorageBarrelTile) barrelTileBase;
            ItemStack held = player.getHeldItem(hand);
            if (!state.get(SEALED)) {
                if (held.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).isPresent()) {
                    FluidUtil.interactWithFluidHandler(player, hand, worldIn, pos, null);
                    storageBarrelTile.markDirty();
                    worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 8);
                    return true;
                }

                if (held.getItem() == EpicuriousReferenceObjects.spongeBlock.asItem()) {
                    ItemStack stack = player.getHeldItem(hand);
                    storageBarrelTile.getTank().setFluid(FluidStack.EMPTY);
                    if (!player.isCreative()) {
                        stack.shrink(1);
                        player.inventory.addItemStackToInventory(new ItemStack(EpicuriousReferenceObjects.wetSpongeBlock, 1));
                    }
                    storageBarrelTile.markDirty();
                    worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 8);
                    return true;
                }
            }
            storageBarrelTile.openGui(player);
            return true;
        }
        return false;
    }

    @Override
    public boolean isToolEffective(BlockState state, ToolType tool) {
        return tool.equals(ToolType.AXE);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(SEALED);
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isSolid(BlockState state) {
        return false;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        long handle = Minecraft.getInstance().mainWindow.getHandle();
        if (InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_LEFT_SHIFT)) {
            if (stack.hasTag()) {
                tooltip.add(new TranslationTextComponent("info.epicurious.state.sealed")
                        .applyTextStyle(TextFormatting.GRAY)
                        .appendText(String.valueOf(stack.getTag().getBoolean("Sealed")))
                        .applyTextStyle(TextFormatting.WHITE));
                //tooltip.add(new StringTextComponent(stack.getTag().toString()));
            }
        } else {
            tooltip.add(new TranslationTextComponent("info.epicurious.shift"));
        }

        if (stack != null && stack.hasTag()) {
            CompoundNBT compound = stack.getTag();
            CompoundNBT tag = stack.getChildTag("BlockEntityTag");
            if (InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_LEFT_SHIFT)) {
                if (compound != null && !compound.getCompound("Tank").isEmpty()) {
                    FluidTank fluidTank = new FluidTank(EpicuriousConfigs.getInstance().getBarrels().getStorage().storageTankSize.get());
                    FluidStack fluidStack = fluidTank.readFromNBT(compound.getCompound("Tank")).getFluid();
                    if (!fluidStack.isEmpty()) {
                        if (InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_LEFT_CONTROL)) {
                            tooltip.add(new TranslationTextComponent("info.epicurious.barrel.input")
                                    .applyTextStyle(TextFormatting.GRAY)
                                    .appendText(fluidStack.getDisplayName() + ": " + fluidStack.getAmount())
                                    .applyTextStyle(TextFormatting.WHITE));
                        } else if (!InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_LEFT_CONTROL)) {
                            tooltip.add(new TranslationTextComponent("info.epicurious.shiftl.ctrll").applyTextStyle(TextFormatting.GRAY));
                        }
                    }
                }

                if (compound != null && !tag.getCompound("Items").isEmpty()) {
                    if (InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_RIGHT_CONTROL)) {
                        int c = 0;
                        int d = 0;

                        NonNullList<ItemStack> nonNullList = NonNullList.withSize(9, ItemStack.EMPTY);
                        ItemStackHelper.loadAllItems(tag.getCompound("Items"), nonNullList);

                        tooltip.add(new TranslationTextComponent("info.epicurious.barrel.inventory").applyTextStyle(TextFormatting.GRAY));

                        for (ItemStack itemstack : nonNullList) {
                            if (!itemstack.isEmpty()) {
                                ++d;
                                if (c <= 4) {
                                    ++c;
                                    tooltip.add(new StringTextComponent(String.format(" %dx %s", itemstack.getCount(), itemstack.getDisplayName()) + TextFormatting.WHITE));
                                }
                            }
                        }
                        if (d - c > 0) {
                            tooltip.add(new StringTextComponent(" ")
                                    .applyTextStyle(TextFormatting.ITALIC)
                                    .appendSibling(new TranslationTextComponent("container.shulkerBox.more", c-d)));
                        }
                    } else if (!InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_RIGHT_CONTROL)) {
                        tooltip.add(new StringTextComponent(TextFormatting.GRAY + "Press Shift + R-Ctrl for Item Information"));
                    }
                }
            } else if (!tooltip.contains(new TranslationTextComponent("info.epicurious.shift"))) {
                tooltip.add(new TranslationTextComponent("info.epicurious.shift").applyTextStyle(TextFormatting.GRAY));
            }
        }
    }
}
