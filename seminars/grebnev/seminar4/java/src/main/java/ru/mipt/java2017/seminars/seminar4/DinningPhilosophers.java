package ru.mipt.java2017.seminars.seminar4;

import java.util.*;

public class DinningPhilosophers {

    public static void main(String args[]) {
        List<Philosopher> philosophers = Arrays.asList(
                new Philosopher("Аристотель"),
                new Philosopher("Сократ"),
                new Philosopher("Ницще"),
                new Philosopher("Кант"),
                new Philosopher("Фрейд"),
                new Philosopher("Лишний философ"));

        List<Thread> threads = new ArrayList<>();
        Object lock = new Object();
        for (Philosopher philosopher : philosophers) {

            threads.add(new Thread(() -> {
                try {
                    //synchronized (lock) {
                        philosopher.seatAtTable();
                    //}
                }
                catch (OutOfPlaceException e){
                    System.out.println("Философу " + philosopher.getName()
                            + " не хватило места за столом");
                }

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                philosopher.dinning();
            }));
        }

        threads.forEach(Thread::start);
    }
}

