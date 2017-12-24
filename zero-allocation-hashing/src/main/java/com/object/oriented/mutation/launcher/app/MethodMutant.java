package com.object.oriented.mutation.launcher.app;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;

import java.io.IOException;
import java.util.logging.Logger;

public class MethodMutant {
    private static Logger logger = Logger.getLogger("AccessModifierMutant");

    /**
     * Takes an instance of CtClass and renames all the methods in it
     * @param ctClass
     */
    public static void mutate(CtClass ctClass) {
        ctClass.defrost();
        CtMethod[] methods = ctClass.getDeclaredMethods();
        for(CtMethod method : methods){
            logger.info("Renaming method: "+method.getName());
            method.setName("_"+method.getName());
        }
        try {
            ctClass.writeFile("out/production/classes");
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
