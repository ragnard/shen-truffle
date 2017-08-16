package com.github.ragnard.shen.klambda.nodes.builtins.arithmetic;

import com.github.ragnard.shen.klambda.nodes.builtins.BuiltinNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "*")
public abstract class Multiply extends BuiltinNode {
    @Specialization
    public long multiply(long x, long y) {
        return Math.multiplyExact(x, y);
    }

    @Specialization
    public double multiply(double x, double y) {
        return x * y;
    }
}
