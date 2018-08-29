package com.fratics.precis.fis.base;

import com.fratics.precis.fis.util.Util;

/*
 * An Abstract Base Class of all the Precis Applications
 * Execution, Processing, Value Objects etc.
 * 
 * Provides default initialize, unInitialize & reinitialize methods
 * and their definitions to override.
 * 
 * Also give instance ids for all the Objects by default, in case later
 * we need to send events to these Precis Objects in future.
 * 
 */

public abstract class PrecisBase {

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
