package ru.mipt.java2017.sm04;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/* Old school-style */
class PhilosopherWorker implements Runnable {

    private final Philosohper body;

    PhilosopherWorker(Philosohper body) {
        this.body = body;
    }

    @Override
    public void run() {
        body.eatForever();
    }
}

public class Simulator {
    public static void main(String args[]) {
        List<Philosohper> philosohpers = Arrays.asList(
                new Philosohper("Кант"),
                new Philosohper("Гегель"),
                new Philosohper("Аристотель"),
                new Philosohper("Сократ"),
                new Philosohper("Ленин")
        );

        philosohpers.forEach(philosohper -> Table.getInstance().seat(philosohper));

        List<Thread> threads = new LinkedList<>();
        philosohpers.forEach(philosohper -> {
            // 1. Very old school-style
//            Thread thread = new Thread(new PhilosopherWorker(philosohper));

            // 2. Old school (<= Java 1.7)
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    philosohper.eatForever();
//                }
//            });

            // 3. Lambdas
//            Thread thread = new Thread(() -> philosohper.eatForever());

            // 4. Function pointer
            Thread thread = new Thread(philosohper::eatForever);
            threads.add(thread);
        });

        // Register shutdown hook (TERM signal handler)
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                threads.forEach(Thread::interrupt);
            }
        }));

        threads.forEach(Thread::start);

        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    } // end of main
}
