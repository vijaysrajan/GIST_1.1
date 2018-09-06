package com.fratics.precis.fpg.fptreebilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class HeaderTable {
	private ArrayList<HeaderTableRecord> element = null;
	private HashMap<String,HeaderTableRecord> lookup = null;
	private HashMap<String,Integer> lookupIndex = null;
	
	public void add (HeaderTableRecord htr) {
		if (element == null) {
			element = new ArrayList<HeaderTableRecord>();
			lookup  = new HashMap<String,HeaderTableRecord> ();
			lookupIndex = new HashMap<String,Integer>();
		}
		element.add(htr);
		Collections.sort(element);
		lookup.clear();
		lookupIndex.clear();
		for (int i = 0; i < element.size();  i++) {
			lookup.put(element.get(i).getElement().getDimNameSeparatorDimVal(), element.get(i));
			lookupIndex.put(element.get(i).getElement().getDimNameSeparatorDimVal(), i);
		}
		
	}
	
	public int getHeaderTableSize() {
		return element.size();
	}
	
	public HeaderTableRecord get(int i) {
		return element.get(i);
	}
	
	public int getInt(String s) {
		//System.out.println(s);
		return lookupIndex.get(s);
	}
	
	public HeaderTableRecord get(String s) {
		return lookup.get(s);
	}
	
	public void updatePeerNodes(String nodeName, FPTreeNode fptn) {
		HeaderTableRecord htr = lookup.get(nodeName);
		if (htr.getFirstNode() == null) {
			htr.setFirstNode(fptn);
			htr.setLastNode(fptn);
		} else {
			htr.getLastNode().setNextPeer(fptn);
			htr.setLastNode(fptn);
		}
	}
	

	public static void main(String [] args) {
		//test code
		StringBuilder separator = new StringBuilder();
		separator.append('=');
		HeaderTableRecordElement htre1 = new HeaderTableRecordElement("Vijay", "Kavya",separator.toString(), 25 );
		HeaderTableRecordElement htre2 = new HeaderTableRecordElement("Vijay", "Keertana",separator.toString(),25);
		HeaderTableRecordElement htre3 = new HeaderTableRecordElement("Vijay", "Shuba",separator.toString(),25);
		MetricList ml1 = new MetricList();
		MetricList ml2 = new MetricList();
		MetricList ml3 = new MetricList();
		
		ml1.addMetricToList("imp", 45, false);
		ml1.addMetricToList("clk", 16, false);
		ml1.addMetricToList("conv", 9, true);
			
		ml2.addMetricToList("imp", 55, false);
		ml2.addMetricToList("clk", 15, false);
		ml2.addMetricToList("conv", 11, true);
		
		ml3.addMetricToList("imp", 65, false);
		ml3.addMetricToList("clk", 14, false);
		ml3.addMetricToList("conv", 10, true);
		
		HeaderTableRecord htr1 = new HeaderTableRecord(htre1,ml1);
		HeaderTableRecord htr2 = new HeaderTableRecord(htre2,ml2);
		HeaderTableRecord htr3 = new HeaderTableRecord(htre3,ml3);
		HeaderTable ht = new HeaderTable();
		ht.add(htr1);
		ht.add(htr2);
		ht.add(htr3);
		
		for (HeaderTableRecord htr : ht.element) {
			System.out.println(htr.getElement().getDimName() + "=" + 
							   htr.getElement().getDimVal() + "  " + 
							   htr.getMetrics().toString());
		}
		System.out.println();
		
		for (int i = 0; i < ht.element.size(); i++) {
			System.out.println(ht.get(i).getElement().getDimNameSeparatorDimVal() + "  " + 
					           ht.get(i).getMetrics().toString());
		}
		
		System.out.println(ht.getInt("Vijay=Kavya"));
		System.out.println(ht.get(ht.getInt("Vijay=Kavya")).getElement().getDimNameSeparatorDimVal());
		
	}
	
	
}
