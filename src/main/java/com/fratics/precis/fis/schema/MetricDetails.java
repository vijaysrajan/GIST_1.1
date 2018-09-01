package com.fratics.precis.fis.schema;

public class MetricDetails {
	protected String metricName;
	protected int metricIndex;
	protected boolean isOn = true;
	protected double metricThreshold = -1;
	
	MetricDetails (String m, int idx, boolean on, double t) {
		this.metricName = m;
		this.metricIndex = idx;
		this.isOn = on; 
		this.metricThreshold = t;
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
