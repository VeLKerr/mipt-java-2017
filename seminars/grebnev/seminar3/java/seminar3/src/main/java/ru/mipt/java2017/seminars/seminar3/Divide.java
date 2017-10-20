package ru.mipt.java2017.seminars.seminar3;

public class Divide implements Calculable {

    public final Calculable dividend;
    public final Calculable divisor;

    public Divide(Calculable dividend, Calculable divisor) {
        this.dividend = dividend;
        this.divisor = divisor;
    }

    @Override
    public int calculate() {
        throw new UnsupportedOperationException();
    }
}
