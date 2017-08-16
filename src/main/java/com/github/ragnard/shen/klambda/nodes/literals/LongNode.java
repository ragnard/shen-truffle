package com.github.ragnard.shen.klambda.nodes.literals;

import com.github.ragnard.shen.klambda.nodes.ExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

public class LongNode extends ExpressionNode {

    private final long value;

    public LongNode(long value) {
        this.value = value;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return this.value;
    }

    @Override
    public long executeLong(VirtualFrame frame) throws UnexpectedResultException {
        return this.value;
    }
}
