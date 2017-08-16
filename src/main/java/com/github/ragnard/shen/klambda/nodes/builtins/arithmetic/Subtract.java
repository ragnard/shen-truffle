package com.github.ragnard.shen.klambda.nodes.builtins.arithmetic;

import com.github.ragnard.shen.klambda.nodes.builtins.BuiltinNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "-")
public abstract class Subtract extends BuiltinNode {

    @Specialization
    public long subtract(long x, long y) {
        return Math.subtractExact(x, y);
    }

    @Specialization
    public double subtract(double x, double y) {
        return x - y;
    }
}
