package lanse505.epicurious.core.network;

import com.hrznstudio.titanium.network.Message;
import lanse505.epicurious.Epicurious;
import lanse505.epicurious.content.barrels.storage.StorageBarrelTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class BarrelButtonMessage extends Message {
    public BlockPos pos;
    public int id;

    public BarrelButtonMessage(){}

    public BarrelButtonMessage(BlockPos pos, int id) {
        this.pos = pos;
        this.id = id;
    }

    @Override
    protected void handleMessage(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            TileEntity te = context.getSender().getServerWorld().getTileEntity(pos);
            if (te instanceof StorageBarrelTile) {
                StorageBarrelTile storageBarrelTile = (StorageBarrelTile) te;
                storageBarrelTile.updateSealedState(id != 0);
            }
        });
    }
}
