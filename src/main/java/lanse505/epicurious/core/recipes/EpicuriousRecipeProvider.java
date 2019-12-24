package lanse505.epicurious.core.recipes;

import com.hrznstudio.titanium.recipe.generator.TitaniumRecipeProvider;
import lanse505.epicurious.content.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;

import java.util.function.Consumer;

public class EpicuriousRecipeProvider extends TitaniumRecipeProvider {

    public EpicuriousRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    public void register(Consumer<IFinishedRecipe> consumer) {
        ModBlocks.compostBinBlock.registerRecipe(consumer);
    }
}
