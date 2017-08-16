package com.github.ragnard.shen.klambda.nodes;

import com.github.ragnard.shen.klambda.Types;
import com.github.ragnard.shen.klambda.TypesGen;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.Instrumentable;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.api.source.SourceSection;

@TypeSystemReference(Types.class)
@Instrumentable(factory = ExpressionNodeWrapper.class)
public abstract class ExpressionNode extends Node {

    @CompilationFinal
    private boolean isTail = false;

    private SourceSection sourceSection;
    private Object form;

    public boolean isTail() {
        return this.isTail;
    }

    public void setIsTail() {
        this.isTail = true;
    }

    public Object getForm() {
        return this.form;
    }

    @Override
    public SourceSection getSourceSection() {
        return this.sourceSection;
    }

    public ExpressionNode setSourceSection(SourceSection sourceSection) {
        this.sourceSection = sourceSection;
        return this;
    }

    public ExpressionNode setForm(Object form) {
        this.form = form;
        return this;
    }

    public abstract Object executeGeneric(VirtualFrame frame);

    public long executeLong(VirtualFrame frame) throws UnexpectedResultException {
        return TypesGen.expectLong(executeGeneric(frame));
    }

    public double executeDouble(VirtualFrame frame) throws UnexpectedResultException {
        return TypesGen.expectDouble(executeGeneric(frame));
    }

    protected static MaterializedFrame getLexicalClosure(Frame frame) {
        Object[] args = frame.getArguments();
        if (args.length > 0) {
            return (MaterializedFrame) frame.getArguments()[0];
        } else {
            return null;
        }
    }
}
