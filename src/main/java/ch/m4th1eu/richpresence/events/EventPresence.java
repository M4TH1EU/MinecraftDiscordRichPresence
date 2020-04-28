package ch.m4th1eu.richpresence.events;

import ch.m4th1eu.richpresence.Main;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

/**
 * @author M4TH1EU_#0001
 */
public class EventPresence {

    public static long oldTime;
    private static Thread callbackRunner;

    public synchronized static void init() {
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        DiscordRPC.discordInitialize(Main.applicationId, handlers, true);
        if (EventPresence.callbackRunner == null) {
            (EventPresence.callbackRunner = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    DiscordRPC.discordRunCallbacks();
                }
                return;
            }, "RPC-Callback-Handler")).start();
        }
        Main.logger.info("EventPresence has been started.");
    }

    public static void updatePresence(String details, String action, Boolean changeTime) {
        DiscordRichPresence presence = new DiscordRichPresence();
        presence.largeImageKey = Main.largeimage;
        presence.largeImageText = Main.largeimagetext;
        if (details != null) {
            presence.details = details;

            if (changeTime) {
                presence.startTimestamp = System.currentTimeMillis() / 1000L;
                oldTime = presence.startTimestamp;
            } else {
                presence.startTimestamp = oldTime;
            }
        }

        if (action != null) {
            presence.state = action;
        }
        DiscordRPC.discordUpdatePresence(presence);
    }
}
