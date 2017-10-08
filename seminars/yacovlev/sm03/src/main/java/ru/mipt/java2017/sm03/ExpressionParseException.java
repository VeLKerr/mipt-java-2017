package ru.mipt.java2017.sm03;

/**
 * Исключение при парсинге строки в классе {@link TrivialCalculator}
 */
public class ExpressionParseException extends Exception {

    private final String sourceExpression;
    private final String errorToken;

    public ExpressionParseException(String sourceExpression, String errorToken) {
        this.sourceExpression = sourceExpression;
        this.errorToken = errorToken;
    }

    @Override
    public String toString() {
        return "Error while parsing " + sourceExpression + " in token" + errorToken;
    }
}
