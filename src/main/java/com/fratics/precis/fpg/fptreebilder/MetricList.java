package com.fratics.precis.fpg.fptreebilder;

import java.util.HashMap;
import java.util.Set;


public class MetricList {
	private HashMap<String,Double> allMetric = null;
	private String nameOfMetricForSupportThreshold = null; 
	private static HashMap<String,Double> referenceMetricList = null;
	private static String staticNameOfMetricForSupportThreshold = null;
	

	
	
	public static MetricList makeReplica(MetricList ml) {
		MetricList ret = makeBlankMetricList();
		Set<String> keySet = ml.allMetric.keySet();
		for (String s1 : keySet) {
			ret.addMetricToList(s1, ml.allMetric.get(s1).doubleValue());
		}
		//ret.nameOfMetricForSupportThreshold = ml.nameOfMetricForSupportThreshold;
		return ret;
	}
	
	public static MetricList makeBlankMetricList() {
		MetricList ret_ml = new MetricList();
		Set<String> keySet = referenceMetricList.keySet();
		for (String s : keySet) {
			ret_ml.addMetricToList(s, 0.0);
		}
		ret_ml.nameOfMetricForSupportThreshold = MetricList.staticNameOfMetricForSupportThreshold;
		return ret_ml;
	}	
	
	
	
	public double getSupportMetricValue() {
		return allMetric.get(nameOfMetricForSupportThreshold).doubleValue();
	}
	
	
	public void addMetricToList(String metricName, double metric, boolean supportThresholdAvailable) {
		if (allMetric == null) {
			this.allMetric = new HashMap<String,Double>();
			if (referenceMetricList == null) {
				referenceMetricList = this.allMetric; 
			}
		}

		allMetric.put(metricName, metric);
		//allMetric.add(new MetricObject (metricName, metric));
		if ((supportThresholdAvailable) && (nameOfMetricForSupportThreshold == null) ){
			nameOfMetricForSupportThreshold = metricName;
			staticNameOfMetricForSupportThreshold = this.nameOfMetricForSupportThreshold;
		} else if ((supportThresholdAvailable) && (nameOfMetricForSupportThreshold != null)) {
			//throw exception
		} //else all is well 
	}
	
	//This method may be unnecessary
	private 	void addMetricToList(String metricName, double metric) {
		if (allMetric == null) {
			this.allMetric = new HashMap<String,Double>();
		}
		allMetric.put(metricName, metric);
	}	
	
//	public int size() {
//		return allMetric.size();
//	}
	public void updateMetricList(MetricList ml) {
		if (allMetric == null) {
			this.allMetric = new HashMap<String,Double>();
		}

		Set<String> keySet = ml.allMetric.keySet();
		for (String s1 : keySet) {
					this.allMetric.put(s1, ml.allMetric.get(s1).doubleValue() + this.allMetric.get(s1).doubleValue());
		}
	}


	public HashMap<String,Double> getAllMetric() {
		return allMetric;
	}


	public String getIndexOfMetricWithSupportThreshold() {
		return nameOfMetricForSupportThreshold;
	}


	public static String getStaticIndexOfMetricWithSupportThreshold() {
		return staticNameOfMetricForSupportThreshold;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		Set<String> keySet = allMetric.keySet();
		for (String s1 : keySet) {
			sb.append(s1);
			sb.append("=");
			sb.append(allMetric.get(s1));
			sb.append("::");
		}
		return sb.toString();
	}

	public String toString(String separatorBetwnMetrics) {
		StringBuilder sb = new StringBuilder();
		
		Set<String> keySet = allMetric.keySet();
		int sz = keySet.size();
		int i = 0;
		for (String s1 : keySet) {
			sb.append(allMetric.get(s1));
			if (i < (sz -1)) {
				sb.append(separatorBetwnMetrics);
			}
			i++;
		}
		return sb.toString();
	}
	

}
