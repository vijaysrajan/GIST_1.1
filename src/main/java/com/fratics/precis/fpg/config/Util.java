package com.fratics.precis.fpg.config;

import java.io.RandomAccessFile;
//import java.io.File;


public class Util {

    public static boolean isIgnoredWord(String s) {
        return FPGConfig.IGN_WORDS.contains(s);
    }

    public static String generateRandomId() {
        return Integer.toString((int) (Math.random() * 100000));
    }

    private static RandomAccessFile openFile () throws Exception {
		StringBuilder fullpathFileName = new StringBuilder();
		fullpathFileName.append(FPGConfig.OUTPUT_DIR);
		fullpathFileName.append("/");
		fullpathFileName.append(FPGConfig.GIST_OUPUT_CANDIDATE_FILE);
        RandomAccessFile pw = new RandomAccessFile(fullpathFileName.toString(), "rw");
        return pw;
    }
    
    
    private static void writeCandidatesToOutputFile(String fisLine, RandomAccessFile pw) throws Exception {    	
        try {
    				//File f = new File(fullpathFileName.toString());
    				//long fileLength = f.length();
        			//pw.seek(fileLength);
				//if (fileLength > 0) pw.writeChars("\n");
        			pw.writeBytes(fisLine);
        } catch (Exception e) {
        		throw e;
        }
    }
    
    private static void closeFile (RandomAccessFile pw) throws Exception {
    		pw.close();
    }
    
}
