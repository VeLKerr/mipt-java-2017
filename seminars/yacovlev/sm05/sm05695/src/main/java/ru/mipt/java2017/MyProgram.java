package ru.mipt.java2017;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MyProgram {

    public static void main(String args[]) throws ExecutionException, InterruptedException {
        Logger log = LoggerFactory.getLogger(MyProgram.class);

        log.info("Hello, World!, {}, {}", 123, Logger.class);
        log.error("Error :(");
        log.warn("Warning!");
        log.debug("Debug: {}", Arrays.asList(1,2,3,4,5,6));

        Logger componentLog = LoggerFactory.getLogger("Some Program Part");
        componentLog.info("Part info");
        componentLog.debug("Part debug");

        PrimeChecker checker = new PrimeChecker();

        long start = 1000*1000*1000 + 1;

        // 1. Trivial implementation
//        for (int i=0; i<1000; ++i) {
//            long value = start + i;
//            boolean prime = checker.isPrime(value);
//            if (prime) {
//                log.info("Number {} is prime!", value);
//            }
//        }


        // 2. The same using promises .get()
//        for (int i=0; i<1000; ++i) {
//            long value = start + i;
//            Future<Boolean> promise = checker.isPrimePromise(value);
//            if (promise.get().booleanValue()) {
//                log.info("Number {} is prime!", value);
//            }
//        }

        List<Future<Boolean>> promises = new ArrayList<Future<Boolean>>(1000);
        // 3. Using promises in array
        for (int i=0; i<1000; ++i) {
            long value = start + i;
            Future<Boolean> promise = checker.isPrimePromise(value);
            promises.add(promise);
        }

        for (int i=0; i<1000; ++i) {
            long value = start + i;
            Future<Boolean> promise = promises.get(i);
            if (promise.get().booleanValue()) {
                log.info("Number {} is prime!", value);
            }
        }

    }

}
