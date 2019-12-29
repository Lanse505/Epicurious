package lanse505.epicurious.content;

import lanse505.epicurious.content.barrels.storage.StorageBarrelBlock;
import lanse505.epicurious.content.farming.composting.CompostBinBlock;
import net.minecraftforge.registries.ObjectHolder;

public class ModBlocks {
    @ObjectHolder("epicurious:compost_bin")
    public static CompostBinBlock compostBinBlock;

    @ObjectHolder("epicurious:storage_barrel")
    public static StorageBarrelBlock storageBarrelBlock;
}
