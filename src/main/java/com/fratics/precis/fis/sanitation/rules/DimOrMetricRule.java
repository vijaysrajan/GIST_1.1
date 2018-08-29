package com.fratics.precis.fis.sanitation.rules;

import com.fratics.precis.fis.base.FieldObject;
import com.fratics.precis.fis.base.MutableDouble;
import com.fratics.precis.fis.base.ValueObject;

import java.util.Iterator;
import java.util.Map;

public class DimOrMetricRule extends SanitationRuleBase {
    public void applyRule(ValueObject vo) throws Exception {
        this.setRuleName("DimOrMetricRule");
        long numOfLines = vo.inputObject.getNoOfLines();
        FieldObject[] fo = vo.inputObject.getFieldObjects();
        for (int i = 0; i < fo.length; i++) {
            Map<String, MutableDouble> map = fo[i].getMap();
            Iterator<String> it = map.keySet().iterator();
            boolean flag = true;
            while (it.hasNext()) {
                String x = it.next();
                try {
                    Double.parseDouble(x);
                } catch (NumberFormatException ne) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                vo.resultObject.loadResult(new SingleRuleResult("MetricRule", true, i));
            } else {
                vo.resultObject.loadResult(new SingleRuleResult("MetricRule", false, i));
            }
            // dim part
            if (fo[i].getNumberOfUniques() < numOfLines) {

                vo.resultObject.loadResult(new SingleRuleResult("DimRule", true, i));
            } else {
                vo.resultObject.loadResult(new SingleRuleResult("DimRule", false, i));
            }
            // merge rules
            if (fo[i].getNumberOfUniques() == numOfLines) {
                vo.resultObject.loadResult(new SingleRuleResult("UniqueValueRule", true, i));
            } else {
                vo.resultObject.loadResult(new SingleRuleResult("UniqueValueRule", false, i));
            }

            if (fo[i].getNumberOfUniques() == 1) {
                vo.resultObject.loadResult(new SingleRuleResult("SameValueRule", true, i));
            } else {
                vo.resultObject.loadResult(new SingleRuleResult("SameValueRule", false, i));
            }
            /*
			 * if (fo[i].getNumberOfUniques() == 2 ) {
			 * vo.resultObject.loadResult(new
			 * SingleRuleResult("BinaryValueRule", true, i)); } else {
			 * vo.resultObject.loadResult(new
			 * SingleRuleResult("BinaryValueRule", false, i)); }
			 */
            if (fo[i].getNumberOfUniques() <= 5) {
                vo.resultObject.loadResult(new SingleRuleResult("ClassifierValueRule", true, i));
            } else {
                vo.resultObject.loadResult(new SingleRuleResult("ClassifierValueRule", false, i));
            }
        }
    }
}
