package com.fratics.precis.fpg.fptreeminer;

import com.fratics.precis.fpg.config.FPGConfig;
import com.fratics.precis.fpg.fptreebilder.HeaderTable;
import com.fratics.precis.fpg.fptreebilder.MetricList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
//import java.util.Collections;
import java.util.Map;
import java.util.Arrays;
import java.util.BitSet;

public class MineFPTree {
	
	
	private HashMap<String,MetricList> FIS = null;
	private String separatorBetwnSuccessiveDimVal = null; 
	private String separatorBetwnlevelAndRule = null;
	private String separatorBetwnRuleAndMetric = null;
	private String separatorBetwnSuccessiveFIS = null;
	//private int metricIndexForSupport = -1;
	private int numberOfStages = 0;
	private ArrayList<String> stagingBufferForCombExplosion = null;
	private String pathPrefix = null;;
	private String [] prefixParts = null;
	
	private MetricList ml = null;
	private String headerTableEntryDimValName = null;
	private StringBuilder tmpSB = new StringBuilder();
	
	//private ArrayList<FrequentPathToHeaderNode> arrListFreqPath = new ArrayList<FrequentPathToHeaderNode>();
	
	
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
		//System.out.println(headerTableEntryDimValName + " : " + prefix);
		//ArrayList<String> arrayL = new ArrayList();
		//TBD Tree is not at all optimised.
		//System.out.println(prefix);
		
		this.pathPrefix = prefix;
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
			createFIS4(0,this.prefixParts.length);
		}
	}	
	
	
	
	private void putInFIS() {

		//tmpSB.setLength(0);
		tmpSB.delete(0, tmpSB.length());
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
	
	
	//This is a better implementation that the other three implementations because as tested independently, 
	//unnecessary work is avoided in looping and just adding then removing the prefixParts. 
	private void createFIS4(int from, int to) {
		putInFIS();
		if (stagingBufferForCombExplosion.size() == this.numberOfStages) {
			return;
		}
		
		for (int i = from; i < to && (stagingBufferForCombExplosion.size() < this.numberOfStages); i++) {
			stagingBufferForCombExplosion.add(this.prefixParts[i]);
			createFIS4(i+1,to);
			stagingBufferForCombExplosion.remove(stagingBufferForCombExplosion.size() -1);
		}
	}
	
	private void createFIS3(int from, int to) {
		 
		putInFIS();
		for (int i = from; i < to; i++) {
			stagingBufferForCombExplosion.add(this.prefixParts[i]);
			if (stagingBufferForCombExplosion.size() < this.numberOfStages) {
				createFIS3(i+1,to);
			}
			stagingBufferForCombExplosion.remove(stagingBufferForCombExplosion.size() -1);
		}
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

	
//	// Returns length of LCS for X[0..m-1], Y[0..n-1] 
//  static ArrayList<String> lcs(String [] X, String [] Y, int m, int n) 
//  { 
//      int[][] L = new int[m+1][n+1]; 
//      // Following steps build L[m+1][n+1] in bottom up fashion. Note 
//      // that L[i][j] contains length of LCS of X[0..i-1] and Y[0..j-1]  
//      for (int i=0; i<=m; i++) 
//      { 
//          for (int j=0; j<=n; j++) 
//          { 
//              if (i == 0 || j == 0) 
//                  L[i][j] = 0; 
//              else if (X[i-1] == Y[j-1]) 
//                  L[i][j] = L[i-1][j-1] + 1; 
//              else
//                  L[i][j] = Math.max(L[i-1][j], L[i][j-1]); 
//          } 
//      } 
// 
//      // Following code is used to print LCS 
//      int index = L[m][n]; 
//      int temp = index; 
// 
//      // Create a character array to store the lcs string 
//      String[] lcs = new String[index+1]; 
//      lcs[index] = ""; // Set the terminating character 
// 
//      // Start from the right-most-bottom-most corner and 
//      // one by one store characters in lcs[] 
//      int i = m, j = n; 
//      while (i > 0 && j > 0) 
//      { 
//      		// If current character in X[] and Y are same, then 
//      		// current character is part of LCS 
//      		if (X[i-1] == Y[j-1]) 
//      		{ 
//      			// Put current character in result 
//      			lcs[index-1] = X[i-1];  
//        
//      			// reduce values of i, j and index 
//      			i--;  
//      			j--;  
//      			index--;      
//          } 
// 
//      		// If not same, then find the larger of two and 
//      		// go in the direction of larger value 
//      		else if (L[i-1][j] > L[i][j-1]) 
//      			i--; 
//      		else
//      			j--; 
//      } 
//      ArrayList<String> retVal = new ArrayList<String> ();
//      for(int k=0;k<=temp;k++) {
//            //System.out.print(lcs[k]);
//            retVal.add(lcs[k]);
//      }
//      return retVal;
//  } 


	
//	private void createFIS(int from,
//			   int to,
//			   ArrayList<String> fis,
//			   int nestLoopCount,
//			   int r) {
//		if( (nestLoopCount == r) ) { // || r == FPGConfig.NO_OF_STAGES) {
//			//Collections.sort(fis); -- Don't uncomment or delete this line. Let it be a reminder that it screws things up
//			tmpSB.setLength(0);
//			tmpSB.append(fis.size() + 1);
//			tmpSB.append(separatorBetwnlevelAndRule);
//			tmpSB.append(this.headerTableEntryDimValName);
//			if (fis.size() > 0 ) tmpSB.append(this.separatorBetwnSuccessiveDimVal);
//			for (int j = 0; j < fis.size(); j++) {
//				if (!fis.get(j).equals("")) {
//					tmpSB.append(fis.get(j));
//					if (j < fis.size() - 1) {
//						tmpSB.append(this.separatorBetwnSuccessiveDimVal);
//					}
//				}
//			}
//			if(FIS.containsKey(tmpSB.toString())) {
//				MetricList ml2 = FIS.get(tmpSB.toString());
//				ml2.updateMetricList(ml);
//			} else {
//				FIS.put(tmpSB.toString(), MetricList.makeReplica(ml));
//			}
//			return;
//		}
//		for (int i = from; i < to; i++) {
//			fis.add(prefixParts[i]);
//			createFIS(i+1,to+1,fis,nestLoopCount + 1,r);
//			fis.remove(fis.size() - 1);
//		}
//	}
	
//	public void mineFISFromFPTree2 (ArrayList<FrequentPathToHeaderNode> _arrListFreqPath,
//            String headerTableEntryDimValName,
//            double supportVal,
//            HeaderTable ht) {
//arrListFreqPath.clear();
//arrListFreqPath = _arrListFreqPath;
//
//
//int len = arrListFreqPath.size();
//for (int i = 0; i < len; i++) {
////System.out.println(arrListFreqPath.get(i).getPath() +  "  " + arrListFreqPath.get(i).getPathMetric().toString());
//if (arrListFreqPath.get(i).getPathMetric().getSupportMetricValue() >= supportVal) {
//this.prefixParts = arrListFreqPath.get(i).getElementsOfPath();
//this.ml = arrListFreqPath.get(i).getPathMetric();
//this.headerTableEntryDimValName = headerTableEntryDimValName;
////explode freq path and put in FIS cache with metric value
//createFIS2(0,this.prefixParts.length);		
//}
//}
//System.out.println(len);
//ArrayList<FrequentPathToHeaderNode> tempArrListFreqPathContenders = new ArrayList<FrequentPathToHeaderNode>();		
//for (int i = 0; i < len - 1; i++) {
//for (int j = i + 1; j < len; j++) {
//
//BitSet b = new BitSet();
//b.or(arrListFreqPath.get(i).getPathBitSet());
//b.and(arrListFreqPath.get(j).getPathBitSet());
//MetricList ml = null;
//if ( (arrListFreqPath.get(i).getPathMetric().getSupportMetricValue() < supportVal) && 
//(arrListFreqPath.get(j).getPathMetric().getSupportMetricValue() < supportVal) ) {
//ml = MetricList.makeReplica(arrListFreqPath.get(i).getPathMetric());
//ml.updateMetricList(arrListFreqPath.get(j).getPathMetric());
//} else if ((arrListFreqPath.get(i).getPathMetric().getSupportMetricValue() < supportVal) && 
//   (arrListFreqPath.get(j).getPathMetric().getSupportMetricValue() >= supportVal)) {
//ml = MetricList.makeReplica(arrListFreqPath.get(i).getPathMetric());
//} else if ((arrListFreqPath.get(i).getPathMetric().getSupportMetricValue() >= supportVal) && 
//   (arrListFreqPath.get(j).getPathMetric().getSupportMetricValue() < supportVal)) {
//ml = MetricList.makeReplica(arrListFreqPath.get(j).getPathMetric());
//}  else {
//continue;
//}
//
//if (!b.isEmpty()) {
//tempArrListFreqPathContenders.add(new FrequentPathToHeaderNode(b, ml, ht));
//}
//}
//}
//
//arrListFreqPath = tempArrListFreqPathContenders;		
//len = arrListFreqPath.size();
//for (int i = 0; i < len; i++) {
////System.out.println(arrListFreqPath.get(i).getPath() +  "  " + arrListFreqPath.get(i).getPathMetric().toString());
//if (arrListFreqPath.get(i).getPathMetric().getSupportMetricValue() >= supportVal) {
//this.prefixParts = arrListFreqPath.get(i).getElementsOfPath();
//this.ml = arrListFreqPath.get(i).getPathMetric();
//this.headerTableEntryDimValName = headerTableEntryDimValName;
////explode freq path and put in FIS cache with metric value
//createFIS2(0,this.prefixParts.length);		
//}
//}
//
//}	
	
	
}
