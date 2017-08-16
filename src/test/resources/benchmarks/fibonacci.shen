(define fib
  N -> (fib-help 0 1 N))

(define fib-help
  A B 0 -> A
  A B N -> (fib-help B (+ A B) (- N 1)))

(define fib-slow
  0 -> 1
  1 -> 1
  N -> (+ (fib-slow (- N 1)) (fib-slow (- N 2))))

(time (fib-slow 40))