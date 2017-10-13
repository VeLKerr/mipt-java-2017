package ru.mipt.java2017;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.lang.Math.sqrt;

public class PrimeChecker {

    private ExecutorService executor = Executors.newFixedThreadPool(4);
//    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public boolean isPrime(long number) {
        if (number == 2) return true;
        if ( (number & 1) == 0 ) return false;
//        long upperBound = (long) sqrt(number) + 1;
        long upperBound = number - 1;
        for (int test = 3; test < upperBound; test += 2) {
            if ( (number % test) == 0 ) {
                return false;
            }
        }
        return true;
    }

    public Future<Boolean> isPrimePromise(long number) {
        return executor.submit(() -> isPrime(number));
    }

}
