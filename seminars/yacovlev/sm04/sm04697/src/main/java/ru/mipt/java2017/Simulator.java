package ru.mipt.java2017;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

// 1. Very old school style
class PhilospherWorker implements Runnable {

    private final Philosopher body;

    PhilospherWorker(Philosopher body) {
        this.body = body;
    }

    @Override
    public void run() {
        body.eatForever();
    }
}

public class Simulator {

    public static void main(String args[]) {
//        // 1. Old school style creation
//        List<Philosopher> philosophers = new LinkedList<Philosopher>();
//        philosophers.add(new Philosopher("Кант"));
//
//
        // 2. New style
        List<Philosopher> philosophers = Arrays.asList(
                new Philosopher("Кант"),
                new Philosopher("Диоген"),
                new Philosopher("Маркс"),
                new Philosopher("Ленин"),
                new Philosopher("Ницше")
        );


        philosophers.forEach(philosopher -> {
            try {
                Table.getInstance().seat(philosopher);
            } catch (OutOfPlaceException e) {
                System.exit(1);
            }
        });

        List<Thread> threads = new LinkedList<>();

//        // 1. very old school
//        philosophers.forEach(body -> {
//            threads.add(new Thread(new PhilospherWorker(body)));
//        });
//
//        // 2. old school
//        philosophers.forEach(body -> {
//            threads.add(new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    body.eatForever();
//                }
//            }));
//        });

        // 3. new style (Java 1.8 +)
        philosophers.forEach(body -> {
            threads.add(new Thread( () -> body.eatForever()) );
        });

//        // 4. alternative new style
//        philosophers.forEach(body -> {
//            threads.add(new Thread(body::eatForever) );
//        });

        // Register handler for SIGTERM
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            threads.forEach(thread -> thread.interrupt());
        }));

        threads.forEach(Thread::start);

        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }

}
