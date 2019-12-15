package lanse505.epicurious.core;

import lanse505.epicurious.core.capabilities.ICompost;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class EpicuriousCapabilities {
    @CapabilityInject(ICompost.class)
    public static final Capability<ICompost> COMPOST_CAPABILITY = null;
}
