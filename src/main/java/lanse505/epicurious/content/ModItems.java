package lanse505.epicurious.content;

import lanse505.epicurious.content.crops.*;
import lanse505.epicurious.content.farming.composting.CompostItem;
import lanse505.epicurious.content.fermenting.YeastItem;
import net.minecraftforge.registries.ObjectHolder;

public class ModItems {
    // Crops
    @ObjectHolder("epicurious:barley")
    public static BarleyItem barleyItem;

    @ObjectHolder("epicurious:corn")
    public static CornItem cornItem;

    @ObjectHolder("epicurious:grapes")
    public static GrapeItem grapeItem;

    @ObjectHolder("epicurious:hops")
    public static HopItem hopItem;

    @ObjectHolder("epicurious:rice")
    public static RiceItem riceItem;

    @ObjectHolder("epicurious:rye")
    public static RyeItem ryeItem;


    // Farming
    @ObjectHolder("epicurious:compost")
    public static CompostItem compostItem;


    // Fermenting
    @ObjectHolder("epicurious:yeast")
    public static YeastItem yeastItem;


}
