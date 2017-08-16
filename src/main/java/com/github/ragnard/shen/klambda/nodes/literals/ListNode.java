package com.github.ragnard.shen.klambda.nodes.literals;

import com.github.ragnard.shen.klambda.nodes.ExpressionNode;
import com.github.ragnard.shen.klambda.runtime.Cons;
import com.oracle.truffle.api.frame.VirtualFrame;

public class ListNode extends ExpressionNode {

    private final Cons cons;

    public ListNode(Cons cons) {
        this.cons = cons;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return cons;
    }
}
