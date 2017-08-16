package com.github.ragnard.shen.klambda.nodes;


import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

public class DoNode extends ExpressionNode {

    @Children
    private final ExpressionNode[] exprs;

    public DoNode(ExpressionNode[] exprs) {
        this.exprs = exprs;
    }

    @Override
    @ExplodeLoop
    public Object executeGeneric(VirtualFrame frame) {
        CompilerAsserts.compilationConstant(this.exprs.length);

        Object ret = null;
        for(ExpressionNode expr : this.exprs) {
            ret = expr.executeGeneric(frame);
        }
        return ret;
    }

    public static DoNode create(ExpressionNode... nodes) {
        return new DoNode(nodes);
    }

    @Override
    public void setIsTail() {
        super.setIsTail();
        this.exprs[this.exprs.length-1].setIsTail();
    }
}
