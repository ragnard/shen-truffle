package com.github.ragnard.shen.klambda.nodes.builtins.symbol;

import com.github.ragnard.shen.klambda.nodes.builtins.BuiltinNode;
import com.github.ragnard.shen.klambda.runtime.Symbol;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "intern")
public abstract class Intern extends BuiltinNode {
    @Specialization
    @CompilerDirectives.TruffleBoundary
    public Symbol intern(String name) {
        return Symbol.intern(name);
    }
}
