package lanse505.epicurious.core.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

public class JSONSerializableHandlers {
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
}
