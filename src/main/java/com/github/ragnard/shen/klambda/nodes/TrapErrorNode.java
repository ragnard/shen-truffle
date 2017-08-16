package com.github.ragnard.shen.klambda.nodes;

import com.github.ragnard.shen.klambda.runtime.Function;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.VirtualFrame;

public class TrapErrorNode extends ExpressionNode {

    @Child
    private ExpressionNode bodyNode;

    @Child
    private ExpressionNode handlerNode;

    @Child
    private DispatchNode dispatchNode;

    public TrapErrorNode(ExpressionNode bodyNode, ExpressionNode handlerNode) {
        this.bodyNode = bodyNode;
        this.handlerNode = handlerNode;
        this.dispatchNode = DispatchNodeGen.create();
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        try {
            return this.bodyNode.executeGeneric(frame);
        } catch (RuntimeException e) {
           Function handler = (Function) this.handlerNode.executeGeneric(frame);
            return call(frame, handler.getCallTarget(), new Object[]{handler.getLexicalScope(), e});
        }
    }

    protected Object call(VirtualFrame frame, CallTarget callTarget, Object[] arguments) {
        if (this.isTail()) {
            throw new TailCallException(callTarget, arguments);
        }
        while (true) {
            try {
                return dispatchNode.executeDispatch(frame, callTarget, arguments);
                //return callTarget.call(arguments);
            } catch (TailCallException e) {
                callTarget = e.callTarget;
                arguments = e.arguments;
            }
        }
    }

    @Override
    public void setIsTail() {
        super.setIsTail();
        //this.bodyNode.setIsTail();
        this.handlerNode.setIsTail();
    }
}
