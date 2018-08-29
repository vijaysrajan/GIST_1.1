package com.fratics.precis.fis.threshold;

import com.fratics.precis.fis.base.InputObject;
import com.fratics.precis.fis.base.MutableDouble;

public class ThresholdInputObject extends InputObject {
    protected static final String DEF_VALUE = "";
    private static final long serialVersionUID = 6369612372079922497L;

    protected boolean isInitialized() {
        return true;
    }

    public String toString() {
        return "\nNo of Records :: " + thresholdCounter.size() + "\n\n";
    }

    public void loadInputCharacteristics(Object o) throws Exception {
        String[] str = (String[]) o;
        // No Schema will be provided.
        for (int i = 0; i < str.length; i++) {
            String field_value = "";
            if (str[i] == null) {
                field_value = "field" + i + DEF_VALUE;
            } else {
                field_value = "field" + i + str[i];
            }

            MutableDouble x = thresholdCounter.get(field_value);
            if (x == null) {
                thresholdCounter.put(field_value, new MutableDouble());
            } else {
                x.inc();
            }
        }
    }
}
