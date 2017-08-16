package com.github.ragnard.shen.klambda.nodes.literals;

import com.github.ragnard.shen.klambda.nodes.ExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;

public class StringNode extends ExpressionNode {
    private final String string;

    public StringNode(String string) {
        this.string = string;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return this.string;
    }
}
