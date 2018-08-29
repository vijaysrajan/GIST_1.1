package com.fratics.precis.fis.threshold;

import com.fratics.precis.fis.base.PrecisProcessor;
import com.fratics.precis.fis.base.ValueObject;
import com.fratics.precis.reader.PrecisFileStream;
import com.fratics.precis.reader.PrecisInputCharacteristicsProcessor;

import java.io.File;

public class ThresholdMain extends PrecisProcessor {

    private PrecisProcessor[] ps = null;

    public ThresholdMain(String dataFileName) {
        ps = new PrecisProcessor[2];
        ps[0] = new PrecisInputCharacteristicsProcessor(new PrecisFileStream(dataFileName));
        ps[1] = new ThresholdProcessor();
    }

    public static void main(String[] args) {
        try {

            if (args.length <= 0) {
                throw new Exception("Data File Needs to be Passed as Arguement");
            }

            if (args.length > 0) {
                if (!new File(args[0]).exists())
                    throw new Exception("Data File " + args[0] + " doesn't exist");
            }

            // Initialize the Value Objects.
            ValueObject vo = new ValueObject();

            // Create & Link the input and output objects to Value Objects.
            vo.inputObject = new ThresholdInputObject();
            vo.resultObject = new ThresholdOutputObject();

            // Spawn the main Driver & run.
            ThresholdMain sm = new ThresholdMain(args[0]);
            sm.initialize();
            sm.process(vo);
            sm.unInitialize();
            // System.err.println(vo);
            System.err.println();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean initialize() throws Exception {
        for (int i = 0; i < ps.length; i++)
            ps[i].initialize();
        return true;
    }

    public boolean unInitialize() throws Exception {
        for (int i = ps.length - 1; i >= 0; i--)
            ps[i].unInitialize();
        return true;

    }

    public boolean process(ValueObject o) throws Exception {
        // Initialize & run the various Processors in sequence on the Value
        // Object
        for (int i = 0; i < ps.length; i++)
            ps[i].process(o);
        return true;
    }
}
