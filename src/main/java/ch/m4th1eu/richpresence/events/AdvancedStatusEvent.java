package ch.m4th1eu.richpresence.events;

import ch.m4th1eu.json.JSONObject;
import ch.m4th1eu.richpresence.Utils;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.io.File;
import java.net.URISyntaxException;

/**
 * @author M4TH1EU_#0001
 */
@Mod.EventBusSubscriber
public class AdvancedStatusEvent {

    JSONObject config = null;

    {
        try {
            config = new JSONObject(Utils.readFileToString(new File(getClass().getResource("/config/richpresence.json").toURI())));
        } catch (
                URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onJoinServer(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        JSONObject onJoinServer = config.getJSONObject("advanced-status-custom").getJSONObject("onJoinServer");

        if (onJoinServer.getBoolean("enable")) {
            Utils.instance.updateStatus(1);
        }
    }

    @SubscribeEvent
    public void onQuitServer(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        JSONObject onQuitServer = config.getJSONObject("advanced-status-custom").getJSONObject("onQuitServer");

        if (onQuitServer.getBoolean("enable")) {
            Utils.instance.updateStatus(1);
        }
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {

        JSONObject inPauseMenu = config.getJSONObject("advanced-status-custom").getJSONObject("inPauseMenu");
        JSONObject inMainMenu = config.getJSONObject("advanced-status-custom").getJSONObject("inMainMenu");
        JSONObject inInventory = config.getJSONObject("advanced-status-custom").getJSONObject("inInventory");

        if (inPauseMenu.getBoolean("enable")) {
            if (event.getGui() instanceof GuiIngameMenu) {
                Utils.instance.updateStatus(3);
            }
        }

        if (inMainMenu.getBoolean("enable")) {
            if (event.getGui() instanceof GuiMainMenu) {
                Utils.instance.updateStatus(4);
            }
        }

        if (inInventory.getBoolean("enable")) {
            if (event.getGui() instanceof GuiInventory) {
                Utils.instance.updateStatus(5);
            }
        }

        if (event.getGui() == null) {
            if (config.getJSONObject("advanced-status-custom").getJSONObject("onJoinServer").getBoolean("enable")) {
                Utils.instance.updateStatus(6);
            }
        }
    }
}
