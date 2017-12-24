package com.object.oriented.mutation.launcher.app;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TraceFileComparator {

    static int counter = 0;
    static PrintStream out;


    static void hashCodeComparator(String mutationToApply) throws IOException {
        try {
            Class<?> systemClass = null;
            try {
                systemClass = Class.forName("java.lang.System");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            String currLine;
            Set<Integer> hashCodes = new HashSet<>();
            List<String> list1 = new ArrayList<String>();
            BufferedReader br1 = new BufferedReader(new FileReader("UnMutatedTraceFile"+mutationToApply+".txt"));
            BufferedReader br2 = new BufferedReader(new FileReader("TraceFile.txt"));
            if (counter == 0)
                initializeTrace(mutationToApply);
            System.setOut(out);
            Field outField = systemClass.getDeclaredField("out");
            Class<?> printStreamClass = outField.getType();
            Method printlnMethod = printStreamClass.getDeclaredMethod("println", String.class);
            Object object = outField.get(null);
            while ((currLine = br1.readLine()) != null) {
                list1.add(currLine);
                hashCodes.add(currLine.hashCode());
            }
            while ((currLine = br2.readLine()) != null) {
               if(!hashCodes.contains(currLine.hashCode()));
                    printlnMethod.invoke(object, currLine);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }finally {
            counter = 0;
        }
    }

    private static void initializeTrace(String mutationToApply){
        try {
            out = new PrintStream("TraceFileDifference.txt");
            counter ++;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
