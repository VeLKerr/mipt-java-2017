package ru.mipt.java2017.seminars.seminar5.dinningphilosophers;

import java.util.*;

public class DinningPhilosophers {

    public static void main(String args[]) throws InterruptedException {
        List<Philosopher> philosophers = Arrays.asList(
                new Philosopher("Аристотель", 3),
                new Philosopher("Сократ", 5),
                new Philosopher("Ницще", 7),
                new Philosopher("Кант", 2),
                new Philosopher("Фрейд", 1)
                //new Philosopher("Лишний философ")
        );

        List<Thread> threads = new ArrayList<>();
        Object lock = new Object();
        for (Philosopher philosopher : philosophers) {

            Thread thread = new Thread(() -> {
                try {
                    philosopher.seatAtTable();

                    philosopher.dinning();
                }
                catch (OutOfPlaceException e){
                    System.out.println("Философу " + philosopher.getName()
                            + " не хватило места за столом");
                }
            });
            thread.setDaemon(true);

            threads.add(thread);
        }

        threads.forEach(Thread::start);

        Thread.sleep(1000);
        threads.forEach(Thread::interrupt);

        for (Thread thread : threads) {
            thread.join();
        }
    }
}

