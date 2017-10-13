package ru.mipt.java2017.sm05;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PrimesChecker {

    private ExecutorService executorService = Executors.newFixedThreadPool(2);
//    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public boolean isPrime(long number) {
        if (number==2) return true;
        if ( (number & 1)==0 ) return false;
        for (int test = 3; test < number; test += 2) {
            if (number % test == 0) return false;
        }
        return true;
    }

    public Future<Boolean> isPrimePromise(final long number) {
        return executorService.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return Boolean.valueOf(isPrime(number));
            }
        });
    }

}
