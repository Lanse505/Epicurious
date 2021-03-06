package lanse505.epicurious.core.recipes.compost;

import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.hrznstudio.titanium.recipe.serializer.SerializableRecipe;
import lanse505.epicurious.Epicurious;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompostSerializableRecipe extends SerializableRecipe implements IConditionBuilder {
    public static GenericSerializer<CompostSerializableRecipe> SERIALIZER = new GenericSerializer<>(new ResourceLocation(Epicurious.MODID, "compost"), CompostSerializableRecipe.class);
    public static List<CompostSerializableRecipe> RECIPES = new ArrayList<>();

    public Ingredient.IItemList[] compostable;
    public int value;

    public CompostSerializableRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public CompostSerializableRecipe(ResourceLocation resourceLocation, Ingredient.IItemList[] compostable, int value) {
        super(resourceLocation);
        this.compostable = compostable;
        this.value = value;
        RECIPES.add(this);
    }

    public static List<CompostSerializableRecipe> getRECIPES() {
        return RECIPES;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return false;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    public GenericSerializer<? extends SerializableRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public IRecipeType<?> getType() {
        return SERIALIZER.getRecipeType();
    }

    public boolean isValid(ItemStack input) {
        return Ingredient.fromItemListStream(Arrays.stream(this.compostable)).test(input);
    }
}
