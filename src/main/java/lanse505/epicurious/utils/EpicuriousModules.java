package lanse505.epicurious.utils;

import com.hrznstudio.titanium.module.Feature;
import com.hrznstudio.titanium.module.Module;
import lanse505.epicurious.Epicurious;
import lanse505.epicurious.content.barrels.brewing.BrewingBarrelBlock;
import lanse505.epicurious.content.barrels.storage.StorageBarrelBlock;
import lanse505.epicurious.content.crops.*;
import lanse505.epicurious.content.farming.composting.CompostBinBlock;
import lanse505.epicurious.content.farming.composting.CompostItem;
import lanse505.epicurious.content.fermenting.YeastItem;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class EpicuriousModules {
    public static final Module.Builder farming = Module.builder("farming")
            .force()
            .description("Farming")
            .feature(
                    Feature.builder("compost")
                            .description("Compost-Related")
                            .content(Item.class, new CompostItem().setRegistryName(new ResourceLocation(Epicurious.MODID, "compost")))
                            .content(Block.class, new CompostBinBlock().setRegistryName(new ResourceLocation(Epicurious.MODID, "compost_bin")))
            )
            .feature(
                    Feature.builder("crops")
                            .description("Crop-Related")
                            .content(Item.class, new BarleyItem().setRegistryName(new ResourceLocation(Epicurious.MODID, "barley")))
                            .content(Item.class, new CornItem().setRegistryName(new ResourceLocation(Epicurious.MODID, "corn")))
                            .content(Item.class, new GrapeItem().setRegistryName(new ResourceLocation(Epicurious.MODID, "grapes")))
                            .content(Item.class, new HopItem().setRegistryName(new ResourceLocation(Epicurious.MODID, "hops")))
                            .content(Item.class, new RiceItem().setRegistryName(new ResourceLocation(Epicurious.MODID, "rice")))
                            .content(Item.class, new RyeItem().setRegistryName(new ResourceLocation(Epicurious.MODID, "rye")))
            );

    public static final Module.Builder barrels = Module.builder("barrels")
            .force()
            .description("Barrels")
            .feature(
                    Feature.builder("barrels")
                            .description("Barrels")
                            .content(Block.class, new BrewingBarrelBlock().setRegistryName(new ResourceLocation(Epicurious.MODID, "brewing_barrel")))
                            .content(Block.class, new StorageBarrelBlock().setRegistryName(new ResourceLocation(Epicurious.MODID, "storage_barrel")))
            );

    public static final Module.Builder fermenting = Module.builder("fermenting")
            .force()
            .description("Fermenting")
            .feature(
                    Feature.builder("items")
                            .description("Items")
                            .content(Item.class, new YeastItem().setRegistryName(new ResourceLocation(Epicurious.MODID, "yeast")))
            );
}
