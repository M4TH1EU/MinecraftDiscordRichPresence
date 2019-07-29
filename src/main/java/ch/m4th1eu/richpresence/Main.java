package ch.m4th1eu.richpresence;

import ch.m4th1eu.json.JSONObject;
import ch.m4th1eu.richpresence.events.AdvancedStatusEvent;
import ch.m4th1eu.richpresence.events.EventPresence;
import ch.m4th1eu.richpresence.proxy.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author M4TH1EU_#0001
 */
@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION)
public class Main {
    public static final String MODID = "richpresence";
    public static final String NAME = "Discord Rich Presence";
    public static final String VERSION = "1.5";

    @Mod.Instance(Main.MODID)
    public static Main instance;

    @SidedProxy(clientSide = "ch.m4th1eu.richpresence.proxy.ClientProxy", serverSide = "ch.m4th1eu.richpresence.proxy.CommonProxy")
    public static CommonProxy proxy;

    /**
     * Variables pour la config
     */
    public static String applicationId, largeimage, largeimagetext;
    public static String config_file_text = "";
    public static JSONObject config_object;


    public static Logger logger;
    public EventPresence rpcClient;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event.getSuggestedConfigurationFile());

        //Configuration
        event.getModConfigurationDirectory().mkdir();


        InputStream in = getClass().getResourceAsStream("/config/richpresence.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        try {
            config_file_text = IOUtils.toString(reader);
            config_object = new JSONObject(config_file_text);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (config_file_text != null || config_file_text.length() < 10) {
            event.getModLog().warn("Impossible de charger la configuration du mod : " + Main.MODID);

        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(instance);
        MinecraftForge.EVENT_BUS.register(new AdvancedStatusEvent());


        applicationId = config_object.getJSONObject("application-settings").getString("applicationID");
        largeimage = config_object.getJSONObject("application-settings").getString("large-image-name");
        largeimagetext = Utils.instance.replaceArgsString(config_object.getJSONObject("application-settings").getString("large-image-text"));

        proxy.init();
        rpcClient = new EventPresence();

        proxy.rpcinit();

        if (config_object.getJSONObject("advanced-status-custom").getJSONObject("inMainMenu").getBoolean("enable")) {
            proxy.rpcupdate(config_object.getJSONObject("advanced-status-custom").getJSONObject("inMainMenu").getString("message"), null, true);
        } else {
            proxy.rpcupdate("", null, false);
        }

    }


}

