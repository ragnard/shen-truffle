package com.github.ragnard.shen.klambda.nodes.builtins.stream;

import com.github.ragnard.shen.klambda.nodes.builtins.BuiltinNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

import java.io.IOException;
import java.io.InputStream;

@NodeInfo(shortName = "read-byte")
public abstract class ReadByte extends BuiltinNode {
    @Specialization
    public long readByte(InputStream stream) {
        try {
            return (long)stream.read();
        } catch (IOException e) {
            throw new RuntimeException("read-byte: " + e);
        }
    }
}
