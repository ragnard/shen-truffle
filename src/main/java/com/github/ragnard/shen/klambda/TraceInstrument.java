package com.github.ragnard.shen.klambda;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.*;
import com.oracle.truffle.api.nodes.Node;

import java.io.PrintStream;


@TruffleInstrument.Registration(id = TraceInstrument.ID)
public final class TraceInstrument extends TruffleInstrument {

    public static final String ID = "tracer";

    @Override
    protected void onCreate(final Env env) {
        SourceSectionFilter.Builder builder = SourceSectionFilter.newBuilder();
        SourceSectionFilter filter = builder.build();
        //SourceSectionFilter filter = builder.tagIs(StandardTags.CallTag.class, StandardTags.RootTag.class).build();
        //SourceSectionFilter filter = builder.tagIs(StandardTags.StatementTag.class).build();
        Instrumenter instrumenter = env.getInstrumenter();
        instrumenter.attachFactory(filter, new TraceEventFactory(env));
    }

    private class TraceEventFactory implements ExecutionEventNodeFactory {

        private final Env env;

        TraceEventFactory(final Env env) {
            this.env = env;
        }

        public ExecutionEventNode create(final EventContext ec) {
            final PrintStream out = new PrintStream(env.out());

            return new ExecutionEventNode() {
                /*@Override
                protected void onReturnValue(VirtualFrame frame, Object result) {
                    out.println("return" + this);
                }*/

                @Override
                protected void onEnter(VirtualFrame frame) {
                    Node node = ec.getInstrumentedNode();
                    out.println("node: " + node);
                    out.println("-> " + node.getEncapsulatingSourceSection().toString());
//                    if (node instanceof InvokeNode) {
//                        InvokeNode invokeNode = (InvokeNode) node;
//                        out.println("invoke: " + invokeNode.getForm());
//                    } else if (node instanceof EvalFactory.EvalNodeGen) {
//                        EvalFactory.EvalNodeGen evalNode = (EvalFactory.EvalNodeGen) node;
//                        out.println("eval: " + evalNode);
//                    } else if (node instanceof RootNode) {
//                        RootNode rootNode = (RootNode) node;
//                        out.println("root: " + rootNode);
//                    }
                }

            };
        }
    }

}