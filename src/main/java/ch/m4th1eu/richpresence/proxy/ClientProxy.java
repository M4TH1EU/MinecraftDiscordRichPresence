package ch.m4th1eu.richpresence.proxy;

import ch.m4th1eu.richpresence.events.EventPresence;
import net.minecraftforge.common.MinecraftForge;

import java.io.File;

/**
 * @author M4TH1EU_#0001
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(File configFile) {
        super.preInit(configFile);
        System.out.println("Pre-init client-side");
    }

    @Override
    public void init() {
        super.init();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void rpcinit() {
        EventPresence.init();
    }

    @Override
    public void rpcupdate(String details, String action, Boolean changeTime) {
        EventPresence.updatePresence(details, action, changeTime);
    }
}
