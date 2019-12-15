package lanse505.epicurious.core.capabilities;

import lanse505.epicurious.Epicurious;
import lanse505.epicurious.core.EpicuriousCapabilities;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CompostProvider implements ICapabilitySerializable<CompoundNBT> {
    private final int value;
    private final LazyOptional<ICompost> optional;

    public CompostProvider(int value) {
        ICompost compost = EpicuriousCapabilities.COMPOST_CAPABILITY.getDefaultInstance();
        if (compost != null) {
            compost.setCompost(value);
        }
        this.optional = LazyOptional.of(() -> compost);
        this.value = value;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == EpicuriousCapabilities.COMPOST_CAPABILITY) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        ICompost compost = EpicuriousCapabilities.COMPOST_CAPABILITY.getDefaultInstance();
        if (compost != null) {
            compost.setCompost(value);
        }
        return (CompoundNBT) EpicuriousCapabilities.COMPOST_CAPABILITY.getStorage().writeNBT(EpicuriousCapabilities.COMPOST_CAPABILITY, compost, null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        ICompost compost = EpicuriousCapabilities.COMPOST_CAPABILITY.getDefaultInstance();
        if (compost != null) {
            compost.setCompost(value);
        }
        EpicuriousCapabilities.COMPOST_CAPABILITY.getStorage().readNBT(EpicuriousCapabilities.COMPOST_CAPABILITY, compost, null, nbt);
    }
}
