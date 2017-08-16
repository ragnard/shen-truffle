package com.github.ragnard.shen.klambda.nodes;

import com.github.ragnard.shen.klambda.runtime.Function;
import com.oracle.truffle.api.frame.VirtualFrame;

public class LambdaNode extends ExpressionNode {

    private final Function function;

    public LambdaNode(Function function) {
        this.function = function;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return this.function.setLexicalClosure(frame.materialize());
    }

}
