package com.github.ragnard.shen.klambda.nodes;


import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

public class EvaluateArgumentsNode extends ExpressionNode {

    @Children
    private final ExpressionNode[] argumentNodes;

    public EvaluateArgumentsNode(ExpressionNode[] argumentNodes) {
        this.argumentNodes = argumentNodes;
    }


    @Override
    @ExplodeLoop
    public Object executeGeneric(VirtualFrame frame) {
        CompilerAsserts.compilationConstant(this.argumentNodes.length);

        Object[] argumentValues = new Object[this.argumentNodes.length];
        for(int i = 0; i < argumentNodes.length; i++) {
            argumentValues[i] = this.argumentNodes[i].executeGeneric(frame);
        }
        return argumentValues;
    }

    public static EvaluateArgumentsNode create(ExpressionNode... nodes) {
        return new EvaluateArgumentsNode(nodes);
    }

}
