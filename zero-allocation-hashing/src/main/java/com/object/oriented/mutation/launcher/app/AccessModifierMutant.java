package com.object.oriented.mutation.launcher.app;

import javassist.*;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.logging.Logger;

public class AccessModifierMutant {

    private static Logger logger = Logger.getLogger("AccessModifierMutant");

    /**
     * Takes the CtClass instance and mutates the access modifiers for all the fields declared in the class
     * @param ctClass
     */
    public static void mutate(CtClass ctClass){
        try {
			ctClass.defrost();            logger.info("Getting all fields for:" + ctClass.getName());            CtField[] fields = ctClass.getDeclaredFields();
            for(CtField field : fields){
                logger.info("Mutating field: "+field.getName());
                field.setModifiers(Modifier.PUBLIC+Modifier.STATIC);
            }
            ctClass.writeFile("out/production/classes");
            ctClass.defrost();

        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
