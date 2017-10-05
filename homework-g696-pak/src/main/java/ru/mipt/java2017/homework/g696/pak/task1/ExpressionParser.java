package ru.mipt.java2017.homework.g696.pak.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;

public interface ExpressionParser {

  Calculable parse(String expression) throws ParsingException;
}
