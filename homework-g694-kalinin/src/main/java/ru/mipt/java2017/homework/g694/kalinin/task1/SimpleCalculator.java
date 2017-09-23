package ru.mipt.java2017.homework.g694.kalinin.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

/**
 * One-pass recursive parser calculator realization
 *
 * @author Stepan A. Kalinin
 * @since 21.09.17
 */
public class SimpleCalculator implements Calculator {
    private int currentPosition;
    private String expression;
    private static final String ALLOWED_CHARACTERS_REGEX = "[0-9.()+\\-*/\\s]*";

    /**
     * @param inputExpression arithmetic expression to calculate
     * @return value of expression if valid
     * @throws ParsingException exception with error message thrown in case of invalid expression
     */
    @Override
    public double calculate(String inputExpression) throws ParsingException {
        if (inputExpression == null) {
            throw new ParsingException("Null expression");
        }
        if (!checkSpacing(inputExpression)) {
            throw new ParsingException("Unexpected space between number digits/decimal");
        }
        if (!inputExpression.matches(ALLOWED_CHARACTERS_REGEX)) {
            throw new ParsingException("Incorrect character");
        }
        this.expression = inputExpression.replaceAll("\\s", "");
        this.currentPosition = 0;
        double result = parseExpression();
        if (currentPosition != expression.length()) {
            throw new ParsingException("Incorrect expression");
        }
        return result;
    }

    /**
     * Checks whether expression contains space-divided numbers or not
     *
     * @param inputExpression expression to check
     * @return true if expression doesn't contain bad numbers, false otherwise
     */
    private boolean checkSpacing(String inputExpression) {
        String[] pieces = inputExpression.split("\\s+");
        for (int i = 0; i < pieces.length - 1; ++i) {
            if (pieces[i].isEmpty() || pieces[i + 1].isEmpty()) {
                continue;
            }
            if (isNumberCharacter(pieces[i].charAt(pieces[i].length() - 1)) &&
                    isNumberCharacter(pieces[i + 1].charAt(0))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether character is digit or decimal
     *
     * @param symbol character to check
     * @return true if symbol is number character, false otherwise
     */
    private boolean isNumberCharacter(char symbol) {
        return Character.isDigit(symbol) || symbol == '.';
    }

    /**
     * Parses expression assuming that currentPosition is the beginning of correct expression
     * Expression should not contain any space-characters or invalid characters
     *
     * @return value of parsed expression
     * @throws ParsingException invalid expression
     */
    private double parseExpression() throws ParsingException {
        if (currentPosition >= expression.length()) {
            throw new ParsingException("Unexpected end of expression while parsing expression");
        }
        double result = parseSum();
        if (currentPosition != expression.length() && expression.charAt(currentPosition) != ')') {
            throw new ParsingException("Incorrect expression");
        }
        return result;
    }

    /**
     * Parses sum of terms (may be with minuses) starting from currentPosition
     *
     * @return resulting sum
     * @throws ParsingException invalid expression
     */
    private double parseSum() throws ParsingException {
        if (currentPosition >= expression.length()) {
            throw new ParsingException("Unexpected end of expression while parsing sum");
        }
        double result;
        result = parseProduct(true);
        while (currentPosition < expression.length() &&
                (expression.charAt(currentPosition) == '+' ||
                        expression.charAt(currentPosition) == '-')) {
            boolean extract = expression.charAt(currentPosition) == '-';
            ++currentPosition;
            double product = parseProduct(false);
            if (extract) {
                result -= product;
            } else {
                result += product;
            }
        }
        return result;
    }

    /**
     * Parses product of tokens (may be with divisions) starting from currentPosition
     *
     * @param unaryOperationAllowed true if unary plus/minus is allowed as first symbol
     * @return resulting product
     * @throws ParsingException invalid expression
     */
    private double parseProduct(boolean unaryOperationAllowed) throws ParsingException {
        if (currentPosition >= expression.length()) {
            throw new ParsingException("Unexpected end of expression while parsing product");
        }
        double result;
        result = parseToken(unaryOperationAllowed);
        while (currentPosition < expression.length() &&
                (expression.charAt(currentPosition) == '*' ||
                        expression.charAt(currentPosition) == '/')) {
            boolean divide = expression.charAt(currentPosition) == '/';
            ++currentPosition;
            double token = parseToken(true);
            if (divide) {
                result /= token;
            } else {
                result *= token;
            }
        }
        return result;
    }

    /**
     * Parses single token starting at currentPosition
     * Token is number or bracketed expression, possibly with single preceding minus
     *
     * @param unaryOperationAllowed true if unary plus/minus is allowed as first symbol
     * @return value of token
     * @throws ParsingException invalid expression
     */
    private double parseToken(boolean unaryOperationAllowed) throws ParsingException {
        if (currentPosition >= expression.length()) {
            throw new ParsingException("Unexpected end of expression while parsing token");
        }
        double result;
        if (expression.charAt(currentPosition) == '(') {
            ++currentPosition;
            result = parseExpression();
            if (currentPosition >= expression.length() || expression.charAt(currentPosition) != ')') {
                throw new ParsingException("Unexpected end of expression while parsing token: unclosed brace");
            }
            ++currentPosition;
            return result;
        }
        if (!unaryOperationAllowed &&
                (expression.charAt(currentPosition) == '-' || expression.charAt(currentPosition) == '+')) {
            throw new ParsingException("Unexpected unary plus/minus");
        }
        if (expression.charAt(currentPosition) == '+' || expression.charAt(currentPosition) == '-') {
            if (currentPosition + 1 >= expression.length() ||
                    (expression.charAt(currentPosition + 1) != '(' &&
                            !Character.isDigit(expression.charAt(currentPosition + 1)))) {
                throw new ParsingException("Unary operation incorrect usage");
            }
        }
        if (expression.charAt(currentPosition) == '-') {
            ++currentPosition;
            result = parseToken(false);
            return -result;
        }
        if (expression.charAt(currentPosition) == '+') {
            ++currentPosition;
            result = parseToken(false);
            return result;
        }
        result = parseNumber();
        return result;
    }

    /**
     * Parses decimal number starting from current position
     *
     * @return parsed number
     * @throws ParsingException incorrect number format
     */
    private double parseNumber() throws ParsingException {
        if (currentPosition >= expression.length() || !Character.isDigit(expression.charAt(currentPosition))) {
            throw new ParsingException("Error while parsing number");
        }
        int position = currentPosition;
        while (position < expression.length() && isNumberCharacter(expression.charAt(position))) {
            ++position;
        }
        try {
            double ans = Double.parseDouble(expression.substring(currentPosition, position));
            currentPosition = position;
            return ans;
        } catch (NumberFormatException e) {
            throw new ParsingException("Error while parsing number", e.getCause());
        }
    }
}
