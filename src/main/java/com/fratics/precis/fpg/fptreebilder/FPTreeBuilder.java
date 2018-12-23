package com.fratics.precis.fpg.fptreebilder;

import java.util.BitSet;
import java.util.HashSet;

import com.fratics.precis.fpg.config.FPGConfig;
import com.fratics.precis.fpg.config.Util;
import com.fratics.precis.fpg.fptreeminer.MineFPTree;
import com.fratics.precis.exception.PrecisException;
import java.io.RandomAccessFile;

public class FPTreeBuilder {
	private static HeaderTable 		ht         = null;
	private boolean isInitialized = false;
	private FPTreeNode rootNode = null;

	public void initialize(HeaderTable tbl) throws Exception{
		this.setHeaderTable (tbl);		
		isInitialized = true;
	}
	
	public void setHeaderTable (HeaderTable tbl) throws Exception {
		if (FPTreeBuilder.ht == null) {
			FPTreeBuilder.ht = tbl;
		} else {
			throw new PrecisException("Trying to reset the header table for FPTreeBuilder");
		}
	}
	
	private boolean isDimIgnored(int idx, int [] dimIndexesToIgnore) {
		if (dimIndexesToIgnore == null) return false;
		for (int i = 0; i < dimIndexesToIgnore.length; i++) {
			if (dimIndexesToIgnore[i] == idx) 
				return true;
		}
		return false;
	}
	
	private boolean isDimIgnored(int idx, HashSet<Integer> dimIndexesToIgnore) {
		if (dimIndexesToIgnore == null) return false;
		return dimIndexesToIgnore.contains(new Integer(idx));
	}

	
	
//	//use HashSet instead of String array
//	private boolean isDimValInIgnoreList(String [] ignoreList, String valPart) {
//		if (ignoreList.length == 0) return false;
//		for (int i = 0; i < ignoreList.length; i++) {
//			if (ignoreList[i].equalsIgnoreCase(valPart)) return true;
//		}
//		return false;
//	}
	
	private boolean isDimValInIgnoreList( HashSet<String> dimValIgnoreList, String valPart, String dimName, String separator) {
		if (dimValIgnoreList == null) return false;
		StringBuilder sb__ = new StringBuilder();
		sb__.append(dimName);
		sb__.append(separator);
		sb__.append(valPart);
		return dimValIgnoreList.contains(sb__.toString());
	}
	
	
	private boolean isMetricIndex(int idx, int [] metricIndexes) {
		if (metricIndexes.length == 0) return false;
		for (int i = 0; i < metricIndexes.length; i++) {
			if (metricIndexes[i]== idx) return true;
		}
		return false;
	}
	
	private boolean isMetricSupportIndexInMetricIndexList(int metricIdxForSuppThrshldCmptn, int [] metricIndexes) {
		if (metricIndexes.length == 0) { 
			return false;
		}
		for (int i = 0; i < metricIndexes.length; i++) {
			if (metricIndexes[i] == metricIdxForSuppThrshldCmptn) return true;
		}
		return false;
	}
	
	private void gatherMetric (MetricList ml, 
		                       int metricIndexLength, 
			                   int metricIdxForSuppThrshldCmptn,
			                   String [] referenceSchema,
			                   String [] valPart,
			                   int    [] metricIndexes
			                   ) {
		for (int i = 0; i < metricIndexLength; i++) {
			//new MetricObject(referenceSchema[i], Double.parseDouble(valParts[i]));
			boolean supportIndex = false;
			if (metricIndexes[i] == metricIdxForSuppThrshldCmptn) {
				supportIndex = true;
			}
			ml.addMetricToList(referenceSchema[metricIndexes[i]], Double.parseDouble(valPart[metricIndexes[i]]), supportIndex);
		}	
	}
	
	
	private BitSet b = new BitSet();
	private StringBuilder sb = new StringBuilder();
	
	public void addPathToTree(String    inputData, 
							 String 	   colSeparator, 
							 String [] referenceSchema, 
							 int    [] metricIndexes,
							 int       metricIdxForSuppThrshldCmptn,
							 //String [] valueIgnoreList,
							 HashSet<String> dimValsToIgnore,
							 HashSet<String> valueIgnoreList,
							 HashSet<Integer> dimIndexesToIgnore,
							 String    dimToValSeparator) throws Exception{
		
		if ((isInitialized == false) || ht == null) {
			//throw exception
			throw new PrecisException("Initialized is not called or header table is null. Exception cannot proceed.");
		}
		
		String [] valParts = inputData.split(colSeparator, -1);
		for (int i = 0; i < valParts.length; i++) {
			valParts[i] = valParts[i].trim();
		}
		if ( (referenceSchema.length != valParts.length) && (referenceSchema.length != (1 + valParts.length))) {
			throw new PrecisException("Data and schema have a massive mismatch.");
		}
		
		if (!isMetricSupportIndexInMetricIndexList (metricIdxForSuppThrshldCmptn, metricIndexes) ) {
			//Throw Exception
			throw new PrecisException("The index for support counting is not in the range specified for metric list.");
		}		
		
		MetricList ml = new MetricList();
		gatherMetric (ml, metricIndexes.length, metricIdxForSuppThrshldCmptn,referenceSchema,valParts,metricIndexes ); 

		//ArrayList<Integer> hdrTableSortedIndexMap = new ArrayList<Integer>();
		
		b.clear();
		//reorder record based on header table lookup
		for (int i = 0; i < referenceSchema.length; i++) {
			if ((!isDimValInIgnoreList(dimValsToIgnore, valParts[i],referenceSchema[i],referenceSchema[i])) && 
			    (!isMetricIndex(i,metricIndexes) &&
			    (!isDimIgnored(i,dimIndexesToIgnore))) && 
			    (!valueIgnoreList.contains(valParts[i]))) {
				sb.setLength(0);
				sb.append(referenceSchema[i]);
				sb.append(dimToValSeparator);
				sb.append(valParts[i]);
				if (ht.hasDimVal(sb.toString())) {
					b.set(ht.getInt(sb.toString()));
				}
			} else {
				continue;
			}
		}
		
		if (rootNode == null) {
			rootNode = new FPTreeNode("Null", ml);
			rootNode.setNullNode(true);
		} 
		int nextsetBitIndex = -1;
		FPTreeNode tempNode = rootNode;
		for (int i = 1; i <= b.cardinality(); i++) {
			nextsetBitIndex = b.nextSetBit(nextsetBitIndex + 1);
			String nodeName = ht.get(nextsetBitIndex).getElement().getDimNameSeparatorDimVal();
			 
			if (tempNode.hasChild(nodeName)) {
				//update the metrics in the tree
				tempNode.getChild(nodeName).getMetrics().updateMetricList(ml);
				tempNode = tempNode.getChild(nodeName);
			} else {
				FPTreeNode chNode = new FPTreeNode(nodeName,ml);
				//System.out.print(nodeName + " ");
				tempNode.addChild(chNode);
				tempNode = chNode;
				ht.updatePeerNodes(nodeName,chNode);
			}
		}
		//System.out.println();
	}
	
	//private static Queue<FPTreeNode> q = new Queue<FPTreeNode>();
	public static void testBFS(FPTreeNode n) {
		
	}
	
	public void mineFPTree(
  			  String separatorBetwnSuccessiveDimVal, 
	          String separatorBetwnlevelAndRule,
	          String separatorBetwnRuleAndMetric,
	          String separatorBetwnSuccessiveFIS,
	          int numberOfStages,
	          double supportVal,
	          RandomAccessFile raf
			) throws Exception {
		//StringBuilder sb = new StringBuilder();
		for (int i = ht.getHeaderTableSize() -1 ; i >=0 ; i--) {
			HeaderTableRecord htr = ht.get(i);
			FPTreeNode fptn = htr.getFirstNode();
			if (fptn == null) {
				continue;
			}
			//MineFPTree mfpt = new MineFPTree(" & ",  "," ,  "," , "\n",5);
			MineFPTree mfpt = new MineFPTree(separatorBetwnSuccessiveDimVal, separatorBetwnlevelAndRule, 
											separatorBetwnRuleAndMetric, separatorBetwnSuccessiveFIS, 
											numberOfStages);
			String nodeOfInterest = fptn.getDimValName();
			
			//Use PreTrav and visit each node until given level 'p' and for all trees 
			//with more than 1 child, pre-compute the string wise enumerations.
			
			
			
			
			while (fptn != null) {
				mfpt.mineFISFromFPTree(nodeOfInterest, fptn.getLineageStr() , fptn.getMetrics());
				fptn = fptn.getNextPeer();
			}
			//here write to the output file
			if (raf == null) {
				System.out.print(mfpt.toString(supportVal));
			} else {
				Util.writeCandidatesToOutputFile(mfpt.toString(supportVal), raf);
			}
		}
		
	}
	
	
	public String printHeader (MetricList ml, 
							  String separatorBetwnMetrics, 
					          String separatorBetwnlevelAndRule,
					          String separatorBetwnRuleAndMetric
					          ) {
		StringBuilder sb = new StringBuilder();
		sb.append("Level");
		sb.append(separatorBetwnlevelAndRule);
		sb.append("Rule");
		sb.append(separatorBetwnRuleAndMetric);
		sb.append(ml.getMetricNameHeader(separatorBetwnMetrics));
		return sb.toString();
	}
	
	
	public static void main(String [] args) throws Exception {
		
		String [] inputDataArr = {    // b,a,c,e,d
									"10,t,t,t, , ,1",
									"10,t,t,t, ,t,1",
									"10,t, ,t,t, ,1",
									"10,t, ,t, , ,1",
									"10,t,t,t, , ,1",
									"10,t,t, , , ,1",
									"10,t, ,t, ,t,1",
									"10, ,t,t,t,t,1",
									"10, ,t, ,t,t,1",
									"10,t,t, , ,t,1",
									
		};

		String [] schema = {"additional_metric","b","a","c","e","d","metric"};
	
		String colSeparator = ",";
		int [] metricIndexes = {6,0};
		int metricIdxForSuppThrshldCmptn = 6;
		HashSet<String> valueIgnoreList = new HashSet<String>();
		//{"", "null", "na", "0"};
		//valueIgnoreList.add(""); valueIgnoreList.add("null"); valueIgnoreList.add("na"); valueIgnoreList.add("0");
		valueIgnoreList = FPGConfig.IGNORE_VALUES;
		HashSet<Integer> dimIndexesToIgnore = null;
		String    dimToValSeparator = "=";
		
		
		HeaderTableRecordElement htre1 = new HeaderTableRecordElement("b", "t",dimToValSeparator, 1 );		
		MetricList ml = new MetricList();
		ml.addMetricToList("metric", 8, false);
		ml.addMetricToList("additional_metric", 80, true);
		HeaderTableRecord htr1 = new HeaderTableRecord(htre1,ml);
		HeaderTable ht = new HeaderTable();
		ht.add(htr1);
		
		htre1 = new HeaderTableRecordElement("a", "t",dimToValSeparator, 2);		
		ml = new MetricList();
		ml.addMetricToList("metric", 7, false);
		ml.addMetricToList("additional_metric", 70, true);
		htr1 = new HeaderTableRecord(htre1,ml);
		ht.add(htr1);
		
		htre1 = new HeaderTableRecordElement("c", "t",dimToValSeparator, 3);		
		ml = new MetricList();
		ml.addMetricToList("metric", 5, false);
		ml.addMetricToList("additional_metric", 50, true);
		htr1 = new HeaderTableRecord(htre1,ml);
		ht.add(htr1);
		
		htre1 = new HeaderTableRecordElement("d", "t",dimToValSeparator, 4);		
		ml = new MetricList();
		ml.addMetricToList("metric", 5, false);
		ml.addMetricToList("additional_metric", 50, true);
		htr1 = new HeaderTableRecord(htre1,ml);
		ht.add(htr1);
		
		htre1 = new HeaderTableRecordElement("e", "t",dimToValSeparator, 5);		
		ml = new MetricList();
		ml.addMetricToList("metric", 3, false);
		ml.addMetricToList("additional_metric", 30, true);
		htr1 = new HeaderTableRecord(htre1,ml);
		ht.add(htr1);
		
		FPTreeBuilder fptb = new FPTreeBuilder();
		fptb.initialize(ht);
		for (String s : inputDataArr) {
			//System.out.print(s + " ");
			fptb.addPathToTree(s, colSeparator, schema,
					           metricIndexes, metricIdxForSuppThrshldCmptn, 
					           FPGConfig.IGNORE_DIMVALS,valueIgnoreList, dimIndexesToIgnore, 
					           dimToValSeparator);
		}
		
		
		System.out.println(fptb.printHeader(ml, ",", ",",  ","));
		fptb.mineFPTree(" & ",  "," ,  "," , "\n",5,2,null);
		
		
		
		//schema and data swapping many dimensions gives the same results - done
		//Check from the header table if all nodes get connected - yes they do
		//test having more than one metric - done
		//try with different metric index - done
		//System.out.println(FPTreeNode.getMaxLevel());
	}
	
	
	
}
