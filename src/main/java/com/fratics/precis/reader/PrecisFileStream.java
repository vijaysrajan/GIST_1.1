package com.fratics.precis.reader;

import com.fratics.precis.exception.PrecisException;
import com.fratics.precis.fis.base.PrecisStream;
import com.fratics.precis.fis.util.PrecisConfigProperties;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class PrecisFileStream extends PrecisStream {

    private BufferedReader br = null;
    private String recordSeperator = PrecisConfigProperties.INPUT_RECORD_SEPERATOR;

    public PrecisFileStream(String fileName) {
        super(fileName);
    }

    public PrecisFileStream(String fileName, String recordSeperator) {
        super(fileName);
        this.recordSeperator = recordSeperator;
    }

    public boolean initialize() throws Exception {
        File file = new File(this.getStreamName());
        if (!(file.exists() && file.canRead()))
            throw new PrecisException("Error Reading file:: " + this.getStreamName());
        try {
            this.br = new BufferedReader(new FileReader(file));
        } catch (Exception e) {
            // log error
            throw e;
        }
        return true;
    }

    public boolean unInitialize() throws Exception {
        try {
            this.br.close();
            return true;
        } catch (Exception e) {
            // log error
            throw e;
        }
    }

    public String[] readStream() throws Exception {
        String[] str = read();
        if (str != null)
            this.incLines();
        return str;
    }

    private String[] read() throws Exception {
        try {
            String thisLine = br.readLine();
            // Handle Comments
            while (thisLine != null && thisLine.startsWith("#"))
                thisLine = br.readLine();
            if (thisLine != null) {
                return thisLine.split(this.recordSeperator, -1); // added limit
                // of -1 to
                // allow
                // trailing
                // empty
                // strings.
            } else {
                return null;
            }
        } catch (IOException e) {
            // log error
            throw e;

        } catch (Exception ex) {
            // log error
            throw new PrecisException(ex.getMessage());
        }
    }
}
