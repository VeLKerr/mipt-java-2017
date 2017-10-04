package ru.mipt.java2017.homework.g697.IvaninE.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

public class Token {
    public char symbol;
    public double value;

    Token(char character) {
        symbol = character;
    }
    Token(double val) {
        symbol = 'x';
        value = val;
    }
}
