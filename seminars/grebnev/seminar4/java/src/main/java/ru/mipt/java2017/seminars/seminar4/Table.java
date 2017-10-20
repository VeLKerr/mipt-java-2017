package ru.mipt.java2017.seminars.seminar4;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Table {

    private final static int TABLE_SIZE = 5;

    private final static Table instance = new Table();

    public static Table getInstance() {
        return instance;
    }

    private final Place[] places = new Place[TABLE_SIZE];
    private final Fork[] forks = new Fork[TABLE_SIZE];

    private final Object lock = new Object();

    private Table() {
        Fork leftFork = new Fork();
        for (int i = 0; i < TABLE_SIZE; i ++) {
            forks[i] = leftFork;

            Fork rightFork = new Fork();
            places[i] = new Place(leftFork, rightFork);
            leftFork = rightFork;
        }
    }

    public Place seat() {
        synchronized (lock)
        {
            for (int i = 0; i < place.length; i++) {
                if (!place[i]) {
                    place[i] = true;
                    return i;
                }
            }
        }

        throw new OutOfPlaceException();
    }

    public void takeLeftFork(int placeIndex) {
    }

    public void releaseLeftFork(int placeIndex) {
    }

    public void takeRightFork(int placeIndex) {
    }

    public void releaseRightFork(int placeIndex) {
    }
}

