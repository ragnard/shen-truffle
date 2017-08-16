package com.github.ragnard.shen.klambda.nodes.builtins.arithmetic;

import com.github.ragnard.shen.klambda.nodes.builtins.BuiltinNode;
import com.github.ragnard.shen.klambda.runtime.Symbol;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = ">")
public abstract class GreaterThan extends BuiltinNode {
    @Specialization
    public Symbol greaterThan(long x, long y) {
        return Symbol.fromBoolean(x > y);
    }

    @Specialization
    public Symbol greaterThan(double x, double y) {
        return Symbol.fromBoolean(x > y);
    }

}
