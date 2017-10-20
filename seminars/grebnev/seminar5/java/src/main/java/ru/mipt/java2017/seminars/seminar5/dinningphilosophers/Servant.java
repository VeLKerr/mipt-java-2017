package ru.mipt.java2017.seminars.seminar5.dinningphilosophers;

import java.util.concurrent.Semaphore;

public class Servant {

    private final Semaphore semaphore;

    public Servant(int forkCount) {
        semaphore = new Semaphore(forkCount);
    }

    public void getForks(Place place) throws InterruptedException {
        semaphore.acquire(2);
        place.leftFork().take();
        Thread.sleep(500);
        place.rightFor().take();
    }

    public void releaseFork(Place place) {
        place.leftFork().release();
        place.rightFor().release();
        semaphore.release(2);
    }
}
