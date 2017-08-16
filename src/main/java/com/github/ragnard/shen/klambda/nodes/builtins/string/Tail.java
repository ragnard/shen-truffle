package com.github.ragnard.shen.klambda.nodes.builtins.string;

import com.github.ragnard.shen.klambda.nodes.builtins.BuiltinNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "tlstr")
public abstract class Tail extends BuiltinNode {
    @Specialization
    public String tail(String s) {
        if (s.length() < 1) {
            throw new RuntimeException("tlstr: too short: " + s);
        }
        return s.substring(1);
    }
}
