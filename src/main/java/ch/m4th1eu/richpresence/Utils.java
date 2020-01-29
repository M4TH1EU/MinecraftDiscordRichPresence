package ch.m4th1eu.richpresence;

import ch.m4th1eu.json.JSONObject;
import net.minecraft.client.Minecraft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

public class Utils {

    public static final Utils instance = new Utils();

    public static String readTextFromURL(String url) throws IOException {
        URL urlObject;
        URLConnection uc;
        StringBuilder parsedContentFromUrl = new StringBuilder();
        urlObject = new URL(url);
        uc = urlObject.openConnection();
        uc.connect();
        uc = urlObject.openConnection();
        uc.addRequestProperty("User-Agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        uc.getInputStream();
        InputStream is = uc.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

        int ch;
        while ((ch = in.read()) != -1) {
            parsedContentFromUrl.append((char) ch);
        }
        return parsedContentFromUrl.toString();
    }

    /**
     * @author M4TH1EU_
     */
    public String replaceArgsString(String variable) {
        String serverip = Main.config_object.getString("server-ip");
        String serverport = Main.config_object.getString("server-port");

        try {
            variable = variable.replaceAll("%player-name%", Minecraft.getMinecraft().getSession().getUsername());
            variable = variable.replaceAll("%server-connected-player%", readTextFromURL("https://api.serveurs-minecraft.com/api.php?Joueurs_En_Ligne_Ping&ip=" + serverip + "&port=" + serverport));
            variable = variable.replaceAll("%server-max-slot%", readTextFromURL("https://api.serveurs-minecraft.com/api.php?Joueurs_Maximum_Ping&ip=" + serverip + "&port=" + serverport));
        } catch (Exception e) {
        }

        return variable;
    }

    public void updateStatus(int id) {
        Thread t = new Thread(() -> {

            JSONObject onQuitServer = Main.config_object.getJSONObject("advanced-status-custom").getJSONObject("onQuitServer");
            JSONObject onJoinServer = Main.config_object.getJSONObject("advanced-status-custom").getJSONObject("onJoinServer");
            JSONObject inPauseMenu = Main.config_object.getJSONObject("advanced-status-custom").getJSONObject("inPauseMenu");
            JSONObject inMainMenu = Main.config_object.getJSONObject("advanced-status-custom").getJSONObject("inMainMenu");
            JSONObject inInventory = Main.config_object.getJSONObject("advanced-status-custom").getJSONObject("inInventory");

            switch (id) {
                case 0:
                    Main.proxy.rpcupdate(replaceArgsString(Main.config_object.getJSONObject("advanced-status-custom").getJSONObject("inMainMenu").getString("message")), null, false);
                    break;
                case 1:
                    Main.proxy.rpcupdate(replaceArgsString(onJoinServer.getString("message")), null, false);
                    break;
                case 2:
                    Main.proxy.rpcupdate(replaceArgsString(onQuitServer.getString("message")), null, false);
                    break;
                case 3:
                    Main.proxy.rpcupdate(replaceArgsString(inPauseMenu.getString("message")), null, false);
                    break;
                case 4:
                    Main.proxy.rpcupdate(replaceArgsString(inMainMenu.getString("message")), null, false);
                    break;
                case 5:
                    Main.proxy.rpcupdate(replaceArgsString(inInventory.getString("message")), null, false);
                    break;
                case 6:
                    Main.proxy.rpcupdate(replaceArgsString(Main.config_object.getJSONObject("advanced-status-custom").getJSONObject("onJoinServer").getString("message")), null, false);
                    break;
                default:
                    break;
            }
        });

        t.start();


    }
}

