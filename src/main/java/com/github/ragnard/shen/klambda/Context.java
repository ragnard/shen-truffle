package com.github.ragnard.shen.klambda;

import com.github.ragnard.shen.klambda.nodes.ExpressionNode;
import com.github.ragnard.shen.klambda.nodes.RootNode;
import com.github.ragnard.shen.klambda.nodes.builtins.BuiltinNode;
import com.github.ragnard.shen.klambda.nodes.builtins.Builtins;
import com.github.ragnard.shen.klambda.nodes.local.ReadArgumentNode;
import com.github.ragnard.shen.klambda.runtime.Function;
import com.oracle.truffle.api.Assumption;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

import java.nio.charset.Charset;
import java.util.HashMap;

public class Context {

    private final Language language;
    private final MaterializedFrame globalFrame;

    public final static class RegisteredFunction {
        public final Function function;
        public final Assumption assumption;

        RegisteredFunction(Function function, Assumption assumption) {
            this.function = function;
            this.assumption = assumption;
        }
    }

    private final HashMap<String, RegisteredFunction> functions;

    public Context(Language language, TruffleLanguage.Env env) {
        this.language = language;
        this.globalFrame = Truffle.getRuntime().createMaterializedFrame(null);
        this.functions = new HashMap<>();

        set("*language*", "Java");
        set("*implementation*", "Truffle / " + Truffle.getRuntime().getName());
        set("*porters*", new String("Ragnar Dahlén".getBytes(), Charset.forName("ISO-8859-1")));
        set("*port*", "0.1");
        set("*stinput*", env.in());
        set("*stoutput*", env.out());
        set("*home-directory*", System.getProperty("user.dir"));

        for(NodeFactory nodeFactory : Builtins.NODE_FACTORIES) {
            installBuiltin(nodeFactory);
        }
    }

    public Language getLanguage() {
        return this.language;
    }

    public MaterializedFrame getGlobalFrame() {
        return this.globalFrame;
    }

    private void set(String name, Object value) {
        FrameSlot slot = this.globalFrame.getFrameDescriptor().addFrameSlot(name);
        this.globalFrame.setObject(slot, value);
    }

    public void registerFunction(String name, Function f) {
        functions.compute(name, (k, v) -> {
            if (v != null) {
                v.assumption.invalidate();
            }
            return new RegisteredFunction(f, Truffle.getRuntime().createAssumption(name));
        });
        f.setName(name);
    }

    public RegisteredFunction lookupFunction(String name) {
        return functions.get(name);
    }

    public void installBuiltin(NodeFactory<? extends BuiltinNode> factory) {
        int argumentCount = factory.getExecutionSignature().size();

        ExpressionNode[] argumentNodes = new ExpressionNode[argumentCount];
        for (int i = 0; i < argumentCount; i++) {
            argumentNodes[i] = new ReadArgumentNode(i+1);
        }

        BuiltinNode builtinBodyNode = factory.createNode(argumentNodes, this);
        //builtinBodyNode.addRootTag();
        /* The name of the builtin function is specified via an annotation on the node class. */
        String name = lookupNodeInfo(builtinBodyNode.getClass()).shortName();
        //final SourceSection srcSection = BUILTIN_SOURCE.createUnavailableSection();
        //builtinBodyNode.setSourceSection(srcSection);

        RootNode rootNode = new RootNode(language, new FrameDescriptor(), builtinBodyNode, name);
        Function f = new Function(Truffle.getRuntime().createCallTarget(rootNode), argumentCount);
        f.setName(name);

        registerFunction(name, f);
    }

    public static NodeInfo lookupNodeInfo(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        NodeInfo info = clazz.getAnnotation(NodeInfo.class);
        if (info != null) {
            return info;
        } else {
            return lookupNodeInfo(clazz.getSuperclass());
        }
    }

}
