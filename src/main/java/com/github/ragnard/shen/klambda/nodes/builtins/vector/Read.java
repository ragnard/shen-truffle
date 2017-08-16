package com.github.ragnard.shen.klambda.nodes.builtins.vector;

import com.github.ragnard.shen.klambda.nodes.builtins.BuiltinNode;
import com.github.ragnard.shen.klambda.runtime.Cons;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "<-address")
public abstract class Read extends BuiltinNode {
    @Specialization
    public Object read(Object[] vector, long pos) {
        Object v = vector[(int)pos];
        return v != null ? v : Cons.EMPTY; //Symbol.intern("fail!");
    }
}
