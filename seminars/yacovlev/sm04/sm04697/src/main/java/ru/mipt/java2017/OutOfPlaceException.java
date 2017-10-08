package ru.mipt.java2017;

public class OutOfPlaceException extends Exception {
    private final String bodyName;

    public OutOfPlaceException(String bodyName) {
        this.bodyName = bodyName;
    }

    @Override
    public String toString() {
        return "Не хватило место для " + bodyName;
    }
}
