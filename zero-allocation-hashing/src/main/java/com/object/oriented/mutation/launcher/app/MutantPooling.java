package com.object.oriented.mutation.launcher.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class MutantPooling {


    private static List<String> mutationsToApply = new ArrayList<>(Arrays.asList("Conditional","AccessModifier","Arithmetic","Inheritence","MethodMutant","MethodInvocation"));

    private static Logger logger = Logger.getLogger("MutantPooling");
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        logger.info("Creating Executor Service.....");


        for(int i=0;i<mutationsToApply.size();i++){

            Runnable runnable = new MutantRunnable(mutationsToApply.get(i));
            executorService.execute(runnable);


        }

        executorService.shutdown();

        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            logger.info("Shutting down Executor Service.....");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
