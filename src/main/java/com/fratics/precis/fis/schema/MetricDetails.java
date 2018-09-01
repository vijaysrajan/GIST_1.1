package com.fratics.precis.fis.schema;

public class MetricDetails {
	protected String metricName;
	protected int metricIndex = -1;
	protected boolean isOn = true;
	protected double metricThreshold = -1;
	protected boolean isCountPrecis = false;

	MetricDetails (String m, int idx, boolean on, double t) {
		this.metricName = m;
		this.metricIndex = idx;
		this.isOn = on; 
		this.metricThreshold = t;
	}

	MetricDetails (String m, double t, boolean isCountPrecis) {
		this.metricName = m;
		this.isOn = true; 
		this.metricThreshold = t;
		this.isCountPrecis = isCountPrecis;
	}

	
	public boolean isCountPrecis() {
		return isCountPrecis;
	}
	
	
	public String getMetricName() {
		return metricName;
	}

	public int getMetricIndex() {
		return metricIndex;
	}

	public boolean isOn() {
		return isOn;
	}

	public double getMetricThreshold() {
		return metricThreshold;
	}	
	

}
