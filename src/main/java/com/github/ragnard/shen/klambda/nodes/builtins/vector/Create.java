package com.github.ragnard.shen.klambda.nodes.builtins.vector;

import com.github.ragnard.shen.klambda.nodes.builtins.BuiltinNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "absvector")
public abstract class Create extends BuiltinNode {
    @Specialization
    public Object[] create(long size) {
        return new Object[(int)size];
    }
}
