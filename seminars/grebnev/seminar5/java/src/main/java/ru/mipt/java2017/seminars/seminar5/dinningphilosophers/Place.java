package ru.mipt.java2017.seminars.seminar5.dinningphilosophers;

public class Place {

    private final int index;

    private final Fork leftFork;
    private final Fork rightFork;

    public Place(int index, Fork leftFork, Fork rightFork) {
        this.index = index;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    public Fork leftFork() {
        return leftFork;
    }

    public Fork rightFor() {
        return rightFork;
    }

    @Override
    public String toString() {
        return Integer.toString(index);
    }
}
