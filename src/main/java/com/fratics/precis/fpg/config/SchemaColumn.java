package com.fratics.precis.fpg.config;

public class SchemaColumn {
	
	public enum TYPE { DIMENSION, METRIC, COUNT };
	public enum DATATYPE { STRING, LONG, DOUBLE };
	
	private String name;
	private TYPE type;
	private int columnNumber = -1;
	private boolean isColumnTracked = true;
	private DATATYPE dataType = DATATYPE.DOUBLE;
	//if computed but threshold not applied longThreshold && threshold will be null
	//private MetricThresholds<Long> longThreshold = null;
	private MetricThresholds<Double> threshold = null;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public TYPE getType() {
		return type;
	}
	public void setType(TYPE type) {
		this.type = type;
	}
	public int getColumnNumber() {
		return columnNumber;
	}
	public void setColumnNumber(int columnNumber) {
		this.columnNumber = columnNumber;
	}	
	public boolean isColumnTracked() {
		return isColumnTracked;
	}
	public void setColumnTracked(boolean isColumnTracked) {
		this.isColumnTracked = isColumnTracked;
	}
	public DATATYPE getDataType() {
		return dataType;
	}
	public void setDataType(DATATYPE dataType) {
		this.dataType = dataType;
	}
	
	public void setThreshold(double d) {
		threshold = new MetricThresholds<Double>(new Double(d) );
	}
	
//	public void setThreshold(long l) {
//		longThreshold = new MetricThresholds<Long>(new Long(l) );
//	}
	
	public boolean colIsMetricOrCountAndHasThreshold() {
		if ( (type == TYPE.METRIC || type == TYPE.COUNT) && (threshold != null )) {
			return true;
		} else {
			return false;
		}
	}
	
	public double getThreshold() {
		if (threshold != null) {
			return threshold.getThreshold();
		} else {
			return -1;
		}
	}
	
//	public long getLongThreshold() {
//		if (longThreshold != null) {
//			return longThreshold.getThreshold();
//		} else {
//			return -1;
//		}
//	}
	
}
