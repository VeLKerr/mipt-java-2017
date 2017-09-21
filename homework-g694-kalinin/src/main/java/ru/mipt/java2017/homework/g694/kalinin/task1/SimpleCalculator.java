package ru.mipt.java2017.homework.g694.kalinin.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

public class SimpleCalculator implements Calculator {
    private int currentPosition;
    private String expression;

    public void main(String[] args) throws ParsingException, IllegalArgumentException {
        if (args.length == 0) {
            throw new IllegalArgumentException("Missing argument");
        }
        System.out.println(this.calculate(args[0]));
    }

    @Override
    public double calculate(String inputExpression) throws ParsingException {
        if (inputExpression == null) {
            throw new ParsingException("Null expression");
        }
        if (!checkSpacing(inputExpression)) {
            throw new ParsingException("Unexpected space between number digits/decimal");
        }
        if (!inputExpression.matches("[0-9\\.()+\\-*/\\s]*")) {
            throw new ParsingException("Incorrect character");
        }
        this.expression = inputExpression.replaceAll("\\s", "");
        this.currentPosition = 0;
        double ans = parseExpression();
        if (currentPosition != expression.length()) {
            throw new ParsingException("Incorrect expression");
        }
        return ans;
    }

    private boolean checkSpacing(String inputExpression) throws ParsingException {
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

    private boolean isNumberCharacter(char symbol) {
        return Character.isDigit(symbol) || symbol == '.';
    }

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
