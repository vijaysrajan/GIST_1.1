package com.fratics.precis.fis.sanitation.rules;

public class SingleRuleResult {
    public String ruleName;
    public boolean result;
    public int fieldIndex;

    public SingleRuleResult(String ruleName, boolean result, int fieldIndex) {
        this.ruleName = ruleName;
        this.result = result;
        this.fieldIndex = fieldIndex;
    }

}
