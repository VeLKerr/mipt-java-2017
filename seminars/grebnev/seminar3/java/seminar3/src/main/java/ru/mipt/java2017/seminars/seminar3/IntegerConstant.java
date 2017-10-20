package ru.mipt.java2017.seminars.seminar3;

public class IntegerConstant implements Calculable {

    private final int value;

    public IntegerConstant(int value) {
        this.value = value;
    }

    @Override
    public int calculate() {
        throw new UnsupportedOperationException();
    }
}
