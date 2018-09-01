package com.fratics.precis.fis.schema;

import java.util.ArrayList;

import com.fratics.precis.util.Logger;

public class MetricListFromSchema {
	private static ArrayList<MetricDetails> metricList = new ArrayList<MetricDetails>();
	private static Logger logger = Logger.getInstance();
	
	public static boolean addMetricDetails (MetricDetails md) {
		boolean ret = true;
		for (MetricDetails m : metricList) {
			if ((m.getMetricName().equalsIgnoreCase(md.getMetricName())) && (m.isOn() == true) && (md.isOn() == true)){
				logger.info("Metric with name " + md.getMetricName() +  
						    " at index " + md.getMetricIndex() + 
						    " with threshold " + md.getMetricThreshold() +
						    " seems to be a rduplicate of metric with name "  + md.getMetricName() +
						    " at index " + md.getMetricIndex() +  " with threshold " + md.getMetricThreshold()
						    );
				ret = false;
				break;
			}
		}
		if (ret == true) {
			metricList.add(md);
		}
		return ret;
	}

	public static double getMetricThreshold() {
		return metricList.get(0).getMetricThreshold();
	}
	
	public static double getMetricThreshold(int i) {
		return metricList.get(i).getMetricThreshold();
	}
	
	public static MetricDetails getMetric(int i) {
		return metricList.get(i);
	}
	
	public static double getMetricThreshold(String s) {
		double ret = 0.0;
		for (MetricDetails md : metricList) {
			if (md.getMetricName().equalsIgnoreCase(s)) {
				ret = md.getMetricThreshold();
				break;
			}
		}
		return ret;
	}
	
}
