package com.object.oriented.mutation.launcher.app;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.util.Arrays;

public class JavaCompilerUnit {


    public boolean compileFiles(String[] files){

        files = appendJava(files);
        String[] options = new String[] { "-d", "out/production/classes" };

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

        Iterable<? extends JavaFileObject> compilationUnits1 =
                fileManager.getJavaFileObjectsFromStrings(Arrays.asList(files));
        compiler.getTask(null, fileManager, null, Arrays.asList(options), null, compilationUnits1).call();

        try {
            fileManager.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    private String[] appendJava(String[] files) {
        for(int i=0;i<files.length;i++){
            files[i] = "src/main/java/net/openhft/hashing/"+files[i]+".java";
        }
        return files;
    }

}
