package com.github.ragnard.shen.klambda.nodes.builtins.vector;

import com.github.ragnard.shen.klambda.nodes.builtins.BuiltinNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "address->")
public abstract class Write extends BuiltinNode {
    @Specialization
    public Object write(Object[] vector, long pos, Object value) {
        vector[(int)pos] = value;
        return vector;
    }
}
