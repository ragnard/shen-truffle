package com.github.ragnard.shen.klambda.nodes.builtins.string;

import com.github.ragnard.shen.klambda.nodes.builtins.BuiltinNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "n->string")
public abstract class NumberToString extends BuiltinNode {

    @Specialization
    public String numberToString(long n) {
        return Character.toString((char) n);
    }
}
