package com.fratics.precis.fis.feed.dimval;

import com.fratics.precis.fis.base.PrecisProcessor;
import com.fratics.precis.fis.util.PrecisConfigProperties;

import java.io.File;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

/*
 * Indexes are created for all the dims and values that cross the threshold in stage 1.
 * 
 * We have 4 maps totally, they are
 * 	1) dim map
 * 	2) dim_val map
 * 	3) reverse dim map
 * 	4) reverse dim_val map
 * 
 * The format we used for dim_val map element is "dim^Bvalue" -> Index.
 * 
 * Creating MetricDimValIndex completes the first stage of Precis.
 * 
 */

public abstract class DimValIndexBase extends PrecisProcessor {

    // Definition of the Dimension , Dimension Value & reverse Index Maps.
    public static Map<String, Integer> dimMap = new TreeMap<String, Integer>();
    public static Map<Integer, String> revDimMap = new TreeMap<Integer, String>();
    public static Map<String, Integer> dimValMap = new TreeMap<String, Integer>();
    public static Map<Integer, String> revDimValMap = new TreeMap<Integer, String>();

    // Length of the Precis Bit Set
    public static int getPrecisBitSetLength() {
        return dimMap.size() + dimValMap.size();
    }

    // To String method to dump all Maps.
    public static String dumpIndexes() {
        return dimMap + "\n" + dimValMap + "\n" + revDimMap + "\n" + revDimValMap + "\n";
    }

    // Dumps the Maps to a feed file for debugging.
    public void dump() throws Exception {
        try {
            PrintWriter pw = new PrintWriter(new File(PrecisConfigProperties.DIM_FEED));
            pw.append(dimMap.toString());
            pw.flush();
            pw.close();
            pw = new PrintWriter(new File(PrecisConfigProperties.DIMVAL_FEED));
            pw.append(dimValMap.toString());
            pw.flush();
            pw.close();
        } catch (Exception e) {
            throw e;
        }
    }

}
