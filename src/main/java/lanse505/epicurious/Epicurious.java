package lanse505.epicurious;

import com.hrznstudio.titanium.module.ModuleController;
import lanse505.epicurious.client.CompostBinTESR;
import lanse505.epicurious.content.ModItems;
import lanse505.epicurious.content.farming.composting.CompostBinTile;
import lanse505.epicurious.utils.EpicuriousModules;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("epicurious")
public class Epicurious extends ModuleController {
    public static final String MODID = "epicurious";
    private static final Logger LOGGER = LogManager.getLogger();

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

    public Epicurious() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    protected void initModules() {
        this.addModule(EpicuriousModules.compost);
        this.addModule(EpicuriousModules.crops);
        this.addModule(EpicuriousModules.fermenting);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Registering Capabilities");
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntitySpecialRenderer(CompostBinTile.class, new CompostBinTESR());
    }

}
