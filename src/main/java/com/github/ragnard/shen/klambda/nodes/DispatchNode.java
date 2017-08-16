package com.github.ragnard.shen.klambda.nodes;

import com.github.ragnard.shen.klambda.Types;
import com.github.ragnard.shen.klambda.runtime.Function;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.DirectCallNode;
import com.oracle.truffle.api.nodes.IndirectCallNode;
import com.oracle.truffle.api.nodes.Node;

@TypeSystemReference(Types.class)
public abstract class DispatchNode extends Node {

    protected static final int INLINE_CACHE_SIZE = 2;

    public abstract Object executeDispatch(VirtualFrame frame, CallTarget callTarget, Object[] arguments);

    @Specialization(limit = "INLINE_CACHE_SIZE",
                    guards = "callTarget == cachedCallTarget")
    public static Object doDirect(VirtualFrame frame, CallTarget callTarget, Object[] arguments,
                           @Cached("callTarget") CallTarget cachedCallTarget,
                           @Cached("create(cachedCallTarget)") DirectCallNode callNode) {
        return callNode.call(arguments);
    }

    @Specialization(replaces = "doDirect")
    protected static Object doIndirect(VirtualFrame frame, CallTarget callTarget, Object[] arguments,
                                       @Cached("create()") IndirectCallNode callNode) {
        return callNode.call(callTarget, arguments);
    }

}
