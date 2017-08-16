package com.github.ragnard.shen.klambda.nodes;

import com.github.ragnard.shen.klambda.runtime.Function;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.StandardTags;
import com.oracle.truffle.api.nodes.NodeInfo;

import java.util.Arrays;

@NodeInfo(shortName = "invoke")
@NodeChild("functionNode")
@NodeChild(value = "arguments", type = EvaluateArgumentsNode.class)
public abstract class InvokeNode extends ExpressionNode {

    @Specialization(guards = "function.getArity() > arguments.length")
    public Object doPartial(VirtualFrame frame, Function function, Object[] arguments) {
        return function.curry(arguments);
    }

    @Specialization(guards = "function.getArity() == arguments.length")
    public Object doExact(VirtualFrame frame, Function function, Object[] arguments,
                          @Cached("createDispatchNode()") DispatchNode dispatchNode) {

        return call(dispatchNode, frame, function, function.packArguments(arguments));
    }

    @Specialization(guards = {"function.getArity() < arguments.length"})
    public Object doOver(VirtualFrame frame, Function function, Object[] arguments,
                         @Cached("createDispatchNode()") DispatchNode dispatchNode) {
        int argIndex = 0;

        while (function.getArity() < arguments.length-argIndex) {
            Object[] argumentValues = Arrays.copyOfRange(arguments, argIndex, argIndex+function.getArity());

            argIndex += function.getArity();
            function = (Function) call(dispatchNode, frame, function, function.packArguments(argumentValues), false);
        }

        Object[] remainingArguments = Arrays.copyOfRange(arguments, argIndex, arguments.length);

        if (function.getArity() > remainingArguments.length) {
            return function.curry(remainingArguments);
        } else {
            return call(dispatchNode, frame, function, function.packArguments(remainingArguments));
        }
    }

    public Object call(DispatchNode dispatchNode, VirtualFrame frame, Function function, Object[] arguments) {
        return call(dispatchNode, frame, function, arguments, this.isTail());
    }

    public Object call(DispatchNode dispatchNode, VirtualFrame frame, Function function, Object[] arguments, boolean tailPosition) {
        CallTarget callTarget = function.getCallTarget();

        if (tailPosition) {
            throw new TailCallException(callTarget, arguments);
        }
        while (true) {
            try {
                return dispatchNode.executeDispatch(frame, callTarget, arguments);
            } catch (TailCallException e) {
                callTarget = e.callTarget;
                arguments = e.arguments;
            }
        }
    }

    public static DispatchNode createDispatchNode() {
        return DispatchNodeGen.create();
    }

    @Override
    protected boolean isTaggedWith(Class<?> tag) {
        return tag == StandardTags.CallTag.class || super.isTaggedWith(tag);
    }

}
