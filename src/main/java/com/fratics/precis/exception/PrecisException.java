package com.fratics.precis.exception;

/*
 * A simple Exception class to envelop any unexpected events that the precis application throws.
 * Need to think from a framework perspective and try capture all the events from logging, monitoring,
 * instrumentation per se. 
 * 
 */

public class PrecisException extends Exception {
    private static final long serialVersionUID = -5057736247093243420L;

    public PrecisException(String str) {
        super(str);
    }
}
