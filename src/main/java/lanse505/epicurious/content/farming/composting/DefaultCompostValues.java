package lanse505.epicurious.content.farming.composting;

import lanse505.epicurious.content.ModItems;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class DefaultCompostValues {

    public static final Map<ResourceLocation, Integer> values = new HashMap<>();

    public static void setupDefaultValues() {
        values.put(ModItems.barleyItem.getRegistryName(), 5);
        values.put(ModItems.cornItem.getRegistryName(), 5);
        values.put(ModItems.grapeItem.getRegistryName(), 5);
        values.put(ModItems.hopItem.getRegistryName(), 5);
        values.put(ModItems.riceItem.getRegistryName(), 5);
        values.put(ModItems.ryeItem.getRegistryName(), 5);
    }
}
