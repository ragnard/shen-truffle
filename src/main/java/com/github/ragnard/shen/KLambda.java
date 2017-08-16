package com.github.ragnard.shen;

import com.github.ragnard.shen.klambda.Language;
import com.github.ragnard.shen.klambda.TraceInstrument;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.vm.PolyglotEngine;
import com.oracle.truffle.api.vm.PolyglotRuntime;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class KLambda {

    private final PolyglotEngine engine;

    public KLambda() {
        this(System.in, System.out);
    }

    public KLambda(InputStream in, OutputStream out) {
        this.engine = PolyglotEngine.newBuilder().setIn(in).setOut(out).build();
        PolyglotRuntime.Instrument traceInstrument = this.engine.getRuntime().getInstruments().get(TraceInstrument.ID);
        //traceInstrument.setEnabled(true);
    }

    public Object eval(String s) {
        Source code = Source.newBuilder(s)
                .name("<eval>")
                .interactive()
                .mimeType(Language.MIME_TYPE)
                .build();

        return eval(code);
    }

    public Object eval(InputStream s) throws IOException {
        Source code = Source.newBuilder(new InputStreamReader(s))
                .name("<eval>")
                .interactive()
                .mimeType(Language.MIME_TYPE)
                .build();

        return eval(code);
    }

    public Object eval(Source source) {
        try {
            PolyglotEngine.Value result = engine.eval(source);

            return result.get();

        } catch (UnsupportedSpecializationException e) {
            throw e;
            //throw new RuntimeException(e.getNode().getEncapsulatingSourceSection().toString(), e);
        }
    }

    public static void main(String[] args) throws IOException {
        KLambda kl = new KLambda();

        kl.eval(System.in);
    }
}
