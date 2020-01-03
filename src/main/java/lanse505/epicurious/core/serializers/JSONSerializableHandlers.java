package lanse505.epicurious.core.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hrznstudio.titanium.recipe.serializer.JSONSerializableDataHandler;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JSONSerializableHandlers {
    // Biome
    public static Biome readBiome(PacketBuffer buffer) {
        return ForgeRegistries.BIOMES.getValue(new ResourceLocation(buffer.readString()));
    }

    public static void writeBiome(PacketBuffer buffer, Biome biome) {
        buffer.writeString(biome.getRegistryName().toString());
    }

    public static JsonObject writeBiomeType(Biome biome) {
        JsonObject object = new JsonObject();
        object.addProperty("biome", biome.getRegistryName().toString());
        return object;
    }

    public static Biome readBiomeType(JsonElement element) {
        return ForgeRegistries.BIOMES.getValue(new ResourceLocation(element.getAsString()));
    }

    public static Biome[] readBiomeArray(PacketBuffer buffer) {
        Biome[] biomes = new Biome[buffer.readInt()];
        for (int i = 0; i < biomes.length; i++) {
            biomes[i] = ForgeRegistries.BIOMES.getValue(new ResourceLocation(buffer.readString()));
        }
        return biomes;
    }

    public static void writeBiomeArray(PacketBuffer buffer, Biome[] biomes) {
        buffer.writeInt(biomes.length);
        for (Biome biome : biomes) {
            buffer.writeString(biome.getRegistryName().toString());
        }
    }

    //Ingredient[]
    public static Ingredient[] readIngredientArray(PacketBuffer buffer) {
        Ingredient[] ingredients = new Ingredient[buffer.readInt()];
        for (int i = 0; i < ingredients.length; i++) {
            ingredients[i] = Ingredient.read(buffer);
        }
        return ingredients;
    }

    public static void writeIngredientArray(PacketBuffer buffer, Ingredient[] ingredients) {
        buffer.writeInt(ingredients.length);
        for (Ingredient ingredient : ingredients) {
            ingredient.write(buffer);
        }
    }
}
