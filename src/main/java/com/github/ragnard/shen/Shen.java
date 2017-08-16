package com.github.ragnard.shen;


import com.github.ragnard.shen.klambda.Language;
import com.oracle.truffle.api.source.Source;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Shen {

    private static String[] SOURCES = new String[]{
            "toplevel.kl",
            "core.kl",
            "sys.kl",
            "sequent.kl",
            "yacc.kl",
            "reader.kl",
            "prolog.kl",
            "track.kl",
            "load.kl",
            "writer.kl",
            "macros.kl",
            "declarations.kl",
            "types.kl",
            "t-star.kl",
    };

    private final KLambda kl;

    public Shen() {
        kl = new KLambda(System.in, System.out);

        for (String sourceName : SOURCES) {
            System.out.println("Loading " + sourceName);
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(Shen.class.getResourceAsStream("/klambda/" + sourceName)))) {
                Source source = Source.newBuilder(reader)
                        .name(sourceName)
                        .mimeType(Language.MIME_TYPE)
                        .build();

                kl.eval(source);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (RuntimeException e) {
                System.out.println("UGH: " + e);
                e.printStackTrace();
                throw e;
            }
        }
    }

    public Object eval(String expr) {
        return kl.eval(expr);
    }

    public static void main(String[] args) {

        Shen shen = new Shen();

        boolean startToplevel = true;

        for(int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-v":
                case "--version":
                    System.out.println("version");
                    System.exit(0);
                case "-h":
                case "--help":
                    System.out.println("help");
                    System.exit(0);
                case "-e":
                case "--eval":
                    if (i+1 >= args.length) {
                        System.out.println("missing argument to --eval");
                        System.exit(1);
                    }
                    startToplevel = false;
                    shen.eval(args[i+1]);
                    i += 1;
                    continue;
                case "-l":
                case "--load":
                    if (i+1 >= args.length) {
                        System.out.println("missing argument to --load");
                        System.exit(1);
                    }
                    startToplevel = false;
                    shen.eval(String.format("(load \"%s\")", args[i+1]));
                    i += 1;
                    continue;
            }

        }

        if(startToplevel) {
            shen.eval("(shen.shen)");
        }

    }

    private static void usage() {
        System.out.println("usage");
//        "Shen 20.1\n" +
//                "Shen CL 2.1\n" +
//                "\n" +
//                "Usage: truffleshen [OPTIONS...]\n" +
//                "  -v, --version       : Prints Shen, shen-cl version numbers and exits\n" +
//                "  -h, --help          : Shows this help and exits\n" +
//                "  -e, --eval <expr>   : Evaluates expr and prints result\n" +
//                "  -l, --load <file>   : Reads and evaluates file\n" +
//                "\n" +
//                "Evaluates options in order\n" +
//                "Starts the REPL if no eval/load options specified"
    }
}
