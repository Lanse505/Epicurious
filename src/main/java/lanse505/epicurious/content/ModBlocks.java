package lanse505.epicurious.content;

import lanse505.epicurious.content.barrels.brewing.BrewingBarrelBlock;
import lanse505.epicurious.content.barrels.soaking.SoakingBarrelBlock;
import lanse505.epicurious.content.barrels.storage.StorageBarrelBlock;
import lanse505.epicurious.content.farming.composting.CompostBinBlock;
import net.minecraftforge.registries.ObjectHolder;

public class ModBlocks {
    //Composting
    @ObjectHolder("epicurious:compost_bin")
    public static CompostBinBlock compostBinBlock;

    //Barrels
    @ObjectHolder("epicurious:brewing_barrel")
    public static BrewingBarrelBlock brewingBarrelBlock;

    //@ObjectHolder("epicurious:soaking_barrel")
    //public static SoakingBarrelBlock soakingBarrelBlock;

    @ObjectHolder("epicurious:storage_barrel")
    public static StorageBarrelBlock storageBarrelBlock;

    //Crops

}
