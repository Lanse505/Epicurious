package lanse505.epicurious.utils;

import com.hrznstudio.titanium.event.handler.EventManager;
import com.hrznstudio.titanium.module.Feature;
import com.hrznstudio.titanium.module.Module;
import lanse505.epicurious.Epicurious;
import lanse505.epicurious.content.crops.*;
import lanse505.epicurious.content.farming.composting.CompostBinBlock;
import lanse505.epicurious.content.farming.composting.CompostItem;
import lanse505.epicurious.core.recipes.compost.CompostSerializableRecipe;
import lanse505.epicurious.content.fermenting.YeastItem;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class EpicuriousModules {
    public static final Module.Builder compost = Module.builder("compost")
            .force()
            .description("compost")
            .feature(
                    Feature.builder("items")
                            .description("Items")
                            .content(Item.class, new CompostItem().setRegistryName(new ResourceLocation(Epicurious.MODID, "compost")))
            )
            .feature(
                    Feature.builder("blocks")
                            .description("Blocks")
                            .content(Block.class, new CompostBinBlock().setRegistryName(new ResourceLocation(Epicurious.MODID, "compost_bin")))
            )
            .feature(
                    Feature.builder("serializer")
                            .description("Recipe Serializer")
                            .content(IRecipeSerializer.class, (IRecipeSerializer) CompostSerializableRecipe.SERIALIZER)
                            .event(EventManager.mod(FMLCommonSetupEvent.class).process(event -> Registry.register(Registry.RECIPE_TYPE, CompostSerializableRecipe.SERIALIZER.getRegistryName(), CompostSerializableRecipe.SERIALIZER.getRecipeType())))
            );


    public static final Module.Builder crops = Module.builder("crops")
            .force()
            .description("Crops")
            .feature(
                    Feature.builder("barley")
                            .description("Barley")
                            .content(Item.class, new BarleyItem().setRegistryName(new ResourceLocation(Epicurious.MODID, "barley")))
            )
            .feature(
                    Feature.builder("corn")
                    .description("Corn")
                    .content(Item.class, new CornItem().setRegistryName(new ResourceLocation(Epicurious.MODID, "corn")))
            )
            .feature(
                    Feature.builder("grape")
                    .description("Grapes")
                    .content(Item.class, new GrapeItem().setRegistryName(new ResourceLocation(Epicurious.MODID, "grapes")))
            )
            .feature(
                    Feature.builder("hops")
                    .description("Hops")
                    .content(Item.class, new HopItem().setRegistryName(new ResourceLocation(Epicurious.MODID, "hops")))
            )
            .feature(
                    Feature.builder("rice")
                    .description("Rice")
                    .content(Item.class, new RiceItem().setRegistryName(new ResourceLocation(Epicurious.MODID, "rice")))
            )
            .feature(
                    Feature.builder("rye")
                    .description("Rye")
                    .content(Item.class, new RyeItem().setRegistryName(new ResourceLocation(Epicurious.MODID, "rye")))
            );

    public static final Module.Builder fermenting = Module.builder("fermenting")
            .force()
            .description("fermenting")
            .feature(
                    Feature.builder("items")
                    .description("Items")
                    .content(Item.class, new YeastItem().setRegistryName(new ResourceLocation(Epicurious.MODID, "yeast")))
            );
}
