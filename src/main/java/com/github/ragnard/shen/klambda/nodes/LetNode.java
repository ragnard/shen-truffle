package com.github.ragnard.shen.klambda.nodes;

import com.github.ragnard.shen.klambda.nodes.local.WriteLocalVariableNodeGen;
import com.github.ragnard.shen.klambda.runtime.Symbol;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;

public class LetNode extends ExpressionNode {

    private final FrameSlot slot;

    @Child
    private ExpressionNode bodyNode;

    public LetNode(FrameSlot slot, ExpressionNode valueNode, ExpressionNode bodyNode) {
        this.slot = slot;
        this.bodyNode = DoNode.create(WriteLocalVariableNodeGen.create(valueNode, slot), bodyNode);
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return this.bodyNode.executeGeneric(frame);
    }

    @Override
    public void setIsTail() {
        super.setIsTail();
        this.bodyNode.setIsTail();
    }
}
