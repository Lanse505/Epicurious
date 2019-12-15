package lanse505.epicurious.core.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class CompostStorage implements Capability.IStorage<ICompost> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<ICompost> capability, ICompost instance, Direction side) {
        final CompoundNBT compound = new CompoundNBT();
        compound.putInt("compost", instance.getCompost());
        return compound;
    }

    @Override
    public void readNBT(Capability<ICompost> capability, ICompost instance, Direction side, INBT nbt) {
        final CompoundNBT compound = (CompoundNBT) nbt;
        instance.setCompost(compound.getInt("compost"));
    }

    public static CompostStorage create() {
        return new CompostStorage();
    }
}
