package com.fratics.precis.fis.base;

import java.util.TreeSet;

/*
 * Schema Element of a single field in a Record of the Input Feed.
 * 
 */

public class Schema {

    // List of all the SchemaElements for all the Records in the Input Feed.
    private TreeSet<SchemaElement> list = new TreeSet<SchemaElement>();

    ;

    // No of Fields in the input feed.
    public int getNoOfFields() {
        return list.size();
    }

    ;

    // Add a Schema Element to the Record List.
    public void addSchemaElement(String fieldName, int fieldIndex, FieldType fieldType) {
        addSchemaElement(new SchemaElement(fieldName, fieldIndex, fieldType));
    }

    // Add a Schema Element to the Record List.
    public void addSchemaElement(SchemaElement e) {
        list.add(e);
    }

    public TreeSet<SchemaElement> getSchemaList() {
        return list;
    }

    // Type of the Field as Enum
    public static enum FieldType {
        DIMENSION, METRIC, INVALID
    }

    // Each Schema Element is defined by the fieldName, fieldType & fieldIndex
    // (in the input feed).
    public class SchemaElement implements Comparable<SchemaElement> {
        public String fieldName;
        public int fieldIndex;
        public FieldType fieldType;

        public SchemaElement(String fieldName, int fieldIndex, FieldType fieldType) {
            this.fieldName = fieldName;
            this.fieldIndex = fieldIndex;
            this.fieldType = fieldType;
        }

        public int compareTo(SchemaElement o) {
            return this.fieldName.compareTo(o.fieldName);
        }
    }
}
