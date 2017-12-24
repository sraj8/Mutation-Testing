package com.object.oriented.mutation.launcher.app;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.Opcode;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class TransformBytecode {

    private String mutationToApply;

    private static Logger logger = Logger.getLogger("TransformBytecode");

    TransformBytecode(String mutationToApply){
        this.mutationToApply = mutationToApply;
    }


   public synchronized void transform(Map<String,List<String>> mutateExpressionMap,ClassPool pool){

       for(String fileName: mutateExpressionMap.keySet()){
           try {
               applyMutation(fileName,mutateExpressionMap.get(fileName),pool);
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
   }


   private void applyMutation(String fileName,List<String> operationsToMutate,ClassPool pool)throws Exception{

       //pool.insertClassPath(new ClassClassPath(TransformBytecode.class));

       logger.info("Applying "+mutationToApply+" mutation...");


       CtClass ctClass = pool.get("net.openhft.hashing."+fileName);
       CtMethod[] methods = ctClass.getDeclaredMethods();
       for(CtMethod method : methods){
            ctClass.defrost();
            if(operationsToMutate != null && !(operationsToMutate.isEmpty()))
               mutateMethodCode(method,ctClass,operationsToMutate);

       }

      // ctClass.detach();
        if(mutationToApply.equalsIgnoreCase("AccessModifier")) {

            applyAccessModifierMutatnt(ctClass);
        }

       if(mutationToApply.equalsIgnoreCase("Inheritence")) {


           InheritanceMutant.mutate(ctClass);
       }
       if(mutationToApply.equalsIgnoreCase("MethodMutant")) {

           MethodMutant.mutate(ctClass);
       }
       if(mutationToApply.equalsIgnoreCase("MethodInvocation")) {

           MethodInvocationMutant.mutate(ctClass);
       }



   }

    private void applyAccessModifierMutatnt(CtClass ctClass) {

        AccessModifierMutant accessModifierMutant = new AccessModifierMutant();
        accessModifierMutant.mutate(ctClass);

    }


    private synchronized void mutateMethodCode(CtMethod ctMethod,CtClass ctClass,List<String> operationsToMutate) {

        try {


            CodeAttribute _codeAttribute = ctMethod.getMethodInfo().getCodeAttribute();
            CodeIterator _codeIterator = _codeAttribute.iterator();

            while (_codeIterator.hasNext()) {
                int _indexOfCode = _codeIterator.next();
                int _valueOfIndex8Bit = _codeIterator.byteAt(_indexOfCode);


                if(mutationToApply.equals("Conditional")) {
                    //Checking index 6 and if Opcode is ifeq
                    if (Opcode.IFEQ == _valueOfIndex8Bit) {
                        //Changing instruction from ifeq to ifne
                        _codeIterator.writeByte(Opcode.IFNE, _indexOfCode);


                    }

                    else if(Opcode.IFLE == _valueOfIndex8Bit){
                        //chnaging LE to GE
                        _codeIterator.writeByte(Opcode.IFGE,_indexOfCode);
                    }

                    else if(Opcode.IF_ICMPLE == _valueOfIndex8Bit){
                        _codeIterator.writeByte(Opcode.IF_ICMPGE,_indexOfCode);
                    }

                    else if(Opcode.IF_ICMPGE == _valueOfIndex8Bit){
                        _codeIterator.writeByte(Opcode.IF_ICMPLE,_indexOfCode);
                    }

                    else if(Opcode.IFGT == _valueOfIndex8Bit){
                        _codeIterator.writeByte(Opcode.IFLT,_indexOfCode);
                    }

                    else if(Opcode.IFLT == _valueOfIndex8Bit){
                        _codeIterator.writeByte(Opcode.IFGT,_indexOfCode);
                    }

                    else if(Opcode.IFGE == _valueOfIndex8Bit){
                        _codeIterator.writeByte(Opcode.IFLE,_indexOfCode);
                    }

                    else if(Opcode.IF_ICMPGT == _valueOfIndex8Bit){
                        _codeIterator.writeByte(Opcode.IF_ICMPLT,_indexOfCode);
                    }

                    else if(Opcode.IF_ICMPLT == _valueOfIndex8Bit){
                        _codeIterator.writeByte(Opcode.IF_ICMPGT,_indexOfCode);
                    }



                }



                if(mutationToApply.equalsIgnoreCase("Arithmetic")) {
                    if (operationsToMutate.contains("arithmetic")) {

                        AdditionMutation(_codeIterator, _indexOfCode, _valueOfIndex8Bit);
                        SubtractionMutation(_codeIterator, _indexOfCode, _valueOfIndex8Bit);










                    }






                }


            }

            ctClass.writeFile("out/production/classes");


        }catch (Exception e){
           e.printStackTrace();
        }


    }

    private static void AdditionMutation(CodeIterator _codeIterator, int _indexOfCode, int _valueOfIndex8Bit) {
        if (Opcode.IADD == _valueOfIndex8Bit) {
            _codeIterator.writeByte(Opcode.ISUB, _indexOfCode);
        }

        else if (Opcode.DADD == _valueOfIndex8Bit) {
            _codeIterator.writeByte(Opcode.DSUB, _indexOfCode);
        }

        else if (Opcode.FADD == _valueOfIndex8Bit) {
            _codeIterator.writeByte(Opcode.FSUB, _indexOfCode);
        }

       else if (Opcode.LADD == _valueOfIndex8Bit) {
            _codeIterator.writeByte(Opcode.LSUB, _indexOfCode);
        }
    }

    private static void SubtractionMutation(CodeIterator _codeIterator, int _indexOfCode, int _valueOfIndex8Bit) {
        if (Opcode.ISUB == _valueOfIndex8Bit) {
            _codeIterator.writeByte(Opcode.IADD, _indexOfCode);
        }

        else if (Opcode.DSUB == _valueOfIndex8Bit) {
            _codeIterator.writeByte(Opcode.DADD, _indexOfCode);
        }

        else if (Opcode.FSUB == _valueOfIndex8Bit) {
            _codeIterator.writeByte(Opcode.FADD, _indexOfCode);
        }

        else if (Opcode.LSUB == _valueOfIndex8Bit) {
            _codeIterator.writeByte(Opcode.LADD, _indexOfCode);
        }
    }


}
