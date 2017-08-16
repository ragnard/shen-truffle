package com.github.ragnard.shen.klambda.nodes.literals;

import com.github.ragnard.shen.klambda.nodes.ExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;

public class DoubleNode extends ExpressionNode {

    private final double value;

    public DoubleNode(double value) {
        this.value = value;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return this.value;
    }

    @Override
    public double executeDouble(VirtualFrame frame) {
        return this.value;
    }
}
