package lanse505.epicurious.content.farming.composting;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BlockTileBase;
import lanse505.epicurious.Epicurious;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class CompostBinBlock extends BlockTileBase<CompostBinTile> {

    public CompostBinBlock() {
        super(Block.Properties.create(Material.WOOD).hardnessAndResistance(1.5f).sound(SoundType.WOOD), CompostBinTile.class);
        setItemGroup(Epicurious.epicurious);
    }

    private CompostBinTile getTE(World world, BlockPos pos) {
        if (world.getTileEntity(pos) != null) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof CompostBinTile) {
                return (CompostBinTile) te;
            }
        }
        return null;
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult ray) {
        CompostBinTile tile = getTE(worldIn, pos);

        if (tile == null) {
            return false;
        }

        if (worldIn.isRemote) {
            return true;
        }

        ItemStack stack = player.getHeldItemMainhand();


        return super.onBlockActivated(state, worldIn, pos, player, hand, ray);
    }

    @Override
    public boolean canHarvestBlock(BlockState state, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return true;
    }

    @Override
    public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
        super.harvestBlock(worldIn, player, pos, state, te, stack);
    }

    @Override
    public IFactory<CompostBinTile> getTileEntityFactory() {
        return CompostBinTile::new;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
}
