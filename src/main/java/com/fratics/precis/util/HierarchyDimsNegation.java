package com.fratics.precis.util;

import com.fratics.precis.fis.base.BaseCandidateElement;
import com.fratics.precis.fis.base.PrecisBase;
import com.fratics.precis.fis.feed.dimval.DimValIndex;
import com.fratics.precis.fis.util.PrecisConfigProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class HierarchyDimsNegation extends PrecisBase {
    private static HierarchyDimsNegation hierarchyDimsNegation;
    private ArrayList<HashSet<String>> hierarchyDimSets = new ArrayList<HashSet<String>>();

    private HierarchyDimsNegation() {
    }

    public static HierarchyDimsNegation getInstance() {
        if (hierarchyDimsNegation == null) {
            hierarchyDimsNegation = new HierarchyDimsNegation();
        }
        return hierarchyDimsNegation;
    }

    public static void main(String[] args) throws Exception {
        PrecisConfigProperties.init();
        HierarchyDimsNegation hierarchyDimsNegation = HierarchyDimsNegation.getInstance();
        hierarchyDimsNegation.initialize();
    }

    private void loadHierarcyDims() {
        String[] hieraryGroups = PrecisConfigProperties.HIERARCHY_DIM_GROUPS.split(":");
        for (int i = 0; i < hieraryGroups.length; i++)
            hierarchyDimSets.add(new HashSet<String>(Arrays.asList(hieraryGroups[i].split(","))));
    }

    public boolean initialize() {
        if (PrecisConfigProperties.HIERARCHY_DIMS_ENABLED) loadHierarcyDims();
        return true;
    }

    public boolean checkIfBelongToSameHierarchyGroup(BaseCandidateElement a, BaseCandidateElement b) {
        int dimIndex1 = a.getBitSet().nextSetBit(0);
        int dimIndex2 = b.getBitSet().nextSetBit(0);
        String dimName1 = DimValIndex.revDimMap.get(dimIndex1);
        String dimName2 = DimValIndex.revDimMap.get(dimIndex2);
        for (int i = 0; i < hierarchyDimSets.size(); i++) {
            boolean b1 = hierarchyDimSets.get(i).contains(dimName1);
            boolean b2 = hierarchyDimSets.get(i).contains(dimName2);
            if (b1 && b2) return true;
        }
        return false;
    }
}
