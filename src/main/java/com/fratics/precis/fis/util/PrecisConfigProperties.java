package com.fratics.precis.fis.util;

import com.fratics.precis.util.ConfigObject;

import java.util.Arrays;
import java.util.HashSet;

public class PrecisConfigProperties {

    public static String OUTPUT_DIR = "./data/";
    public static String INPUT_RECORD_SEPERATOR = Character.toString('\001');
    public static String INPUT_DATA_FILE = "./data/dataFile";
    public static String OUTPUT_RECORD_SEPERATOR_DIMENSION = Character.toString('\001');
    public static String OUTPUT_RECORD_SEPERATOR_METRIC = Character.toString('\003');
    public static String OUTPUT_RECORD_SEPERATOR_STAGENUMBER = Character.toString('\004');
    public static String INPUT_SCHEMA_FILE = "./data/schemaFile";
    public static int NO_OF_STAGES = 8;
    public static long   GIST_MAX_NUMBER_OF_INSIGHTS=1000000;
    public static boolean GIST_GENERATE_SINGLE_OUTPUT_FILE = false;
    public static String OUPUT_CANDIDATE_FILE_PATTERN = "stage_${stage_number}_candidate_file.txt";
    public static String GIST_OUPUT_CANDIDATE_FILE="default_insights.txt";
    public static boolean IS_COUNT_PRECIS = false;
    public static boolean GENERATE_RAW_CANDIDATE_FILE = false;
    public static String COUNT_PRECIS_METRIC_NAME = "count";
    public static double THRESHOLD = 36000;
    public static String OUTPUT_DIMVAL_SEPERATOR = Character.toString('\002');
    public static String OUPUT_RAW_CANDIDATE_FILE_PATTERN = "stage_${stage_number}_raw_candidate_file.txt";
    public static String SCHEMA_RECORD_SEPERATOR = ":";
    public static String BITSET_FEED_FILENAME = "./data/bitSetFeed.txt";
    public static String DIM_FEED = "./data/dimFeed.txt";
    public static String DIMVAL_FEED = "./data/dimValFeed.txt";
    public static boolean DUMP_DIM_FEED = false;
    public static boolean DUMP_BITSET_FEED = false;
    public static HashSet<String> IGN_WORDS = new HashSet<String>(Arrays.asList("null", "", "0"));
    public static boolean HIERARCHY_DIMS_ENABLED = false;
    public static String HIERARCHY_DIM_GROUPS = null;
    public static boolean USE_THRESHOLD_GEN = false;
    public static boolean USE_THRESHOLD_PERCENTAGE_AFTER_LEVEL_3 = false;
    public static double THRESHOLD_UPTO_LEVEL_3 = 1;
    public static boolean USE_THRESHOLD_PERCENTAGE_UPTO_LEVEL_3 = false;
    public static String THRESHOLD_GEN_FORMULA_AFTER_LEVEL_3 = "4:1,5:2,6:3,7:4,8:5";
    public static boolean LOGGING_ENABLED = true;
    public static String LOGGING_LEVEL = "INFO";


    private static String convertSpecialChar(String s) {
        if (s.charAt(0) == '\\' && s.charAt(1) == 'u') {
            String tmp = s.substring(2);
            String ret = "";
            try {
                int hexVal = Integer.parseInt(tmp, 16);
                ret += hexVal;
                return ret;
            } catch (Exception e) {
                return null;
            }
        } else {
            return s;
        }
    }

    public static void init() throws Exception {
        ConfigObject c = new ConfigObject();
        c.initialize();
        loadConfig(c);
    }

    public static void loadConfig(ConfigObject c) {

        String tmp = c.getProperties().getProperty("OUPUT_DIR");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
            OUTPUT_DIR = tmp;
        }

        tmp = c.getProperties().getProperty("COUNT_PRECIS_METRIC_NAME");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
            COUNT_PRECIS_METRIC_NAME = tmp;
        }
        tmp = c.getProperties().getProperty("INPUT_RECORD_SEPERATOR");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
            String s = convertSpecialChar(tmp);
            if (s != null)
                INPUT_RECORD_SEPERATOR = s;
        }

        tmp = c.getProperties().getProperty("INPUT_DATA_FILE");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
            INPUT_DATA_FILE = tmp;
        }

        tmp = c.getProperties().getProperty("OUTPUT_RECORD_SEPERATOR_DIMENSION");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
            String s = convertSpecialChar(tmp);
            if (s != null)
                OUTPUT_RECORD_SEPERATOR_DIMENSION = s;
        }

        tmp = c.getProperties().getProperty("OUTPUT_RECORD_SEPERATOR_METRIC");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
            String s = convertSpecialChar(tmp);
            if (s != null)
                OUTPUT_RECORD_SEPERATOR_METRIC = s;
        }

        tmp = c.getProperties().getProperty("OUTPUT_RECORD_SEPERATOR_STAGENUMBER");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
            String s = convertSpecialChar(tmp);
            if (s != null)
                OUTPUT_RECORD_SEPERATOR_STAGENUMBER = s;
        }

        tmp = c.getProperties().getProperty("INPUT_SCHEMA_FILE");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
            INPUT_SCHEMA_FILE = tmp;
        }

        tmp = c.getProperties().getProperty("NO_OF_STAGES");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
            try {
                NO_OF_STAGES = Integer.parseInt(tmp);
                if (NO_OF_STAGES <= 0)
                    NO_OF_STAGES = 1;
            } catch (NumberFormatException e) {
            }
        }
        
        tmp = c.getProperties().getProperty("GIST_MAX_NUMBER_OF_INSIGHTS");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
            try {
            		GIST_MAX_NUMBER_OF_INSIGHTS = Long.parseLong(tmp);
                if (GIST_MAX_NUMBER_OF_INSIGHTS <= 0)
                		GIST_MAX_NUMBER_OF_INSIGHTS = 1000000;
            } catch (NumberFormatException e) {
            }
        }
        
        
        

        tmp = c.getProperties().getProperty("OUPUT_CANDIDATE_FILE_PATTERN");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
            OUPUT_CANDIDATE_FILE_PATTERN = tmp;
        }

        tmp = c.getProperties().getProperty("IS_COUNT_PRECIS");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
            IS_COUNT_PRECIS = Boolean.parseBoolean(tmp);
        }

        tmp = c.getProperties().getProperty("GENERATE_RAW_CANDIDATE_FILE");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
            GENERATE_RAW_CANDIDATE_FILE = Boolean.parseBoolean(tmp);
        }
        
        tmp = c.getProperties().getProperty("GIST_GENERATE_SINGLE_OUTPUT_FILE");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
        		GIST_GENERATE_SINGLE_OUTPUT_FILE = Boolean.parseBoolean(tmp);
        }

        
        
        tmp = c.getProperties().getProperty("GIST_OUPUT_CANDIDATE_FILE");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
        		GIST_OUPUT_CANDIDATE_FILE = tmp;
        }

        
        
        tmp = c.getProperties().getProperty("DUMP_DIM_FEED");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
            DUMP_DIM_FEED = Boolean.parseBoolean(tmp);
        }

        tmp = c.getProperties().getProperty("DUMP_BITSET_FEED");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
            DUMP_BITSET_FEED = Boolean.parseBoolean(tmp);
        }

        tmp = c.getProperties().getProperty("THRESHOLD");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
            try {
                THRESHOLD = Double.parseDouble(tmp);
            } catch (Exception e) {
            }
        }

        tmp = c.getProperties().getProperty("OUTPUT_DIMVAL_SEPERATOR");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
            String s = convertSpecialChar(tmp);
            if (s != null)
                OUTPUT_DIMVAL_SEPERATOR = s;
        }

        tmp = c.getProperties().getProperty("OUPUT_RAW_CANDIDATE_FILE_PATTERN");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
            OUPUT_RAW_CANDIDATE_FILE_PATTERN = tmp;
        }

        tmp = c.getProperties().getProperty("IGN_WORDS");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
            IGN_WORDS = new HashSet<String>(Arrays.asList(tmp.split(",")));
        }

        tmp = c.getProperties().getProperty("HIERARCHY_DIMS_ENABLED");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
            HIERARCHY_DIMS_ENABLED = Boolean.parseBoolean(tmp);
        }

        tmp = c.getProperties().getProperty("HIERARCHY_DIM_GROUPS");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
            HIERARCHY_DIM_GROUPS = tmp;
        }

        tmp = c.getProperties().getProperty("USE_THRESHOLD_GEN");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
            USE_THRESHOLD_GEN = Boolean.parseBoolean(tmp);
        }


        tmp = c.getProperties().getProperty("USE_THRESHOLD_PERCENTAGE_AFTER_LEVEL_3");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
            USE_THRESHOLD_PERCENTAGE_AFTER_LEVEL_3 = Boolean.parseBoolean(tmp);
        }

        tmp = c.getProperties().getProperty("THRESHOLD_UPTO_LEVEL_3");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
            THRESHOLD_UPTO_LEVEL_3 = Double.parseDouble(tmp);
        }

        tmp = c.getProperties().getProperty("THRESHOLD_GEN_FORMULA_AFTER_LEVEL_3");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
            THRESHOLD_GEN_FORMULA_AFTER_LEVEL_3 = tmp;
        }

        tmp = c.getProperties().getProperty("USE_THRESHOLD_PERCENTAGE_UPTO_LEVEL_3");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
            USE_THRESHOLD_PERCENTAGE_UPTO_LEVEL_3 = Boolean.parseBoolean(tmp);
        }

        tmp = c.getProperties().getProperty("LOGGING_LEVEL");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
            LOGGING_LEVEL = tmp;
        }

        tmp = c.getProperties().getProperty("LOGGING_ENABLED");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
            LOGGING_ENABLED = Boolean.parseBoolean(tmp);
        }
    }

}
