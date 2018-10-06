package com.fratics.precis.fpg.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import com.fratics.precis.exception.PrecisException;

public class FPGConfig {
	
    public static String INPUT_DATA_FILE = "./data/dataFile";
    public static String OUPUT_FILE="./data/fis_output.csv";
    public static String INPUT_RECORD_SEPERATOR = Character.toString('\001');	
    public static String SEPERATOR_BETWEEN_SUCCESSIVE_DIMVALS="  ~and~  ";
    public static String SEPARATOR_BETWEEN_DIM_AND_VAL="==";
    public static String SEPARATOR_BETWEEN_FIS_AND_METRIC=",";
    public static String SEPARATOR_BETWEEN_SUCCESSIVE_METRICS=" , ";
    public static String SEPERATOR_BETWEEN_STAGENUMBER_AND_FIS=" , ";
    public static String SCHEMA = "";
    public static long   NO_OF_STAGES = 5;
    public static long   GIST_MAX_NUMBER_OF_INSIGHTS=4000000;
    private static String IGNORE_VALUES_TEMP = "null,";
    public static HashSet<String> IGNORE_VALUES = new HashSet<String>(Arrays.asList("null", "", "0"));
    private static String IGNORE_DIMVALS_TEMP="CITY:TIMBUCKTOO;STATE:OOTKCUBMIT";
    public static HashSet<String> IGNORE_DIMVALS = new HashSet<String>();
    //public static boolean HIERARCHY_DIMS_ENABLED = false;
    public static String HIERARCHY_DIM_GROUPS_TEMP = "";
    public static HashMap<String,HashSet<String>> HIERARCHY_DIM_GROUPS = new HashMap<String,HashSet<String>>();   
    public static boolean LOGGING_ENABLED = true;
    public static String LOGGING_LEVEL = "INFO";
    public static SchemaProcessor schemaInstance = new SchemaProcessor();


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
        FPGConfigObject c = new FPGConfigObject();
        c.initialize();
        loadConfig(c);
    }
    
    private static void readParseAndPopulateFromSchemaConfigParam (String schema) throws Exception{
    		//TBD 
	    	//	d:source:t;\
	    	//	d:estimated_usage_bins:t;\
	    	//	d:city:t;\
	    	//	d:zone:t;\
	    	//	d:zone_demand_popularity:t;\
	    	//	d:dayType:t;\
	    	//	d:pickUpHourOfDay:t;\
	    	//	d:sla:t;\
	    	//	d:booking_type:t;\
	    	//	m:DU_Bad_Count:double:t:20;\
	    	//	m:DU_No_Bad_Count:double:t:20;\
	    	//	m:cnt:long:t:25;\
	    	//	C:_GISTcountThreshold:long:t:10	
		String [] splitter1 = schema.split(";",-1);
		for (String s1 : splitter1) {
			String [] splitter2 = s1.split(":", -1);
			for (int i = 0; i < splitter2.length; i++) {
				splitter2[i] = splitter2[i].trim();
			}
			if (splitter2[0].equalsIgnoreCase("d")) {
				schemaInstance.addColumn(splitter2[1], 
						                 splitter2[0], 
						                 (splitter2[2].equalsIgnoreCase("t"))?true:false,
						                 "String",
						                 null
						                 ); 	
			} else if (splitter2[0].equalsIgnoreCase("m")) {
				schemaInstance.addColumn( splitter2[1], 
									      splitter2[0], 
									      (splitter2[3].equalsIgnoreCase(""))?true:false,
									      splitter2[2],
									      splitter2[4]
									    	);
			} else if (splitter2[0].equalsIgnoreCase("c")) {
				schemaInstance.addColumn( splitter2[1], 
                                          splitter2[0], 
					      				 (splitter2[3].equalsIgnoreCase(""))?true:false,
					                      splitter2[2],
					    	                  splitter2[4]
					                    );
			} else {
				throw new PrecisException("Schema has column type which is incorrect. " +  splitter2[0]);
			}
		}
		
		
		//schemaInstance
    		
    }
    
    private static void readAndPopulateIgnoreValueHashSet (String str) {
    		//private static String IGNORE_VALUES_TEMP = "null,";
        //public static HashSet<String> IGNORE_VALUES = new HashSet<String>(Arrays.asList("null", "", "0"));
    		//IGNORE_VALUES=null,,0,na,n/a
		String [] splitter1 = str.split(",",-1);
		for (String s : splitter1) {
			IGNORE_VALUES.add(s);
			IGNORE_VALUES.add(s.toLowerCase());
			IGNORE_VALUES.add(s.toUpperCase());
		}
    }
    
    private static void readAndPopulateIgnoreDimValHashSet (String str) {
    		//private static String IGNORE_DIMVALS_TEMP="CITY:TIMBUCKTOO;STATE:OOTKCUBMIT";
        //public static HashSet<String> IGNORE_DIMVALS = new HashSet<String>();
    		StringBuilder sb = new StringBuilder ();
		String [] splitter1 = str.split(";",-1);
		for (String s : splitter1) {
			String [] splitter2 = s.split(":",-1);
			sb.append(splitter2[0]);
			sb.append(SEPARATOR_BETWEEN_DIM_AND_VAL);
			sb.append(splitter2[1]);
			IGNORE_DIMVALS.add(sb.toString());
			sb.setLength(0);
		}
    }
    
    private static void readAndPopulateHierarchyDimsHashMap(String str) {
        //public static String HIERARCHY_DIM_GROUPS_TEMP = "";
        //public static HashMap<String,HashSet<String>> HIERARCHY_DIM_GROUPS = new HashMap<String,HashSet<String>>();
	    	//#List of Comma Seperated Hierarchy Dim Groups, Groups are Seperated by ":"
	    	//#example "lt20,lt30,lt40:gt10,gt20,gt30" tells there are 2 hierarchy groups(lt & gt), candidate removal logic will be applied only
	    	//#within a group
    		String [] splitter1 = str.split(":",-1);
    		for (String s : splitter1) {
    			String [] splitter2 = s.split(",",-1);
    			for (String s2: splitter2) {
        			HashSet<String> hs = new HashSet<String> ();
        			for (String s3: splitter2) {
        				if (!s2.equalsIgnoreCase(s3)) { 
        					hs.add(s3);
        				}
        			}
        			HIERARCHY_DIM_GROUPS.put(s2, hs);
    			}
    		}
    }
    
    
    public static void loadConfig(FPGConfigObject c) throws Exception{
    	
    	

		//        String tmp = c.getProperties().getProperty("INPUT_DATA_FILE");
		//        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
		//            INPUT_DATA_FILE = tmp;
		//        }

    		//populateStringConfigVariable(c, INPUT_DATA_FILE, "INPUT_DATA_FILE");
        String tmp = c.getProperties().getProperty("INPUT_DATA_FILE");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
        		INPUT_DATA_FILE = tmp;
        }
        //populateStringConfigVariable(c, OUPUT_FILE, "OUPUT_FILE");
        tmp = c.getProperties().getProperty("OUPUT_FILE");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
        		OUPUT_FILE = tmp;
        }
        //populateStringConfigVariable(c, INPUT_RECORD_SEPERATOR, "INPUT_RECORD_SEPERATOR");
        tmp = c.getProperties().getProperty("INPUT_RECORD_SEPERATOR");
        String s = convertSpecialChar(tmp);
        if (s != null) {
        		INPUT_RECORD_SEPERATOR = tmp;
        }
        //populateStringConfigVariableWithSpecialChars(c, SEPERATOR_BETWEEN_SUCCESSIVE_DIMVALS, "SEPERATOR_BETWEEN_SUCCESSIVE_DIMVALS");
        tmp = c.getProperties().getProperty("SEPERATOR_BETWEEN_SUCCESSIVE_DIMVALS");
        s = convertSpecialChar(tmp);
        if (s != null) {
        		SEPERATOR_BETWEEN_SUCCESSIVE_DIMVALS = tmp;
        }
		//populateStringConfigVariableWithSpecialChars(c, SEPARATOR_BETWEEN_DIM_AND_VAL, "SEPARATOR_BETWEEN_DIM_AND_VAL");
        tmp = c.getProperties().getProperty("SEPARATOR_BETWEEN_DIM_AND_VAL");
        s = convertSpecialChar(tmp);
        if (s != null) {
        		SEPARATOR_BETWEEN_DIM_AND_VAL = tmp;
        }	

        //populateStringConfigVariableWithSpecialChars(c, SEPARATOR_BETWEEN_FIS_AND_METRIC, "SEPARATOR_BETWEEN_FIS_AND_METRIC");
        tmp = c.getProperties().getProperty("SEPARATOR_BETWEEN_FIS_AND_METRIC");
        s = convertSpecialChar(tmp);
        if (s != null) {
        		SEPARATOR_BETWEEN_FIS_AND_METRIC = tmp;
        }	
		
		//populateStringConfigVariableWithSpecialChars(c, SEPARATOR_BETWEEN_SUCCESSIVE_METRICS, "SEPARATOR_BETWEEN_SUCCESSIVE_METRICS");
        tmp = c.getProperties().getProperty("SEPARATOR_BETWEEN_SUCCESSIVE_METRICS");
        s = convertSpecialChar(tmp);
        if (s != null) {
        		SEPARATOR_BETWEEN_SUCCESSIVE_METRICS = tmp;
        }
		//populateStringConfigVariableWithSpecialChars(c, SEPERATOR_BETWEEN_STAGENUMBER_AND_FIS, "SEPERATOR_BETWEEN_STAGENUMBER_AND_FIS");
        tmp = c.getProperties().getProperty("SEPERATOR_BETWEEN_STAGENUMBER_AND_FIS");
        s = convertSpecialChar(tmp);
        if (s != null) {
        		SEPERATOR_BETWEEN_STAGENUMBER_AND_FIS = tmp;
        }
		//populateStringConfigVariable(c, SCHEMA, "SCHEMA");
        tmp = c.getProperties().getProperty("SCHEMA");
        if ((tmp == null || tmp.equalsIgnoreCase(""))) {
	  		throw new PrecisException("No Schema found.");
        } else {
    			SCHEMA = tmp;
        }
		readParseAndPopulateFromSchemaConfigParam(SCHEMA);
		//populateLongConfigVariable(c,NO_OF_STAGES,"NO_OF_STAGES");
        tmp = c.getProperties().getProperty("NO_OF_STAGES");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
            try {
                NO_OF_STAGES = Long.parseLong(tmp);
                if (NO_OF_STAGES <= 0)
                    NO_OF_STAGES = 1;
            } catch (NumberFormatException e) {
            }
        }
		
		//populateLongConfigVariable(c,GIST_MAX_NUMBER_OF_INSIGHTS,"GIST_MAX_NUMBER_OF_INSIGHTS");
        tmp = c.getProperties().getProperty("GIST_MAX_NUMBER_OF_INSIGHTS");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
            try {
            		GIST_MAX_NUMBER_OF_INSIGHTS = Long.parseLong(tmp);
                if (GIST_MAX_NUMBER_OF_INSIGHTS <= 0)
                		GIST_MAX_NUMBER_OF_INSIGHTS = 1000000;
            } catch (NumberFormatException e) {
            }
        }
		
		//populateStringConfigVariable(c, IGNORE_VALUES_TEMP, "IGNORE_VALUES");
        tmp = c.getProperties().getProperty("IGNORE_VALUES");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
        		IGNORE_VALUES_TEMP = tmp;
        }
		readAndPopulateIgnoreValueHashSet(IGNORE_VALUES_TEMP);
		
		//populateStringConfigVariable(c, IGNORE_DIMVALS_TEMP, "IGNORE_DIMVALS");
        tmp = c.getProperties().getProperty("IGNORE_DIMVALS");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
        		IGNORE_DIMVALS_TEMP = tmp;
        }
		readAndPopulateIgnoreDimValHashSet(IGNORE_DIMVALS_TEMP);
		
		//populateStringConfigVariable(c, HIERARCHY_DIM_GROUPS_TEMP, "HIERARCHY_DIM_GROUPS");
        tmp = c.getProperties().getProperty("HIERARCHY_DIM_GROUPS");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
        		HIERARCHY_DIM_GROUPS_TEMP = tmp;
        }
        readAndPopulateHierarchyDimsHashMap(HIERARCHY_DIM_GROUPS_TEMP);
        
        tmp = c.getProperties().getProperty("LOGGING_LEVEL");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
            LOGGING_LEVEL = tmp;
        }

        tmp = c.getProperties().getProperty("LOGGING_ENABLED");
        if (!(tmp == null || tmp.equalsIgnoreCase(""))) {
            LOGGING_ENABLED = Boolean.parseBoolean(tmp);
        }
    }
    
    
    
    public static void main (String [] args) throws Exception {
    		FPGConfig.init();
    		System.out.println("Done");
    		
    }
    

}




