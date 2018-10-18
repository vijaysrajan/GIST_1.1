package com.fratics.precis.fpg.job;

import com.fratics.precis.fpg.config.FPGConfig;
import com.fratics.precis.fpg.fptreebilder.MetricList;
import java.lang.Comparable;

public class LevelOneDimValMetrics  implements Comparable<LevelOneDimValMetrics>  {

	private String dim = null;
	private String val = null;
	private String separator = null;
	private String dimval = null;
	private MetricList ml = null;
	private String thresholdMetricName = null;
	
	public String getSeparator() {
		return separator;
	}
	public LevelOneDimValMetrics (String _s, MetricList _ml, String _thresholdMetricName, String separator) {
		dimval = _s;
		this.separator = separator;
		String [] sArr = this.dimval.split(separator, -1);
		dim = sArr[0];
		val = sArr[1];
		ml = _ml;
		thresholdMetricName = _thresholdMetricName;
	}
	public String getDimval() {
		return dimval;
	}

	public MetricList getMl() {
		return ml;
	}

	public String getThresholdIndex() {
		return thresholdMetricName;
	}
	
	//@Override
	public int compareTo(LevelOneDimValMetrics lodvm)  {
		int retVal = 0;
		if ( this.ml.getSupportMetricValue()  > lodvm.ml.getSupportMetricValue() ) {
			retVal = 1;
		} else if (this.ml.getSupportMetricValue()  < lodvm.ml.getSupportMetricValue()) {
			retVal = -1;
		}
		return retVal;
	}
	
	private StringBuilder _sb_toString = new StringBuilder();
	public String toString() {
		_sb_toString.setLength(0);
		_sb_toString.append(this.getDimval());
		_sb_toString.append(FPGConfig.SEPARATOR_BETWEEN_FIS_AND_METRIC);
		_sb_toString.append(this.ml.toString(FPGConfig.SEPARATOR_BETWEEN_SUCCESSIVE_METRICS));
		return _sb_toString.toString();
	}
	public String getDim() {
		return dim;
	}
	public String getVal() {
		return val;
	}

	
}