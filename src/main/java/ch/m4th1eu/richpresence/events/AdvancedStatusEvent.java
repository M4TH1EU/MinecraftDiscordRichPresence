package ch.m4th1eu.richpresence.events;

import ch.m4th1eu.richpresence.Main;
import net.minecraft.client.gui.GuiIngameMenu;
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
    public void onServerJoin(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        if (Main.advancedstatus) {
            Main.proxy.rpcupdate("En jeu.", null);
        }
    }

    @SubscribeEvent
    public void onQuitServer(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        if (Main.advancedstatus) {

            Main.proxy.rpcupdate("Dans le menu principal.", null);
        }
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (Main.advancedstatus) {
            if (event.getGui() instanceof GuiIngameMenu) {
                Main.proxy.rpcupdate("Dans le menu pause.", null);
            }

            System.out.println(event.getGui());
        }

    }


}
