package com.github.ragnard.shen.klambda.nodes.builtins.string;

import com.github.ragnard.shen.klambda.nodes.builtins.BuiltinNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "pos")
public abstract class Pos extends BuiltinNode {
    @Specialization
    public String pos(String string, long pos) {
        return string.substring((int)pos, (int)pos+1);
    }
}
