package ru.mipt.homework.g696.pak.task1;

import sun.security.pkcs.ParsingException;

public interface ExpressionParser {
    Calculable parse(String expression)throws ParsingException;
}
