package com.github.ragnard.shen.klambda.nodes.builtins.arithmetic;

import com.github.ragnard.shen.klambda.nodes.builtins.BuiltinNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "/")
public abstract class Divide extends BuiltinNode {
    //@Specialization
    //public long divide(long x, long y) {
    //    return x / y;
    //}

    @Specialization
    public double divide(double x, double y) {
        return x / y;
    }
}
