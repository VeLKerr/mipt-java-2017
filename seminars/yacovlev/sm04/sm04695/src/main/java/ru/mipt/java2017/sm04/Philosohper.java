package ru.mipt.java2017.sm04;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Philosohper {
    private final String name;

    public Philosohper(String name) {
        this.name = name;
    }

    public void eat() throws InterruptedException {
        Table.getInstance().takeForks(this);
        System.out.println(name + " кушает...");
        Thread.sleep(50);
        System.out.println(name + " закончил трапезу");
        Table.getInstance().releaseForks(this);
    }

    public void eatForever() {
        while (true) {
            try {
                eat();
            } catch (InterruptedException e) {
                System.out.println(name + " выгнали из-за стола");
                break;
            }
        }
    }
}

class Fork {
    private Lock lock = new ReentrantLock();

    public void take() {
        lock.lock();
    }

    public void release() {
        lock.unlock();
    }
}

class Waiter {
    private Semaphore semaphore = new Semaphore(4);

    public void notifyBeforeForksTake(int forksCount) throws InterruptedException {
        semaphore.acquire(forksCount);
    }

    public void notifyAfterForksRelease(int forksCount) {
        semaphore.release(forksCount);
    }
}

class Table {
    private List<Philosohper> philosohpers = new ArrayList<Philosohper>(5);
    private List<Fork> forks = new ArrayList<Fork>(5);
    private Waiter waiter = new Waiter();

    public static Table getInstance() {
        return instance;
    }

    private static Table instance = new Table();

    private Table() {
        for (int i = 0; i < 5; i++) {
            forks.add(new Fork());
        }
    }

    public void seat(Philosohper body) {
        philosohpers.add(body);
    }

    public Fork getLeftFork(Philosohper body) {
        int philosopherIndex = philosohpers.indexOf(body);
        return forks.get(philosopherIndex);
    }

    public Fork getRightFork(Philosohper body) {
        int philosopherIndex = philosohpers.indexOf(body);
        int forkIndex = philosopherIndex < philosohpers.size()-1
                ? philosopherIndex + 1 : 0;
        return forks.get(forkIndex);
    }

    public void takeForks(Philosohper body) throws InterruptedException {
        waiter.notifyBeforeForksTake(2);
        getLeftFork(body).take();
        getRightFork(body).take();
    }

    public void releaseForks(Philosohper body) {
        getLeftFork(body).release();
        getRightFork(body).release();
        waiter.notifyAfterForksRelease(2);
    }

}
