package ru.mipt.java2017.sm12;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;


public class Generator {

    static {
        //  BAD:
        //  - absolute file path
        //  - prefix 'lib'
        //  - .so v.s. .dll
        // System.load("/home/victor/sm12695/src/main/build-native-Desktop-Debug/libMyGreatLibrary.so");

        // use canonical name:
        // - no prefix + suffix for file name
        // - find it in : java.library.path
        System.loadLibrary("MyGreatLibrary");
        System.out.println("Library loaded");
    }

    private Logger logger = LoggerFactory.getLogger("primes");

    public boolean isPrime(long test) {
        for (long i=2; i<test; ++i) {
            if (test % i == 0) return false;x
        }
        return true;
    }

    public native boolean isPrimeNative(long test);

    public void printPrimes(long start, long end) {
        List<Long> primes = new LinkedList<>();
        for (long i=start; i<=end; ++i) {
            if (isPrimeNative(i)) {
                primes.add(i);
            }
        }
        logger.info("Primes: {}", primes);
    }

    public static void main(String args[]) {
        Generator generator = new Generator();
        System.out.println("Let's go!");
        Logger logger = LoggerFactory.getLogger("timings");
        long startTime, endTime;
        startTime = System.nanoTime();
        generator.printPrimes(100_000_000, 100_000_050);
        endTime = System.nanoTime();
        logger.info("Time: {}", (endTime-startTime)/1000./1000/1000);
    }

}
