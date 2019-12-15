package lanse505.epicurious;

import com.hrznstudio.titanium.module.ModuleController;
import lanse505.epicurious.client.CompostBinTESR;
import lanse505.epicurious.content.ModItems;
import lanse505.epicurious.content.farming.composting.CompostBinTile;
import lanse505.epicurious.content.farming.composting.DefaultCompostValues;
import lanse505.epicurious.core.EpicuriousCapabilities;
import lanse505.epicurious.core.capabilities.CompostProvider;
import lanse505.epicurious.core.capabilities.CompostStorage;
import lanse505.epicurious.core.capabilities.DefaultCompostHandler;
import lanse505.epicurious.core.capabilities.ICompost;
import lanse505.epicurious.utils.EpicuriousModules;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

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
        CapabilityManager.INSTANCE.register(ICompost.class, CompostStorage.create(), DefaultCompostHandler::new);
        DefaultCompostValues.setupDefaultValues();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        //ClientRegistry.bindTileEntitySpecialRenderer(CompostBinTile.class, new CompostBinTESR());
    }

    @SubscribeEvent
    public void onAttachCompostCap(AttachCapabilitiesEvent<ItemStack> event) {
        if (event.getObject().getItem().getRegistryName().getNamespace().contains("epicurious")) {
            if (DefaultCompostValues.values.containsKey(event.getObject().getItem().getRegistryName())) {
                int value = DefaultCompostValues.values.get(event.getObject().getItem().getRegistryName());
                event.addCapability(new ResourceLocation(MODID, "compost"), new CompostProvider(value));
                LOGGER.info("Added Compost Value: '" + value + "' for Item: '" + event.getObject().getDisplayName().getUnformattedComponentText() + "'");
            }
        }
    }

}
