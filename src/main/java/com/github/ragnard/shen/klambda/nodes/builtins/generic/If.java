package com.github.ragnard.shen.klambda.nodes.builtins.generic;

import com.github.ragnard.shen.klambda.nodes.builtins.BuiltinNode;
import com.github.ragnard.shen.klambda.runtime.Symbol;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "if")
public abstract class If extends BuiltinNode {

    @Specialization
    public Object if_(Symbol condition, Object thenValue, Object elseValue) {
        if(condition.asBoolean()) {
            return thenValue;
        } else {
            return elseValue;
        }
    }
}
