package ru.mipt.java2017.seminars.seminar3;

public class TrivialCalculator {

    public int calculate(String expression) {
        return new TrivialExpressionParser().parse(expression).calculate();
    }
}