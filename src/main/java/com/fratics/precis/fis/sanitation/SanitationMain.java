package com.fratics.precis.fis.sanitation;

import com.fratics.precis.fis.base.PrecisProcessor;
import com.fratics.precis.fis.base.ValueObject;
import com.fratics.precis.fis.schema.PrecisSchemaProcessor;
import com.fratics.precis.fis.util.PrecisConfigProperties;
import com.fratics.precis.reader.PrecisFileStream;
import com.fratics.precis.reader.PrecisInputCharacteristicsProcessor;
import com.fratics.precis.util.ConfigObject;

import java.io.File;

public class SanitationMain extends PrecisProcessor {

    private PrecisProcessor[] ps = null;

    public SanitationMain() {
        ps = new PrecisProcessor[3];
        ps[0] = new PrecisSchemaProcessor(new PrecisFileStream(PrecisConfigProperties.INPUT_SCHEMA_FILE,
                PrecisConfigProperties.SCHEMA_RECORD_SEPERATOR));
        ps[1] = new PrecisInputCharacteristicsProcessor(new PrecisFileStream(PrecisConfigProperties.INPUT_DATA_FILE));
        ps[2] = new SanitationRuleProcessor();
    }

    public static void main(String[] args) {
        try {

            // Check for configuration file in command line args.
            if (args.length > 0) {
                if (!new File(args[0]).exists())
                    throw new Exception("Configuration File " + args[0] + " doesn't exist");
                ConfigObject.setConfigFile(args[0]);
            }

            // Initialize the precis configuration.
            PrecisConfigProperties.init();

            // Initialize the Value Objects.
            ValueObject vo = new ValueObject();

            // Create & Link the input and output objects to Value Objects.
            vo.inputObject = new SanitationInputObject();
            vo.resultObject = new SanitationOutputObject();

            // Spawn the main Driver & run.
            SanitationMain sm = new SanitationMain();
            sm.initialize();
            sm.process(vo);
            sm.unInitialize();
            System.err.println(vo);
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
