package ch.m4th1eu.richpresence;

import ch.m4th1eu.json.JSONObject;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

public class Utils {

    public static final Utils instance = new Utils();

    private JSONObject config;

    {
        try {
            config = new JSONObject(Utils.readFileToString(new File(getClass().getResource("/config/richpresence.json").toURI())));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    /**
     * @author Nathanaël#4314
     */
    public static String readFileToString(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            String ls = System.getProperty("line.separator");
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            reader.close();
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

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
        File config_file = null;
        try {
            config_file = new File(getClass().getResource("/config/richpresence.json").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        JSONObject config = new JSONObject(Utils.readFileToString(config_file));

        String serverip = config.getString("server-ip");
        String serverport = config.getString("server-port");

        try {
            variable = variable.replaceAll("%player-name%", Minecraft.getMinecraft().getSession().getUsername());
            variable = variable.replaceAll("%server-connected-player%", readTextFromURL("https://minecraft-api.com/api/ping/playeronline.php?ip=" + serverip + "&port=" + serverport));
            variable = variable.replaceAll("%server-max-slot%", readTextFromURL("https://minecraft-api.com/api/ping/maxplayer.php?ip=" + serverip + "&port=" + serverport));
        } catch (Exception e) {
        }

        return variable;
    }

    public void updateStatus(int id) {
        Thread t = new Thread(() -> {

            JSONObject onQuitServer = config.getJSONObject("advanced-status-custom").getJSONObject("onQuitServer");
            JSONObject onJoinServer = config.getJSONObject("advanced-status-custom").getJSONObject("onJoinServer");
            JSONObject inPauseMenu = config.getJSONObject("advanced-status-custom").getJSONObject("inPauseMenu");
            JSONObject inMainMenu = config.getJSONObject("advanced-status-custom").getJSONObject("inMainMenu");
            JSONObject inInventory = config.getJSONObject("advanced-status-custom").getJSONObject("inInventory");

            switch (id) {
                case 0:
                    Main.proxy.rpcupdate(replaceArgsString(config.getJSONObject("advanced-status-custom").getJSONObject("inMainMenu").getString("message")), null, false);
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
                    Main.proxy.rpcupdate(replaceArgsString(config.getJSONObject("advanced-status-custom").getJSONObject("onJoinServer").getString("message")), null, false);
                    break;
                default:
                    break;
            }
        });

        t.start();


    }
}

