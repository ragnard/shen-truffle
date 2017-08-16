package com.github.ragnard.shen.klambda.nodes.local;

import com.github.ragnard.shen.klambda.nodes.ExpressionNode;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.NodeFields;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

@NodeFields({@NodeField(name = "slot", type = FrameSlot.class),
             @NodeField(name = "depth", type = int.class)})
public abstract class ReadLexicalClosureVariableNode extends ExpressionNode {

    public interface FrameGet<T> {
        T get(Frame frame, FrameSlot slot) throws FrameSlotTypeException;
    }

    public abstract FrameSlot getSlot();
    public abstract int getDepth();

    @ExplodeLoop
    public <T> T readUpStack(FrameGet<T> getter, Frame frame)
            throws FrameSlotTypeException {

        Frame lookupFrame = frame;
        for (int i = 0; i < this.getDepth(); i++) {
            lookupFrame = getLexicalClosure(lookupFrame);
        }
        return getter.get(lookupFrame, this.getSlot());
    }

    @Specialization(rewriteOn = FrameSlotTypeException.class)
    protected long readLong(VirtualFrame virtualFrame) throws FrameSlotTypeException {
        return this.readUpStack(Frame::getLong, virtualFrame);
    }

    @Specialization(rewriteOn = FrameSlotTypeException.class)
    protected double readDouble(VirtualFrame virtualFrame) throws FrameSlotTypeException {
        return this.readUpStack(Frame::getDouble, virtualFrame);
    }

    @Specialization(rewriteOn = FrameSlotTypeException.class)
    protected Object readObject(VirtualFrame virtualFrame) throws FrameSlotTypeException {
        return this.readUpStack(Frame::getObject, virtualFrame);
    }

    @Specialization(contains = { "readLong", "readDouble", "readObject" })
    protected Object read(VirtualFrame virtualFrame) {
        try {
            return this.readUpStack(Frame::getValue, virtualFrame);
        } catch (FrameSlotTypeException e) {
            // FrameSlotTypeException not thrown
        }
        return null;
    }
}
