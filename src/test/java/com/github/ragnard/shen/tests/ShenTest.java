package com.github.ragnard.shen.tests;

import com.github.ragnard.shen.tests.categories.Integration;
import com.github.ragnard.shen.Shen;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(Integration.class)
public class ShenTest {

    @Test
    public void run() {
        Shen shen = new Shen();
        shen.eval("(cd \"src/test/resources/test-programs\")");
        shen.eval("(load \"README.shen\")");
        shen.eval("(load \"tests.shen\")");
    }
}
