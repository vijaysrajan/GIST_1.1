package com.fratics.precis.fpg.fptreeminer;

import com.fratics.precis.fpg.fptreebilder.MetricList;
import java.util.HashMap;
import java.util.ArrayList;
//import java.util.Collections;
import java.util.Map;
import java.util.Arrays;

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
	
	public String getSeparatorBetwnSuccessiveDimVal() {
		return separatorBetwnSuccessiveDimVal;
	}

	public String getSeparatorBetwnlevelAndRule() {
		return separatorBetwnlevelAndRule;
	}

	public String getSeparatorBetwnRuleAndMetric() {
		return separatorBetwnRuleAndMetric;
	}

	public String getSeparatorBetwnSuccessiveFIS() {
		return separatorBetwnSuccessiveFIS;
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
			for (int i = 0; i <= this.numberOfStages -1 && i <= this.prefixParts.length; i++) {
				createFIS(0,this.prefixParts.length - i + 1,new ArrayList<String>(),0,i);
			}
		}
	}	
	
	private void createFIS(int from,
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
			createFIS(i+1,to+1,fis,nestLoopCount + 1,r);
			fis.remove(fis.size() - 1);
		}
	}
	
	private static StringBuilder sbReOrder = new StringBuilder();
	public static String reOrderLexicographically ( String str, String separator1st,  String separator2nd) {
		sbReOrder.setLength(0);
		String [] splitter =  str.split(separator1st, -1);
		String [] splitter2 = splitter[1].split(separator2nd, -1);
		Arrays.sort(splitter2);
		sbReOrder.append(splitter[0]);
		sbReOrder.append(separator1st);
		for (int i = 0; i < splitter2.length; i++) {
			sbReOrder.append(splitter2[i]);
			if (i < (splitter2.length - 1)) {
				sbReOrder.append(separator2nd);
			}
		}
		return sbReOrder.toString();
	}
	
	
	public String toString (double supportThreshold) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, MetricList> entry : this.FIS.entrySet()) {
			if (entry.getValue().getSupportMetricValue() >= supportThreshold) {
				//System.out.println(entry.getKey() + ":" + entry.getValue().getSupportMetricValue() );
				
				sb.append(MineFPTree.reOrderLexicographically(entry.getKey(), this.separatorBetwnlevelAndRule, this.separatorBetwnSuccessiveDimVal));
				sb.append(this.separatorBetwnRuleAndMetric);
				sb.append(entry.getValue().toString(this.separatorBetwnRuleAndMetric));
				sb.append(this.separatorBetwnSuccessiveFIS);
				
			}
		}
		return sb.toString();
} 	
	
	public static void main(String [] args) {
		MineFPTree mfpt = new MineFPTree(" & ", "," , ";","\n", 5);
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
