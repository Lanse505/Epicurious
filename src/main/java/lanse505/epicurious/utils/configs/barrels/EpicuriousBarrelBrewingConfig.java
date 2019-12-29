package lanse505.epicurious.utils.configs.barrels;

import net.minecraftforge.common.ForgeConfigSpec;

public class EpicuriousBarrelBrewingConfig {
    public ForgeConfigSpec.IntValue brewingInputSize;
    public ForgeConfigSpec.IntValue brewingOutputSize;

    public EpicuriousBarrelBrewingConfig(ForgeConfigSpec.Builder builder) {
        builder.push("Brewing Barrel");
        brewingInputSize = builder.defineInRange("Size of Input Tank", 16000, 0, Integer.MAX_VALUE);
        brewingOutputSize = builder.defineInRange("Size of Output Tank", 16000, 0, Integer.MAX_VALUE);
        builder.pop();
    }
}
