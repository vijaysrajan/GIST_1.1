package com.fratics.precis.fpg.fptreeminer;

import com.fratics.precis.fpg.fptreebilder.MetricList;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class MineFPTree {
	
	
	private HashMap<String,MetricList> FIS = null;
	private String separatorBetwnSuccessiveDimVal = null; 
	private int metricIndexForSupport = -1;
	private int numberOfStages = 0;
	private String [] prefixParts = null;
	private MetricList ml = null;
	
	MineFPTree(String separatorBetwnSuccessiveDimVal, 
			   int metricIndexForSupport,
			   int numberOfStages) {
		FIS = new HashMap<String,MetricList>();
		this.separatorBetwnSuccessiveDimVal = separatorBetwnSuccessiveDimVal;
		this.metricIndexForSupport = metricIndexForSupport;
		this.numberOfStages = numberOfStages;
	}
	
	public void mineFISFromFPTree(String bottomDim, 
			                      String prefix, 
			                      MetricList ml			                      
			                      ) {
		//ArrayList<String> arrayL = new ArrayList();
		this.prefixParts = prefix.split(separatorBetwnSuccessiveDimVal);
		this.ml = ml;
		for (int i = 1; i <= this.numberOfStages; i++) {
			createFIS(0,this.prefixParts.length - i + 1,new ArrayList<String>(),0,i);
		}
	}
	
	private StringBuilder tmpSB = new StringBuilder();
	private void createFIS(int from,
						   int to,
						   ArrayList<String> fis,
						   int nestLoopCount,
						   int r) {
		if(nestLoopCount == r) {
			//Collections.sort(fis);
			for (int j = 0; j < fis.size(); j++) {
				tmpSB.append(fis.get(j));
				if (j < fis.size() - 1) {
					tmpSB.append(this.separatorBetwnSuccessiveDimVal);
				}
			}
			
			if(FIS.containsKey(tmpSB.toString())) {
				MetricList ml2 = FIS.get(tmpSB.toString());
				ml2.updateMetricList(ml);
			} else {
				FIS.put(tmpSB.toString(), ml);
			}
			tmpSB.setLength(0);
			return;
		}
		for (int i = from; i < to; i++) {
				fis.add(prefixParts[i]);
				createFIS(i+1,to+1,fis,nestLoopCount + 1,r);
				fis.remove(fis.size() - 1);
		}
	}
	
	public static void main(String [] args) {
		MineFPTree mfpt = new MineFPTree(",", 0, 3);
		MetricList ml = new MetricList();
		ml.addMetricToList("imp", 45, true);
		mfpt.mineFISFromFPTree("f=t", "a=t,b=t,c=t,d=t,e=t",ml);
		
		for (Map.Entry<String, MetricList> entry : mfpt.FIS.entrySet()) {
			if (entry.getValue().getSupportMetricValue() > 44) {
				System.out.println(entry.getKey() + ":" + entry.getValue().getSupportMetricValue() );
			}
		}
		
	}
	
	
}
