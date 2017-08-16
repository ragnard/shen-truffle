package com.github.ragnard.shen.klambda.nodes;

import com.github.ragnard.shen.klambda.nodes.literals.ListNode;
import com.github.ragnard.shen.klambda.nodes.literals.StringNode;
import com.github.ragnard.shen.klambda.nodes.literals.SymbolNode;
import com.github.ragnard.shen.klambda.runtime.Cons;
import com.github.ragnard.shen.klambda.runtime.Symbol;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.profiles.ConditionProfile;

public class IfNode extends ExpressionNode {

    @Child
    private ExpressionNode conditionNode;

    @Child
    private ExpressionNode thenNode;

    @Child
    private ExpressionNode elseNode;

    private final ConditionProfile condition = ConditionProfile.createCountingProfile();

    public IfNode(ExpressionNode conditionNode, ExpressionNode thenNode, ExpressionNode elseNode) {
        this.conditionNode = conditionNode;
        this.thenNode = thenNode;
        this.elseNode = elseNode;
    }

    public static IfNode or(ExpressionNode a, ExpressionNode b) {
        return new IfNode(a, new SymbolNode(Symbol.TRUE), new IfNode(b, new SymbolNode(Symbol.TRUE), new SymbolNode(Symbol.FALSE)));
    }

    public static IfNode and(ExpressionNode a, ExpressionNode b) {
        return new IfNode(a, new IfNode(b, new SymbolNode(Symbol.TRUE), new SymbolNode(Symbol.FALSE)), new SymbolNode(Symbol.FALSE));
    }

    public static ExpressionNode cond(java.util.List<ExpressionNode> clauses) {
        if (clauses.size() == 0) {
            return InvokeNodeGen.create(FunctionExpressionNodeGen.create(new SymbolNode(Symbol.intern("simple-error"))), EvaluateArgumentsNode.create(new ExpressionNode[] {new StringNode("No condition was true")}));
        } else {
            return new IfNode(clauses.get(0), clauses.get(1), cond(clauses.subList(2, clauses.size())));
        }
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        if(condition.profile(evaluateCondition(frame))) {
            return this.thenNode.executeGeneric(frame);
        } else {
            return this.elseNode.executeGeneric(frame);
        }
    }

    private boolean evaluateCondition(VirtualFrame frame) {
        Object ret = conditionNode.executeGeneric(frame);
        if (ret == Symbol.TRUE) {
            return true;
        } else if (ret == Symbol.FALSE) {
            return false;
        } else {
            throw new UnsupportedSpecializationException(this, new Node[]{conditionNode}, ret);
        }
    }

    @Override
    public void setIsTail() {
        super.setIsTail();
        this.thenNode.setIsTail();
        this.elseNode.setIsTail();
    }
}
