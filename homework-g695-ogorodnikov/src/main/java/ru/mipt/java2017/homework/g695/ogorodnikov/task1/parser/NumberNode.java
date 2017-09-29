package ru.mipt.java2017.homework.g695.ogorodnikov.task1.parser;

public class NumberNode extends ParsedExpr {
    private double data;

    NumberNode(double data) {
        this.data = data;
    }

    @Override
    public double eval() {
        return data;
    }

    @Override
    public String toString() {
        return Double.toString(data);
    }
}
