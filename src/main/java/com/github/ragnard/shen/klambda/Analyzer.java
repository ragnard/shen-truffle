package com.github.ragnard.shen.klambda;

import com.github.ragnard.shen.klambda.nodes.*;
import com.github.ragnard.shen.klambda.nodes.literals.*;
import com.github.ragnard.shen.klambda.nodes.local.ReadArgumentNode;
import com.github.ragnard.shen.klambda.nodes.local.ReadLexicalClosureVariableNodeGen;
import com.github.ragnard.shen.klambda.nodes.local.ReadLocalVariableNodeGen;
import com.github.ragnard.shen.klambda.nodes.local.WriteLocalVariableNodeGen;
import com.github.ragnard.shen.klambda.runtime.Cons;
import com.github.ragnard.shen.klambda.runtime.Function;
import com.github.ragnard.shen.klambda.runtime.Symbol;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.source.SourceSection;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Analyzer {

    private final Language language;

    private static class Env {

        static AtomicInteger counter = new AtomicInteger();

        static class Local {
            private final String name;
            private final FrameSlot frameSlot;

            Local(String name, FrameSlot frameSlot) {
                this.name = name;
                this.frameSlot = frameSlot;
            }
        }

        static class Lexical {
            public final FrameSlot frameSlot;
            public final int depth;

            Lexical(FrameSlot frameSlot, int depth) {
                this.frameSlot = frameSlot;
                this.depth = depth;
            }
        }

        private final Env parent;
        private final FrameDescriptor frameDescriptor;
        private final LinkedList<Local> locals;

        Env() {
            this(null);
        }

        Env(Env parent) {
            this(parent, new FrameDescriptor(), new LinkedList<>());
        }

        Env(Env parent, FrameDescriptor frameDescriptor, LinkedList<Local> locals) {
            this.parent = parent;
            this.frameDescriptor = frameDescriptor;
            this.locals = locals;
        }

        public Env begin() {
            return new Env(this);
        }

        public Env fresh(String name) {
            LinkedList<Local> locals = new LinkedList<>(this.locals);
            FrameSlot slot = this.frameDescriptor.addFrameSlot(name + "_" + counter.incrementAndGet());
            locals.addFirst(new Local(name, slot));
            return new Env(this.parent, this.frameDescriptor, locals);
        }

        public FrameSlot getLocal(String name) {
            for(Local local : this.locals) {
                if (local.name.equals(name)) {
                    return local.frameSlot;
                }
            }
            return null;
        }

        public Lexical getLexical(String name) {
            int depth = 1;
            for(Env env = this.parent; env != null; env = env.parent, depth++) {
                FrameSlot slot = env.getLocal(name);
                if (slot != null) {
                    return new Lexical(slot, depth);
                }
            }
            return null;
        }


    }

    protected Analyzer(Language language) {
        this.language = language;
    }

    public static RootNode analyzeRoot(Language language, Object form, SourceSection sourceSection) {
        Analyzer analyzer = new Analyzer(language);

        Env s = new Env();
        ExpressionNode expr = analyzer.analyze(s, form);
        expr.setSourceSection(sourceSection);
        expr.setForm(form);

        //NodeUtil.printCompactTree(System.out, expr);

        return new RootNode(language, s.frameDescriptor, expr);
    }

    public ExpressionNode analyze(Env e, Object form) {
        if (form instanceof Long) {
            return new LongNode((long) form);
        } else if (form instanceof Double) {
            return new DoubleNode((double) form);
        } else if (form instanceof Symbol) {
            return analyzeSymbol(e, (Symbol) form);
        } else if (form instanceof String) {
            return new StringNode((String)form);
        } else if (form instanceof List) {
            return analyzeList(e, (List)form);
        } else if (form instanceof Cons) {
            return analyzeList(e, ((Cons)form).toList());
        } else {
            throw new RuntimeException("cannot analyze form: " + form.toString());
        }
    }

    private ExpressionNode analyzeSymbol(Env e, Symbol symbol) {
        FrameSlot slot = e.getLocal(symbol.getName());
        if (slot != null) {
            return ReadLocalVariableNodeGen.create(slot);
        }

        Env.Lexical lexical = e.getLexical(symbol.getName());
        if (lexical != null) {
            return ReadLexicalClosureVariableNodeGen.create(lexical.frameSlot, lexical.depth);
        }

        return new SymbolNode(symbol);
    }

    private ExpressionNode analyzeList(Env e, List<Object> forms) {
        if (forms.size() == 0) {
            return new ListNode(Cons.EMPTY);
        }

        return analyzeSpecial(e, forms).orElseGet(() -> analyzeApplication(e, forms));
    }

    private Optional<ExpressionNode> analyzeSpecial(Env e, List<Object> forms) {
        assert forms.size() > 0;

        Object first = forms.get(0);
        if (!(first instanceof Symbol)) {
            return Optional.empty();
        }

        Symbol op = (Symbol)first;

        switch(op.getName()) {
            case "if":
                // (if CONDITION THEN ELSE)
                if (forms.size() != 4) break;
                return Optional.of(new IfNode(analyze(e, forms.get(1)), analyze(e, forms.get(2)), analyze(e, forms.get(3))));
            case "or":
                // (or EXPR EXPR)
                if (forms.size() != 3) break;
                return Optional.of(IfNode.or(analyze(e, forms.get(1)), analyze(e, forms.get(2))));
            case "and":
                // (and EXPR EXPR)
                if (forms.size() != 3) break;
                return Optional.of(IfNode.and(analyze(e, forms.get(1)), analyze(e, forms.get(2))));
            case "cond":
                // (cond (test1 value1) (test2 value2) ...)) )
                return Optional.of(IfNode.cond(forms.subList(1, forms.size()).stream().flatMap(clauseForm -> {
                    java.util.List clause = clauseForm instanceof List ? (List)clauseForm : ((Cons)clauseForm).toList();
                    return Stream.of(analyze(e, clause.get(0)), analyze(e, clause.get(1)));
                }).collect(Collectors.toList())));
            case "let":
                // (let SYMBOL VALUE-EXPR BODY-EXPR)
                Symbol variableName = (Symbol)forms.get(1);
                ExpressionNode valueNode = analyze(e, forms.get(2));
                Env letEnv = e.fresh(variableName.getName());
                FrameSlot letSlot = letEnv.getLocal(variableName.getName());
                return Optional.of(new LetNode(letSlot, valueNode, analyze(letEnv, forms.get(3))));
            case "defun":
                // (defun NAME (ARGS) BODY-EXPR))
                Symbol name = (Symbol)forms.get(1);
                return Optional.of(new DefunNode(name, createFunction(e, forms.get(2), forms.get(3), name.toString())));
            case "lambda":
                // (lambda ARG-SYM BODY-EXPR))
                // (lambda (ARG-SYM...) BODY-EXPR)
                return Optional.of(new LambdaNode(createFunction(e, forms.get(1), forms.get(2), "lambda")));
            case "freeze":
                // (freeze EXPR)
                return Optional.of(new LambdaNode(createFunction(e, new ArrayList<Symbol>(), forms.get(1), "freeze")));
            case "trap-error":
                // (trap-error BODY-EXPR HANDLER)
                return Optional.of(new TrapErrorNode(analyze(e, forms.get(1)), FunctionExpressionNodeGen.create(analyze(e, forms.get(2)))));
        }

        return Optional.empty();
    }

    private ExpressionNode analyzeApplication(Env e, List<Object> forms) {
        assert forms.size() > 0;

        ExpressionNode operatorNode = analyzeOperator(e, forms.get(0));
        ExpressionNode[] operandsNode = analyzeOperands(e, forms.subList(1, forms.size()));

        return InvokeNodeGen.create(operatorNode, EvaluateArgumentsNode.create(operandsNode));
    }

    private ExpressionNode analyzeOperator(Env e, Object operand) {
        if (operand instanceof Symbol) {
            Symbol name = (Symbol)operand;
            if (e.getLocal(name.getName()) != null) {
                return FunctionExpressionNodeGen.create(ReadLocalVariableNodeGen.create(e.getLocal(name.getName())));
            } else if (e.getLexical(name.getName()) != null) {
                Env.Lexical lexical = e.getLexical(name.getName());
                return FunctionExpressionNodeGen.create(ReadLexicalClosureVariableNodeGen.create(lexical.frameSlot, lexical.depth));
            } else {
                return FunctionExpressionNodeGen.create(new SymbolNode(name));
            }
        } else {
            return FunctionExpressionNodeGen.create(analyze(e, operand));
        }
    }

    private ExpressionNode[] analyzeOperands(Env e, List<Object> operands) {
        return operands.stream().map(form -> analyze(e, form)).toArray(ExpressionNode[]::new);
    }

    private Function createFunction(Env e, Object argumentForm, Object bodyForm, String name) {
        e = e.begin();

        List<Symbol> arguments = parseFunctionArguments(argumentForm);
        ArrayList<ExpressionNode> body = new ArrayList<>();

        for(int i = 0; i < arguments.size(); i++) {
            e = e.fresh(arguments.get(i).getName());
            FrameSlot argumentSlot = e.getLocal(arguments.get(i).getName());
            body.add(WriteLocalVariableNodeGen.create(new ReadArgumentNode(i+1), argumentSlot));
        }

        body.add(analyze(e, bodyForm));

        DoNode bodyNode = DoNode.create(body.toArray(new ExpressionNode[body.size()]));
        bodyNode.setIsTail();

        Function function = Function.create(this.language, e.frameDescriptor, arguments.size(), bodyNode, name);
        function.setForm(bodyForm);

        return function;
    }

    private List<Symbol> parseFunctionArguments(Object argumentForm) {
        if (argumentForm instanceof Symbol) {
            return Arrays.asList((Symbol)argumentForm);
        } else if (argumentForm instanceof Cons) {
            return ((Cons)argumentForm).toList();
        } else {
            return (List<Symbol>)argumentForm;
        }
    }

}
