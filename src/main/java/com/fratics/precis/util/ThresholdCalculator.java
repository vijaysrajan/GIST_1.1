package com.fratics.precis.util;

import com.fratics.precis.fis.base.PrecisBase;
import com.fratics.precis.fis.util.PrecisConfigProperties;

public class ThresholdCalculator extends PrecisBase {
    private static ThresholdCalculator thresholdCalculator;
    private double totalValue = 0;
    private double thresholdValues[] = new double[256]; //Max 255, ignore 0th slot
    private Logger logger = Logger.getInstance();

    private ThresholdCalculator() {
    }

    public static void main(String[] args) throws Exception {
        PrecisConfigProperties.init();
        ThresholdCalculator thresholdCalculator = ThresholdCalculator.getInstance();
        thresholdCalculator.initialize();
        Logger logger = Logger.getInstance();
        logger.initialize();
        thresholdCalculator.setTotalValue(100000);
        logger.info("" + thresholdCalculator.getThresholdValue(1));
        logger.info("" + thresholdCalculator.getThresholdValue(2));
        logger.info("" + thresholdCalculator.getThresholdValue(3));
        logger.info("" + thresholdCalculator.getThresholdValue(4));
        logger.info("" + thresholdCalculator.getThresholdValue(5));
        logger.info("" + thresholdCalculator.getThresholdValue(6));
        logger.info("" + thresholdCalculator.getThresholdValue(7));
        logger.info("" + thresholdCalculator.getThresholdValue(8));
        logger.info("" + thresholdCalculator.getThresholdValue(9));
    }

    public static ThresholdCalculator getInstance() {
        if (thresholdCalculator == null) {
            thresholdCalculator = new ThresholdCalculator();
        }
        return thresholdCalculator;
    }

    private void loadThresholds(String formulae) {
        //4:1,5:2,6:3,7:4,8:5
        int key = 0;
        double val = 0;
        int prevKey = 0;
        double prevVal = 0;
        String[] fields = formulae.split(",");
        for (int i = 0; i < fields.length; i++) {
            String[] tmp = fields[i].split(":");
            key = Integer.parseInt(tmp[0]);
            val = Double.parseDouble(tmp[1]);
            thresholdValues[key] = val;
            if (prevKey + 1 != key) {
                for (int j = prevKey + 1; j < key; j++)
                    thresholdValues[j] = prevVal;
            }
            prevKey = key;
            prevVal = val;
        }
        for (prevKey++; prevKey < 255; prevKey++) {
            thresholdValues[prevKey] = prevVal;
        }
    }

    public boolean initialize() {
        if (PrecisConfigProperties.USE_THRESHOLD_GEN) {
            loadThresholds(PrecisConfigProperties.THRESHOLD_GEN_FORMULA_AFTER_LEVEL_3);
        }
        return true;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        logger.info("Total Metric Value for Calculation of Threshold ::" + totalValue);
        this.totalValue = totalValue;
    }

    public double getThresholdValue(int currentStage) {
        double value = getThreshold(currentStage);
        logger.info("Threshold for Stage " + currentStage + " is " + value);
        return value;
    }

    private double getThreshold(int currentStage) {
        if (!PrecisConfigProperties.USE_THRESHOLD_GEN) return PrecisConfigProperties.THRESHOLD;
        if (currentStage <= 3) {
            if (PrecisConfigProperties.USE_THRESHOLD_PERCENTAGE_UPTO_LEVEL_3) {
                //generate percentage.
                logger.info("Threshold Percentage Applied ::" + PrecisConfigProperties.THRESHOLD_UPTO_LEVEL_3 + "%");
                return (PrecisConfigProperties.THRESHOLD_UPTO_LEVEL_3 * totalValue / 100);
            } else {
                logger.info("Threshold Absoulte Value Applied ::" + PrecisConfigProperties.THRESHOLD_UPTO_LEVEL_3);
                return PrecisConfigProperties.THRESHOLD_UPTO_LEVEL_3;
            }
        } else {
            if (PrecisConfigProperties.USE_THRESHOLD_PERCENTAGE_AFTER_LEVEL_3) {
                //logger.info("1:" + thresholdValues[currentStage] + " 2: " + totalValue);
                logger.info("Threshold Percentage Applied ::" + thresholdValues[currentStage] + "%");
                return (thresholdValues[currentStage] * totalValue / 100);
            } else {
                logger.info("Threshold Absoulte Value Applied ::" + thresholdValues[currentStage]);
                return thresholdValues[currentStage];
            }
        }
    }
}
