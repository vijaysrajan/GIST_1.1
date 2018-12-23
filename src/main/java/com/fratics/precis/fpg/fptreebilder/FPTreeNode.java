package com.fratics.precis.fpg.fptreebilder;

import java.util.HashMap;

import com.fratics.precis.fpg.config.FPGConfig;


public class FPTreeNode {
	private boolean	 				nullNode   = false;
	private String                   dimValName = null; //This represents the dimVal pair in the HeaderTable 
	private FPTreeNode 				parentNode = null;
	private HashMap<String,FPTreeNode> children   = null;
	private FPTreeNode 				nextPeer	   = null;
	private MetricList 				metrics    = null;
	private long 					lineageCount = -1;
	private String					lineageStr = null;
	private int 						level  = 0;
	private static int				maxLevel = 0;			
	
	public String getLineageStr() {
		return lineageStr;
	}
	
	public String getDimValName() {
		return dimValName;
	}

	public void setNullNode(boolean nullNode) {
		this.nullNode = nullNode;
		level = 0;
		maxLevel = level;
	}

	public boolean isNullNode() {
		return nullNode;
	}
	
	
	public FPTreeNode(boolean nullNode) {
		this.nullNode = nullNode;
	}
	
	public void addChild(FPTreeNode n) {
		if (children == null) {
			children = new HashMap<String,FPTreeNode>();
		}

		children.put(n.dimValName, n);
		n.parentNode = this;
		n.level = this.level + 1;
		FPTreeNode.maxLevel = (n.level > FPTreeNode.maxLevel)?n.level:FPTreeNode.maxLevel;
		FPTreeNode tmp = n;
		while (tmp.nullNode == false) {
			tmp.lineageCount++;
			tmp = tmp.parentNode;
		}
		
//		
		if (this.parentNode == null)  {
			//n.lineageStr = n.dimValName;
			n.lineageStr = "";
		} else if (this.parentNode.isNullNode()) {
			n.lineageStr = this.dimValName;
		} else {
			StringBuilder sbx = new StringBuilder();
			sbx.append(this.dimValName);
			sbx.append(FPGConfig.SEPERATOR_BETWEEN_SUCCESSIVE_DIMVALS);
			sbx.append(this.lineageStr);
			n.lineageStr = sbx.toString();
		}		
		
	}
	
	public FPTreeNode(String dVN,
			   MetricList metrics 
			  ) {
		this.dimValName = dVN;
		this.metrics = MetricList.makeReplica(metrics);
		
	}
	
	public long getLineageCount() {
		return lineageCount;
	}

	public MetricList getMetrics() {
		return metrics;
	}
//	public int getHeaderTableIndex() {
//		return headerTableIndex;
//	}
	
	public FPTreeNode getNextPeer() {
		return nextPeer;
	}
	public void setNextPeer(FPTreeNode fptn) {
		this.nextPeer = fptn;
	}
	
	
	public boolean hasChild(String child) {
		if (children == null) return false;
		return children.containsKey(child);
	}

	public boolean hasChild(FPTreeNode child) {
		return children.containsKey(child.dimValName);
	}
	
	
	public FPTreeNode getChild(String s) {
		return children.get(s);
	}
	
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		sb.append("dimValName:");
		sb.append(dimValName);
		sb.append("(");
		sb.append(lineageCount);
		sb.append(")");
		sb.append(" ");
		sb.append(metrics.toString());
//		sb.append(" ");
//		sb.append("lineageCount:");

		return sb.toString();
	}
	
	public FPTreeNode getParentNode() {
		return parentNode;
	}
	
	public static int getMaxLevel() {
		return FPTreeNode.maxLevel;
	}
		
}
