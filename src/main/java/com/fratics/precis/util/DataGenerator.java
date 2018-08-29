package com.fratics.precis.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class DataGenerator {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Enter input File name");
            System.exit(0);
        }
        BufferedReader br = null;
        String fileName = args[0];
        String INPUT_RECORD_SEPERATOR = Character.toString('\001');
        String thisLine;
        File file = new File(fileName);
        if (!(file.exists() && file.canRead())) throw new Exception("Error Reading file:: " + fileName);
        br = new BufferedReader(new FileReader(file));
        while ((thisLine = br.readLine()) != null) {
            String[] str = thisLine.split(INPUT_RECORD_SEPERATOR, -1);
            double value = Double.parseDouble(str[7]);
            if (value < 10)
                System.err.println(str[0] + INPUT_RECORD_SEPERATOR + str[1] + INPUT_RECORD_SEPERATOR + str[2] + INPUT_RECORD_SEPERATOR + str[3] + INPUT_RECORD_SEPERATOR +
                        str[4] + INPUT_RECORD_SEPERATOR + str[5] + INPUT_RECORD_SEPERATOR + str[6] + INPUT_RECORD_SEPERATOR +
                        "true" + INPUT_RECORD_SEPERATOR + "false" + INPUT_RECORD_SEPERATOR + "false" + INPUT_RECORD_SEPERATOR + "false" + INPUT_RECORD_SEPERATOR + str[7]);
            else if (value < 50)
                System.err.println(str[0] + INPUT_RECORD_SEPERATOR + str[1] + INPUT_RECORD_SEPERATOR + str[2] + INPUT_RECORD_SEPERATOR + str[3] + INPUT_RECORD_SEPERATOR +
                        str[4] + INPUT_RECORD_SEPERATOR + str[5] + INPUT_RECORD_SEPERATOR + str[6] + INPUT_RECORD_SEPERATOR +
                        "true" + INPUT_RECORD_SEPERATOR + "true" + INPUT_RECORD_SEPERATOR + "false" + INPUT_RECORD_SEPERATOR + "false" + INPUT_RECORD_SEPERATOR + str[7]);
            else if (value < 75)
                System.err.println(str[0] + INPUT_RECORD_SEPERATOR + str[1] + INPUT_RECORD_SEPERATOR + str[2] + INPUT_RECORD_SEPERATOR + str[3] + INPUT_RECORD_SEPERATOR +
                        str[4] + INPUT_RECORD_SEPERATOR + str[5] + INPUT_RECORD_SEPERATOR + str[6] + INPUT_RECORD_SEPERATOR +
                        "true" + INPUT_RECORD_SEPERATOR + "true" + INPUT_RECORD_SEPERATOR + "true" + INPUT_RECORD_SEPERATOR + "false" + INPUT_RECORD_SEPERATOR + str[7]);
            else
                System.err.println(str[0] + INPUT_RECORD_SEPERATOR + str[1] + INPUT_RECORD_SEPERATOR + str[2] + INPUT_RECORD_SEPERATOR + str[3] + INPUT_RECORD_SEPERATOR +
                        str[4] + INPUT_RECORD_SEPERATOR + str[5] + INPUT_RECORD_SEPERATOR + str[6] + INPUT_RECORD_SEPERATOR +
                        "true" + INPUT_RECORD_SEPERATOR + "true" + INPUT_RECORD_SEPERATOR + "true" + INPUT_RECORD_SEPERATOR + "true" + INPUT_RECORD_SEPERATOR + str[7]);
        }
        br.close();
    }
}
