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
        double ans = parseSum();
        if (currentPosition != expression.length() && expression.charAt(currentPosition) != ')') {
            throw new ParsingException("Incorrect expression");
        }
        return ans;
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
        double ans;
        ans = parseProduct();
        while (currentPosition < expression.length() &&
                (expression.charAt(currentPosition) == '+' ||
                        expression.charAt(currentPosition) == '-')) {
            boolean extract = expression.charAt(currentPosition) == '-';
            ++currentPosition;
            double product = parseProduct();
            if (extract) {
                ans -= product;
            } else {
                ans += product;
            }
        }
        return ans;
    }

    /**
     * Parses product of tokens (may be with divisions) starting from currentPosition
     *
     * @return resulting product
     * @throws ParsingException invalid expression
     */
    private double parseProduct() throws ParsingException {
        if (currentPosition >= expression.length()) {
            throw new ParsingException("Unexpected end of expression while parsing product");
        }
        double ans;
        ans = parseToken();
        while (currentPosition < expression.length() &&
                (expression.charAt(currentPosition) == '*' ||
                        expression.charAt(currentPosition) == '/')) {
            boolean divide = expression.charAt(currentPosition) == '/';
            ++currentPosition;
            double token = parseToken();
            if (divide) {
                ans /= token;
            } else {
                ans *= token;
            }
        }
        return ans;
    }

    /**
     * Parses single token starting at currentPosition
     * Token is number or bracketed expression, possibly with single preceding minus
     *
     * @return value of token
     * @throws ParsingException invalid expression
     */
    private double parseToken() throws ParsingException {
        if (currentPosition >= expression.length()) {
            throw new ParsingException("Unexpected end of expression while parsing token");
        }
        double ans;
        if (expression.charAt(currentPosition) == '(') {
            ++currentPosition;
            ans = parseExpression();
            if (currentPosition >= expression.length() || expression.charAt(currentPosition) != ')') {
                throw new ParsingException("Unexpected end of expression while parsing token: unclosed brace");
            }
            ++currentPosition;
            return ans;
        }
        if (expression.charAt(currentPosition) == '-') {
            ++currentPosition;
            if (currentPosition >= expression.length() || expression.charAt(currentPosition) != '(' &&
                    !Character.isDigit(expression.charAt(currentPosition))) {
                throw new ParsingException("Unary minus incorrect usage");
            }
            ans = parseToken();
            return -ans;
        }
        ans = parseNumber();
        return ans;
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
        int pos = currentPosition;
        while (pos < expression.length() && isNumberCharacter(expression.charAt(pos))) {
            ++pos;
        }
        try {
            double ans = Double.parseDouble(expression.substring(currentPosition, pos));
            currentPosition = pos;
            return ans;
        } catch (NumberFormatException e) {
            throw new ParsingException("Error while parsing number", e.getCause());
        }
    }
}
