package com.fratics.precis.fis.threshold;

import com.fratics.precis.fis.base.MutableDouble;
import com.fratics.precis.fis.base.PrecisProcessor;
import com.fratics.precis.fis.base.ValueObject;
import com.fratics.precis.util.Logger;

import java.util.Arrays;
import java.util.Collections;

public class ThresholdProcessor extends PrecisProcessor {
    private Logger logger = Logger.getInstance();

    public boolean process(ValueObject vo) throws Exception {
        MutableDouble[] mb = new MutableDouble[0];
        MutableDouble[] c = vo.inputObject.thresholdCounter.values().toArray(mb);
        Arrays.sort(c, Collections.reverseOrder());
        ThresholdContainer threshold = new ThresholdContainer();
        int i = 1;
        for (MutableDouble s : c) {
            double value = s.get();
            double tmp = Math.sqrt(Math.pow(i, 2) + Math.pow(value, 2));
            logger.info("i = " + i + " value = " + value + " distance = " + tmp);
            if (tmp < threshold.threshold) {
                threshold.threshold = tmp;
                threshold.value = value;
            }
            i++;
        }
        logger.info("Threshold Value ::" + threshold);
        return true;
    }

    private class ThresholdContainer {
        public double threshold = Double.MAX_VALUE;
        public double value = 1.0;

        public String toString() {
            return "threshold distance = " + threshold + " value = " + value;
        }
    }
}