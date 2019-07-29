package ch.m4th1eu.richpresence.events;

import ch.m4th1eu.json.JSONObject;
import ch.m4th1eu.richpresence.Main;
import ch.m4th1eu.richpresence.Utils;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

/**
 * @author M4TH1EU_#0001
 */
@Mod.EventBusSubscriber
public class AdvancedStatusEvent {


    @SubscribeEvent
    public void onJoinServer(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        JSONObject onJoinServer = Main.config_object.getJSONObject("advanced-status-custom").getJSONObject("onJoinServer");

        if (onJoinServer.getBoolean("enable")) {
            Utils.instance.updateStatus(1);
        }
    }

    @SubscribeEvent
    public void onQuitServer(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        JSONObject onQuitServer = Main.config_object.getJSONObject("advanced-status-custom").getJSONObject("onQuitServer");

        if (onQuitServer.getBoolean("enable")) {
            Utils.instance.updateStatus(1);
        }
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {

        JSONObject inPauseMenu = Main.config_object.getJSONObject("advanced-status-custom").getJSONObject("inPauseMenu");
        JSONObject inMainMenu = Main.config_object.getJSONObject("advanced-status-custom").getJSONObject("inMainMenu");
        JSONObject inInventory = Main.config_object.getJSONObject("advanced-status-custom").getJSONObject("inInventory");

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
            if (Main.config_object.getJSONObject("advanced-status-custom").getJSONObject("onJoinServer").getBoolean("enable")) {
                Utils.instance.updateStatus(6);
            }
        }
    }
}
