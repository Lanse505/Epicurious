package lanse505.epicurious;

import com.hrznstudio.titanium.event.handler.EventManager;
import com.hrznstudio.titanium.module.ModuleController;
import com.hrznstudio.titanium.recipe.generator.titanium.DefaultLootTableProvider;
import lanse505.epicurious.client.CompostBinTESR;
import lanse505.epicurious.content.ModItems;
import lanse505.epicurious.content.farming.composting.CompostBinTile;
import lanse505.epicurious.core.recipes.compost.CompostSerializableRecipe;
import lanse505.epicurious.core.recipes.EpicuriousRecipeProvider;
import lanse505.epicurious.core.recipes.serializers.EpicuriousSerializableProvider;
import lanse505.epicurious.utils.EpicuriousModules;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("epicurious")
public class Epicurious extends ModuleController {
    public static final String MODID = "epicurious";
    public static final ItemGroup epicurious = new ItemGroup("epicurious") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.grapeItem);
        }

        @Override
        public boolean hasSearchBar() {
            return true;
        }
    }.setBackgroundImageName("item_search.png");
    private static final Logger LOGGER = LogManager.getLogger();

    public Epicurious() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        MinecraftForge.EVENT_BUS.register(this);
        EventManager.mod(RegistryEvent.Register.class).filter(register -> register.getGenericType().equals(IRecipeSerializer.class))
                .process(register -> register.getRegistry().registerAll(CompostSerializableRecipe.SERIALIZER)).subscribe();
    }

    @Override
    protected void initModules() {
        this.addModule(EpicuriousModules.compost);
        this.addModule(EpicuriousModules.crops);
        this.addModule(EpicuriousModules.fermenting);
    }

    private void setup(final FMLCommonSetupEvent event) {


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
