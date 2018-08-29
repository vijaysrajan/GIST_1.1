package com.fratics.precis.fis.base;

import java.io.Serializable;

/*
 * The Precis Output Object Captures all the results that need to be generated at the end of a Precis run.
 * The candidates generated can be persisted here, and all the stage candidate files can be generated 
 * as a single flow processor.
 * 
 * This object is also used to capture the results of the rules applied by the sanitation process.
 * 
 */

public abstract class OutputObject implements Serializable {

    private static final long serialVersionUID = -6326248205032370705L;

    // Initialization method,
    public abstract void init(int noOfFields);

    // Uploads/persists the results of the processing.
    public abstract void loadResult(Object o) throws Exception;

    // Checker method for Initializatioln.
    protected abstract boolean isInitialized();

    // A simple toString() method to provide the state of
    // the current stage of Precis Processing.
    public abstract String toString();
}
