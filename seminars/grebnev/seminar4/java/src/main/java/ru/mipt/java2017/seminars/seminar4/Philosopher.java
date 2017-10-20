package ru.mipt.java2017.seminars.seminar4;

public class Philosopher {
    private final String name;

    private Place place;

    public Philosopher(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void seatAtTable() {
        place = Table.getInstance().seat();
        System.out.println("Философ " + name +
                " занял место " + place + " за столом");
    }

    public void dinning() {
        while (true) {
            place.leftFork().take();
            place.rightFor().take();

            System.out.println("Философ " + name + " ест");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            place.leftFork().release();
            place.rightFor().release();

            System.out.println("Философ " + name + " думает");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
