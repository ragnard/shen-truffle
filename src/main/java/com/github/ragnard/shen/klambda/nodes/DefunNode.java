package com.github.ragnard.shen.klambda.nodes;

import com.github.ragnard.shen.klambda.Language;
import com.github.ragnard.shen.klambda.runtime.Function;
import com.github.ragnard.shen.klambda.runtime.Symbol;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.frame.VirtualFrame;

public class DefunNode extends ExpressionNode {

    private final Symbol name;
    private final Function function;

    public DefunNode(Symbol name, Function function) {
        this.name = name;
        this.function = function;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        CompilerDirectives.transferToInterpreterAndInvalidate();
        getRootNode().getLanguage(Language.class).getContextReference().get().registerFunction(this.name.getName(), this.function);
        return this.name;
    }
}
