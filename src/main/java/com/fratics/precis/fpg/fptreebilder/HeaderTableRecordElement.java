package com.fratics.precis.fpg.fptreebilder;

public class HeaderTableRecordElement {
	private String dimName = null;
	private String dimVal  = null;
	private String dimNameSeparatorDimVal = null;
	private String separator = null;
	private StringBuilder sb = new StringBuilder();
	private int indexOfDimInSchema = -1;
	
	public HeaderTableRecordElement(String dimName, String dimVal, String separator, int indexOfDimInSchema) {
		this.dimName = dimName;
		this.dimVal = dimVal;
		this.separator = separator;
		sb.setLength(0);
		sb.append(dimName);
		sb.append(separator);
		sb.append(dimVal);
		dimNameSeparatorDimVal = sb.toString();
		this.indexOfDimInSchema = indexOfDimInSchema;
	}

	public int getIndexOfDimInSchema() {
		return indexOfDimInSchema;
	}

	public String getDimName() {
		return dimName;
	}
	public String getDimVal() {
		return dimVal;
	}
	
	public String getDimNameSeparatorDimVal() {
		return dimNameSeparatorDimVal;
	}

	public String getSeparator() {
		return separator;
	}
	
}
