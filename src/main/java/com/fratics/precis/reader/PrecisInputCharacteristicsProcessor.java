package com.fratics.precis.reader;

import com.fratics.precis.fis.base.PrecisProcessor;
import com.fratics.precis.fis.base.PrecisStream;
import com.fratics.precis.fis.base.ValueObject;

public class PrecisInputCharacteristicsProcessor extends PrecisProcessor {

    private PrecisStream ps = null;

    public PrecisInputCharacteristicsProcessor(PrecisStream ps) {
        this.ps = ps;
    }

    public boolean initialize() throws Exception {
        return this.ps.initialize();
    }

    public boolean unInitialize() throws Exception {
        return this.ps.unInitialize();
    }

    public boolean process(ValueObject o) throws Exception {
        String[] str = null;
        while ((str = ps.readStream()) != null) {
            o.inputObject.loadInputCharacteristics(str);
        }
        // Set the Number of Lines as Well.
        o.inputObject.setNoOfLines(ps.getNoOfLines());
        // logger.info(Arrays.toString(o.inputObject.getFieldObjects()));
        return true;
    }
}
