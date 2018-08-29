package com.fratics.precis.fis.base;

import com.fratics.precis.fis.base.Schema.SchemaElement;

import java.util.HashMap;
import java.util.Map;

/*
 * A Single Column & its characteristics in the input feed is captured
 * in this class.
 * 
 * This class captures the schema element of the column.
 * Further it generates the unique values of the columns and its count (or) sum(metrics) 
 * depending on the type of precis.
 * 
 * The class forms the basis to apply various sanitation rules in the input
 * feed.
 * 
 */

public class FieldObject {

    // Schema Element.
    private SchemaElement schElement;

    // Map of Values and their count (or) sum of metrics.
    private Map<String, MutableDouble> map = new HashMap<String, MutableDouble>();

    public SchemaElement getSchemaElement() {
        return schElement;
    }

    public void setSchemaElement(SchemaElement schElement) {
        this.schElement = schElement;
    }

    public Map<String, MutableDouble> getMap() {
        return map;
    }

    public int getNumberOfUniques() {
        return map.keySet().size();
    }

	/*
     * In count Precis, For each record in the input field, we check for the
	 * given value is present. If present, increment the value's count by 1,
	 * else add the value and sent its count 1.
	 */

    public void addFieldValue(String key) {
        MutableDouble value = map.get(key);
        if (value == null) {
            value = new MutableDouble();
            map.put(key, value);
        } else {
            value.inc();
        }
    }

	/*
	 * In meric precis, For each record in the input field, we check for the
	 * given value is present. If present, increment the value's metric from
	 * this record, else add the value and set its metric from this record.
	 */

    public void addFieldValueBy(String key, double metric) {
        MutableDouble value = map.get(key);
        if (value == null) {
            value = new MutableDouble(metric);
            map.put(key, value);
        } else {
            value.incBy(metric);
        }
    }

    public String toString() {
        String str = "fieldName :: " + this.schElement.fieldName + ", fieldIndex :: " + this.schElement.fieldIndex
                + ", fieldType :: " + this.schElement.fieldType + ", Values :: " + this.map.toString() + "\n";
        return str;
    }

}
