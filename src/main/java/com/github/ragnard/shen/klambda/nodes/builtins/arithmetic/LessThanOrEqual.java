package com.github.ragnard.shen.klambda.nodes.builtins.arithmetic;

import com.github.ragnard.shen.klambda.nodes.builtins.BuiltinNode;
import com.github.ragnard.shen.klambda.runtime.Symbol;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "<=")
public abstract class LessThanOrEqual extends BuiltinNode {
    @Specialization
    public Symbol lessThanOrEqual(long x, long y) {
        return Symbol.fromBoolean(x <= y);
    }

    @Specialization
    public Symbol lessThanOrEqual(double x, double y) {
        return Symbol.fromBoolean(x <= y);
    }

}
