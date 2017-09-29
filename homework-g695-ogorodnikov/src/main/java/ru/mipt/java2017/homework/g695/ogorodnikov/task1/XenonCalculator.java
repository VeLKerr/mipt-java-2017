package ru.mipt.java2017.homework.g695.ogorodnikov.task1;

import ru.mipt.java2017.homework.base.task1.*;
import ru.mipt.java2017.homework.g695.ogorodnikov.task1.parser.ParsedExpr;

public class XenonCalculator implements Calculator {
    /**
     * Принимает строку с валидным арифметическим выражением.
     * Возвращает результат выполнения этого выражения.
     * Выражение может содержать числа десятичного формата, операторы +, -, *, / и операторы приоритета (, ).
     * В выражении допустимы любые space-символы.
     *
     * @param expression строка с арифметическим выражением
     * @return результат расчета выражения
     * @throws ParsingException не удалось распознать выражение
     */
    @Override
    public double calculate(String expression) throws ParsingException {
        ParsedExpr expr = ParsedExpr.parse(expression);
        return expr.eval();
    }
}
