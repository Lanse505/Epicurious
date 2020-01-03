package lanse505.epicurious.core.serializers.conditions;

import com.google.gson.JsonObject;
import lanse505.epicurious.Epicurious;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidExistsCondition implements ICondition {
    private static final ResourceLocation ID = new ResourceLocation(Epicurious.MODID, "fluid_exists");
    private final String fluid;

    public FluidExistsCondition(String fluidRL) {
        this.fluid = fluidRL;
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public boolean test() {
        return ForgeRegistries.FLUIDS.containsKey(new ResourceLocation(fluid));
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public static class Serializer implements IConditionSerializer<FluidExistsCondition> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public void write(JsonObject json, FluidExistsCondition value) {
            json.addProperty("fluid", value.fluid);
        }

        @Override
        public FluidExistsCondition read(JsonObject json) {
            return new FluidExistsCondition(JSONUtils.getString(json, "fluid"));
        }

        @Override
        public ResourceLocation getID() {
            return FluidExistsCondition.ID;
        }
    }
}
