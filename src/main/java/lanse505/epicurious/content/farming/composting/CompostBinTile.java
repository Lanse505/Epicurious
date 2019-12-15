package lanse505.epicurious.content.farming.composting;

import com.hrznstudio.titanium.block.tile.TileBase;
import lanse505.epicurious.content.ModBlocks;
import lanse505.epicurious.content.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.math.MathHelper;

public class CompostBinTile extends TileBase {

    private int compost;

    public CompostBinTile() {
        super(ModBlocks.compostBinBlock);
    }

    public void addCompost(PlayerEntity playerEntity, ItemStack stack) {
        if ((world != null ? world.getBlockState(getPos()).getBlock() : null) instanceof CompostBinBlock) {
            //Implement Capability and Check against Cap for Compost "Fill Value"
            //if (stack.getCapability().isPresent()) {
            //
            //}

            if (compost >= 100) {
                compost -= 100;
                playerEntity.addItemStackToInventory(new ItemStack(ModItems.compostItem, MathHelper.getInt(world.rand.toString(), 1, 4)));
                world.notifyBlockUpdate(getPos(), getBlockState(), getBlockState(), 2);
            }
        }
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT compound = new CompoundNBT();
        compound.putInt("compost", compost);
        return new SUpdateTileEntityPacket(getPos(), 1, compound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT compound = pkt.getNbtCompound();
        compost = compound.getInt("compost");
    }

    public int getCompost() {
        return compost;
    }
}
