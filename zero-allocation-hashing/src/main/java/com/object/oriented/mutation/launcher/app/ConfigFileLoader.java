package com.object.oriented.mutation.launcher.app;

import com.mifmif.common.regex.Generex;
import net.openhft.hashing.City64_1_1_Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigFileLoader {
    private static String regex;

    static String loadConfigRegex(String regex){
        Generex generex = new Generex(regex);
        return generex.random();

    }

    static void loadVarRegex(){
        /* BufferedReader reader = new BufferedReader(new FileReader("TraceFile"));
         String var;
         while ((var = reader.readLine()) != null) {
             StringTokenizer token = new StringTokenizer(var, " | ", true);
             while (token.hasMoreTokens()) {
                 String nexttoken = token.nextToken();
                 if ("".contains(nexttoken)) {
                 }
             }
         }*/
        Field[] fields = City64_1_1_Test.class.getFields();
        for(Field field : fields){
            getRegexType(field.getName());
        }
    }

    static Map loadConfig(){
        HashMap <String,String> config = new HashMap<>();
        try {
            File file = new File("config/Config.properties");
            FileInputStream fileInput = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(fileInput);
            fileInput.close();

            Enumeration enuKeys = properties.keys();
            while (enuKeys.hasMoreElements()) {
                String key = (String) enuKeys.nextElement();
                String value = properties.getProperty(key);

                config.put(key,value);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return config;
    }

    static void getRegexType(byte x) {
        regex = "[0-1]{2}";
        loadConfigRegex(regex);
    }
    static void getRegexType(short x) {
        regex = "[0-5]{5}";
        loadConfigRegex(regex);
    }
    static void getRegexType(long x) {
        regex = "[0-9]{19}";
        loadConfigRegex(regex);
    }
    static void getRegexType(int x) {
       regex = "[0-9]{10}";
       loadConfigRegex(regex);
    }
    static void getRegexType(float x) {
        regex = "[0-9]{7}";
        loadConfigRegex(regex);
    }
    static void getRegexType(double x) {
        regex = "[0-9]{15}";
        loadConfigRegex(regex);
    }
    static void getRegexType(char x) {
        regex = "[a-zA-Z_0-9]{1}";
        loadConfigRegex(regex);
    }
    static void getRegexType(String x) {
        regex = "[a-zA-Z_0-9]{10}";
        loadConfigRegex(regex);
    }

}
