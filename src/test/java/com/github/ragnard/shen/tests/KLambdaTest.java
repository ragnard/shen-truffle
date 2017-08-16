package com.github.ragnard.shen.tests;

import com.github.ragnard.shen.tests.categories.Unit;
import com.github.ragnard.shen.KLambda;
import com.github.ragnard.shen.klambda.nodes.TrapException;
import com.github.ragnard.shen.klambda.runtime.Cons;
import com.github.ragnard.shen.klambda.runtime.Function;
import com.github.ragnard.shen.klambda.runtime.Symbol;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@Category(Unit.class)
public class KLambdaTest {

    @Test
    public void bug() {
        is(0L, "(defun h () (lambda x x))",
                "((lambda x ((h) x)) 0)");
    }

    @Test
    public void booleans() {
        is(Symbol.TRUE, "true");
        is(Symbol.FALSE, "false");

        is(Symbol.TRUE, "(intern \"true\")");
        is(Symbol.FALSE, "(intern \"false\")");

        is(Symbol.TRUE, "(eval-kl true)");
        is(Symbol.FALSE, "(eval-kl false)");
    }

    @Test
    public void integers() {
        is(0L, "0");
        is(42L, "42");
        is(-42L, "-42");
        is(Long.MIN_VALUE, Long.toString(Long.MIN_VALUE));
        is(Long.MAX_VALUE, Long.toString(Long.MAX_VALUE));
    }

    @Test
    public void floats() {
        is(0.0, "0.0");
        is(42.0, "42.0");
        is(-42.0, "-42.0");
        is(Math.PI, Double.toString(Math.PI));
    }

    @Test
    public void symbols() {
        is(Symbol.intern("blahonga"), "blahonga");
        is(Symbol.intern("BLAHONGA"), "BLAHONGA");
    }

    @Test
    public void equality() {
        // booleans
        is(Symbol.TRUE, "(= true true)");
        is(Symbol.TRUE, "(= false false)");
        is(Symbol.FALSE, "(= true false)");
        is(Symbol.TRUE, "(= true (intern \"true\"))");
        is(Symbol.TRUE, "(= false (intern \"false\"))");

        // numbers
        is(Symbol.TRUE, "(= 1 1)");
        is(Symbol.TRUE, "(= 1.0 1.0)");
        is(Symbol.TRUE, "(= 1 1.0)");
        is(Symbol.TRUE, "(= 1.0 1)");
        is(Symbol.FALSE, "(= 1 0)");
        is(Symbol.FALSE, "(= 1 one)");

        // strings
        is(Symbol.TRUE, "(= \"foo\" \"foo\")");
        is(Symbol.FALSE, "(= \"foo\" \"bar\")");

        // symbols
        is(Symbol.TRUE, "(= x x)");
        is(Symbol.FALSE, "(= x X)");
        is(Symbol.TRUE, "(= foo foo)");
        is(Symbol.FALSE, "(= foo bar)");
        is(Symbol.TRUE, "(= foo (intern \"foo\"))");

        // lists
        is(Symbol.TRUE, "(= (cons 1 foo) (cons 1 foo))");
        is(Symbol.TRUE, "(= (cons 1 foo) (cons 1.0 foo))");
        is(Symbol.TRUE, "(= (cons 1 ()) (cons 1 ()))");
        is(Symbol.TRUE, "(= (cons 1 (cons foo (cons \"bar\" ()))) (cons 1 (cons foo (cons \"bar\" ()))))");

        // vectors
        is(Symbol.TRUE, "(= (absvector 10) (absvector 10))");
        is(Symbol.TRUE, "(= (address-> (absvector 10) 1 foo) (address-> (absvector 10) 1 foo))");

        // functions
        is(Symbol.TRUE, "(set f (lambda x x))",
                        "(= (value f) (value f))");
        is(Symbol.FALSE, "(set f (lambda x x))",
                        "(set g (lambda x x))",
                        "(= (value f) (value g))");
    }

    @Test
    public void globals() {
        is(RuntimeException.class, "(value a)");
        is(1L, "(set a 1)");
        is(1L, "(set a 1)", "(value a)");
        is(2L, "(set a 1)", "(set a 2)", "(value a)");
    }

    @Test
    public void let() {
        is(1L, "(let A 1 A)");
        is(1L, "(let A 1 (let B 2 A))");
        is(2L, "(let A 1 (let B 2 B))");
        is(1L, "(let A 1 (let B 2 (let C 3 A)))");
        is(2L, "(let A 1 (let B 2 (let C 3 B)))");
        is(3L, "(let A 1 (let B 2 (let C 3 C)))");
        is(3L, "(let A 1 (let B 2 (let A 3 A)))");

        is(RuntimeException.class, "(let (intern \"A\") 1 A)");
        is(RuntimeException.class, "(let 1 1)");
        is(RuntimeException.class, "(let \"A\" 1)");

        // closures
        is(2L, "(let f (let A 1 (lambda (X) (+ A X))) (f 1))");
        is(2L, "(let f (let A 1 (lambda (X) (+ A X))) (let A 2 (f 1)))");
    }

    @Test
    public void lambda() {
        is(function, "(lambda () X)");
        is(function, "(lambda X X)");
        is(function, "(lambda (X) X)");
        is(function, "(lambda (X Y Z) X)");

        is(1L, "((lambda X X) 1)");
        is(Symbol.intern("Y"), "((lambda X Y) 1)");

        is(function, "((lambda X (lambda Y X)) 42)");
        is(4711L, "(((lambda X (lambda Y Y)) 42) 4711)");
        is(42L, "(((lambda X (lambda Y X)) 42) 4711)");

        is(1L, "((lambda () 1))");

        is(1L, "((lambda (X Y) X) 1 2)");
        is(2L, "((lambda (X Y) Y) 1 2)");

        is(1L, "((lambda (X Y Z) X) 1 2 3)");
        is(2L, "((lambda (X Y Z) Y) 1 2 3)");
        is(3L, "((lambda (X Y Z) Z) 1 2 3)");

        // currying
        is(function, "((lambda (X Y) X) 1)");

        is(1L, "(((lambda (X Y) X) 1) 2)");
        is(2L, "(((lambda (X Y) Y) 1) 2)");

        is(function, "((lambda (X Y Z) X) 1)");
        is(function, "((lambda (X Y Z) X) 1 2)");

        is(1L, "((((lambda (X Y Z) X) 1) 2) 3)");
        is(2L, "((((lambda (X Y Z) Y) 1) 2) 3)");
        is(3L, "((((lambda (X Y Z) Z) 1) 2) 3)");

        is(1L, "(((lambda (X Y Z) X) 1) 2 3)");
        is(2L, "(((lambda (X Y Z) Y) 1) 2 3)");
        is(3L, "(((lambda (X Y Z) Z) 1) 2 3)");

        is(1L, "(((lambda (X Y Z) X) 1 2) 3)");
        is(2L, "(((lambda (X Y Z) Y) 1 2) 3)");
        is(3L, "(((lambda (X Y Z) Z) 1 2) 3)");

        // over application
        is(3L, "((lambda X (lambda Y (+ X Y))) 1 2)");

        // closure
        is(2L, "(defun f (x) (lambda y (+ x y)))",
                "(set a (f 1))",
                "(set b (f 2))",
                "((value a) 1)");
        is(3L, "(defun f (x) (lambda y (+ x y)))",
                "(set a (f 1))",
                "(set b (f 2))",
                "((value b) 1)");
    }


    @Test
    public void freeze() {
        is(function, "(freeze (+ 1 2))");
        is(3L, "(let c (freeze (+ 1 2)) (c))");
        is(3L, "(let c (let v 2 (freeze (+ 1 v))) (c))");

        is(0L, "(set x 0)",
               "(freeze (set x (+ 1 (value x))))",
               "(value x)");

        is(1L, "(set x 0)",
                "(set c (freeze (set x (+ 1 (value x)))))",
                "((value c))",
                "(value x)");

        // captures local scope
        is(3L, "(set c (let a 1 (let b 2 (freeze (+ a b)))))",
                "((value c))");

        // caching thawed value does not seem to be required
        is(3L, "(set x 0)",
               "(set c (freeze (set x (+ 1 (value x)))))",
               "((value c))",
               "((value c))",
               "((value c))");
    }
    
    @Test
    public void trap_error() {
        is(3L, "(trap-error (+ 1 2) (lambda e e))");
        is("error", "(trap-error (simple-error \"error\") (lambda e (error-to-string e)))");
        is("error", "(trap-error (simple-error \"error\") error-to-string)");
        is("error", "(let h (lambda e (error-to-string e)) (trap-error (simple-error \"error\") h))");

        is(0L, "(trap-error (tl 0) (lambda e 0))");
        is(2L, "(trap-error (tl 0) (lambda e (+ 1 1)))");

        is(TrapException.class, "(trap-error (tl 0) (lambda e (simple-error \"error\")))");

        is(0L, "(trap-error (trap-error (simple-error \"inner\") (lambda e1 0)) (lambda e2 1))");
        is(1L, "(trap-error (trap-error (simple-error \"inner\") (lambda e1 (simple-error \"outer\"))) (lambda e2 1))");

        is(Symbol.TRUE, "(trap-error (do (pos \"\" 0) false) (lambda E true))");


        // trap-error TCO
//        is(0L, "(defun count-down (x) (trap-error (count-down (- x 1)) (lambda e -1)))",
//               "(count-down 1000)");

//        is(0L, "(defun count-down (x) (trap-error (blah) (lambda e (if (= x 0) x (count-down (- x 1))))))",
//               "(count-down 1000)");
//
//
//        is(0L, "(defun count-down (x) (if (= x 0) x (trap-error (count-down (- x 1)) (lambda e e))))",
//               "(count-down 1000)");

//        is(0L, "(defun count-down (x) (if (= x 0) x (trap-error (count-down (blah)) (lambda e (count-down (- x 1))))))",
//               "(count-down 1000)");
    }

    @Test
    public void defun() {
        is(Symbol.intern("f"), "(defun f (x) x)");

        is(0L, "(defun f () 0)", "(f)");
        is(3L, "(defun f (x y) (+ x y))", "(f 1 2)");
        is(3L, "(defun f (x y) (+ x y))", "((f 1) 2)");

        is(4L, "(defun f (x) (let x 2 (+ x x)))", "(f 1)");

        is(3L, "(defun f (x) (let y 2 (+ x y)))", "(f 1)");

        // redefinition
        is(1L, "(defun f () 1)", "(f)");
        is(2L, "(defun f () 1)", "(defun f () 2)", "(f)");
        is(2L, "(defun f () 1)", "(f)", "(defun f () 2)", "(f)");

        is(1L, "(defun f () (g))", "(defun g () 1)", "(f)");
        is(2L, "(defun f () (g))", "(defun g () 1)", "(defun g () 2)", "(f)");
        is(2L, "(defun f () (g))", "(defun g () 1)", "(f)", "(defun g () 2)", "(f)");
    }

    @Test
    public void invocation() {

        is(1L, "(let f (lambda () 1) (f))");
        
        is(1L, "(defun f () 1)", "(let x f (x))");

        is(1L, "(set f (lambda () 1))", "((value f))");

        is(1L, "(defun f () 1)", "((intern \"f\"))");

        // self recursion
        is(0L, "(defun f (x) (if (= x 0) x (f (- x 1))))", "(f 100000)");

        // nested recursion
        is(0L, "(defun f (x) (if (= x 0) x (f (- x (g 100000)))))",
               "(defun g (x) (if (= x 0) 1 (g (- x 1))))",
               "(f 10)");

        // mutual recursion
        is(0L, "(defun f (x) (if (= x 0) x (g (- x 1))))",
               "(defun g (x) (if (= x 0) x (f (- x 1))))",
               "(f 100000)",
               "(g 100000)");

        // let body is in tail position
        is(0L, "(defun f (x) (if (= x 0) x (let y (- x 1) (f y))))", "(f 100000)");

        // recursive call in let binding
        is(0L, "(defun f (x) (if (= x 0) x (f (- x 1))))", "(let r (f 100000) r)");

        // recursive call in let body
        is(0L, "(defun f (x) (if (= x 0) x (f (- x 1))))", "(let _ 0 (f 100000))");

        // tco
        is(0L, "(defun f (x) (if (= x 0) x ((freeze (f (- x 1))))))", "(f 100000)");

        is(0L, "(defun f (x) (if (= x 0) x ((lambda y (f (- x y))) 1)))", "(f 100000)");

        // TODO?
        //is(0L, "(defun f (x) (trap-error (if (= x 0) x (simple-error \"recur\")) (lambda e (f (- x 1)))))", "(f 100000)");
        // is(0L, "(defun f (x) (trap-error (if (= x 0) x (f (- x 1))) (lambda e -1)))", "(f 10000)");
    }

    // boolean operations

    @Test
    public void if_() {
        is(1L, "(if true 1 2)");
        is(2L, "(if false 1 2)");
        is(1L, "(if true (+ 0 1) (+ 0 2))");
        is(1L, "(if (= 1 1) 1 2)");
        is(2L, "(if (= 1 2) 1 2)");

        is(RuntimeException.class, "(if x 0 1)");
        is(RuntimeException.class, "(if 42 0 1)");

        // can be partially applied
        is(function, "(if true)");
        is(function, "(if true 1)");
        is(1L, "((if true) 1 2)");
        is(2L, "((if false) 1 2)");
    }


    @Test
    public void or() {
        is(Symbol.TRUE, "(or true false)");
        is(Symbol.TRUE, "(or false true)");
        is(Symbol.TRUE, "(or true true)");
        is(Symbol.FALSE, "(or false false)");

        // can be partially applied
        is(function, "(or)");
        is(function, "(or true)");
        is(Symbol.TRUE, ("((or true) false)"));
    }

    @Test
    public void and() {
        is(Symbol.FALSE, "(and true false)");
        is(Symbol.FALSE, "(and false true)");
        is(Symbol.TRUE, "(and true true)");
        is(Symbol.FALSE, "(and false false)");

        // can be partially applied
        is(function, "(and)");
        is(function, "(and true)");
        is(Symbol.FALSE, ("((and true) false)"));
    }

    @Test
    public void cond() {
        is(1L, "(cond (true 1))");
        is(RuntimeException.class, "(cond)");
        is(RuntimeException.class, "(cond (false 1))");
        // is(Cons.EMPTY, "(cond)");
        // is(Cons.EMPTY, "(cond (false 1))");
        is(2L, "(cond (false 1) (true 2))");
        is(3L, "(cond (false 1) (false 2) (true 3))");
        is(2L, "(cond ((if true true true) (+ 1 1)))");
        is(4L, "(cond ((if true false false) (+ 1 1)) ((if true true true) (+ 2 2)))");
    }

    // eval

    @Test
    public void eval() {
        is(Symbol.TRUE, "(eval-kl true)");
        is(Symbol.FALSE, "(eval-kl false)");
        is(1L, "(eval-kl 1)");
        is(1.0, "(eval-kl 1.0)");
        is(Symbol.intern("blahonga"), "(eval-kl blahonga)");
        is(Symbol.TRUE, "(eval-kl true)");
        is(7L, "(eval-kl (cons + (cons 2 (cons 5 ()))))");
        is(Symbol.TRUE, "(eval-kl (cons if (cons true (cons true (cons true ())))))");

        is(42L, "(eval-kl (cons let (cons x (cons 42 (cons x ())))))");
        is(42L, "(let x 42 (eval-kl x))");

        is(0L, "(defun f (x) (if (= x 0) x (f (- x 1))))", "(eval-kl (cons f (cons 100000 ())))");
    }

    // string

    @Test
    public void cn() {
        is("", "(cn \"\" \"\")");
        is("a", "(cn \"a\" \"\")");
        is("ab", "(cn \"a\" \"b\")");
        is("abcdefg", "(cn \"abc\" \"defg\")");
    }

    @Test
    public void n_string() {
        is("\0", "(n->string 0)");
        is("A", "(n->string 65)");

        //ex("(n->string -1)", IllegalArgumentException.class);
    }

    @Test
    public void pos() {
        is(equalTo("a"), "(pos \"abcdef\" 0)");
        is(equalTo("e"), "(pos \"abcdef\" 4)");

        is(StringIndexOutOfBoundsException.class, "(pos \"\" 0)");
    }

    @Test
    public void stringQ() {
        is(Symbol.TRUE, "(string? \"\")");
        is(Symbol.TRUE, "(string? \"blahonga\")");
        is(Symbol.FALSE, "(string? 0)");
        is(Symbol.FALSE, "(string? blahonga)");
        is(Symbol.FALSE, "(string? (cons a b))");
    }

    @Test
    public void str() {
        is("", "(str \"\")");
    }

    @Test
    public void string_n() {
        // TODO: is("(string->n \"\")", );
        is(97L, "(string->n \"a\")");
        is(100L, "(string->n \"d\")");
    }

    @Test
    public void tlstr() {
        // TODO: is("(tlstr \"\")", "");
        // TODO: is("(tlstr \"b\")", "");
        is("lahonga", "(tlstr \"blahonga\")");
    }

    @Test
    public void addition() {
        is(5L, "(+ 2 3)");
        is(5.0, "(+ 2 3.0)");
        is(5.0, "(+ 2.0 3)");
        is(5.0, "(+ 2.0 3.0)");
    }

    @Test
    public void subtraction() {
        is(3L, "(- 5 2)");
        is(3.0, "(- 5.0 2.0)");
    }

    @Test
    public void multiplication() {
        is(10L, "(* 5 2)");
        is(10.0, "(* 5.0 2.0)");
    }

    @Test
    public void division() {
        is(2.5, "(/ 5 2)");
        is(2.5, "(/ 5.0 2.0)");
    }

    @Test
    public void numberQ() {
        is(Symbol.TRUE, "(number? 0)");
        is(Symbol.TRUE, "(number? 0.0)");
        is(Symbol.FALSE, "(number? foo)");
        is(Symbol.FALSE, "(number? \"foo\")");
        is(Symbol.FALSE, "(number? (cons 1 2))");
    }

    @Test
    public void absvector() {
        is(allOf(instanceOf(Object[].class), arrayWithSize(0)), "(absvector 0)");
        is(allOf(instanceOf(Object[].class), arrayWithSize(1000)), "(absvector 1000)");

        is(Cons.EMPTY, "(<-address (absvector 1) 0)");

        is("blahonga", "(<-address (address-> (absvector 1) 0 \"blahonga\") 0)");
        is(Cons.EMPTY, "(<-address (address-> (absvector 2) 0 \"blahonga\") 1)");
    }



    @Test
    public void intern() {
        // TODO: is("(intern \"\")", Symbol.intern("a"));
        is(Symbol.intern("a"), "(intern \"a\")");
        is(Symbol.intern("blahonga"), "(intern \"blahonga\")");
    }


    // list

    @Test
    public void cons() {
        is(equalTo(new Cons(1L, 2L)), "(cons 1 2)");
        is(equalTo(new Cons(1L, new Cons(2L, 3L))), "(cons 1 (cons 2 3))");
        is(equalTo(new Cons(1L, Cons.EMPTY)), "(cons 1 ())");
    }

    @Test
    public void hd() {
        is(Cons.EMPTY, "(hd ())");
        is(1L, "(hd (cons 1 2))");
    }

    @Test
    public void tl() {
        is(Cons.EMPTY, "(tl ())");
        is(2L, "(tl (cons 1 2))");
        is(new Cons(2L, 3L), "(tl (cons 1 (cons 2 3)))");
        is(3L, "(tl (tl (cons 1 (cons 2 3))))");

        is(RuntimeException.class, "(tl x)");
    }


    @Test
    public void getTime() {
        is(within(1.0, System.currentTimeMillis() / 1000.0), "(get-time unix)");
    }

    private static Matcher function = instanceOf(Function.class);

    private static Matcher within(double epsilon, double value) {
        return new BaseMatcher() {
            @Override
            public void describeTo(Description description) {
                description.appendText("within ")
                        .appendText(Double.toString(epsilon))
                        .appendText(" of ")
                        .appendText(Double.toString(value));
            }

            @Override
            public boolean matches(Object item) {
                return item instanceof Double && Math.abs(((Double)item) - value) < epsilon;
            }
        };
    }


    private void is(Matcher matcher, String... exprs) {
        KLambda kl = new KLambda();

        Object ret = null;
        for(String expr : exprs) {
            ret = kl.eval(expr);
        }
        assertThat(ret, matcher);
    }


    private void is(Object expected, String... exprs) {
        KLambda kl = new KLambda();

        Object ret = null;
        for(String expr : exprs) {
            ret = kl.eval(expr);
        }
        assertEquals(expected, ret);
    }

    private void is(Class expected, String code) {
        KLambda kl = new KLambda();

        try {
            Object ret = kl.eval(code);
            fail("Expected " + expected + " to be thrown but returned " + ret);
        } catch (Exception e) {
            assertThat(e, instanceOf(expected));
        }
    }

}
