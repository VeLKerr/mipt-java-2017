package ru.mipt.java2017;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Philosopher {

    private final String name;

    public Philosopher(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void eat() throws InterruptedException {
        Table.getInstance().takeForksPair(this);

        System.out.println(name + " кушает...");
        Thread.sleep(50);
        System.out.println(name + " закончил трапезу");

        Table.getInstance().releaseForksPair(this);
    }

    public void eatForever() {
        while (true) {
            try {
                eat();
            } catch (InterruptedException e) {
                System.out.println(name + " не удалось поесть");
                break;
            }
        }
    }
}

class Fork {

    private Lock mutex = new ReentrantLock();

    public void take() { mutex.lock(); }
    public void release() { mutex.unlock(); }
}

class Waiter {
    private Semaphore semaphore = new Semaphore(4);

    public void notifyBeforeForksTaken() throws InterruptedException {
        semaphore.acquire(2);
    }
    public void notifyAfterForksReleased() {
        semaphore.release(2);
    }
}

class Table {

    private List<Philosopher> philosophers = new ArrayList<Philosopher>(5);
    private List<Fork> forks = new ArrayList<Fork>(5);
    private static Table instance = new Table();
    private Waiter waiter = new Waiter();

    private Table() {
        for (int i = 0; i < 5; i++) {
            forks.add(new Fork());
        }
    }

    public static Table getInstance() {
        return instance;
    }

    public void seat(Philosopher body) throws OutOfPlaceException {
        if (philosophers.size() > 4)
            throw new OutOfPlaceException(body.getName());
        philosophers.add(body);
    }

    public void takeForksPair(Philosopher body) throws InterruptedException {
        int bodyIndex = philosophers.indexOf(body);
        int leftIndex = bodyIndex;
        int rightIndex = bodyIndex < forks.size()-1
                ? bodyIndex + 1
                : 0
                ;
        Fork left = forks.get(leftIndex);
        Fork right = forks.get(rightIndex);
        waiter.notifyBeforeForksTaken();
        left.take();
        right.take();
    }

    public void releaseForksPair(Philosopher body) {
        int bodyIndex = philosophers.indexOf(body);
        int leftIndex = bodyIndex;
        int rightIndex = bodyIndex < forks.size()-1
                ? bodyIndex + 1
                : 0
                ;
        Fork left = forks.get(leftIndex);
        Fork right = forks.get(rightIndex);
        left.release();
        right.release();
        waiter.notifyAfterForksReleased();
    }

}
