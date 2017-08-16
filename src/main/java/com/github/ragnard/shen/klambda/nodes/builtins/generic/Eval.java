package com.github.ragnard.shen.klambda.nodes.builtins.generic;

import com.github.ragnard.shen.klambda.Analyzer;
import com.github.ragnard.shen.klambda.Language;
import com.github.ragnard.shen.klambda.nodes.ExpressionNode;
import com.github.ragnard.shen.klambda.nodes.RootNode;
import com.github.ragnard.shen.klambda.nodes.builtins.BuiltinNode;
import com.github.ragnard.shen.klambda.nodes.builtins.Builtins;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.StandardTags;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;

@NodeInfo(shortName = "eval-kl")
public abstract class Eval extends BuiltinNode {

    @Specialization
    //@CompilerDirectives.TruffleBoundary
    public Object eval(VirtualFrame frame, Object expr) {
        CompilerDirectives.transferToInterpreterAndInvalidate();

        //System.out.println("eval: " + expr);

        RootNode root = Analyzer.analyzeRoot(getContext().getLanguage(), expr, Builtins.BUILTIN_SOURCE.createUnavailableSection());
        /*if(this.isTail()) {
            root.setIsTail();
        }*/
        CallTarget callTarget = Truffle.getRuntime().createCallTarget(root);

        return callTarget.call(frame.getArguments()[0]);
    }
}
