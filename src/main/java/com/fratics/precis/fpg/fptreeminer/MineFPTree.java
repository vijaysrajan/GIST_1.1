package com.fratics.precis.fpg.fptreeminer;

import com.fratics.precis.fpg.fptreebilder.MetricList;
import java.util.HashMap;
import java.util.ArrayList;
//import java.util.Collections;
import java.util.Map;

public class MineFPTree {
	
	
	private HashMap<String,MetricList> FIS = null;
	private String separatorBetwnSuccessiveDimVal = null; 
	private String separatorBetwnlevelAndRule = null;
	private String separatorBetwnRuleAndMetric = null;
	private String separatorBetwnSuccessiveFIS = null;
	//private int metricIndexForSupport = -1;
	private int numberOfStages = 0;
	private String [] prefixParts = null;
	private MetricList ml = null;
	private String headerTableEntryDimValName = null;
	private StringBuilder tmpSB = new StringBuilder();
	
	
	public MineFPTree(String separatorBetwnSuccessiveDimVal, 
			          String separatorBetwnlevelAndRule,
			          String separatorBetwnRuleAndMetric,
			          String separatorBetwnSuccessiveFIS,
			          //int metricIndexForSupport,
			          int numberOfStages) {
		FIS = new HashMap<String,MetricList>();
		this.separatorBetwnSuccessiveDimVal = separatorBetwnSuccessiveDimVal;
		this.separatorBetwnlevelAndRule = separatorBetwnlevelAndRule;
		this.separatorBetwnSuccessiveFIS = separatorBetwnSuccessiveFIS;
		this.separatorBetwnRuleAndMetric = separatorBetwnRuleAndMetric;
		this.numberOfStages = numberOfStages;
	}
	
	
	public void mineFISFromFPTree(String headerTableEntryDimValName, 
			                      String prefix, 
			                      MetricList ml			                      
			                      ) {
		//System.out.println(headerTableEntryDimValName + "--" + prefix );
		//ArrayList<String> arrayL = new ArrayList();
		this.prefixParts = prefix.split(this.separatorBetwnSuccessiveDimVal,-1);
		this.ml = ml;
		this.headerTableEntryDimValName = headerTableEntryDimValName;
		if (this.prefixParts.length == 1 && this.prefixParts[0].equals("")) {
			tmpSB.setLength(0);
			tmpSB.append(1);
			tmpSB.append(this.separatorBetwnlevelAndRule);
			tmpSB.append(this.headerTableEntryDimValName);
			if(FIS.containsKey(tmpSB.toString())) {
				MetricList ml2 = FIS.get(tmpSB.toString());
				ml2.updateMetricList(ml);
			} else {
				FIS.put(tmpSB.toString(), MetricList.makeReplica(ml));
			}
			return;
		} else  {
			for (int i = 0; i <= this.numberOfStages && i <= this.prefixParts.length; i++) {
				createFIS2(0,this.prefixParts.length - i + 1,new ArrayList<String>(),0,i);
			}
		}
	}	
	
	private void createFIS2(int from,
			   int to,
			   ArrayList<String> fis,
			   int nestLoopCount,
			   int r) {
		if(nestLoopCount == r) {
			//Collections.sort(fis); -- Don't uncomment or delete this line. Let it be a reminder that it screws things up
			tmpSB.setLength(0);
			tmpSB.append(fis.size() + 1);
			tmpSB.append(separatorBetwnlevelAndRule);
			tmpSB.append(this.headerTableEntryDimValName);
			if (fis.size() > 0 ) tmpSB.append(this.separatorBetwnSuccessiveDimVal);
			for (int j = 0; j < fis.size(); j++) {
				if (!fis.get(j).equals("")) {
					tmpSB.append(fis.get(j));
					if (j < fis.size() - 1) {
						tmpSB.append(this.separatorBetwnSuccessiveDimVal);
					}
				}
			}
			if(FIS.containsKey(tmpSB.toString())) {
				MetricList ml2 = FIS.get(tmpSB.toString());
				ml2.updateMetricList(ml);
			} else {
				FIS.put(tmpSB.toString(), MetricList.makeReplica(ml));
			}
			return;
		}
		for (int i = from; i < to; i++) {
			fis.add(prefixParts[i]);
			createFIS2(i+1,to+1,fis,nestLoopCount + 1,r);
			fis.remove(fis.size() - 1);
		}
	}
	
	public String toString (double supportThreshold) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, MetricList> entry : this.FIS.entrySet()) {
			if (entry.getValue().getSupportMetricValue() >= supportThreshold) {
				//System.out.println(entry.getKey() + ":" + entry.getValue().getSupportMetricValue() );
				sb.append(entry.getKey());
				sb.append(this.separatorBetwnRuleAndMetric);
				sb.append(entry.getValue().toString());
				sb.append(this.separatorBetwnSuccessiveFIS);
				
			}
		}
		return sb.toString();
} 
	
	
	public static void main(String [] args) {
		MineFPTree mfpt = new MineFPTree(" & ", "," , ";","~~", 5);
		MetricList ml = new MetricList();
		ml.addMetricToList("imp", 45, true);
		mfpt.mineFISFromFPTree("f=t", "a=t & b=t & c=t & d=t & e=t",ml);
		MetricList ml2 = new MetricList();
		ml2.addMetricToList("imp", 12, true);
		mfpt.mineFISFromFPTree("f=t", "a=t",ml2);
		MetricList ml4 = new MetricList();
		ml4.addMetricToList("imp", 2, true);
		mfpt.mineFISFromFPTree("f=t", "e=t",ml4);
		MetricList ml3 = new MetricList();
		ml3.addMetricToList("imp", 1, true);
		mfpt.mineFISFromFPTree("f=t", "",ml3);
		System.out.println(mfpt.toString(2));
	}
	
	
}
