package com.github.ragnard.shen.klambda.nodes;


import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

public class SequenceNode extends ExpressionNode {

    private final CallTarget[] callTargets;

    public SequenceNode(CallTarget[] callTargets) {
        this.callTargets = callTargets;
    }


    @Override
    @ExplodeLoop
    public Object executeGeneric(VirtualFrame frame) {
        CompilerAsserts.compilationConstant(this.callTargets.length);

        Object ret = null;
        for(int i = 0; i < this.callTargets.length; i++) {
            ret = this.callTargets[i].call();
        }
        return ret;
    }

    public static SequenceNode create(CallTarget... callTargets) {
        return new SequenceNode(callTargets);
    }
}
