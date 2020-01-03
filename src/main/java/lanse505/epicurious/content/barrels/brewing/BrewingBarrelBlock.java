package lanse505.epicurious.content.barrels.brewing;

import com.hrznstudio.titanium.api.IFactory;
import lanse505.epicurious.content.ModBlocks;
import lanse505.epicurious.content.barrels.BarrelBlockBase;
import lanse505.epicurious.content.barrels.BarrelTileBase;
import lanse505.epicurious.content.barrels.storage.StorageBarrelTile;
import lanse505.epicurious.utils.EpicuriousReferenceObjects;
import lanse505.epicurious.utils.configs.EpicuriousConfigs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.List;

public class BrewingBarrelBlock extends BarrelBlockBase<BrewingBarrelTile> {

    public BrewingBarrelBlock() {
        super(BrewingBarrelTile.class);
    }

    @Override
    public IFactory<BrewingBarrelTile> getTileEntityFactory() {
        return BrewingBarrelTile::new;
    }

    @Override
    public NonNullList<ItemStack> getDynamicDrops(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.get(SEALED)) {
            return NonNullList.create();
        } else {
            NonNullList<ItemStack> stacks = NonNullList.create();
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity != null) {
                tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent((iItemHandler) -> {
                    for (int i = 0; i < iItemHandler.getSlots(); ++i) {
                        stacks.add(iItemHandler.getStackInSlot(i));
                    }
                });
                stacks.add(new ItemStack(ModBlocks.storageBarrelBlock.asItem(), 1));
            }
            return stacks;
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        BarrelTileBase btb = getTE(worldIn, pos);
        if (btb instanceof BrewingBarrelTile) {
            BrewingBarrelTile brewingBarrelTile = (BrewingBarrelTile) btb;
            if (stack.getTag() != null) {
                brewingBarrelTile.getBlockState().with(SEALED, stack.getTag().getBoolean("Sealed"));
                brewingBarrelTile.getBrewable().readFromNBT(stack.getTag().getCompound("Input_Tank"));
                brewingBarrelTile.getStorage().deserializeNBT(stack.getTag().getCompound("BlockEntityTag"));
                brewingBarrelTile.getBrewed().readFromNBT(stack.getTag().getCompound("Output_Tank"));
            } else {
                state.with(SEALED, false);
            }
        }
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        BarrelTileBase tileEntity = getTE(worldIn, pos);
        if (tileEntity instanceof BrewingBarrelTile) {
            BrewingBarrelTile brewingBarrelTile = (BrewingBarrelTile) tileEntity;
            if (state.get(SEALED)) {
                ItemStack stack = new ItemStack(ModBlocks.storageBarrelBlock.asItem(), 1);
                CompoundNBT compoundNBT = new CompoundNBT();
                compoundNBT.putBoolean("Sealed", state.get(SEALED));

                CompoundNBT input_tank = new CompoundNBT();
                brewingBarrelTile.getBrewable().writeToNBT(input_tank);
                compoundNBT.put("Input_Tank", input_tank);

                compoundNBT.put("BlockEntityTag", brewingBarrelTile.getStorage().serializeNBT());

                CompoundNBT output_tank = new CompoundNBT();
                brewingBarrelTile.getBrewable().writeToNBT(output_tank);
                compoundNBT.put("Output_Tank", output_tank);

                stack.setTag(compoundNBT);

                ItemEntity entity = new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
                entity.setDefaultPickupDelay();
                worldIn.addEntity(entity);
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

        if (barrelTileBase instanceof BrewingBarrelTile) {
            BrewingBarrelTile brewingBarrelTile = (BrewingBarrelTile) barrelTileBase;
            ItemStack held = player.getHeldItem(hand);
            if (!state.get(SEALED)) {
                if (held.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).isPresent()) {
                    FluidUtil.interactWithFluidHandler(player, hand, worldIn, pos, null);
                    brewingBarrelTile.markDirty();
                    worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 8);
                    return true;
                }

                if (held.getItem() == EpicuriousReferenceObjects.spongeBlock.asItem()) {
                    ItemStack stack = player.getHeldItem(hand);
                    brewingBarrelTile.getBrewable().setFluid(FluidStack.EMPTY);
                    brewingBarrelTile.getBrewed().setFluid(FluidStack.EMPTY);
                    if (!player.isCreative()) {
                        stack.shrink(1);
                        player.inventory.addItemStackToInventory(new ItemStack(EpicuriousReferenceObjects.wetSpongeBlock, 1));
                    }
                    brewingBarrelTile.markDirty();
                    worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 8);
                    return true;
                }
            }
            brewingBarrelTile.openGui(player);
            return true;
        }
        return false;
    }

    @Override
    public boolean isToolEffective(BlockState state, ToolType tool) {
        return tool.equals(ToolType.AXE);
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
        if (stack != null && stack.hasTag()) {
            long handle = Minecraft.getInstance().mainWindow.getHandle();
            CompoundNBT compound = stack.getTag();

            if (InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_LEFT_SHIFT)) {
                if (stack.hasTag()) {
                    String state = String.valueOf(stack.getTag().getBoolean("Sealed")).replace("t", "T");
                    tooltip.add(new TranslationTextComponent("info.epicurious.state.sealed").applyTextStyle(TextFormatting.GRAY).appendSibling(new StringTextComponent(" " + state).applyTextStyle(stack.getTag().getBoolean("Sealed") ? TextFormatting.GREEN : TextFormatting.RED)));
                }
            } else {
                tooltip.add(new TranslationTextComponent("info.epicurious.shift"));
            }

            if (InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_LEFT_SHIFT)) {
                if (compound != null && (!compound.getCompound("Input_Tank").isEmpty() || !compound.getCompound("Output_Tank").isEmpty())) {
                    FluidTank fluidTank = new FluidTank(EpicuriousConfigs.getInstance().getBarrels().getStorage().storageTankSize.get());
                    FluidStack inputFS = fluidTank.readFromNBT(compound.getCompound("Tank")).getFluid();
                    FluidStack outputFS = fluidTank.readFromNBT(compound.getCompound("Tank")).getFluid();
                    if (InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_LEFT_CONTROL)) {
                        if (!inputFS.isEmpty()) {
                            tooltip.add(new TranslationTextComponent("info.epicurious.barrel.input").applyTextStyle(TextFormatting.GRAY));
                            tooltip.add(new StringTextComponent(" ").appendSibling(inputFS.getDisplayName().applyTextStyle(TextFormatting.AQUA).applyTextStyle(TextFormatting.ITALIC).appendSibling(new StringTextComponent(" ").applyTextStyle(TextFormatting.GRAY)).appendSibling(new StringTextComponent(inputFS.getAmount() + "mB").applyTextStyle(TextFormatting.WHITE))));
                        }
                        if (!outputFS.isEmpty()) {
                            tooltip.add(new TranslationTextComponent("info.epicurious.barrel.output").applyTextStyle(TextFormatting.GRAY));
                            tooltip.add(new StringTextComponent(" ").appendSibling(outputFS.getDisplayName().applyTextStyle(TextFormatting.AQUA).applyTextStyle(TextFormatting.ITALIC).appendSibling(new StringTextComponent(" ").applyTextStyle(TextFormatting.GRAY)).appendSibling(new StringTextComponent(outputFS.getAmount() + "mB").applyTextStyle(TextFormatting.WHITE))));
                        }
                    } else if (!InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_LEFT_CONTROL)) {
                        tooltip.add(new TranslationTextComponent("info.epicurious.shiftl.ctrll").applyTextStyle(TextFormatting.GRAY));
                    }
                }

                CompoundNBT tag = stack.getTag() != null ? stack.getTag().getCompound("BlockEntityTag") : new CompoundNBT();
                if (tag.getCompound("Items").isEmpty()) {
                    if (InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_RIGHT_CONTROL)) {
                        int c = 0;
                        int d = 0;

                        NonNullList<ItemStack> nonNullList = NonNullList.withSize(9, ItemStack.EMPTY);
                        ItemStackHelper.loadAllItems(tag, nonNullList);

                        tooltip.add(new TranslationTextComponent("info.epicurious.barrel.inventory").applyTextStyle(TextFormatting.GRAY));

                        for (ItemStack itemstack : nonNullList) {
                            if (!itemstack.isEmpty()) {
                                ++d;
                                if (c <= 4) {
                                    ++c;
                                    tooltip.add(new StringTextComponent(" " + itemstack.getCount() + " ").applyTextStyle(TextFormatting.WHITE).appendSibling(itemstack.getDisplayName().applyTextStyle(TextFormatting.BLUE).applyTextStyle(TextFormatting.ITALIC)));
                                }
                            }
                        }
                        if (d - c > 0) {
                            tooltip.add(new StringTextComponent(" ")
                                    .applyTextStyle(TextFormatting.ITALIC)
                                    .appendSibling(new TranslationTextComponent("container.shulkerBox.more", d - c)));
                        }
                    } else if (!InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_RIGHT_CONTROL)) {
                        tooltip.add(new StringTextComponent("Press Shift + R-Ctrl for Item Information").applyTextStyle(TextFormatting.GRAY));
                    }
                }
            } else if (!tooltip.contains(new TranslationTextComponent("info.epicurious.shift"))) {
                tooltip.add(new TranslationTextComponent("info.epicurious.shift").applyTextStyle(TextFormatting.GRAY));
            }
        }
    }
}
