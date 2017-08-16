(report prolog-tests
   (load "prolog.shen") loaded
   (prolog? (f a)) true
   (prolog? (g a)) false
   (prolog? (g b)) true
   (prolog? (mem 1 [X | 2]) (return X)) 1
   (prolog? (rev [1 2] X) (return X)) [2 1]
   (load "einstein.shen") loaded
   (prolog? (einsteins_riddle X) (return X)) german
   (prolog? (enjoys mark X) (return X)) chocolate
   (prolog? (fads mark)) [tea chocolate]
   (prolog? (prop [] [p <=> p]))  true
   (prolog? (mapit consit [1 2 3] Out) (return Out)) [[1 1] [1 2] [1 3]]
   (prolog? (different a b)) true
   (prolog? (different a a)) false
   (prolog? (likes john Who) (return Who)) mary
   (load "parse.prl") loaded
   (prolog? (pparse ["the" + ["boy" + "jumps"]]
                        [[s = [np + vp]]
                          [np = [det + n]]
                          [det = "the"]
                          [n = "girl"]
                          [n = "boy"]
                          [vp = vintrans]
                          [vp = [vtrans + np]]
                          [vintrans = "jumps"]
                          [vtrans = "likes"]
                          [vtrans = "loves"]])) true)


(reset)