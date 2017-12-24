package com.object.oriented.mutation.launcher.app;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class InheritanceMutant {

    private static final Logger logger = Logger.getLogger("InheritanceMutant");

    /**
     * Takes instance of the CtClass and removes all the hidden variables from the nestedClasses
     * @param ctClass
     */
    public static void mutate(CtClass ctClass){
        ctClass.defrost();
        try {
            CtField[] fields = ctClass.getDeclaredFields();
            List<String> fieldNames = getFieldNames(fields);
            for(CtClass nestedClass : ctClass.getNestedClasses()){
                logger.info("working of nested class:" + nestedClass.getName());
                deleteHiddenVariable(nestedClass,fieldNames);
            }
            ctClass.writeFile("out/production/classes");
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Takes in the field names of the outer class and field names of all the inner classes and returns a list of matched
     * fields which are to be deleted
     * @param fieldNames
     * @param nestedFieldNames
     * @return List<String> matchedFields
     */
    private static List<String> getMatchedFields(List<String> fieldNames, List<String> nestedFieldNames) {
        List<String> matchedFields = new ArrayList<>();
        for(String fieldName : fieldNames){
            if(nestedFieldNames.contains(fieldName)){
                matchedFields.add(fieldName);
            }
        }
        return matchedFields;
    }

    private static List<String> getFieldNames(CtField[] fields) {
        List<String> fieldNames = new ArrayList<>();
        for(CtField ctField: fields){
            fieldNames.add(ctField.getName());
        }
        return fieldNames;
    }

    /**
     * Takes an instance and CtClass and the list of fields to delete and then deletes them from the class
     * @param nestedClass
     * @param fieldNames
     * @throws NotFoundException
     * @throws CannotCompileException
     * @throws IOException
     */
    private static void deleteHiddenVariable(CtClass nestedClass, List<String> fieldNames) throws NotFoundException, CannotCompileException, IOException {
        CtField[] nestedFields = nestedClass.getDeclaredFields();
        List<String> nestedFieldNames = getFieldNames(nestedFields);
        List<String> matchedFields = getMatchedFields(fieldNames,nestedFieldNames);
        if(matchedFields.size()>0){
            for(String matchedField : matchedFields){
                logger.info("Removing field : " + matchedField);
                nestedClass.removeField(nestedClass.getField(matchedField));
                nestedClass.writeFile("out/production/classes");
            }
        }
    }




}
