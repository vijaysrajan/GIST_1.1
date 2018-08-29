package com.fratics.precis.fis.feed.dimval;

import com.fratics.precis.fis.base.FieldObject;
import com.fratics.precis.fis.base.MutableDouble;
import com.fratics.precis.fis.base.ValueObject;
import com.fratics.precis.fis.util.PrecisConfigProperties;

import java.util.Iterator;
import java.util.Map;

/*
 * DimValIndex Generates the values from the Input Characteristics Loaded from the Input feed.
 * This Class checks if the metrics passed the threshold, adds the dimension value to the
 * Dimension & Dimension Value Maps.
 * 
 * Two types of maps are created, they are:-
 * 1) Dimension map.
 * 2) Dimension Value map.
 * 
 * The reverse index maps are also created for the above.
 * 
 */

public class DimValIndex extends DimValIndexBase {

    // Threshold for the entire Precis application.
    private double threshold = 0.0;

    public DimValIndex() {
    }

    public DimValIndex(double threshold) {
        this.threshold = threshold;
    }

    // Read the input characteristics, apply thershold to the field values,
    // Add them to the Dim / Dim Val Maps.
    public boolean process(ValueObject o) throws Exception {
        int valIndex = 0;
        int dimIndex = 0;
        this.threshold = thresholdCalculator.getThresholdValue(1);
        o.inputObject.setThreshold(this.threshold);
        FieldObject[] fi = o.inputObject.getFieldObjects();
        StringBuilder tmpSB = new StringBuilder(2048);
        for (int i = 0; i < fi.length; i++) {
            Map<String, MutableDouble> map = fi[i].getMap();
            Iterator<String> it = map.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                MutableDouble val = map.get(key);
                if (val.get() >= threshold) {
                    if (!dimMap.containsKey(fi[i].getSchemaElement().fieldName)) {
                        dimMap.put(fi[i].getSchemaElement().fieldName, dimIndex);
                        revDimMap.put(dimIndex, fi[i].getSchemaElement().fieldName);
                        dimIndex++;
                    }
                    tmpSB.setLength(0);
                    tmpSB.append(fi[i].getSchemaElement().fieldName);
                    tmpSB.append(PrecisConfigProperties.OUTPUT_DIMVAL_SEPERATOR);
                    tmpSB.append(key);
                    
                    if (!dimValMap.containsKey(tmpSB.toString())) {
                        dimValMap.put(tmpSB.toString(), valIndex);
                        valIndex++;
                    }
                }
            }
        }
        // Increment the DimVal Index by the Dim Size().
        // Create the reverse index maps also.
        int inc = dimMap.size();
        Iterator<String> it = dimValMap.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            Integer m = dimValMap.get(key);
            m += inc;
            dimValMap.put(key, m);
            revDimValMap.put(m, key);
        }
        // Dump the feed if necessary.
        if (PrecisConfigProperties.DUMP_DIM_FEED)
            this.dump();
        return true;
    }
}
