package com.fratics.precis.fis.sanitation.rules;

import com.fratics.precis.fis.base.PrecisBase;
import com.fratics.precis.fis.base.ValueObject;

public abstract class SanitationRuleBase extends PrecisBase {
    protected String ruleName = null;

    public String getRuleName() {
        return this.ruleName;
    }

    public void setRuleName(String str) {
        this.ruleName = str;
    }

    public abstract void applyRule(ValueObject vo) throws Exception;
}
