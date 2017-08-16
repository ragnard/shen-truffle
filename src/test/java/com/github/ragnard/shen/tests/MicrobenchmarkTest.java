package com.github.ragnard.shen.tests;

import com.github.ragnard.shen.KLambda;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class MicrobenchmarkTest {


    public static void main(String[] args) {
        Object ret = benchmark("(set x 1)", "(benchmark 1000000 (lambda i (value x)))");
    }

    @Test
    public void invoke_noop() {
        Object ret = benchmark("(benchmark 10000000 (lambda i i))");
    }

    @Test
    public void fib() {
        Object ret = benchmark("(defun fib-slow (V1354) (cond ((= 0 V1354) 1) ((= 1 V1354) 1) (true (+ (fib-slow (- V1354 1)) (fib-slow (- V1354 2))))))",
                "(benchmark 1 (fib-slow 40))");
    }

    @Test
    public void write_global() {
        Object ret = benchmark("(benchmark 1000000 (lambda i (set x i)))");
    }

    private static Object benchmark(String... exprs) {
        KLambda kl = new KLambda();
        kl.eval("(defun dotimes (n f) (if (= 0 n) done (let _ (f n) (dotimes (- n 1) f))))");
        kl.eval("(defun benchmark (n f) (let start (get-time run) (let ret (dotimes n f) (let t (- (get-time run) start) t))))");

        Object ret = null;
        for (String expr : exprs) {
            ret = kl.eval(expr);
        }
        return  ret;

    }
}
