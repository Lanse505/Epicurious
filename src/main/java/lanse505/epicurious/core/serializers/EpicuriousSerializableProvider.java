package lanse505.epicurious.core.serializers;

import com.hrznstudio.titanium.recipe.generator.IJSONGenerator;
import com.hrznstudio.titanium.recipe.generator.IJsonFile;
import com.hrznstudio.titanium.recipe.generator.TitaniumSerializableProvider;
import lanse505.epicurious.Epicurious;
import lanse505.epicurious.core.recipes.barrels.BrewingSerializableRecipe;
import lanse505.epicurious.core.recipes.barrels.SoakingSerializableRecipe;
import lanse505.epicurious.core.recipes.compost.CompostSerializableRecipe;
import net.minecraft.data.DataGenerator;

import java.util.Map;

public class EpicuriousSerializableProvider extends TitaniumSerializableProvider {
    public EpicuriousSerializableProvider(DataGenerator generatorIn) {
        super(generatorIn, Epicurious.MODID);
    }

    @Override
    public void add(Map<IJsonFile, IJSONGenerator> map) {
        CompostSerializableRecipe.RECIPES.forEach(compostSerializableRecipe -> map.put(compostSerializableRecipe, compostSerializableRecipe));
        BrewingSerializableRecipe.RECIPES.forEach(brewingSerializableRecipe -> map.put(brewingSerializableRecipe, brewingSerializableRecipe));
        SoakingSerializableRecipe.RECIPES.forEach(soakingSerializableRecipe -> map.put(soakingSerializableRecipe, soakingSerializableRecipe));
    }
}
