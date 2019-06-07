package ch.m4th1eu.richpresence;

import ch.m4th1eu.richpresence.events.EventPresence;
import ch.m4th1eu.richpresence.events.Events;
import ch.m4th1eu.richpresence.proxy.CommonProxy;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION)
public class Main {
    public static final String MODID = "richpresence";
    public static final String NAME = "Discord Rich Presence";
    public static final String VERSION = "1.0.0";

    public EventPresence rpcClient;


    @SidedProxy(clientSide = "ch.m4th1eu.richpresence.proxy.ClientProxy", serverSide = "ch.m4th1eu.richpresence.proxy.CommonProxy")
    public static CommonProxy proxy;

    public static String applicationId;


    private static Logger logger;

    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event.getSuggestedConfigurationFile());

        //Configuration
        Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
        try {
            cfg.load();
            applicationId = cfg.getString("applicationId", "", "applicationId", "this is a comment");

        } catch (Exception ex) {
            event.getModLog().error("Failed to load configuration");
        } finally {
            if (cfg.hasChanged()) {
                cfg.save();
            }
        }


    }

    public Main() {
        MinecraftForge.EVENT_BUS.register(new Events());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

        proxy.init();
        rpcClient = new EventPresence();

        proxy.rpcinit();
        proxy.rpcupdate("Dans le menu.", null);


    }


}

