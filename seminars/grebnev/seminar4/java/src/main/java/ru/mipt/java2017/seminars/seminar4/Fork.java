package ru.mipt.java2017.seminars.seminar4;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Fork {

    private Lock lock = new ReentrantLock();

    public void take() {
        lock.lock();
    }

    public void release() {
        lock.unlock();
    }
}
