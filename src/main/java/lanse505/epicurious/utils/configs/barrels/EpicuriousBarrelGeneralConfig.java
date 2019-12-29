package lanse505.epicurious.utils.configs.barrels;

import net.minecraftforge.common.ForgeConfigSpec;

public class EpicuriousBarrelGeneralConfig {
    private final EpicuriousBarrelBrewingConfig brewing;
    private final EpicuriousBarrelSoakingConfig soaking;
    private final EpicuriousBarrelStorageConfig storage;
    public ForgeConfigSpec.BooleanValue canBarrelsFillWhileRaining;
    public ForgeConfigSpec.BooleanValue shouldBarrelsRespectRainValueOfBiomes;
    public ForgeConfigSpec.IntValue potionToBottleDrainAmount;

    public EpicuriousBarrelGeneralConfig(ForgeConfigSpec.Builder builder) {
        builder.push("General");
        canBarrelsFillWhileRaining = builder.define("Can barrels fill while raining?", true);
        shouldBarrelsRespectRainValueOfBiomes = builder.define("Should barrels respect Biome Rain-Values?", true);
        potionToBottleDrainAmount = builder.comment("How much 1 Bottle is worth in Potion Fluid amount").defineInRange("Potion to Fluid Amount", 250, 1, Integer.MAX_VALUE);
        builder.pop();

        brewing = new EpicuriousBarrelBrewingConfig(builder);
        soaking = new EpicuriousBarrelSoakingConfig(builder);
        storage = new EpicuriousBarrelStorageConfig(builder);
    }

    public EpicuriousBarrelBrewingConfig getBrewing() {
        return brewing;
    }

    public EpicuriousBarrelSoakingConfig getSoaking() {
        return soaking;
    }

    public EpicuriousBarrelStorageConfig getStorage() {
        return storage;
    }
}
