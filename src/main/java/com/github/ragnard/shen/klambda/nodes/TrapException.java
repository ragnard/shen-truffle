package com.github.ragnard.shen.klambda.nodes;

public class TrapException extends RuntimeException {

    public TrapException(String error) {
        super(error, null, false, false);
    }
}
