package com.fratics.precis.fpg.fptreebilder;

public class HeaderTableRecord implements Comparable<HeaderTableRecord> {
	
	private HeaderTableRecordElement element = null;
	private MetricList metrics = null;
	FPTreeNode firstNode = null;
	FPTreeNode lastNode = null;
	
	
	public HeaderTableRecordElement getElement() {
		return element;
	}

	public MetricList getMetrics() {
		return metrics;
	}
	
	public HeaderTableRecord(HeaderTableRecordElement ele, MetricList metrics) {
		this.element = ele;
		this.metrics = metrics;
	}
	
    public int compareTo( final HeaderTableRecord o) {
        return Double.compare(o.metrics.getAllMetric().get(o.metrics.getNameOfMetricForSupportThreshold()),
        		                  this.metrics.getAllMetric().get(this.metrics.getNameOfMetricForSupportThreshold())  
        		                  );
    }

	public FPTreeNode getFirstNode() {
		return firstNode;
	}

	public void setFirstNode(FPTreeNode firstNode) {
		this.firstNode = firstNode;
		this.lastNode = firstNode;
	}

	public FPTreeNode getLastNode() {
		return lastNode;
	}

	public void setLastNode(FPTreeNode lastNode) {
		this.lastNode = lastNode;
	}

}
