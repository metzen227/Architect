package li.cil.architect.common;

import li.cil.architect.api.API;
import li.cil.architect.api.ConverterAPI;
import li.cil.architect.common.api.ConverterAPIImpl;
import li.cil.architect.common.api.CreativeTab;
import li.cil.architect.common.config.Settings;
import li.cil.architect.common.converter.ConverterAttachedBlock;
import li.cil.architect.common.converter.ConverterFallingBlock;
import li.cil.architect.common.converter.ConverterSolidBlock;
import li.cil.architect.common.converter.ConverterTileEntity;
import li.cil.architect.common.init.Items;
import li.cil.architect.common.integration.Integration;
import li.cil.architect.common.jobs.JobManager;
import li.cil.architect.common.network.Network;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.function.Supplier;

/**
 * Takes care of common setup.
 */
public class ProxyCommon {
    public void onPreInit(final FMLPreInitializationEvent event) {
        // Load our settings first to have all we need for remaining init.
        Settings.load(event.getSuggestedConfigurationFile());

        // Initialize API.
        API.creativeTab = new CreativeTab();

        API.converterAPI = new ConverterAPIImpl();

        // Register blocks and items.
        Items.register(this);

        // Mod integration.
        Integration.preInit(event);
    }

    public void onInit(final FMLInitializationEvent event) {
        // Hardcoded recipes!
        Items.addRecipes();

        // Register network handler.
        Network.INSTANCE.init();

        // Register event handlers.
        MinecraftForge.EVENT_BUS.register(JobManager.INSTANCE);

        // Register built-in dynamic converter.
        ConverterAPI.addConverter(new ConverterTileEntity());
        ConverterAPI.addConverter(new ConverterSolidBlock());
        ConverterAPI.addConverter(new ConverterFallingBlock());
        ConverterAPI.addConverter(new ConverterAttachedBlock());

        // Mod integration.
        Integration.init(event);
    }

    public void onPostInit(final FMLPostInitializationEvent event) {
        // Mod integration.
        Integration.postInit(event);
    }

    // --------------------------------------------------------------------- //

    public Item registerItem(final String name, final Supplier<Item> constructor) {
        final Item item = constructor.get().
                setUnlocalizedName(API.MOD_ID + "." + name).
                setCreativeTab(API.creativeTab).
                setRegistryName(name);
        GameRegistry.register(item);
        return item;
    }
}
