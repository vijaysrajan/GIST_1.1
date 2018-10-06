package com.fratics.precis.fpg.config;

public class MetricThresholds<T extends Number & Comparable<T>> { 
	
	private T  threshold;
	public MetricThresholds (T t) {
		threshold = t;
	}
	
	public void setThreshold(T _t) {
		threshold = _t;
	}
	public  T getThreshold() {
		return threshold;
	}
		
	public static <T extends Comparable<T>> int compare(T t1, T t2){
		return t1.compareTo(t2);
	}	
	
	public  boolean isValueGreaterThanOrEqualToThreshold(T _t) {
		if (_t.compareTo(this.threshold) >= 0) { 
			return true;
		}
		else {
			return false;
		}
	}
	
}
