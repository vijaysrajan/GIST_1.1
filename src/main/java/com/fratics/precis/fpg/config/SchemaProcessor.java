package com.fratics.precis.fpg.config;

import java.util.ArrayList;
import java.util.HashMap;
import com.fratics.precis.exception.PrecisException;


public class SchemaProcessor {
	
	private ArrayList<SchemaColumn>      arrayListColumns = new ArrayList<SchemaColumn>();
	private HashMap<String,SchemaColumn> hashMapColumns   = new HashMap<String,SchemaColumn>();
	private int colNumber = -1;
	
	
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
	
	public void addColumn(String colName, String _type, boolean _tracked, String _dataType, String threshold) throws Exception{
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
				sc.setThreshold(Long.parseLong(threshold));
			} else if (_dataType.equalsIgnoreCase("double")) {
				sc.setDataType(SchemaColumn.DATATYPE.DOUBLE);
				sc.setThreshold(Double.parseDouble(threshold));
			} else {
				throw new PrecisException("Unsupported DataType for GIST");
			}
		} else if (_type.equalsIgnoreCase("C")) {
			type = SchemaColumn.TYPE.COUNT;
			sc.setDataType(SchemaColumn.DATATYPE.LONG);
			sc.setThreshold(Long.parseLong(threshold));
		}		
		sc.setType(type);
		sc.setColumnNumber(colNumber++);
		sc.setColumnTracked(_tracked);
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
	
	public boolean isColumnAMetric(int colNumber) {
		return (arrayListColumns.get(colNumber).getType() == SchemaColumn.TYPE.METRIC)?true:false;
	}
		
	public boolean isCountNeeded(int colNumber) {
		return (arrayListColumns.get(colNumber).getType() == SchemaColumn.TYPE.COUNT)?true:false;
	}
	
}
