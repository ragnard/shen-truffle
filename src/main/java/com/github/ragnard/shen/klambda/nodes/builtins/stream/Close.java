package com.github.ragnard.shen.klambda.nodes.builtins.stream;

import com.github.ragnard.shen.klambda.nodes.builtins.BuiltinNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

import java.io.Closeable;
import java.io.IOException;

@NodeInfo(shortName = "close")
public abstract class Close extends BuiltinNode {
    @Specialization
    public Object close(Closeable stream) {
        try {
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException("error closing stream:" + e);
        }
        // TODO: empty list?
        return null;
    }
}
