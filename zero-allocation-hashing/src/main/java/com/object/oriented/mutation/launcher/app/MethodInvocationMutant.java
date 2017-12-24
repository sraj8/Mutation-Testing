package com.object.oriented.mutation.launcher.app;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import org.apache.commons.lang.RandomStringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class MethodInvocationMutant {
   public static final Logger logger = Logger.getLogger("MethodInvocationMutant");

    /**
     * Mutate Function takes an instance of CtClass finds it subclasses and mutates the overridden methods from the
     * subclass
     * @param ctClass
     */
    public static void mutate(CtClass ctClass){
        ctClass.defrost();
        CtMethod[] methods = ctClass.getDeclaredMethods();
        List<CtClass> subClasses = getSubClasses(ctClass);
        logger.info("Getting sub classes to mutate for:"+ctClass.getName());
        for(CtClass subClass : subClasses){
            try {
                subClass.defrost();
                replaceSubClassMethod(subClass, methods);
            } catch (NotFoundException e) {
                e.printStackTrace();
            }

        }
        try {
            for(CtClass nestedClass : ctClass.getNestedClasses()){
                logger.info("Getting subclasses to mutate for the nested class:"+ nestedClass.getName());
                CtMethod[] methods1 = nestedClass.getDeclaredMethods();
                List<CtClass> subClasses1 = getSubClasses(nestedClass);
                for(CtClass subClass : subClasses1){
                    subClass.defrost();
                    replaceSubClassMethod(subClass, methods1);
                }

            }
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Takes the CtClass instance and returns a list of all of its subclasses
     * @param ctClass
     * @return List<CtClass> subClasses
     */
    private static List<CtClass> getSubClasses(CtClass ctClass){
        List<CtClass> subClasses = new ArrayList<>();
        try {
            CtClass[] nestedClasses = ctClass.getNestedClasses();
            for(CtClass nestedClass : nestedClasses){
                if(nestedClass.getSuperclass().equals(ctClass)){
                    subClasses.add(nestedClass);
                }
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return subClasses;
    }

    /**
     * Takes CtClass instance of subclass and list of methods from the supeclass to identify and mutate the Overridden methods
     * @param ctClass
     * @param methods
     * @throws NotFoundException
     */
    private static void replaceSubClassMethod(CtClass ctClass,CtMethod[] methods) throws NotFoundException {
        ctClass.defrost();
        List<CtMethod> subClassMethods = Arrays.asList(ctClass.getDeclaredMethods());
        for(CtMethod method : methods){
            if(subClassMethods.contains(method)){
                logger.info("Mutating the method: "+method.getName()+" for class: "+ctClass.getName());
                CtMethod methodtoReplace = ctClass.getDeclaredMethod(method.getName());
                ctClass.removeMethod(methodtoReplace);
                String name = methodtoReplace.getName();
                String returnType = methodtoReplace.getReturnType().getName();
                CtClass[] paramTypes = methodtoReplace.getParameterTypes();
                StringBuilder builder = new StringBuilder();
                builder.append("public ").append(returnType+" ").append(name+"(");
                String[] params = new String[paramTypes.length];
                for(int i=0;i<params.length;i++){
                    params[i] = RandomStringUtils.random(1,true,false);
                }
                for(int i=0;i<paramTypes.length;i++){
                    if(i==paramTypes.length-1){
                        builder.append(paramTypes[i].getName()+" ").append(params[i]);
                    }else{
                        builder.append(paramTypes[i].getName()+" ").append(params[i]+", ");
                    }

                }
                builder.append("){");
                if(returnType.equals("void")){
                    builder.append("\n super."+name+"(");
                }else{
                    builder.append("\n return super."+name+"(");
                }

                for(int i=0;i<params.length;i++){
                    if(i==params.length-1){
                        builder.append(params[i]);
                    }else{
                        builder.append(params[i]+",");
                    }
                }
                builder.append(");\n }");
                try {
                    CtMethod newMethod = CtMethod.make(builder.toString(),ctClass);
                    logger.info("Adding mutated method: "+builder.toString());
                    ctClass.addMethod(newMethod);
                    ctClass.writeFile("out/production/classes");
                } catch (CannotCompileException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    ctClass.defrost();
                }
            }
        }
    }


}
