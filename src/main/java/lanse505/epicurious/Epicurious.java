package lanse505.epicurious;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.hrznstudio.titanium.event.handler.EventManager;
import com.hrznstudio.titanium.module.ModuleController;
import com.hrznstudio.titanium.network.CompoundSerializableDataHandler;
import com.hrznstudio.titanium.network.NetworkHandler;
import com.hrznstudio.titanium.recipe.generator.titanium.DefaultLootTableProvider;
import com.hrznstudio.titanium.recipe.serializer.JSONSerializableDataHandler;
import lanse505.epicurious.client.CompostBinTESR;
import lanse505.epicurious.content.ModItems;
import lanse505.epicurious.content.farming.composting.CompostBinTile;
import lanse505.epicurious.core.network.BarrelButtonMessage;
import lanse505.epicurious.core.recipes.barrels.BrewingSerializableRecipe;
import lanse505.epicurious.core.recipes.barrels.SoakingSerializableRecipe;
import lanse505.epicurious.core.recipes.compost.CompostSerializableRecipe;
import lanse505.epicurious.core.serializers.EpicuriousRecipeProvider;
import lanse505.epicurious.core.serializers.EpicuriousSerializableProvider;
import lanse505.epicurious.core.serializers.JSONSerializableHandlers;
import lanse505.epicurious.core.serializers.conditions.FluidExistsCondition;
import lanse505.epicurious.utils.EpicuriousModules;
import lanse505.epicurious.utils.configs.EpicuriousConfigs;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;

@Mod("epicurious")
public class Epicurious extends ModuleController {
    public static final String MODID = "epicurious";
    public static final ItemGroup epicurious = new ItemGroup("epicurious") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.compostItem);
        }

        @Override
        public boolean hasSearchBar() {
            return true;
        }
    }.setBackgroundImageName("item_search.png");
    public static final Logger LOGGER = LogManager.getLogger();
    public static NetworkHandler NETWORK = new NetworkHandler(MODID);

    public Epicurious() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Registers Recipe Serializers and Conditions
        EventManager.mod(RegistryEvent.Register.class).filter(register -> register.getGenericType().equals(IRecipeSerializer.class))
                .process(register -> {
                    register.getRegistry().registerAll(CompostSerializableRecipe.SERIALIZER, BrewingSerializableRecipe.SERIALIZER, SoakingSerializableRecipe.SERIALIZER);
                    CraftingHelper.register(FluidExistsCondition.Serializer.INSTANCE);
                }).subscribe();

        // Registers The Config
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EpicuriousConfigs.initialize(), "epicurious.toml");

        // Registers the Barrel Button Message for Sealing and Un-Sealing
        NETWORK.registerMessage(BarrelButtonMessage.class);
    }

    @Override
    protected void initModules() {
        this.addModule(EpicuriousModules.barrels);
        this.addModule(EpicuriousModules.farming);
        this.addModule(EpicuriousModules.fermenting);
    }

    private void setup(final FMLCommonSetupEvent event) {
        JSONSerializableDataHandler.map(Biome.class, JSONSerializableHandlers::writeBiomeType, JSONSerializableHandlers::readBiomeType);
        JSONSerializableDataHandler.map(Biome[].class, (biomes) -> {
            JsonArray array = new JsonArray();
            for (Biome biome : biomes) {
                array.add(biome.getRegistryName().toString());
            }
            return array;
        }, element -> {
            Biome[] biomes = new Biome[element.getAsJsonArray().size()];
            int i = 0;
            for (Iterator<JsonElement> iterator = element.getAsJsonArray().iterator(); iterator.hasNext(); i++) {
                JsonElement jsonElement = iterator.next();
                biomes[i] = ForgeRegistries.BIOMES.getValue(new ResourceLocation(jsonElement.getAsString()));
            }
            return biomes;
        });
        JSONSerializableDataHandler.map(Ingredient[].class, (type) -> {
            JsonArray array = new JsonArray();
            for (Ingredient ingredient : type) {
                array.add(ingredient.serialize());
            }
            return array;
        }, (element) -> {
            Ingredient[] ingredients = new Ingredient[element.getAsJsonArray().size()];
            int i = 0;
            for (Iterator<JsonElement> iterator = element.getAsJsonArray().iterator(); iterator.hasNext(); i++) {
                JsonElement jsonElement = iterator.next();
                ingredients[i] = Ingredient.deserialize(jsonElement);
            }
            return ingredients;
        });
        CompoundSerializableDataHandler.map(Biome.class, JSONSerializableHandlers::readBiome, JSONSerializableHandlers::writeBiome);
        CompoundSerializableDataHandler.map(Biome[].class, JSONSerializableHandlers::readBiomeArray, JSONSerializableHandlers::writeBiomeArray);
        CompoundSerializableDataHandler.map(Ingredient[].class, JSONSerializableHandlers::readIngredientArray, JSONSerializableHandlers::writeIngredientArray);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntitySpecialRenderer(CompostBinTile.class, new CompostBinTESR());
    }

    @Override
    public void addDataProvider(GatherDataEvent event) {
        super.addDataProvider(event);
        event.getGenerator().addProvider(new EpicuriousRecipeProvider(event.getGenerator()));
        event.getGenerator().addProvider(new EpicuriousSerializableProvider(event.getGenerator()));
        event.getGenerator().addProvider(new DefaultLootTableProvider(event.getGenerator(), MODID));
    }
}
