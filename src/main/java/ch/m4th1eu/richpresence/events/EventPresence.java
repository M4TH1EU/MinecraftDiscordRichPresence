package ch.m4th1eu.richpresence.events;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

public class EventPresence {

    private static Thread callbackRunner;

    public synchronized static final void init() {
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        DiscordRPC.discordInitialize("BOT CLIENT ID", handlers, true, null);
        if (EventPresence.callbackRunner == null) {
            (EventPresence.callbackRunner = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    DiscordRPC.discordRunCallbacks();
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException ex) {
                    }
                }
                return;
            }, "RPC-Callback-Handler")).start();
        }
        System.out.println("EventPresence has been started.");
    }

    public static final void updatePresence(String details, String action) {
        DiscordRichPresence presence = new DiscordRichPresence();
        presence.largeImageKey = "image";
        presence.largeImageText = "TEXT";
        if (details != null) {
            presence.details = details;

            presence.startTimestamp = System.currentTimeMillis() / 1000L;
        } else if (action != null) {
            presence.state = action;
        }
        DiscordRPC.discordUpdatePresence(presence);
    }
}
