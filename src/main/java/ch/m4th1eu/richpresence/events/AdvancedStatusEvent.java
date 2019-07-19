package ch.m4th1eu.richpresence.events;

import ch.m4th1eu.json.JSONObject;
import ch.m4th1eu.richpresence.Main;
import ch.m4th1eu.richpresence.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.io.File;

/**
 * @author M4TH1EU_#0001
 */
@Mod.EventBusSubscriber
public class AdvancedStatusEvent {

    File config_file = new File(Minecraft.getMinecraft().mcDataDir, "\\config\\" + Main.MODID + ".json");
    JSONObject config = new JSONObject(Utils.readFileToString(config_file));

    @SubscribeEvent
    public void onServerJoin(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        if (Main.advancedstatus) {
            Main.proxy.rpcupdate(config.getJSONArray("advanced-status-custom").getJSONObject(0).getString("message"), null);
        }
    }

    @SubscribeEvent
    public void onQuitServer(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        if (Main.advancedstatus) {

            Main.proxy.rpcupdate(config.getJSONArray("advanced-status-custom").getJSONObject(1).getString("message"), null);
        }
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (Main.advancedstatus) {
            if (event.getGui() instanceof GuiIngameMenu) {
                Main.proxy.rpcupdate(config.getJSONArray("advanced-status-custom").getJSONObject(2).getString("message"), null);
            }
        }
    }


}
