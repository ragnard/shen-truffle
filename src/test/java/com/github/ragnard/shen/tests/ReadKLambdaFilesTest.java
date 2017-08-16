package com.github.ragnard.shen.tests;

import com.github.ragnard.shen.tests.categories.Unit;
import com.github.ragnard.shen.klambda.Reader;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

@Category(Unit.class)
@RunWith(Parameterized.class)
public class ReadKLambdaFilesTest {

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Iterable<File> files() throws IOException {
        return Files.walk(Paths.get("src/main/resources/KLambda"))
                .map(p -> p.toFile())
                .filter(f -> f.isFile())
                .collect(Collectors.toList());
    }

    private File file;

    public ReadKLambdaFilesTest(File file) {
        this.file = file;
    }

    @Test
    public void testReadKLambda() throws IOException {
        try(PushbackReader r = new PushbackReader(new BufferedReader(new FileReader(this.file)))) {
            List forms = Reader.readAll(r);
            assertTrue(forms.size() > 0);
        }
    }
}
