package lanse505.epicurious.core.recipes.barrels;

import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.hrznstudio.titanium.recipe.serializer.SerializableRecipe;
import lanse505.epicurious.Epicurious;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class BiomeRainSerializableRecipe extends SerializableRecipe {
    public static GenericSerializer<BiomeRainSerializableRecipe> SERIALIZER = new GenericSerializer<>(new ResourceLocation(Epicurious.MODID, "biomeRain"), BiomeRainSerializableRecipe.class);
    public static List<BiomeRainSerializableRecipe> RECIPES = new ArrayList<>();

    public Biome biome;
    public FluidStack fluidStack;

    public BiomeRainSerializableRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public BiomeRainSerializableRecipe(ResourceLocation resourceLocation, Biome biome, FluidStack fluidStack) {
        super(resourceLocation);
        this.biome = biome;
        this.fluidStack = fluidStack;
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

    public boolean isValid(Biome biome) {
        return biome.equals(this.biome);
    }
}
