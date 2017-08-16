package com.github.ragnard.shen.klambda.nodes.builtins.string;

import com.github.ragnard.shen.klambda.nodes.builtins.BuiltinNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "cn")
public abstract class Concat extends BuiltinNode {
    @Specialization
    public String concat(String s1, String s2) {
        return s1 + s2;
    }
}
