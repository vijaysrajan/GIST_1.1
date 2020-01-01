package com.fratics.precis.fpg.level_one;

import com.fratics.precis.fpg.config.*;
import com.fratics.precis.fpg.fptreebilder.MetricList;
import com.fratics.precis.fpg.fptreebilder.HeaderTable;
import com.fratics.precis.fpg.fptreebilder.HeaderTableRecord;
import com.fratics.precis.fpg.fptreebilder.HeaderTableRecordElement;
import com.fratics.precis.fpg.fptreebilder.FPTreeBuilder;
import com.fratics.precis.exception.PrecisException;
import com.fratics.precis.fpg.job.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
    //private ArrayList<HashMap<String,Long>> arrayListOfHashMapColumnsForLongThresh    = null;
    
    
    
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
		raf = Util.openFile(FPGConfig.BAD_DATA_FILE);
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
    
    
    private void dumpLevelOneValuesIgnoreThreshold() {
	    	for (Entry<String, MetricList> entry : levelOneHashMap.entrySet()) {
	    	    String key = entry.getKey();
	    	    MetricList value = entry.getValue();
	    	    System.out.println(key + "," + value.toString());
	    	}
    }
    
    public void applyThresholdsForEachCase () throws Exception{
    	RandomAccessFile raf = Util.openFile(FPGConfig.OUPUT_FILE);
    		//for (int i = 0; i < this.thresholdIndexes.length; i++) {
    			int i = 0;
	    		ArrayList<LevelOneDimValMetrics> al = new ArrayList<LevelOneDimValMetrics>();
	    		int thresholdIndex = this.thresholdIndexes[i];
		    	for (Entry<String, MetricList> entry : levelOneHashMap.entrySet()) {
		    	    String key = entry.getKey();
		    	    MetricList value = entry.getValue();
		    	    value.setNameOfMetricForSupportThreshold(FPGConfig.schemaInstance.getThresholdMetricName(thresholdIndex));
		    	    if (value.getSupportMetricValue() >= FPGConfig.schemaInstance.getThresholdValue(thresholdIndex) && 
		    	    		FPGConfig.schemaInstance.getThresholdValue(thresholdIndex) >  -1) {
		    	    		al.add(new LevelOneDimValMetrics(key,value,value.getNameOfMetricForSupportThreshold(),FPGConfig.SEPARATOR_BETWEEN_DIM_AND_VAL) );
		    	    }
		    	}
		    	Collections.sort(al);
		    	Collections.reverse(al);

		    	HeaderTable ht = new HeaderTable();
		    	for (LevelOneDimValMetrics l : al) {
		    		HeaderTableRecordElement htre1 = new HeaderTableRecordElement(l.getDim(),
		    																	 l.getVal(),
		    				                                                      l.getSeparator(),
		    				                                                      FPGConfig.schemaInstance.getIndexOfDimInSchema(l.getDim()));
		    		MetricList ml = MetricList.makeReplica(l.getMl());
		    		ml.setNameOfMetricForSupportThreshold(FPGConfig.schemaInstance.getThresholdMetricName(thresholdIndex));
		    		HeaderTableRecord htr1 = new HeaderTableRecord(htre1,ml);
		    		ht.add(htr1);
		    	}
		    	FPTreeBuilder fptb = new FPTreeBuilder();
			fptb.initialize(ht);
			this.openFile();
			
	    		String thisLine = null;
	    		try {
	    			while ((thisLine = br.readLine()) != null) {
	    				thisLine = thisLine.trim();
	    	            if (thisLine.startsWith("#")) {
	    	                continue;
	    	            }
	    	            if (thisLine != null) {
	    	    				fptb.addPathToTree(thisLine, 
	    	    						           FPGConfig.INPUT_RECORD_SEPERATOR,
	    	    						           FPGConfig.schemaInstance.getSchemaInArray(),
	    	    						           FPGConfig.schemaInstance.getMetricsIndexes(),
	    	    						           thresholdIndex, 
	    	    						           FPGConfig.IGNORE_DIMVALS,
	    	    						           FPGConfig.IGNORE_VALUES,//valueIgnoreList,
	    	    						           FPGConfig.schemaInstance.getDimsToIgnoreHashSet(),//dimIndexesToIgnore, 
	    	    						           FPGConfig.SEPARATOR_BETWEEN_DIM_AND_VAL /*dimToValSeparator*/);	    	    				
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
	    		System.out.println( "Time after parsing file for second time : " + (System.currentTimeMillis() - t));
			fptb.mineFPTree(FPGConfig.SEPERATOR_BETWEEN_SUCCESSIVE_DIMVALS,
					        FPGConfig.SEPERATOR_BETWEEN_STAGENUMBER_AND_FIS,
					        FPGConfig.SEPARATOR_BETWEEN_FIS_AND_METRIC,
					        "\n",
					        FPGConfig.NO_OF_STAGES,
					        FPGConfig.schemaInstance.getThresholdValue(thresholdIndex),
					        raf);
    			System.out.println( "Time for recursive FPTree mining : " + (System.currentTimeMillis() - t));
    	//	}    	
		Util.closeFile(raf);
    }
    
    private void printSortedArrayListOfLevelOne(ArrayList<LevelOneDimValMetrics> a) {
    		for (LevelOneDimValMetrics l : a) {
    			System.out.println(l.toString());
    		}
    }
    
    static long t = 0;
    public static void main(String [] args) throws Exception{
    			t = System.currentTimeMillis();
	    		FPGConfig.init();
	    		System.out.println( "Time for init : " + (System.currentTimeMillis() - t));
	    		ProcessLevelOneFromFile p = new ProcessLevelOneFromFile();
	    		System.out.println("Time for processing until level one : " + (System.currentTimeMillis() - t));
	    		p.init();
	    		p.processFile();
	    		p.close();
	    		//System.out.println("Time for building tree and parsing file for second time" + (System.currentTimeMillis() - t));
	    		//p.dumpLevelOneValuesIgnoreThreshold();
	    		p.applyThresholdsForEachCase();
	    		System.out.println("Time for FPTreeMining and applying threshold : " + (System.currentTimeMillis() - t));
    }
    
}
