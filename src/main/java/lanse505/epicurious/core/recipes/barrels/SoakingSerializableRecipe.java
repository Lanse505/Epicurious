package lanse505.epicurious.core.recipes.barrels;

import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.hrznstudio.titanium.recipe.serializer.SerializableRecipe;
import lanse505.epicurious.Epicurious;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SoakingSerializableRecipe extends SerializableRecipe {
    public static GenericSerializer<SoakingSerializableRecipe> SERIALIZER = new GenericSerializer<>(new ResourceLocation(Epicurious.MODID, "soaking"), SoakingSerializableRecipe.class);
    public static List<SoakingSerializableRecipe> RECIPES = new ArrayList<>();

    public FluidStack soakingFluid;
    public Ingredient.IItemList soakable;
    public ItemStack soaked;
    public float fluidUseChance;
    public int ticks;

    public SoakingSerializableRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public SoakingSerializableRecipe(ResourceLocation resourceLocation, FluidStack soakingFluid, Ingredient.IItemList soakable, ItemStack soaked, float fluidUseChance, int ticks) {
        super(resourceLocation);
        this.soakingFluid = soakingFluid;
        this.soakable = soakable;
        this.soaked = soaked;
        this.fluidUseChance = fluidUseChance;
        this.ticks = ticks;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return false;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return this.soaked;
    }

    @Override
    public boolean canFit(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.soaked;
    }

    @Override
    public GenericSerializer<? extends SerializableRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public IRecipeType<?> getType() {
        return SERIALIZER.getRecipeType();
    }

    public boolean isValid(FluidStack storedFluid, ItemStack storedStack) {
        return storedFluid.containsFluid(this.soakingFluid) && Ingredient.fromItemListStream(Arrays.asList(this.soakable).stream()).test(storedStack);
    }
}
