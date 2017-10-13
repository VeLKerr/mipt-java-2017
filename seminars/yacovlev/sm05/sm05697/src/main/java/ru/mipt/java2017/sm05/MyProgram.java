package ru.mipt.java2017.sm05;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MyProgram {

    public static void main(String args[]) throws ExecutionException, InterruptedException {
        Logger log = LoggerFactory.getLogger("MyProgram");

        log.info("The program is running");
        log.error("Error occured!!!!");
        log.warn("Auchtung!!!");
        log.debug("Values is {}", 123);

        Logger log2 = LoggerFactory.getLogger("SomePart");
        log2.info("Info from log2");
        log2.debug("Debug from log2");

        PrimesChecker checker = new PrimesChecker();

        long start = 1000*1000*1000;

//        // 1. Calculations by request (blocking)
//        for (int i=0; i<1000; i++) {
//            long value = start + i;
//            boolean prime = checker.isPrime(value);
//            if (prime) {
//                log.info("Number {} is prime!", value);
//            }
//        }

        // 2. Calculations via Future.get() (blocking)
//        for (int i=0; i<1000; i++) {
//            long value = start + i;
//            Future<Boolean> promise = checker.isPrimePromise(value);
//            boolean prime = promise.get().booleanValue();
//            if (prime) {
//                log.info("Number {} is prime!", value);
//            }
//        }

        // 3. Build promises, then access (non-blocking)
        List<Future<Boolean>> promises = new ArrayList<>(1000);
        for (int i=0; i<1000; i++) {
            long value = start + i;
            Future<Boolean> promise = checker.isPrimePromise(value);
            promises.add(promise);
        }

        for (int i=0; i<1000; i++) {
            long value = start + i;
            Future<Boolean> promise = promises.get(i);
            boolean prime = promise.get().booleanValue();
            if (prime) {
                log.info("Number {} is prime!", value);
            }
        }


    }

}
