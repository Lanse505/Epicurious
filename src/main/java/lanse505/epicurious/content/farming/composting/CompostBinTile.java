package lanse505.epicurious.content.farming.composting;

import com.hrznstudio.titanium.block.tile.TileBase;
import lanse505.epicurious.content.ModBlocks;
import lanse505.epicurious.content.ModItems;
import lanse505.epicurious.core.recipes.compost.CompostSerializableRecipe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.math.MathHelper;

public class CompostBinTile extends TileBase {
    private int compost;
    private CompostSerializableRecipe lastRecipe;

    public CompostBinTile() {
        super(ModBlocks.compostBinBlock);
    }

    public ItemStack addCompost(PlayerEntity playerEntity, ItemStack stack) {
        ItemStack currentStack = stack;
        if (compost < 0) {
            compost = 0;
        }
        if (!stack.isEmpty()) {
            if (stack.hasContainerItem()) {
                currentStack = handleContainerItem(playerEntity, stack);
            } else {
                currentStack = handleHeldItem(stack);
            }

            while (compost >= 100) {
                compost -= 100;
                playerEntity.addItemStackToInventory(new ItemStack(ModItems.compostItem, MathHelper.getInt(world.rand.toString(), 1, 4)));
            }
            world.notifyBlockUpdate(getPos(), getBlockState(), getBlockState(), 2);
        }

        return currentStack;
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

    private ItemStack handleHeldItem(ItemStack stack) {
        if (lastRecipe == null || !lastRecipe.isValid(stack)) {
            lastRecipe = this.getWorld().getRecipeManager().getRecipes()
                    .stream().filter(recipe -> recipe.getType() == CompostSerializableRecipe.SERIALIZER.getRecipeType())
                    .map(recipe -> (CompostSerializableRecipe) recipe).filter(compostSerializableRecipe -> compostSerializableRecipe.isValid(stack)).findFirst().orElse(null);
        }
        if (lastRecipe != null) {
            compost += lastRecipe.value;
            stack.shrink(1);
        }
        return ItemStack.EMPTY;
    }

    private ItemStack handleContainerItem(PlayerEntity playerEntity, ItemStack stack) {
        if (lastRecipe == null || !lastRecipe.isValid(stack)) {
            lastRecipe = this.getWorld().getRecipeManager().getRecipes()
                    .stream().filter(recipe -> recipe.getType() == CompostSerializableRecipe.SERIALIZER.getRecipeType())
                    .map(recipe -> (CompostSerializableRecipe) recipe).filter(compostSerializableRecipe -> compostSerializableRecipe.isValid(stack)).findFirst().orElse(null);
        }
        if (lastRecipe != null) {
            compost += lastRecipe.value;
        }
        return stack.getContainerItem();
    }
}
