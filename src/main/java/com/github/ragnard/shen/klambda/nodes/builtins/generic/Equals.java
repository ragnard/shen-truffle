package com.github.ragnard.shen.klambda.nodes.builtins.generic;

import com.github.ragnard.shen.klambda.nodes.builtins.BuiltinNode;
import com.github.ragnard.shen.klambda.runtime.Cons;
import com.github.ragnard.shen.klambda.runtime.Symbol;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "=")
public abstract class Equals extends BuiltinNode {

    @Specialization
    public Symbol equals(long x, long y) {
        return Symbol.fromBoolean(x == y);
    }

    @Specialization
    public Symbol equals(double x, double y) {
        return Symbol.fromBoolean(x == y);
    }

    @Specialization
    public Symbol equals(String x, String y) {
        return Symbol.fromBoolean(x.equals(y));
    }

    @Specialization
    public Symbol equals(Symbol x, Symbol y) {
        return Symbol.fromBoolean(x.getName().equals(y.getName()));
    }

    @Specialization
    public Symbol equals(Cons x, Cons y) {
        return listEquality(x, y);
    }

    @Specialization
    public Symbol equals(Object[] x, Object[] y) {
        return absvectorEquality(x, y);
    }


    @Specialization
    @CompilerDirectives.TruffleBoundary
    public static Symbol equals(Object x, Object y) {
        if (x == null && y == null) {
            return Symbol.TRUE;
        } else if (x instanceof Long) {
            if (y instanceof Long) {
                return Symbol.fromBoolean(x.equals(y));
            } else if (y instanceof Double) {
                return Symbol.fromBoolean(y.equals(((Long)x).doubleValue()));
            } else {
                return Symbol.FALSE;
            }
        } else if (y instanceof Long) {
            if (x instanceof Long) {
                return Symbol.fromBoolean(y.equals(x));
            } else if (x instanceof Double) {
                return Symbol.fromBoolean(x.equals(((Long)y).doubleValue()));
            } else {
                return Symbol.FALSE;
            }
        } else if (x instanceof Object[] && y instanceof Object[]) {
            return absvectorEquality((Object[])x, (Object[])y);
        } else if (x instanceof Cons && y instanceof Cons) {
            return listEquality((Cons)x, (Cons)y);
        } else {
            return Symbol.fromBoolean(x != null && x.equals(y));
        }
    }

    public static Symbol absvectorEquality(Object[] x, Object[] y) {
        if (x.length != y.length) {
            return Symbol.FALSE;
        }
        for(int i = 0; i < x.length; i++) {
            if (!equals(x[i], y[i]).asBoolean()) {
                return Symbol.FALSE;
            }
        }
        return Symbol.TRUE;
    }

    public static Symbol listEquality(Cons x, Cons y) {
        while(true) {
            if (!equals(x.hd, y.hd).asBoolean()) {
                return Symbol.FALSE;
            }
            if (x.tl instanceof Cons && y.tl instanceof Cons) {
                x = (Cons)x.tl;
                y = (Cons)y.tl;
            } else {
                return equals(x.tl, y.tl);
            }
        }
    }

}
