package lanse505.epicurious.core.recipes.compost;

import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.hrznstudio.titanium.recipe.serializer.SerializableRecipe;
import lanse505.epicurious.Epicurious;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class CompostSerializableRecipe extends SerializableRecipe {
    public static GenericSerializer<CompostSerializableRecipe> SERIALIZER = new GenericSerializer<>(new ResourceLocation(Epicurious.MODID, "compost"), CompostSerializableRecipe.class);
    public static List<CompostSerializableRecipe> RECIPES = new ArrayList<>();

    public NonNullList<Ingredient> compostables;
    public int value;

    public CompostSerializableRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public CompostSerializableRecipe(ResourceLocation resourceLocation, NonNullList<Ingredient> compostable, int value) {
        super(resourceLocation);
        this.compostables = compostable;
        this.value = value;
        RECIPES.add(this);
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return compostables.stream().anyMatch(compostables -> compostables.test(inv.getStackInSlot(0)));
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
        return compostables.stream().anyMatch(compostables -> compostables.test(input));
    }

    public static List<CompostSerializableRecipe> getRECIPES() {
        return RECIPES;
    }
}
