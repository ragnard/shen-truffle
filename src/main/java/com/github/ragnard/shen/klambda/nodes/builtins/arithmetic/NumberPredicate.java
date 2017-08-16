package com.github.ragnard.shen.klambda.nodes.builtins.arithmetic;

import com.github.ragnard.shen.klambda.TypesGen;
import com.github.ragnard.shen.klambda.nodes.builtins.BuiltinNode;
import com.github.ragnard.shen.klambda.runtime.Symbol;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "number?")
public abstract class NumberPredicate extends BuiltinNode {

    @Specialization
    public Symbol isNumber(Object x) {
        return Symbol.fromBoolean(TypesGen.isDouble(x) || TypesGen.isLong(x));
    }
}
