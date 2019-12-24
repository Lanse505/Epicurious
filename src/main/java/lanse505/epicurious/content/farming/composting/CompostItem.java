package lanse505.epicurious.content.farming.composting;

import lanse505.epicurious.Epicurious;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.eventbus.api.Event;

public class CompostItem extends Item {
    public CompostItem() {
        super(new Item.Properties().group(Epicurious.epicurious));
    }

    /**
     * Credit goes out to Natura for this piece of code.
     * The project is licences under CC0 which is a public domain licence.
     * Regardless I will always attribute great code to their creators.
     * So credit where credit is due!
     */

    private static boolean applyBonemeal(ItemStack stack, World worldIn, BlockPos target, PlayerEntity player, Hand hand) {
        BlockState iblockstate = worldIn.getBlockState(target);
        BonemealEvent event = new BonemealEvent(player, worldIn, target, iblockstate, stack);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            return false;
        }
        if (event.getResult() == Event.Result.ALLOW) {
            return true;
        }
        if (iblockstate.getBlock() instanceof IGrowable) {
            IGrowable igrowable = (IGrowable) iblockstate.getBlock();
            if (igrowable.canGrow(worldIn, target, iblockstate, worldIn.isRemote)) {
                if (!worldIn.isRemote) {
                    if (igrowable.canUseBonemeal(worldIn, worldIn.rand, target, iblockstate)) {
                        igrowable.grow(worldIn, worldIn.rand, target, iblockstate);
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        Direction direction = context.getFace();
        PlayerEntity player = context.getPlayer();
        Hand hand = context.getHand();
        BlockPos pos = context.getPos();
        World world = context.getWorld();

        if (direction != Direction.UP) {
            return ActionResultType.FAIL;
        } else {
            ItemStack itemstack = player.getHeldItem(hand);
            BlockPos.MutableBlockPos mutableblockpos = new BlockPos.MutableBlockPos();
            int posY = pos.getY();
            boolean planted = false;

            for (int posX = pos.getX() - 1; posX <= pos.getX() + 1; posX++) {
                for (int posZ = pos.getZ() - 1; posZ <= pos.getZ() + 1; posZ++) {
                    BlockPos position = mutableblockpos.setPos(posX, posY, posZ);
                    if (player.canPlayerEdit(position, direction, itemstack) && player.canPlayerEdit(position.up(), direction, itemstack)) {
                        if (applyBonemeal(itemstack, world, position, player, hand)) {
                            planted = true;
                            if (!world.isRemote) {
                                world.playEvent(2005, position, 0);
                            }
                        }
                    }
                }
            }
            if (planted) {
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
                if (itemstack.getCount() < 1) {
                    ForgeEventFactory.onPlayerDestroyItem(player, itemstack, hand);
                    player.setHeldItem(hand, ItemStack.EMPTY);
                }
                return ActionResultType.SUCCESS;
            }
            return ActionResultType.PASS;
        }
    }
}
