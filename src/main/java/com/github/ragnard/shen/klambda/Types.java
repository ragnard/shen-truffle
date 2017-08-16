package com.github.ragnard.shen.klambda;

import com.github.ragnard.shen.klambda.runtime.Function;
import com.github.ragnard.shen.klambda.runtime.Symbol;
import com.oracle.truffle.api.dsl.ImplicitCast;
import com.oracle.truffle.api.dsl.TypeSystem;

@TypeSystem({String.class, long.class, double.class, Function.class})
public class Types {

    @ImplicitCast
    public static double promoteToDouble(long value) {
        return value;
    }

}
