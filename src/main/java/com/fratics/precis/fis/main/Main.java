package com.fratics.precis.fis.main;

import com.fratics.precis.fis.main.count.CountPrecisMain;
import com.fratics.precis.fis.main.metrics.MetricsPrecisMain;
import com.fratics.precis.fis.util.PrecisConfigProperties;
import com.fratics.precis.util.ConfigObject;
import com.fratics.precis.util.HierarchyDimsNegation;
import com.fratics.precis.util.Logger;
import com.fratics.precis.util.ThresholdCalculator;

import java.io.File;

/*
 * Main Driver for Precis Application Engine.
 */

public class Main {

    // prints the command line usage function.
    private static void printUsage() {
        System.out.println();
        System.out.println();
        System.out.print("Usage :: java com.fratics.precis.fis.main.Main [${fileName}]");
        System.out.println("Optional Argument - Configuration File Name, If Configuration file is not Specified");
        System.out.println("Default Configuration file in location ./conf/precisconfig.properties will be loaded");
        System.out.println();
        System.out.println();
    }

    public static void main(String[] args) {
        try {

            // Check for configuration file in command line args.
            if (args.length > 0) {
                if (!new File(args[0]).exists())
                    throw new Exception("Configuration File " + args[0] + " doesn't exist");
                ConfigObject.setConfigFile(args[0]);
            }

            // Initialize the precis configuration.
            PrecisConfigProperties.init();

            //Initialize Threshold calculator
            if (PrecisConfigProperties.USE_THRESHOLD_GEN) {
                ThresholdCalculator thresholdCalculator = ThresholdCalculator.getInstance();
                thresholdCalculator.initialize();
            }

            //Initialize Hierarchy Dims Negation.
            if (PrecisConfigProperties.HIERARCHY_DIMS_ENABLED) {
                HierarchyDimsNegation hierarchyDimsNegation = HierarchyDimsNegation.getInstance();
                hierarchyDimsNegation.initialize();
            }
            //Logging Init
            if (PrecisConfigProperties.LOGGING_ENABLED) {
                Logger logger = Logger.getInstance();
                logger.initialize();
            }

            // Verify, its either a count precis (or) metrics precis.
            // and launch the desired Precis.
            if (PrecisConfigProperties.IS_COUNT_PRECIS) {
                CountPrecisMain.run(args);
            } else {
                MetricsPrecisMain.run(args);
            }
        } catch (Exception e) {
            System.err.println("Exception Raised ::" + e.toString());
            printUsage();
            e.printStackTrace();
        }
    }

}
