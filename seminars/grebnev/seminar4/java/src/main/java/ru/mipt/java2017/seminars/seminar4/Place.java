package ru.mipt.java2017.seminars.seminar4;

public class Place {

    private final Fork leftFork;
    private final Fork rightFork;

    public Place(Fork leftFork, Fork rightFork) {
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    public Fork leftFork() {
        return leftFork;
    }

    public Fork rightFor() {
        return rightFork;
    }
}
