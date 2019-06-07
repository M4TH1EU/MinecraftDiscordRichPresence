package ch.m4th1eu.richpresence.events;

import ch.m4th1eu.richpresence.Main;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

@Mod.EventBusSubscriber
public class Events {

    @SubscribeEvent
    public void onServerJoin(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        Main.proxy.rpcupdate("En jeu.", null);
    }

    @SubscribeEvent
    public void onQuitServer(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        Main.proxy.rpcupdate("Dans le menu.", null);

    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiIngameMenu) {
            Main.proxy.rpcupdate("Dans le menu.", null);
        }

        System.out.println(event.getGui());

    }


}
