package com.fratics.precis.config;

public class FeedSchemaElement {

    private String dimName;

    ;
    private DIM_TYPE dimType;

    ;
    private DIM_DATA_TYPE dimDataType;
    private boolean includedForPrecisCalculation;

    public String getDimName() {
        return dimName;
    }

    public void setDimName(String dimName) {
        this.dimName = dimName;
    }

    public DIM_TYPE getDimType() {
        return dimType;
    }

    public void setDimType(DIM_TYPE dimType) {
        this.dimType = dimType;
    }

    public DIM_DATA_TYPE getDimDataType() {
        return dimDataType;
    }

    public void setDimDataType(DIM_DATA_TYPE dimDataType) {
        this.dimDataType = dimDataType;
    }

    public boolean isIncludedForPrecisCalculation() {
        return includedForPrecisCalculation;
    }

    public void setIncludedForPrecisCalculation(boolean includedForPrecisCalculation) {
        this.includedForPrecisCalculation = includedForPrecisCalculation;
    }

    public enum DIM_TYPE {
        DIMENSION, METRIC
    }

    public enum DIM_DATA_TYPE {
        STRING, INT, LONG, DOUBLE
    }
}
