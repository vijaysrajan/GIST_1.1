package com.fratics.precis.fpg.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.fratics.precis.exception.PrecisException;


public class SchemaProcessor {
	
	private ArrayList<SchemaColumn>      arrayListColumns = new ArrayList<SchemaColumn>();
	private HashMap<String,SchemaColumn> hashMapColumns   = new HashMap<String,SchemaColumn>();
	private int colNumber = -1;
	//private Boolean hasLongMetricToSum = null;
	private Boolean hasMetricToSum = null;
	private boolean hasCountColumn = false;
	private int omittedDimCount = 0;
	private int [] dimsToIgnore = null;
	private HashSet<Integer> dimsToIgnoreHashSet = null;
	
	
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
	//	m:cnt:long:f;\
	//	C:_GISTcountThreshold:long:t
	
	public void addColumn(String colName,
						  String _type, 
						  boolean _tracked, 
						  String _dataType, 
						  String threshold
						  ) throws Exception{
		SchemaColumn sc = new SchemaColumn();
		sc.setName(colName);
		
		SchemaColumn.TYPE type = SchemaColumn.TYPE.DIMENSION;
		if (_type.equalsIgnoreCase("d")) {
			type = SchemaColumn.TYPE.DIMENSION;
			sc.setDataType(SchemaColumn.DATATYPE.STRING);
		} else if (_type.equalsIgnoreCase("m")) {
			type = SchemaColumn.TYPE.METRIC;
			if (_dataType.equalsIgnoreCase("long")) {
				sc.setDataType(SchemaColumn.DATATYPE.LONG);
				if (threshold != null) {
					sc.setThreshold(Long.parseLong(threshold));
				}
			} else if (_dataType.equalsIgnoreCase("double")) {
				sc.setDataType(SchemaColumn.DATATYPE.DOUBLE);
				if (threshold != null) {
					sc.setThreshold(Double.parseDouble(threshold));
				}
			} else {
				throw new PrecisException("Unsupported DataType for GIST");
			}
		} else if (_type.equalsIgnoreCase("C")) {
			//C:_GISTcountThreshold
			if (colName.equalsIgnoreCase("_GISTcountThreshold")) {
				if (this.hasCountColumn == false) {
					this.hasCountColumn = true;
				} else {
					throw new PrecisException ("Cannot have more than one column with _GISTcountThreshold in the schema.");
				}
			}
			
			type = SchemaColumn.TYPE.COUNT;
			sc.setDataType(SchemaColumn.DATATYPE.LONG);
			if (threshold != null) {
				sc.setThreshold(Long.parseLong(threshold));
			}
		}		
		sc.setType(type);
		sc.setColumnNumber(colNumber++);
		sc.setColumnTracked(_tracked);
		if (_tracked == false) {
			omittedDimCount++;
		}
		
		hashMapColumns.put(colName, sc);
		arrayListColumns.add(sc);
	}
	
	
	public boolean isColumnADimension(String name) {
		return (hashMapColumns.get(name).getType() == SchemaColumn.TYPE.DIMENSION)?true:false;
	}
	
	public boolean isColumnADimension(int colNumber) {
		return (arrayListColumns.get(colNumber).getType() == SchemaColumn.TYPE.DIMENSION)?true:false;
	}

	public boolean isColumnAMetric(String name) {
		return (hashMapColumns.get(name).getType() == SchemaColumn.TYPE.METRIC)?true:false;
	}
	
	public int getIndexOfDimInSchema(String dimName) {
		return hashMapColumns.get(dimName).getColumnNumber();
	}
	
	
	public boolean isColumnAMetric(int colNumber) {
		return (arrayListColumns.get(colNumber).getType() == SchemaColumn.TYPE.METRIC)?true:false;
	}
		
	public boolean isCountNeeded(int colNumber) {
		return (arrayListColumns.get(colNumber).getType() == SchemaColumn.TYPE.COUNT)?true:false;
	}
	
	public boolean doesSchemaHaveADoubleMetricWithThreshold() {
		if ( (hasMetricToSum == null) && (arrayListColumns != null)  && (arrayListColumns.size() > 0) ) {
			boolean isValSet = false;
			hasMetricToSum = new Boolean(false);
			for (int i = 0; i < arrayListColumns.size(); i++) {
				if ( 
					this.arrayListColumns.get(i).colIsMetricOrCountAndHasThreshold() &&
					arrayListColumns.get(i).isColumnTracked() && 
					(arrayListColumns.get(i).getDataType() == SchemaColumn.DATATYPE.DOUBLE)) {
					hasMetricToSum = true;
					isValSet = true;
					break;
				}
			}
			if (isValSet == false) {
				hasMetricToSum = false;
			}
		}
		return hasMetricToSum;
	}
	
//	public boolean doesSchemaHaveALongMetricWithThreshold() {
//		if ( (hasLongMetricToSum == null) && (arrayListColumns != null)  && (arrayListColumns.size() > 0) ) {
//			boolean isValSet = false;
//			hasLongMetricToSum = new Boolean(false);
//			for (int i = 0; i < arrayListColumns.size(); i++) {
//				if (this.arrayListColumns.get(i).colIsMetricOrCountAndHasThreshold() && 
//					arrayListColumns.get(i).colIsMetricOrCountAndHasThreshold() &&
//					arrayListColumns.get(i).isColumnTracked() && 
//					(arrayListColumns.get(i).getDataType() == SchemaColumn.DATATYPE.LONG)) {
//					hasLongMetricToSum = true;
//					isValSet = true;
//					break;
//				}
//			}
//			if (isValSet == false) {
//				hasLongMetricToSum = false;
//			}
//		}
//		return hasLongMetricToSum;
//	}
	
	public int [] getMetricsIndexes() {
		ArrayList<Integer> retVal = new ArrayList<Integer> ();
		for (int i = 0; i < arrayListColumns.size(); i++) {
			if ( (arrayListColumns.get(i).isColumnTracked() ) && 
				 (arrayListColumns.get(i).getType() == SchemaColumn.TYPE.METRIC )) {
				retVal.add(i);
			}
		}
		if (retVal.size() > 0) {
			Integer [] r = new Integer[retVal.size()]; 
			retVal.toArray(r);
			int [] ret = new int[r.length];
			for (int i = 0; i < r.length; i++) {
				ret[i] = r[i].intValue();
			}
			return ret;
		} else {
			return null;
		}
	}

	
	public int [] getThresholdIndexes() {
		ArrayList<Integer> retVal = new ArrayList<Integer> ();
		for (int i = 0; i < arrayListColumns.size(); i++) {
			if ( (arrayListColumns.get(i).getThreshold() >=0 ) && 
				 (arrayListColumns.get(i).isColumnTracked() ) && 
				 (arrayListColumns.get(i).getType() == SchemaColumn.TYPE.METRIC || 
				 arrayListColumns.get(i).getType() == SchemaColumn.TYPE.COUNT)) {
				retVal.add(i);
			}
		}
		if (retVal.size() > 0) {
			Integer [] r = new Integer[retVal.size()]; 
			retVal.toArray(r);
			int [] ret = new int[r.length];
			for (int i = 0; i < r.length; i++) {
				ret[i] = r[i].intValue();
			}
			return ret;
		} else {
			return null;
		}
	}
	
	public int getSchemaLength() {
		if (hasCountColumn == true) {
			return (arrayListColumns.size() - 1);
		}
		return arrayListColumns.size();
	}
	
	public SchemaColumn getSchemaColumn(int i) {
		return arrayListColumns.get(i);
	}
	
	public String getThresholdMetricName (int thresholdIndex) {
		SchemaColumn sc = arrayListColumns.get(thresholdIndex);
		
		if ((sc.getType() == SchemaColumn.TYPE.METRIC)  && (sc.getThreshold() > -1 )) {
			return sc.getName();
		}
		else {
			return null;
		}
	}
	
	public double getThresholdValue (int idx) {
		return arrayListColumns.get(idx).getThreshold();
	}
	
	public int []  getDimsToIgnore() {
		if (dimsToIgnore == null) {
			dimsToIgnore = new int[omittedDimCount];
			int j = 0;
			for (int i = 0; i < this.arrayListColumns.size(); i++) {
				if (arrayListColumns.get(i).isColumnTracked() == false) {
					dimsToIgnore[j] = i;
					j++;
				}
			}
		}
		return dimsToIgnore;
	}
	public HashSet<Integer>  getDimsToIgnoreHashSet() {
		
		if (dimsToIgnoreHashSet == null) {
			dimsToIgnoreHashSet = new HashSet<Integer>();
			for (int i = 0; i < this.arrayListColumns.size(); i++) {
				if (arrayListColumns.get(i).isColumnTracked() == false) {
					dimsToIgnoreHashSet.add(new Integer(i));
				}
			}
		}
		return dimsToIgnoreHashSet;
	}
	
	
	public HashSet<String> getValsToIgnore() {
		return FPGConfig.IGNORE_VALUES;
	}

	public HashSet<String> getDimValsToIgnore() {
		return FPGConfig.IGNORE_DIMVALS;
	}
	
	public String toString(String separator) {
		StringBuilder sb___ = new StringBuilder();
		
		for (int i = 0; i < arrayListColumns.size(); i++) {
			sb___.append(arrayListColumns.get(i).getName());
			if (i < (arrayListColumns.size() - 1)) {
				sb___.append(separator);
			}
		}
		return sb___.toString();
	}
	public String []  getSchemaInArray() {
		String [] retVal = new String[arrayListColumns.size()];
		for (int i = 0; i < arrayListColumns.size(); i++) {
			retVal[i] = arrayListColumns.get(i).getName();
		}
		return retVal;
	}
	
	
}
