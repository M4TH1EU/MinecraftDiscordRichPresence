package ch.m4th1eu.richpresence;

import ch.m4th1eu.richpresence.events.AdvancedStatusEvent;
import ch.m4th1eu.richpresence.events.EventPresence;
import ch.m4th1eu.richpresence.proxy.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

/**
 * @author M4TH1EU_#0001
 */
@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION)
public class Main {
    public static final String MODID = "richpresence";
    public static final String NAME = "Discord Rich Presence";
    public static final String VERSION = "1.1";
    @SidedProxy(clientSide = "ch.m4th1eu.richpresence.proxy.ClientProxy", serverSide = "ch.m4th1eu.richpresence.proxy.CommonProxy")
    public static CommonProxy proxy;

    /**
     * Variables pour la config
     */
    public static String applicationId, largeimage, largeimagetext;
    public static boolean advancedstatus;


    public static Logger logger;
    public EventPresence rpcClient;


    public Main() {
        MinecraftForge.EVENT_BUS.register(new AdvancedStatusEvent());
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event.getSuggestedConfigurationFile());

        //Configuration
        Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
        try {
            cfg.load();
            applicationId = cfg.getString("applicationID", "DiscordRichPresence", "ex: 491941559181246465", "Ici mettez le client id de votre application (https://bit.ly/2Lu1CC3).");
            largeimage = cfg.getString("largeimage", "DiscordRichPresence", "logo", "Ici mettez le nom de votre image (celle dans RichPresence -> Art Assets). (sans le .png)");
            largeimagetext = cfg.getString("largeimagetext", "DiscordRichPresence", "Mon serveur !", "Ici mettez le texte qui s'affichera quand vous passerez la souris sur l'image.");

            advancedstatus = cfg.getBoolean("advancedstatus", "DiscordRichPresence", true, "Ici laissez \"true\" si vous voulez un status avancé sinon mettez \"false\".");

        } catch (Exception ex) {
            event.getModLog().error("Failed to load configuration");
        } finally {
            if (cfg.hasChanged()) {
                cfg.save();
            }
        }


    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

        proxy.init();
        rpcClient = new EventPresence();

        proxy.rpcinit();
        if (Main.advancedstatus) {
            proxy.rpcupdate("Dans le menu.", null);
        } else {
            proxy.rpcupdate("", null);

        }

    }


}

