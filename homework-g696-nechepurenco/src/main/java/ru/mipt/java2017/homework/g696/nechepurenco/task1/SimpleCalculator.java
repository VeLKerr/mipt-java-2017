package ru.mipt.java2017.homework.g696.nechepurenco.task1;

import  ru.mipt.java2017.homework.base.task1.Calculator;
import  ru.mipt.java2017.homework.base.task1.ParsingException;

public class SimpleCalculator implements  Calculator{
    public double calculate(String expression) throws ParsingException {
        StringParser sp = new StringParser(expression);
        sp.Parse();
        return sp.getanswer();
    }
}
