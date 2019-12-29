package lanse505.epicurious.utils.configs;

import lanse505.epicurious.utils.configs.barrels.EpicuriousBarrelGeneralConfig;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Objects;

public class EpicuriousConfigs {
    private static EpicuriousConfigs instance;

    private final EpicuriousBarrelGeneralConfig barrels;

    private final ForgeConfigSpec spec;

    private EpicuriousConfigs(ForgeConfigSpec.Builder builder) {
        this.barrels = new EpicuriousBarrelGeneralConfig(builder);
        this.spec = builder.build();
    }

    public static ForgeConfigSpec initialize() {
        EpicuriousConfigs configs = new EpicuriousConfigs(new ForgeConfigSpec.Builder());
        instance = configs;
        return configs.spec;
    }

    public static EpicuriousConfigs getInstance() {
        return Objects.requireNonNull(instance, "Called for Config before it's Initialization");
    }

    public EpicuriousBarrelGeneralConfig getBarrels() {
        return barrels;
    }

    public ForgeConfigSpec getSpec() {
        return spec;
    }
}
