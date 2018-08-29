package com.fratics.precis.fis.base;

/*
 * A simple definition of Precis Application Stream Object.
 * 
 * This is the base class of all precis data streams, which 
 * provides an abstract template for all the data streams.
 * 
 */

public abstract class PrecisStream extends PrecisBase {

    // No of lines in the stream.
    private long noOfLines = 0;

    // Stream Name.
    private String streamName = null;

    public PrecisStream(String streamName) {
        this.streamName = streamName;
    }

    public String getStreamName() {
        return streamName;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }

    // Abstract method to read stream.
    public abstract String[] readStream() throws Exception;

    // Increment method for no of Lines in the stream,
    // to be called in conjunction with the readStream() method.
    protected void incLines() {
        ++noOfLines;
    }

    public long getNoOfLines() {
        return this.noOfLines;
    }
}
