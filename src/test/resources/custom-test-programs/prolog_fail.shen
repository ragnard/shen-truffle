(defprolog foobar
  foo <--;)

(defprolog foobars
  X <-- (findall X [foobar X] Foobars) (return Foobars);)

(prolog? (findall X [foobar X] Foobars))

(prolog? (findall X [foobar X] Foobars) (return Foobars)


