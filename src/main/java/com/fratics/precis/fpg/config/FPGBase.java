package com.fratics.precis.fpg.config;

public class FPGBase {

    // A Simple GUID / Instance id for this Precis Object.
    public static String id = Util.generateRandomId();

    // A default Initializer / template.
    public boolean initialize() throws Exception {
        return true;
    }

    // A default unInitializer / template.
    public boolean unInitialize() throws Exception {
        return true;
    }

    // A default reInitializer / template.
    public boolean reInitialize() throws Exception {
        if (this.unInitialize())
            return this.initialize();
        return false;
    }
}

