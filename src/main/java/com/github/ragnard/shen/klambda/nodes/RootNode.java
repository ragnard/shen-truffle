package com.github.ragnard.shen.klambda.nodes;

import com.github.ragnard.shen.klambda.Language;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.ProvidedTags;
import com.oracle.truffle.api.instrumentation.StandardTags;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.source.SourceSection;

@NodeInfo(language = "Kλ")
public class RootNode extends com.oracle.truffle.api.nodes.RootNode {

    private final String name;

    @Child
    private ExpressionNode body;

    public RootNode(Language language, FrameDescriptor frameDescriptor, ExpressionNode body) {
        this(language, frameDescriptor, body, null);
    }

    public RootNode(Language language, FrameDescriptor frameDescriptor, ExpressionNode body, String name) {
        super(language, frameDescriptor);
        this.body = body;
        this.name = name;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        return this.body.executeGeneric(frame);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isCloningAllowed() {
        return true;
    }

    public void setIsTail() {
        this.body.setIsTail();
    }

    @Override
    protected boolean isTaggedWith(Class<?> tag) {
        return tag == StandardTags.RootTag.class || super.isTaggedWith(tag);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
