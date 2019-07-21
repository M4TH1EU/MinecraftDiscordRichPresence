package ch.m4th1eu.richpresence;

import ch.m4th1eu.json.JSONObject;
import ch.m4th1eu.richpresence.events.AdvancedStatusEvent;
import ch.m4th1eu.richpresence.events.EventPresence;
import ch.m4th1eu.richpresence.proxy.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * @author M4TH1EU_#0001
 */
@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION)
public class Main {
    public static final String MODID = "richpresence";
    public static final String NAME = "Discord Rich Presence";
    public static final String VERSION = "1.2";

    @Mod.Instance(Main.MODID)
    public static Main instance;

    @SidedProxy(clientSide = "ch.m4th1eu.richpresence.proxy.ClientProxy", serverSide = "ch.m4th1eu.richpresence.proxy.CommonProxy")
    public static CommonProxy proxy;

    /**
     * Variables pour la config
     */
    public static String applicationId, largeimage, largeimagetext;


    public static Logger logger;
    public EventPresence rpcClient;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event.getSuggestedConfigurationFile());

        //Configuration
        event.getModConfigurationDirectory().mkdir();
        File config_file = new File(event.getModConfigurationDirectory(), "\\" + Main.MODID + ".json");

        if (!config_file.exists() || config_file.length() < 10) {
            try {
                event.getModLog().warn("Impossible de charger la configuration du mod : " + Main.MODID);
                event.getModLog().warn("Création du fichier de configuration");

                PrintWriter writer = new PrintWriter(config_file, "UTF-8");
                writer.println("{\n" +
                        "  \"_comment\": \"Variables disponibles :\",\n" +
                        "  \"_comment2\": \"%player-name% - Nom du joueur.\",\n" +
                        "  \"_comment3\": \"%server-connected-player% - Nombre de joueur connecté au serveur.\",\n" +
                        "  \"_comment4\": \"%server-max-slot% - Nombre de slots du serveur\",\n" +
                        "  \"server-ip\": \"mc.hypixel.net\",\n" +
                        "  \"server-port\": \"25565\",\n" +
                        "  \"application-settings\": {\n" +
                        "    \"applicationID\": \"601875975533232158\",\n" +
                        "    \"large-image-name\": \"discord_logo\",\n" +
                        "    \"large-image-text\": \"En train de tester ce mod !\"\n" +
                        "  },\n" +
                        "  \"advanced-status-custom\": {\n" +
                        "    \"onJoinServer\": {\n" +
                        "      \"enable\": true,\n" +
                        "      \"message\": \"En jeu.\"\n" +
                        "    },\n" +
                        "    \"onQuitServer\": {\n" +
                        "      \"enable\": true,\n" +
                        "      \"message\": \"Dans le menu principal.\"\n" +
                        "    },\n" +
                        "    \"inPauseMenu\": {\n" +
                        "      \"enable\": true,\n" +
                        "      \"message\": \"Dans le menu pause.\"\n" +
                        "    },\n" +
                        "    \"inMainMenu\": {\n" +
                        "      \"enable\": true,\n" +
                        "      \"message\": \"Dans le menu principal.\"\n" +
                        "    },\n" +
                        "    \"inInventory\": {\n" +
                        "      \"enable\": false,\n" +
                        "      \"message\": \"Dans l'inventaire.\"\n" +
                        "    }\n" +
                        "  }\n" +
                        "}");
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(instance);
        MinecraftForge.EVENT_BUS.register(new AdvancedStatusEvent());

        JSONObject config = new JSONObject(Utils.readFileToString(new File(Minecraft.getMinecraft().mcDataDir, "\\config\\" + Main.MODID + ".json")));

        applicationId = config.getJSONObject("application-settings").getString("applicationID");
        largeimage = config.getJSONObject("application-settings").getString("large-image-name");
        largeimagetext = Utils.replaceArgsString(config.getJSONObject("application-settings").getString("large-image-text"));

        proxy.init();
        rpcClient = new EventPresence();


        proxy.rpcinit();

        if (config.getJSONObject("advanced-status-custom").getJSONObject("inMainMenu").getBoolean("enable")) {
            proxy.rpcupdate(config.getJSONObject("advanced-status-custom").getJSONObject("inMainMenu").getString("message"), null, true);
        } else {
            proxy.rpcupdate("", null, false);
        }

    }


}

