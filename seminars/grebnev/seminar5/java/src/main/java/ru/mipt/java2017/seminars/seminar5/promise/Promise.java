package ru.mipt.java2017.seminars.seminar5.promise;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Promise {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        for (long i = 1000000000; i < 1000001000; i++) {
//            if (isPrime(i)) {
//                System.out.println(i);
//            }
//        }

        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<Future<Long>> futures = new ArrayList<>();
        for (long i = 1000000000; i < 1000001000; i++) {
            long j = i;
            futures.add(executor.submit(() -> primeCheck(j)));
        }

        for (Future<Long> future : futures) {
            long prime = future.get();
            if (prime > 0) {
                System.out.println(prime);
            }
        }
    }

    private static long primeCheck(long number) {
        if (number == 1 || number == 2) {
            return number;
        }

        for (long i = 2; i < number; i++) {
            if (number % i == 0) {
                return -1;
            }
        }

        return number;
    }
}
