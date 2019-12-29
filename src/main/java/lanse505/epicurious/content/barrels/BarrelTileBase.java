package lanse505.epicurious.content.barrels;

import com.hrznstudio.titanium.block.BlockTileBase;
import com.hrznstudio.titanium.block.tile.TileActive;
import net.minecraft.block.BlockState;

public class BarrelTileBase extends TileActive {
    protected boolean poweredLastTick = false;

    public BarrelTileBase(BlockTileBase base) {
        super(base);
    }

    @Override
    public void tick() {
        if (world.isRemote) {
            return;
        }

        boolean powered = world.isBlockPowered(pos);
        if (poweredLastTick != powered) {
            updateSealedState(powered);
        }
        poweredLastTick = powered;
    }

    public boolean isSealed() {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BarrelBlockBase) {
            return state.get(BarrelBlockBase.SEALED);
        }
        return false;
    }

    public void updateSealedState(boolean sealed) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BarrelBlockBase) {
            world.setBlockState(pos, state.with(BarrelBlockBase.SEALED, sealed));
        }
    }
}
