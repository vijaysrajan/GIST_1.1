package com.fratics.precis.fis.schema;

import com.fratics.precis.exception.PrecisException;
import com.fratics.precis.fis.base.PrecisProcessor;
import com.fratics.precis.fis.base.PrecisStream;
import com.fratics.precis.fis.base.Schema;
import com.fratics.precis.fis.base.ValueObject;

public class PrecisSchemaProcessor extends PrecisProcessor {
    private PrecisStream ps = null;

    public PrecisSchemaProcessor(PrecisStream ps) {
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
        Schema schema = new Schema();
        int i = 0;
        int metricCount = 0;
        while ((str = ps.readStream()) != null) {
            if (str[3].equalsIgnoreCase("t")) {
                Schema.FieldType fieldType;
                if (str[1].equalsIgnoreCase("d")) {
                    fieldType = Schema.FieldType.DIMENSION;
                } else {
                    fieldType = Schema.FieldType.METRIC;
                    o.inputObject.setMetricIndex(i);
                    o.inputObject.setMetricName(str[0]);
                    if (metricCount > 0)
                        throw new PrecisException("More than 1 Metric Count in Schema");
                    metricCount++;
                }
                schema.addSchemaElement(str[0], i, fieldType);
            }
            i++;
        }
        o.inputObject.loadSchema(schema);
        return true;
    }
}
