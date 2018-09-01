package com.fratics.precis.fis.schema;

import com.fratics.precis.exception.PrecisException;
import com.fratics.precis.fis.base.PrecisProcessor;
import com.fratics.precis.fis.base.PrecisStream;
import com.fratics.precis.fis.base.Schema;
import com.fratics.precis.fis.base.ValueObject;

public class PrecisSchemaProcessor extends PrecisProcessor {
    //private PrecisStream ps = null;
	String schema_string = "";
	
	boolean isCountPrecisOnly = false;
	boolean isSingleMetricPrecisOnly = false;
	boolean isMultiMetricPrecis = false;

	public PrecisSchemaProcessor(String schema) {
        this.schema_string = schema;
    }

    //dummy function
    public boolean initialize() throws Exception {
        //return this.ps.initialize();
    		return true;
    }

    //dummy function
    public boolean unInitialize() throws Exception {
        return true; //this.ps.unInitialize();
    }

    public boolean process(ValueObject o) throws Exception {
        String[] strArr = this.schema_string.split(";",-1);
        Schema schema = new Schema();
        int i = 0;
        int metricCount = 0;
        
        //while ((str = ps.readStream()) != null) {
        for (String s : strArr) {
        		String[] str = s.split(":", -1);
        		if (str.length ==2) { //indicating countPrecis
                double threshold = Double.parseDouble(str[1]);
        			MetricListFromSchema.addMetricDetails(new MetricDetails (str[0],threshold,true));
        		} else  if (str[3].equalsIgnoreCase("t")) {
                Schema.FieldType fieldType;
                if (str[1].equalsIgnoreCase("d")) {
                    fieldType = Schema.FieldType.DIMENSION;
                } else {
                    fieldType = Schema.FieldType.METRIC;
                    o.inputObject.setMetricIndex(i);
                    o.inputObject.setMetricName(str[0]);
                    //if (metricCount > 0)
                    //    throw new PrecisException("More than 1 Metric Count in Schema");
                    double threshold = -2;
                    if(str.length == 5) {
                    		threshold = Double.parseDouble(str[4]);
                    }
                    MetricListFromSchema.addMetricDetails(new MetricDetails (str[0],i,true,threshold));
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
