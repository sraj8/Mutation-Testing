package com.object.oriented.mutation.launcher.app;

public class MutantRunnable implements Runnable {

    private String mutationToApply;

    MutantRunnable(String mutationToApply){
        this.mutationToApply = mutationToApply;
    }


    @Override
    public void run() {

        Launcher launcher = new Launcher(mutationToApply);

        launcher.launch();

    }
}
