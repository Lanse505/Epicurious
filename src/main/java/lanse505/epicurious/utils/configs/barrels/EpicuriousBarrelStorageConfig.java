package lanse505.epicurious.utils.configs.barrels;

import net.minecraftforge.common.ForgeConfigSpec;

public class EpicuriousBarrelStorageConfig {
    public ForgeConfigSpec.IntValue storageTankSize;

    public EpicuriousBarrelStorageConfig(ForgeConfigSpec.Builder builder) {
        builder.push("Storage Barrel");
        storageTankSize = builder.defineInRange("Size of Storage Tank", 16000, 0, Integer.MAX_VALUE);
        builder.pop();
    }
}
