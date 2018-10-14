package com.fratics.precis.fpg.level_one;

import com.fratics.precis.fpg.config.*;
import com.fratics.precis.fpg.fptreebilder.MetricList;
import com.fratics.precis.exception.PrecisException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.io.RandomAccessFile;



public class ProcessLevelOneFromFile {
	
	private String inputFile = FPGConfig.INPUT_DATA_FILE;
    private String recordSeperator = FPGConfig.INPUT_RECORD_SEPERATOR;
    private File file = null;
    private BufferedReader br = null;
    private RandomAccessFile raf = null;
    private int [] metricIndexes = null;
    private int [] thresholdIndexes = null;
    private HashMap<String,MetricList> levelOneHashMap = new HashMap<String,MetricList>(); 
    private ArrayList<HashMap<String,Double>> arrayListOfHashMapColumnsForDblThresh   = null;
    //	private ArrayList<HashMap<String,Long>> arrayListOfHashMapColumnsForLongThresh    = null;
    
    
    
    private void openFile() throws PrecisException{
        file = new File(this.inputFile);
        if (!(file.exists() && file.canRead()))
            throw new PrecisException("Error Reading file:: " + this.inputFile);
        try {
            this.br = new BufferedReader(new FileReader(file));
        } catch (Exception e) {
            // log error
            throw new PrecisException("Exception while reading input file " + this.inputFile  +" into BufferedReader.");
        }
    	
    }
    private void closeFile() throws PrecisException{
		try {
			br.close();
		} catch (Exception e) {
			throw new PrecisException ("Exception when closing input file " + this.inputFile);
		}
    }

    
    public void init() throws Exception{
		//		if (FPGConfig.schemaInstance.doesSchemaHaveADoubleMetricWithThreshold()) {
		//			
		//		}
		//		if (FPGConfig.schemaInstance.doesSchemaHaveALongMetricWithThreshold()) {
		//			//arrayListOfHashMapColumnsForLongThresh   = new ArrayList<HashMap<String,Long>> ();
		//		}
		raf = Util.openFile(FPGConfig.BAD_FATA_FILE);
		this.thresholdIndexes = FPGConfig.schemaInstance.getThresholdIndexes() ;
		this.metricIndexes    = FPGConfig.schemaInstance.getMetricsIndexes();
		arrayListOfHashMapColumnsForDblThresh   = new ArrayList<HashMap<String,Double>> ();
		
    }
    
    public void close() throws Exception {
    		Util.closeFile(raf);
    		br.close();
    }
    
    
    public void processFile ()  throws PrecisException {
    		if (FPGConfig.isInitialized() == false) {
    			throw new PrecisException("FPG Configuration is not initialized. Aborting the processing ....");
    		}
    		this.openFile();

    		String [] parts = null;
    		String thisLine = null;
    		try {
    			while ((thisLine = br.readLine()) != null) {
    				thisLine = thisLine.trim();
    	            if (thisLine.startsWith("#")) {
    	                continue;
    	            }
    	            if (thisLine != null) {
    	                 parts = thisLine.split(this.recordSeperator, -1); // added limit
    	                 processLine(parts);
    	            }
    			}
    		} catch (IOException e) {
	            // log error
	            throw new PrecisException ("Got IOException while reading data from input file " + this.inputFile);

	    } catch (Exception ex) {
	            // log error
	            ex.printStackTrace();
	            throw new PrecisException(ex.getMessage());
	    }

    		this.closeFile();    	
    }

    private StringBuilder _sb1 = new StringBuilder();    
    private MetricList _ml1 = null;
    private void insertOrUpdateHashMapLevelOne (String k, MetricList v) {
    		if (levelOneHashMap.containsKey(k)) {
    			 _ml1 =  levelOneHashMap.get(k);
    			 _ml1.updateMetricList(v);
    		} else {
    			levelOneHashMap.put(k, v);
    		}
    }
    
    
    private void processLine(String [] parts) throws Exception{
    		if (parts.length != FPGConfig.schemaInstance.getSchemaLength()) {
    			StringBuilder sb = new StringBuilder();
    			int i = -1;
    			for (String s : parts) {
    				sb.append(s);
    				i++;
    				if (i != parts.length - 1) {
    					sb.append(FPGConfig.INPUT_RECORD_SEPERATOR);
    				} else {
    					sb.append("\n");
    				}
    			}
    			Util.writeCandidatesToOutputFile(sb.toString(), raf);
    			return;
    		}
    		
    		MetricList ml = new MetricList(); 
    		//Consolidate the Metric Values
    		for (int i = 0; i< this.metricIndexes.length; i++) {
    			ml.addMetricToList(FPGConfig.schemaInstance.getSchemaColumn(metricIndexes[i]).getName(), 
    							   Double.parseDouble(parts[metricIndexes[i]]),false);
    		}
    		

    		for (int i = 0; i < parts.length; i++) {
    			if (FPGConfig.schemaInstance.getSchemaColumn(i).isColumnTracked()) {
    				if (FPGConfig.schemaInstance.getSchemaColumn(i).getType() == SchemaColumn.TYPE.DIMENSION) {
    					_sb1.append(FPGConfig.schemaInstance.getSchemaColumn(i).getName());
    					_sb1.append(FPGConfig.SEPARATOR_BETWEEN_DIM_AND_VAL);
    					_sb1.append(parts[i]);
    					insertOrUpdateHashMapLevelOne(_sb1.toString(), MetricList.makeReplica(ml));
    					_sb1.setLength(0);
    				} 
    			}
    		}
    		
    }
    
    
    public void dumpLevelOneValuesIgnoreThreshold() {
	    	for (Entry<String, MetricList> entry : levelOneHashMap.entrySet()) {
	    	    String key = entry.getKey();
	    	    MetricList value = entry.getValue();
	    	    System.out.println(key + "," + value.toString());
	    	}
    }
    
//    public void applyThresholdsForEachCase () {
//    		for (int i = 0; i <= this.thresholdIndexes.length; i++) {
//    			arrayListOfHashMapColumnsForDblThresh.
//    		}
//    }
    
    

    public static void main(String [] args) throws Exception{
	    		FPGConfig.init();
	    		ProcessLevelOneFromFile p = new ProcessLevelOneFromFile();
	    		p.init();
	    		p.processFile();
	    		p.close();
	    		p.dumpLevelOneValuesIgnoreThreshold();
    }
    
}
