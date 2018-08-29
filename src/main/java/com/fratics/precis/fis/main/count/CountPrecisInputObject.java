package com.fratics.precis.fis.main.count;

import com.fratics.precis.exception.PrecisException;
import com.fratics.precis.fis.base.InputObject;

import java.util.Arrays;

import static com.fratics.precis.fis.util.Util.isIgnoredWord;

public class CountPrecisInputObject extends InputObject {
    // protected static final String DEF_VALUE = "";
    private static final long serialVersionUID = 6369672872079922497L;

    protected boolean isInitialized() {
        return (noOfFields > 0);
    }

    public void loadInputCharacteristics(Object o) throws Exception {
        int index = 0;
        String[] str = (String[]) o;
        if (!this.isInitialized())
            throw new PrecisException("Schema Not Loaded");
        try {
            for (int i = 0; i < fieldObjects.length; i++) {
                index = fieldObjects[i].getSchemaElement().fieldIndex;
                if (str[index] == null) {
                    // fieldObjects[i].addFieldValue(DEF_VALUE);
                    // Ignore Null fields
                    continue;
                } else {
                    if (!isIgnoredWord(str[index]))
                        fieldObjects[i].addFieldValue(str[index].trim());
                }
            }
        } catch (Exception e) {
            System.err.println("Error Rec ::" + Arrays.toString(str));
            e.printStackTrace();
        }
    }

}
