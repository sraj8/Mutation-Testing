package com.object.oriented.mutation.launcher.app;


import eclipse.ast.parser.InstrumentationTemplate;
import javassist.ClassPool;
import net.openhft.hashing.LaunchTestCases;
import org.junit.runner.notification.Failure;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;

import static com.object.oriented.mutation.launcher.app.ConfigFileLoader.loadVarRegex;

public class Launcher {

    private  String mutationToApply;

    private static Logger logger = Logger.getLogger("Launcher");

    Launcher(String mutationToApply){
        this.mutationToApply = mutationToApply;

    }



    public  synchronized void launch() {
        logger.info("<< launch");
        ClassPool pool = ClassPool.getDefault();

        //read configuration file - includes test cases to be run and inputs
        readConfigFile();

        //Runs parser to insert logging statement in classes
        runParser();

        runTestCaseToGenerateTrace();

        createTraceBackup();


       // System.setOut(new PrintStream(System.out));

        //Runs all test cases to generate trace file from original app
        //runTestCaseToGenerateTrace();

        //read trace file and generate mutation table
        Map<String,List<String>> mutateExpressionMap = readTraceAndGenerateMutationTable();



        TransformBytecode transformBytecode = new TransformBytecode(mutationToApply);
        if(mutateExpressionMap!=null && !(mutateExpressionMap.isEmpty()))
            transformBytecode.transform(mutateExpressionMap,pool);






       // ClassPool pool = ClassPool.getDefault();
       /* AccessModifierMutant modifierMutant = new AccessModifierMutant(pool);
        modifierMutant.mutate("net.openhft.hashing.CityAndFarmHash_1_1");*/

        loadAllClasses();

       List<Failure> failures =  runTestCaseToGenerateTrace();

        traceFileComparator();

        createTraceBackupForMutations(failures);

        recompileUnits(mutateExpressionMap.keySet().toArray(new String[mutateExpressionMap.size()]));

        logger.info(">> launch");

    }

    private synchronized void recompileUnits(String[] files) {

        logger.info("<< recompileUnits");

        logger.info("Recompiling files after "+mutationToApply+" mutation");

        JavaCompilerUnit javaCompilerUnit = new JavaCompilerUnit();
        javaCompilerUnit.compileFiles(files);

        logger.info(">> recompileUnits");

    }

    public static void loadAllClasses() {
        File file = new File("out"+File.separator+"production"+File.separator+"classes"+File.separator+"net"+File.separator+"openhft"+File.separator+"hashing"+File.separator);
        URL url = null;
        try {
            url = file.toURI().toURL();
            URL[] urls = new URL[]{ url };
            URLClassLoader classLoader = new URLClassLoader(urls);
            Class.forName("net.openhft.hashing.UnsafeAccess", true, classLoader);
            Class.forName("net.openhft.hashing.LongHashFunction",true, classLoader);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private  synchronized void traceFileComparator() {
        logger.info("<< traceFileComparator");
        try {
            TraceFileComparator.hashCodeComparator(mutationToApply);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //TraceFileComparator.showDifference();

        logger.info(">> traceFileComparator");
    }

    private synchronized  void createTraceBackup() {
        /*logger.info("<< createTraceBackup");
        String filePath = "TraceFile.txt";
        File fileToBackup = new File(filePath);
        filePath = "UnMutatedTraceFile.txt";
        File newFile = new File(filePath);
        fileToBackup.renameTo(newFile);
        InstrumentationTemplate.counter = 0;
        logger.info(">> createTraceBackup");*/

        Path source = Paths.get("TraceFile.txt");
        Path target = Paths.get("UnMutatedTraceFile"+mutationToApply+".txt");
        try {
            Files.copy(source, target);
            InstrumentationTemplate.counter = 0;
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }


    private synchronized  void createTraceBackupForMutations(List<Failure> failures) {
        logger.info("<< createTraceBackupForMutations");

        if(failures.isEmpty()) {

            String filePath = "TraceFile.txt";
            File fileToBackup = new File(filePath);
            filePath = "MutatedTraceFile-" + mutationToApply + ".txt";
            File newFile = new File(filePath);
            fileToBackup.renameTo(newFile);
            InstrumentationTemplate.counter = 0;
        }else{
            //write failures
            logger.info("Failures detected post mutation.");
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter("MutatedTraceFile-" + mutationToApply + ".txt"));

                for(Failure failure : failures){
                    out.write(failure.toString());
                }
                out.close();
                logger.info("Created file "+"MutatedTraceFile-" + mutationToApply + ".txt"+" to record failures");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        logger.info(">> createTraceBackupForMutations");
    }



    private  synchronized void readConfigFile() {
        logger.info("<< readConfigFile");
        logger.info("Reading config file...");
        //loadConfig();
        loadVarRegex();
        logger.info(">> readConfigFile");
    }

    private  void runParser(){

    }

    private synchronized  List<Failure>  runTestCaseToGenerateTrace(){
        logger.info("<< runTestCaseToGenerateTrace");
        logger.info("Running test cases....");
       List<Failure> failures = LaunchTestCases.launchTest();
        logger.info(">> runTestCaseToGenerateTrace");
       return failures;
    }

    private synchronized  Map<String,List<String>> readTraceAndGenerateMutationTable(){
        //input - trace file
        //output - mutation map
        logger.info("<< readTraceAndGenerateMutationTable");
        String filePath = "UnMutatedTraceFile"+mutationToApply+".txt";
        Map<String,List<String>> mutateExpressionMap= readTraceFile(filePath);
        logger.info(">> readTraceAndGenerateMutationTable");
        return mutateExpressionMap;

    }

    private synchronized Map<String,List<String>> readTraceFile(String filePath){
        logger.info("<< readTraceFile");
        logger.info("Read Trace file and create mutation map");
        Map<String,List<String>> mutateExpressionMap = new HashMap();
        try  {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
                String line;
                while ((line = br.readLine()) != null) {
                    // process the line.
                    String[] lineAnalyzer = Arrays.stream(line.split("\\|")).map(String::trim).toArray(String[]::new);
                     createMap(lineAnalyzer[1],lineAnalyzer[2],lineAnalyzer[3].substring(0,lineAnalyzer[3].indexOf(".java")),lineAnalyzer[4],mutateExpressionMap);
                }


        }catch (IOException e){
            e.printStackTrace();
        }
        logger.info(">> readTraceFile");
        return mutateExpressionMap;
    }

    private  synchronized void createMap(String lineNumber, String expression, String fileName, String operation,Map<String, List<String>> mutateExpressionMap) {




        if(!mutateExpressionMap.containsKey(fileName)){
            mutateExpressionMap.put(fileName,new ArrayList<>());
        }

        if(operation.equalsIgnoreCase("ASSIGNMENT")){
            if(checkIfAssignmentIncludeArithmetic(expression)){
                if(checkIfArithmeticShortcut(expression)){
                    //mutation table for shorthand arithmetic
                    if(expression.contains("++")){

                    }else if(expression.contains("--")){

                    }


                }else{
                    //mutation table for arithmetic - can include shorthand assignment - check here
                    if(isShorthandAssignment(expression)){
                        //short hand assignment with arithmetic

                    }else{
                        //normal assignment with normal arithmetic
                      List<String> mutationsToBePerformed = mutateExpressionMap.get(fileName);
                      if(!mutationsToBePerformed.contains("arithmetic"))
                         mutationsToBePerformed.add("arithmetic");
                    }
                }

            }
        }



    }

    private  boolean checkIfAssignmentIncludeArithmetic(String expression) {
        return expression.contains("+") || expression.contains("-") || expression.contains("*")
                || expression.contains("/") || expression.contains("%") ;
    }

    private  boolean checkIfArithmeticShortcut(String expression){
        return expression.contains("++") || expression.contains("--")  ;
    }

    private  boolean isShorthandAssignment(String expression){
        return expression.contains("+=") || expression.contains("-=") || expression.contains("*=")
                || expression.contains("/=") || expression.contains("%=");
    }

}
