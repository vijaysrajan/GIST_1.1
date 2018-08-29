package com.fratics.precis.fis.util;

import com.fratics.precis.fis.base.BaseCandidateElement;
import com.fratics.precis.fis.base.ValueObject;
import com.fratics.precis.fis.feed.dimval.DimValIndex;

import java.io.RandomAccessFile;
import java.io.File;
import java.util.ArrayList;

public class Util {

    public static boolean isIgnoredWord(String s) {
        return PrecisConfigProperties.IGN_WORDS.contains(s);
    }

    public static String generateRandomId() {
        return Integer.toString((int) (Math.random() * 100000));
    }

    private static String convertToDims(int stage, BaseCandidateElement bce, String metricName) {
        StringBuilder ret = new StringBuilder();
        ret.append(stage);
        ret.append(PrecisConfigProperties.OUTPUT_RECORD_SEPERATOR_STAGENUMBER);
        // String ret = sb.toString();
        int startIndex = 0;
        int val = 0;
        int index = stage;
        // logger.info(bce);
        BitSet b = bce.getBitSet();
        index = stage;
        val = 0;
        // ret = "";
        startIndex = DimValIndex.dimMap.size();
        while (index > 0) {
            val = b.nextSetBit(startIndex);
            ret.append(DimValIndex.revDimValMap.get(val));
            startIndex = val + 1;
            index--;
            if (index > 0)
                ret.append(PrecisConfigProperties.OUTPUT_RECORD_SEPERATOR_DIMENSION);
        }
        ret.append(PrecisConfigProperties.OUTPUT_RECORD_SEPERATOR_METRIC);
        ret.append(metricName);
        ret.append(PrecisConfigProperties.OUTPUT_DIMVAL_SEPERATOR);
        ret.append(bce.getMetric());
        ret.append("\n");
        return ret.toString();

    }

    private static void writeCandidates(int stage, RandomAccessFile pw, ValueObject o) throws Exception {
        String ret = "";
        if (stage == 1) {
            for (BaseCandidateElement bce : o.inputObject.firstStageCandidates.values()) {
                ret = convertToDims(stage, bce, o.inputObject.getMetricName());
                pw.writeBytes(ret);
            }

        } else {
            for (ArrayList<BaseCandidateElement> al : o.inputObject.currCandidatePart.values()) {
                for (BaseCandidateElement bce : al) {
                    ret = convertToDims(stage, bce, o.inputObject.getMetricName());
                    pw.writeBytes(ret);
                }
            }
        }
    }
    
    
    public static void dump(int currStage, ValueObject o) throws Exception {
        if (PrecisConfigProperties.GIST_GENERATE_SINGLE_OUTPUT_FILE == true) {
            dumpToOneFile(currStage, o);
        } else {
           dumpByStage(currStage, o);
        }
    }
    	
    
    //milliSec1 = new Date().getTime();
	//fullpathFileName.append(milliSec1);
    
    public static void dumpToOneFile(int currStage, ValueObject o) throws Exception {
        try {
				StringBuilder fullpathFileName = new StringBuilder();
				fullpathFileName.append(PrecisConfigProperties.OUTPUT_DIR);
				fullpathFileName.append("/");
				fullpathFileName.append(PrecisConfigProperties.GIST_OUPUT_CANDIDATE_FILE);
				
				File f = new File(fullpathFileName.toString());
				long fileLength = f.length();
	            RandomAccessFile pw = new RandomAccessFile(fullpathFileName.toString(), "rw");
				pw.seek(fileLength);
				//if (fileLength > 0) pw.writeChars("\n");
				writeCandidates(currStage, pw, o);
				pw.close();

        } catch (Exception e) {
        throw e;
}
    }
    
    

    public static void dumpByStage(int currStage, ValueObject o) throws Exception {
        try {
            String outputDir = PrecisConfigProperties.OUTPUT_DIR + "/";
            String fileName = PrecisConfigProperties.OUPUT_CANDIDATE_FILE_PATTERN.replace("${stage_number}",
                    "" + currStage);
            RandomAccessFile pw = new RandomAccessFile(outputDir + fileName, "rw");

            // FileWriter pw = new FileWriter(new File(outputDir + fileName));
            writeCandidates(currStage, pw, o);
            // pw.flush();
            pw.close();

            if (PrecisConfigProperties.GENERATE_RAW_CANDIDATE_FILE) {
                fileName = PrecisConfigProperties.OUPUT_RAW_CANDIDATE_FILE_PATTERN.replace("${stage_number}",
                        "" + currStage);
                // pw = new FileWriter(new File(outputDir + fileName));
                pw = new RandomAccessFile(outputDir + fileName, "rw");

                if (currStage == 1) {
                    String x = o.inputObject.firstStageCandidates.keySet().toString();
                    pw.writeBytes(x);
                } else {
                    String x = o.inputObject.currCandidateSet.toString();
                    pw.writeBytes(x);
                }
                // pw.flush();
                pw.close();
            }

        } catch (Exception e) {
            throw e;
        }
    }
}
