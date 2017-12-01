package ru.mipt.java2017.sm12;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Generator {

    static {
        // BAD!!!
        // - full path to file
        // - platform-dependent file name
        // System.load("/home/victor/sm12697/src/main/build-native-Desktop-Debug/libMyGreatLib.so");

        // load from java.library.path
        System.loadLibrary("MyGreatLib");
    }

    Logger logger = LoggerFactory.getLogger("primes");

    public boolean isPrime(long value) {
        for (long test=2; test<value; test++) {
            if (value % test==0) return false;
        }
        return true;
    }

    public native boolean isPrimeNative(long value);

    public void printPrimes(long start, long end) {
        List<Long> primes = new ArrayList<>();
        for (long x=start; x<end; ++x) {
            if (isPrimeNative(x)) {
//            if (isPrime(x)) {
                primes.add(x);
            }
        }
        logger.info("Primes: {}", primes);
    }

    public static void main(String args[]) {
        Logger logger = LoggerFactory.getLogger("timings");
        long startTime, endTime;
        Generator generator = new Generator();

        startTime = System.nanoTime();
        generator.printPrimes(100_000_000, 100_000_050);
        endTime = System.nanoTime();
        logger.info("Time: {}", (endTime-startTime)/1000./1000/1000);
    }

}
