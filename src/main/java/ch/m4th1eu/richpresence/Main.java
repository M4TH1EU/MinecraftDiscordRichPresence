package ch.m4th1eu.richpresence;

import ch.m4th1eu.json.JSONArray;
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
import org.apache.logging.log4j.Logger;

import java.io.*;

/**
 * @author M4TH1EU_#0001
 */
@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION)
public class Main {
    public static final String MODID = "richpresence";
    public static final String NAME = "Discord Rich Presence";
    public static final String VERSION = "1.2";
    @SidedProxy(clientSide = "ch.m4th1eu.richpresence.proxy.ClientProxy", serverSide = "ch.m4th1eu.richpresence.proxy.CommonProxy")
    public static CommonProxy proxy;

    /**
     * Variables pour la config
     */
    public static String applicationId, largeimage, largeimagetext, serveurIP;
    public static boolean advancedstatus;


    public static Logger logger;
    public EventPresence rpcClient;


    public Main() {
        if (advancedstatus) {
            MinecraftForge.EVENT_BUS.register(new AdvancedStatusEvent());
        }
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event.getSuggestedConfigurationFile());

        //Configuration

        event.getModConfigurationDirectory().mkdir();
        File config_file = new File(event.getModConfigurationDirectory(), "\\" + Main.MODID + ".json");
        if (!config_file.exists()) {
            try {
                event.getModLog().warn("Impossible de charger la configuration du mod : " + Main.MODID);
                event.getModLog().warn("Création du fichier de configuration");
                config_file.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        if (config_file.length() < 10) {
            try {
                PrintWriter writer = new PrintWriter(config_file, "UTF-8");
                writer.println("{\n" +
                        "  \"application-settings\": [\n" +
                        "    {\n" +
                        "      \"applicationID\": \"601875975533232158\",\n" +
                        "      \"large-image-name\": \"discord_logo\",\n" +
                        "      \"large-image-text\": \"En train de tester ce mod !\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"advanced-status\": true,\n" +
                        "  \"advanced-status-custom\": [\n" +
                        "    {\n" +
                        "      \"onJoinServer\": {\n" +
                        "        \"message\": \"En jeu.\"\n" +
                        "      },\n" +
                        "      \"onQuitServer\": {\n" +
                        "        \"message\": \"Dans le menu principal.\"\n" +
                        "      },\n" +
                        "      \"inPauseMenu\": {\n" +
                        "        \"message\": \"Dans le menu pause\"\n" +
                        "      }\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}\n" +
                        "\n");
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }


        JSONObject config = new JSONObject(Utils.readFileToString(config_file));

        applicationId = config.getJSONObject("application-settings").getString("applicationID");
        largeimage = config.getJSONObject("application-settings").getString("large-image-name");
        largeimagetext = config.getJSONObject("application-settings").getString("large-image-text");

        advancedstatus = config.getBoolean("advanced-status");
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

        proxy.init();
        rpcClient = new EventPresence();

        proxy.rpcinit();
        if (advancedstatus) {
            proxy.rpcupdate("Dans le menu.", null);
        } else {
            proxy.rpcupdate("", null);

        }

    }


}

