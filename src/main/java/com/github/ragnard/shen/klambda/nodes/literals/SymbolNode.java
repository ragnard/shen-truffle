package com.github.ragnard.shen.klambda.nodes.literals;

import com.github.ragnard.shen.klambda.nodes.ExpressionNode;
import com.github.ragnard.shen.klambda.runtime.Symbol;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

public class SymbolNode extends ExpressionNode {
    private final Symbol symbol;

    public SymbolNode(Symbol symbol) {
        this.symbol = symbol;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return this.symbol;
    }
}
