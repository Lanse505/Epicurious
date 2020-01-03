package lanse505.epicurious.content.barrels;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BlockTileBase;
import lanse505.epicurious.Epicurious;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BarrelBlockBase<T extends BarrelTileBase> extends BlockTileBase<T> {
    public static final BooleanProperty SEALED = BooleanProperty.create("sealed");

    public BarrelBlockBase(Class<T> tileClass) {
        super(Block.Properties.create(Material.WOOD).sound(SoundType.WOOD).variableOpacity(), tileClass);
        setItemGroup(Epicurious.epicurious);
    }

    public static BarrelTileBase getTE(World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof BarrelTileBase) {
            return (BarrelTileBase) te;
        }
        return null;
    }

    @Override
    public IFactory<T> getTileEntityFactory() {
        return null;
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
    public BlockState getStateForPlacement(BlockState state, Direction facing, BlockState state2, IWorld world, BlockPos pos1, BlockPos pos2, Hand hand) {
        return getDefaultState().with(SEALED, false);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
        return side != null && side.getAxis() != Direction.Axis.Y;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(SEALED);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public boolean canCreatureSpawn(BlockState state, IBlockReader world, BlockPos pos, EntitySpawnPlacementRegistry.PlacementType type, @Nullable EntityType<?> entityType) {
        return state.get(SEALED);
    }

    @Override
    public boolean canHarvestBlock(BlockState state, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return true;
    }

    @Override
    public boolean isFlammable(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return true;
    }

    @Override
    public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return 0;
    }
}
