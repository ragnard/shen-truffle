package com.github.ragnard.shen.klambda.nodes.builtins.time;

import com.github.ragnard.shen.klambda.nodes.builtins.BuiltinNode;
import com.github.ragnard.shen.klambda.runtime.Symbol;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "get-time")
public abstract class GetTime extends BuiltinNode {
    @Specialization
    public double getTime(Symbol type) {
        switch(type.getName()) {
            case "run":
            case "real":
            case "unix":
                return System.currentTimeMillis() / 1000.0;
        }
        throw new RuntimeException("get-time: illegal argument: " + type);
    }
}
