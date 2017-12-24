package eclipse.ast.parser;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class InstrumentationTemplate {

    public static int counter = 0;
    static PrintStream out;

    public static void instrum(String expression) {
        Class<?> systemClass = null;
        try {
            systemClass = Class.forName("java.lang.System");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (counter == 0)
                initializeTrace();
            System.setOut(out);
            Field outField = systemClass.getDeclaredField("out");
            Class<?> printStreamClass = outField.getType();
            Method printlnMethod = printStreamClass.getDeclaredMethod("println", String.class);
            Object object = outField.get(null);
            printlnMethod.invoke(object, expression);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    private static void initializeTrace(){
            try {
                out = new PrintStream("TraceFile.txt");
                counter ++;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
    }
}
