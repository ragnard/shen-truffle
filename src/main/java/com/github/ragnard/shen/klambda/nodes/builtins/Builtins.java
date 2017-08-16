package com.github.ragnard.shen.klambda.nodes.builtins;

import com.github.ragnard.shen.klambda.Language;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.source.Source;

public class Builtins {

    public static final Source BUILTIN_SOURCE = Source.newBuilder("").name("primitive").mimeType(Language.MIME_TYPE).build();

    public static NodeFactory[] NODE_FACTORIES = new NodeFactory[] {

            // symbol
            com.github.ragnard.shen.klambda.nodes.builtins.symbol.InternFactory.getInstance(),

            // string
            com.github.ragnard.shen.klambda.nodes.builtins.string.ConcatFactory.getInstance(),
            com.github.ragnard.shen.klambda.nodes.builtins.string.NumberToStringFactory.getInstance(),
            com.github.ragnard.shen.klambda.nodes.builtins.string.PosFactory.getInstance(),
            com.github.ragnard.shen.klambda.nodes.builtins.string.PredicateFactory.getInstance(),
            com.github.ragnard.shen.klambda.nodes.builtins.string.StrFactory.getInstance(),
            com.github.ragnard.shen.klambda.nodes.builtins.string.StringToNumberFactory.getInstance(),
            com.github.ragnard.shen.klambda.nodes.builtins.string.TailFactory.getInstance(),

            // assignment
            com.github.ragnard.shen.klambda.nodes.builtins.assignment.SetFactory.getInstance(),
            com.github.ragnard.shen.klambda.nodes.builtins.assignment.ValueFactory.getInstance(),

            // error
            com.github.ragnard.shen.klambda.nodes.builtins.error.SimpleErrorFactory.getInstance(),
            com.github.ragnard.shen.klambda.nodes.builtins.error.ErrorToStringFactory.getInstance(),

            // list
            com.github.ragnard.shen.klambda.nodes.builtins.list.ConsFactory.getInstance(),
            com.github.ragnard.shen.klambda.nodes.builtins.list.HeadFactory.getInstance(),
            com.github.ragnard.shen.klambda.nodes.builtins.list.TailFactory.getInstance(),
            com.github.ragnard.shen.klambda.nodes.builtins.list.PredicateFactory.getInstance(),

            // generic functions
            com.github.ragnard.shen.klambda.nodes.builtins.generic.EqualsFactory.getInstance(),
            com.github.ragnard.shen.klambda.nodes.builtins.generic.EvalFactory.getInstance(),
            com.github.ragnard.shen.klambda.nodes.builtins.generic.IfFactory.getInstance(),
            com.github.ragnard.shen.klambda.nodes.builtins.generic.AndFactory.getInstance(),
            com.github.ragnard.shen.klambda.nodes.builtins.generic.OrFactory.getInstance(),

            // vector
            com.github.ragnard.shen.klambda.nodes.builtins.vector.CreateFactory.getInstance(),
            com.github.ragnard.shen.klambda.nodes.builtins.vector.PredicateFactory.getInstance(),
            com.github.ragnard.shen.klambda.nodes.builtins.vector.ReadFactory.getInstance(),
            com.github.ragnard.shen.klambda.nodes.builtins.vector.WriteFactory.getInstance(),

            // stream
            com.github.ragnard.shen.klambda.nodes.builtins.stream.OpenFactory.getInstance(),
            com.github.ragnard.shen.klambda.nodes.builtins.stream.CloseFactory.getInstance(),
            com.github.ragnard.shen.klambda.nodes.builtins.stream.ReadByteFactory.getInstance(),
            com.github.ragnard.shen.klambda.nodes.builtins.stream.WriteByteFactory.getInstance(),

            // time
            com.github.ragnard.shen.klambda.nodes.builtins.time.GetTimeFactory.getInstance(),

            // arithmetic
            com.github.ragnard.shen.klambda.nodes.builtins.arithmetic.AddFactory.getInstance(),
            com.github.ragnard.shen.klambda.nodes.builtins.arithmetic.SubtractFactory.getInstance(),
            com.github.ragnard.shen.klambda.nodes.builtins.arithmetic.MultiplyFactory.getInstance(),
            com.github.ragnard.shen.klambda.nodes.builtins.arithmetic.DivideFactory.getInstance(),
            com.github.ragnard.shen.klambda.nodes.builtins.arithmetic.GreaterThanFactory.getInstance(),
            com.github.ragnard.shen.klambda.nodes.builtins.arithmetic.GreaterThanOrEqualFactory.getInstance(),
            com.github.ragnard.shen.klambda.nodes.builtins.arithmetic.LessThanFactory.getInstance(),
            com.github.ragnard.shen.klambda.nodes.builtins.arithmetic.LessThanOrEqualFactory.getInstance(),
            com.github.ragnard.shen.klambda.nodes.builtins.arithmetic.NumberPredicateFactory.getInstance(),

            // integer
            com.github.ragnard.shen.klambda.nodes.builtins.integer.PredicateFactory.getInstance(),

    };
}
