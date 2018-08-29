package com.fratics.precis.fis.base;

import java.io.Serializable;

/*
 * Value Object is a Serializable Holder Object for both InputObject & OutputObject.
 * 
 * The Value Object is passed along to all the Flow Processors in sequence.
 * 
 */

public class ValueObject implements Serializable {
    private static final long serialVersionUID = 1590842569304698182L;
    public InputObject inputObject = null;
    public OutputObject resultObject = null;

    // A Simple toString() method, used in printing both Input & Output Objects.
    public String toString() {
        return inputObject.toString() + "\n" + resultObject.toString();
    }
}
