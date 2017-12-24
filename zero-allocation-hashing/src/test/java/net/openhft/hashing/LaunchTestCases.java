package net.openhft.hashing;

import org.junit.runner.JUnitCore;
import org.junit.runner.notification.Failure;

import java.util.List;


public class LaunchTestCases {

        public static List<Failure> launchTest(){
            org.junit.runner.Result result = JUnitCore.runClasses(City64_1_1_Test.class);

            return result.getFailures();
    }

}
