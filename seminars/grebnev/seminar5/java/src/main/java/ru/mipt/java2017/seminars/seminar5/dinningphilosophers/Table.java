package ru.mipt.java2017.seminars.seminar5.dinningphilosophers;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Table {

    private final static int TABLE_SIZE = 5;

    private final static Table instance = new Table();

    public static Table getInstance() {
        return instance;
    }

    private Servant servant = new Servant(TABLE_SIZE);

    private final List<Place> places = new ArrayList<>(TABLE_SIZE);
    private final Fork[] forks = new Fork[TABLE_SIZE];
    private final Queue<Place> freePlaces = new ArrayDeque<>();

    private Table() {
        Fork firstFork = new Fork();
        Fork leftFork = firstFork;
        for (int i = 0; i < TABLE_SIZE; i ++) {
            forks[i] = leftFork;

            Fork rightFork = i < TABLE_SIZE - 1 ? new Fork() : firstFork;

            Place place = new Place(i, leftFork, rightFork);
            places.add(place);
            freePlaces.add(place);

            leftFork = rightFork;
        }
    }

    public synchronized Place seat() {

        Place place = freePlaces.poll();

        if (place == null) {
            throw new OutOfPlaceException();
        }

        return place;
    }

    public Servant getServant() {
        return servant;
    }
}

