package com.fratics.precis.fis.sanitation;

import com.fratics.precis.fis.base.PrecisProcessor;
import com.fratics.precis.fis.base.ValueObject;
import com.fratics.precis.fis.sanitation.rules.DimOrMetricRule;
import com.fratics.precis.fis.sanitation.rules.SanitationRuleBase;

public class SanitationRuleProcessor extends PrecisProcessor {
    SanitationRuleBase[] srb = {new DimOrMetricRule()}; // new
    // SameValueRule(),
    // new
    // UniqueValueRule()
    // };

    public boolean process(ValueObject vo) throws Exception {
        vo.resultObject.init(vo.inputObject.getFieldObjects().length);
        for (int i = 0; i < srb.length; i++) {
            srb[i].applyRule(vo);
        }
        return true;
    }
}