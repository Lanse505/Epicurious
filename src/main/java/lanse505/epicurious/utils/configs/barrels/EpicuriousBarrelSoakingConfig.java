package lanse505.epicurious.utils.configs.barrels;

import net.minecraftforge.common.ForgeConfigSpec;

public class EpicuriousBarrelSoakingConfig {
    public ForgeConfigSpec.IntValue soakingTankSize;

    public EpicuriousBarrelSoakingConfig(ForgeConfigSpec.Builder builder) {
        builder.push("Soaking Barrel");
        soakingTankSize = builder.defineInRange("Size of Soaking Tank", 16000, 0, Integer.MAX_VALUE);
        builder.pop();
    }
}
