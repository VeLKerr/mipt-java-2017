package ru.mipt.java2017.seminars.seminar5.dinningphilosophers;

public class Philosopher {
    private final String name;
    private final int eatCount;

    private Place place;

    public Philosopher(String name, int eatCount) {
        this.name = name;
        this.eatCount = eatCount;
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
        boolean flag = false;
        try {
            for (int i = 0; i < eatCount; i++) {
                Table.getInstance().getServant().getForks(place);
                flag = true;

                System.out.println("Философ " + name + " ест");
                Thread.sleep(1000);

                Table.getInstance().getServant().releaseFork(place);
                flag = false;

                System.out.println("Философ " + name + " думает");
                Thread.sleep(1000);

                if (Thread.interrupted()) {
                    System.out.println("Философ " + name + " не доел");
                    break;
                }
            }

            System.out.println("Философ " + name + " завершил трапезу");
        } catch (InterruptedException e) {
            if (flag) {
                Table.getInstance().getServant().releaseFork(place);
            }
            System.out.println("Философа " + name + " прервали");
        }
    }
}
