package ru.mipt.homework.g696.pak.task1;

import sun.security.pkcs.ParsingException;

public class Calculator {

    private Parser parser_;

    Calculator() {
        parser_ = new Parser();
    }

    double calculate(String expression) throws ParsingException {
        return parser_.parse(expression).calculate();
    }
}
