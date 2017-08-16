package com.github.ragnard.shen.klambda.nodes.builtins.assignment;

import com.github.ragnard.shen.klambda.nodes.builtins.BuiltinNode;
import com.github.ragnard.shen.klambda.runtime.Symbol;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "value")
public abstract class Value extends BuiltinNode {

    @Specialization(guards = "symbol == cachedSymbol", limit = "2")
    public Object value(Symbol symbol,
                        @Cached("symbol") Symbol cachedSymbol,
                        @Cached("lookupFrameSlot(cachedSymbol)") FrameSlot cachedFrameSlot) {
        if (cachedFrameSlot == null) {
            throw new RuntimeException("value: not set: " + symbol);
        }
        try {
            return this.getContext().getGlobalFrame().getObject(cachedFrameSlot);
        } catch (FrameSlotTypeException e) {
            throw new RuntimeException("value: frame slot type", e);
        }
    }

    @Specialization(replaces = "value")
    public Object valueSlow(Symbol symbol) {
        CompilerDirectives.transferToInterpreterAndInvalidate();

        FrameSlot frameSlot = lookupFrameSlot(symbol);

        if (frameSlot == null) {
            throw new RuntimeException("value: not set: " + symbol);
        }
        try {
            return this.getContext().getGlobalFrame().getObject(frameSlot);
        } catch (FrameSlotTypeException e) {
            throw new RuntimeException("value: frame slot type", e);
        }
    }

    protected FrameSlot lookupFrameSlot(Symbol symbol) {
        MaterializedFrame globalFrame = this.getContext().getGlobalFrame();
        return globalFrame.getFrameDescriptor().findFrameSlot(symbol.getName());
    }
}
