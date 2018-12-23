package com.fratics.precis.fpg.fptreeminer;

import com.fratics.precis.fpg.config.FPGConfig;
import com.fratics.precis.fpg.fptreebilder.MetricList;
import java.util.HashMap;
import java.util.HashSet;
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
	private ArrayList<String> stagingBufferForCombExplosion = null; 
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
		this.stagingBufferForCombExplosion = new ArrayList<String>();
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
		System.out.println(prefix + "--" + headerTableEntryDimValName  );
		//ArrayList<String> arrayL = new ArrayList();
		//TBD Tree is not at all optimised.
		//System.out.println(prefix);
		
		
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
			createFIS2(0,this.prefixParts.length);
			//retaining the below for legacy purposes
			//			for (int i = 0; i <= this.numberOfStages -1 && i <= this.prefixParts.length; i++) {
			//				createFIS(0,this.prefixParts.length - i + 1,new ArrayList<String>(),0,i);
			//			}
		}
	}	
	
	
	
	private void putInFIS() {

		tmpSB.setLength(0);
		tmpSB.append(stagingBufferForCombExplosion.size() + 1);
		tmpSB.append(separatorBetwnlevelAndRule);
		tmpSB.append(this.headerTableEntryDimValName);
		if (stagingBufferForCombExplosion.size() > 0 ) tmpSB.append(this.separatorBetwnSuccessiveDimVal);
		for (int j = 0; j < stagingBufferForCombExplosion.size(); j++) {
			tmpSB.append(stagingBufferForCombExplosion.get(j));
				if (j < stagingBufferForCombExplosion.size() - 1) {
					tmpSB.append(this.separatorBetwnSuccessiveDimVal);
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
	
	private void createFIS2(int from, int to) {
		putInFIS();
		for (int i = from; i < to; i++) {
			stagingBufferForCombExplosion.add(this.prefixParts[i]);
			if (stagingBufferForCombExplosion.size() < this.numberOfStages) {
				createFIS2(i+1,to);
			}
			stagingBufferForCombExplosion.remove(stagingBufferForCombExplosion.size() -1);
		}
	}
	
	
	private void createFIS(int from,
			   int to,
			   ArrayList<String> fis,
			   int nestLoopCount,
			   int r) {
		if( (nestLoopCount == r) ) { // || r == FPGConfig.NO_OF_STAGES) {
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
	
	
	ArrayList<String> arr = new ArrayList<String>();
	private void createFIS_TestParts(int from, int to, boolean print) {
		if (print == true)
			System.out.println(Arrays.toString(stagingBufferForCombExplosion.toArray()));
		arr.add(Arrays.toString(stagingBufferForCombExplosion.toArray()));
		for (int i = from; i < to; i++) {
			stagingBufferForCombExplosion.add(this.prefixParts[i]);
			//if (stagingBufferForCombExplosion.size() < this.numberOfStages) {
			createFIS_TestParts(i+1,to,print);
			//}
			stagingBufferForCombExplosion.remove(stagingBufferForCombExplosion.size() -1);
		}
	}
	
	
	
	
	public static void main(String [] args) {
//		MineFPTree mfpt = new MineFPTree(" & ", "," , ";","\n", 5);
//		MetricList ml = new MetricList();
//		ml.addMetricToList("imp", 45, true);
//		mfpt.mineFISFromFPTree("f=t", "a=t & b=t & c=t & d=t & e=t",ml);
//		MetricList ml2 = new MetricList();
//		ml2.addMetricToList("imp", 12, true);
//		mfpt.mineFISFromFPTree("f=t", "a=t",ml2);
//		MetricList ml4 = new MetricList();
//		ml4.addMetricToList("imp", 2, true);
//		mfpt.mineFISFromFPTree("f=t", "e=t",ml4);
//		MetricList ml3 = new MetricList();
//		ml3.addMetricToList("imp", 1, true);
//		mfpt.mineFISFromFPTree("f=t", "",ml3);
//		
//		System.out.println(mfpt.toString(2));
		MineFPTree mfpt = new MineFPTree(" & ", "," , ";","\n", 5);
		mfpt.prefixParts = new String[10];
		mfpt.prefixParts[0] = "a";
		mfpt.prefixParts[1] = "b";
		mfpt.prefixParts[2] = "c";
		mfpt.prefixParts[3] = "d";
		mfpt.prefixParts[4] = "e";
		mfpt.prefixParts[5] = "f";
		mfpt.prefixParts[6] = "g";
		mfpt.prefixParts[7] = "h";
		mfpt.prefixParts[8] = "i";
		mfpt.prefixParts[9] = "j";
		long t0 = System.currentTimeMillis();
		
		//here we compute the time taken to compute all the 2^10 powersets 
		mfpt.createFIS_TestParts(0, 10,true);
		System.out.println(System.currentTimeMillis() - t0);
		
		//cleanup
		while (mfpt.arr.size() > 0) {
			mfpt.arr.remove(0);
		}
		
		//here we pre-compute the 2^5 combinations
		mfpt.createFIS_TestParts(0, 6,false);
		ArrayList<String> arr2 = new ArrayList<String>();
		//we store the results in a temp array while simultaneously cleaning up the staging array which is the holding area
		while (mfpt.arr.size() > 0) {
			arr2.add(mfpt.arr.remove(0));
		}
		t0 = System.currentTimeMillis();
		
		//now we compute the remaining 2^5 remaining powerset of f through j
		mfpt.createFIS_TestParts(6, 10,false);

		for (int i = 0; i < mfpt.arr.size();i++) {
			for (int j = 0; j < arr2.size(); j++) {
				System.out.println( arr2.get(j) +  mfpt.arr.get(i)) ;
				
			}
		}
		System.out.println(System.currentTimeMillis() - t0);

	}
	
	
}
