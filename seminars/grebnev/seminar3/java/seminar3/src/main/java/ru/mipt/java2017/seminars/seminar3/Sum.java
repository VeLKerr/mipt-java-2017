package ru.mipt.java2017.seminars.seminar3;

import java.util.Arrays;
import java.util.List;

public class Sum implements Calculable{

    private final int[] args;

    public Sum(int[] args) {
        this.args = Arrays.copyOf(args, args.length);
    }

    @Override
    public int calculate() {
        throw new UnsupportedOperationException();
    }
}
