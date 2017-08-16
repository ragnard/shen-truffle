package com.github.ragnard.shen.klambda.nodes.builtins.error;

import com.github.ragnard.shen.klambda.nodes.builtins.BuiltinNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "error-to-string")
public abstract class ErrorToString extends BuiltinNode {

    @Specialization
    public String errorToString(RuntimeException error) {
        return error.getMessage();
    }
}
