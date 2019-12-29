package lanse505.epicurious.utils;

import net.minecraft.block.SpongeBlock;
import net.minecraft.block.WetSpongeBlock;
import net.minecraftforge.registries.ObjectHolder;

public class EpicuriousReferenceObjects {
    public static String VERSION = "1.0.0-alpha";


    @ObjectHolder("minecraft:sponge")
    public static SpongeBlock spongeBlock;

     @ObjectHolder("minecraft:wet_sponge")
    public static WetSpongeBlock wetSpongeBlock;
}
