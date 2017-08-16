package com.github.ragnard.shen.klambda.nodes.builtins.string;

import com.github.ragnard.shen.klambda.nodes.builtins.BuiltinNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "string->n")
public abstract class StringToNumber extends BuiltinNode {

    @Specialization
    public long stringToNumber(String s) {
        if (s.length() != 1) {
            throw new RuntimeException("string->n: not a unit string: " + s);
        }
        return s.charAt(0);
    }
}
