package lanse505.epicurious.core.recipes;

import com.hrznstudio.titanium.recipe.generator.IJSONGenerator;
import com.hrznstudio.titanium.recipe.generator.IJsonFile;
import com.hrznstudio.titanium.recipe.generator.TitaniumSerializableProvider;
import lanse505.epicurious.Epicurious;
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
    }
}
