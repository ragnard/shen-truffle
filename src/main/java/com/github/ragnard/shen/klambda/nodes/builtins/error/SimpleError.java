package com.github.ragnard.shen.klambda.nodes.builtins.error;

import com.github.ragnard.shen.klambda.nodes.TrapException;
import com.github.ragnard.shen.klambda.nodes.builtins.BuiltinNode;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

import java.util.ArrayList;

@NodeInfo(shortName = "simple-error")
public abstract class SimpleError extends BuiltinNode {

    @Specialization
    public Object simpleError(String message) {
        throw new TrapException(message);
    }
}
