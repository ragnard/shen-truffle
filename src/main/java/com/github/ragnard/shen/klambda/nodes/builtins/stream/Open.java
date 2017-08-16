package com.github.ragnard.shen.klambda.nodes.builtins.stream;

import com.github.ragnard.shen.klambda.nodes.builtins.BuiltinNode;
import com.github.ragnard.shen.klambda.runtime.Symbol;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

import java.io.*;

@NodeInfo(shortName = "open")
public abstract class Open extends BuiltinNode {
    @Specialization(rewriteOn = FrameSlotTypeException.class)
    @CompilerDirectives.TruffleBoundary
    public Closeable open(String path, Symbol direction) throws FrameSlotTypeException {
        File file = new File(path);
        if (!file.isAbsolute()) {
            MaterializedFrame globals = this.getContext().getGlobalFrame();
            FrameSlot homeDirectorySlot = globals.getFrameDescriptor().findFrameSlot("*home-directory*");
            String homeDirectory = (String) globals.getObject(homeDirectorySlot);
            file = new File(homeDirectory, path);
            //throw new RuntimeException("not implemented");
        }

        //System.out.println(file.getAbsoluteFile());

        try {
            switch (direction.getName()) {
                case "in":
                    return new BufferedInputStream(new FileInputStream(file));
                case "out":
                    return new BufferedOutputStream(new FileOutputStream(file));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("file not found:" + e);
        }

        throw new IllegalArgumentException("invalid direction");
    }
}
