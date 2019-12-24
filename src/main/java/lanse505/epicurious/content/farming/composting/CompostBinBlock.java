package lanse505.epicurious.content.farming.composting;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BlockTileBase;
import com.hrznstudio.titanium.recipe.generator.TitaniumLootTableProvider;
import com.hrznstudio.titanium.recipe.generator.TitaniumShapedRecipeBuilder;
import lanse505.epicurious.Epicurious;
import lanse505.epicurious.content.ModBlocks;
import lanse505.epicurious.core.recipes.compost.CompostSerializableRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CompostBinBlock extends BlockTileBase<CompostBinTile> {

    public CompostBinBlock() {
        super(Block.Properties.create(Material.WOOD).hardnessAndResistance(1.5f).sound(SoundType.WOOD), CompostBinTile.class);
        setItemGroup(Epicurious.epicurious);
    }

    @Override
    public List<VoxelShape> getBoundingBoxes(BlockState state, IBlockReader source, BlockPos pos) {
        List<VoxelShape> shapes = new ArrayList<>();
        return super.getBoundingBoxes(state, source, pos);
    }

    @Override
    public void createLootTable(TitaniumLootTableProvider provider) {
        provider.createSimple(this);
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
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return false;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return true;
    }

    @Override
    public boolean canCreatureSpawn(BlockState state, IBlockReader world, BlockPos pos, EntitySpawnPlacementRegistry.PlacementType type, @Nullable EntityType<?> entityType) {
        return false;
    }

    @Override
    public IFactory<CompostBinTile> getTileEntityFactory() {
        return CompostBinTile::new;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
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
        if (worldIn.isRemote) {
            return true;
        }

        CompostBinTile tile = getTE(worldIn, pos);

        if (tile == null) {
            return false;
        }

        ItemStack stack = player.getHeldItem(hand);
        if (tile.getWorld() != null) {
            player.addItemStackToInventory(tile.addCompost(player, stack));
        }

        return true;
    }

    @Override
    public void registerRecipe(Consumer<IFinishedRecipe> consumer) {
        TitaniumShapedRecipeBuilder.shapedRecipe(ModBlocks.compostBinBlock.asItem())
                .patternLine("f f")
                .patternLine("f f")
                .patternLine("fsf")
                .key('f', Ingredient.fromTag(Tags.Items.FENCES)).key('s', Ingredient.fromTag(ItemTags.WOODEN_SLABS))
                .build(consumer);
    }
}
