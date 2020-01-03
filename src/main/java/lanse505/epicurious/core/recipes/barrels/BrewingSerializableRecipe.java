package lanse505.epicurious.core.recipes.barrels;

import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.hrznstudio.titanium.recipe.serializer.SerializableRecipe;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import lanse505.epicurious.Epicurious;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class BrewingSerializableRecipe extends SerializableRecipe {
    public static GenericSerializer<BrewingSerializableRecipe> SERIALIZER = new GenericSerializer<>(new ResourceLocation(Epicurious.MODID, "brewing"), BrewingSerializableRecipe.class);
    public static List<BrewingSerializableRecipe> RECIPES = new ArrayList<>();

    public FluidStack brewableFluid;
    public Ingredient[] brewingIngredients;
    public FluidStack brewedFluid;
    public int ticks;

    public BrewingSerializableRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public BrewingSerializableRecipe(ResourceLocation resourceLocation, FluidStack brewableFluid, Ingredient[] brewingIngredients, FluidStack brewedFluid, int ticks) {
        super(resourceLocation);
        this.brewableFluid = brewableFluid;
        this.brewingIngredients = brewingIngredients;
        this.brewedFluid = brewedFluid;
        this.ticks = ticks;
        RECIPES.add(this);
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return false;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return null;
    }

    @Override
    public boolean canFit(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

    @Override
    public GenericSerializer<? extends SerializableRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public IRecipeType<?> getType() {
        return SERIALIZER.getRecipeType();
    }

    public boolean isValid(FluidStack storedBrewableFluid, ItemStackHandler inventory) {
        boolean fluid = storedBrewableFluid != null && storedBrewableFluid.containsFluid(this.brewableFluid);
        IntList usedSlots = new IntArrayList(3);
        IntList matchedSlots = new IntArrayList(3);
        int counter = 0;
        for (Ingredient i : this.brewingIngredients) {
            for (int idx = 0; idx < 3; idx++) {
                ItemStack s = inventory.getStackInSlot(idx);
                if (!matchedSlots.contains(counter) && i.test(s) && !usedSlots.contains(idx)) {
                    usedSlots.add(idx);
                    matchedSlots.add(counter);
                }
            }
            counter++;
        }

        return fluid && matchedSlots.size() == this.brewingIngredients.length;
    }
}
