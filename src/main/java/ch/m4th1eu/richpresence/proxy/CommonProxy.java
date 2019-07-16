package ch.m4th1eu.richpresence.proxy;

import java.io.File;

/**
 * @author M4TH1EU_#0001
 */
public class CommonProxy {

    public void preInit(File configFile) {
        System.out.println("Pre-init server-side");
    }

    public void init() {

    }


    public void rpcinit() {
    }

    public void rpcupdate(String details, String action) {
    }

}
