package com.fratics.precis.fpg.fptreebilder;

public class MetricObject {
	private String metricName = "";
	private double metric = 0.0;
	
	public MetricObject(String name) {
		this.metricName = name;
	}
	
	public MetricObject(String name, double metric) {
		this.metricName = name;
		this.metric = metric;
	}

	public double getMetric() {
		return metric;
	}

	public void setMetric(double metric) {
		this.metric = metric;
	}

	public String getMetricName() {
		return metricName;
	}

	public void setMetricName(String metricName) {
		this.metricName = metricName;
	}
	
	
}
