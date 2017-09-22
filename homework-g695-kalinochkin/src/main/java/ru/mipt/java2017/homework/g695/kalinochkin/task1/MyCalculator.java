package ru.mipt.java2017.homework.g695.kalinochkin.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;
import ru.mipt.java2017.homework.base.task1.Calculator;

import java.util.ArrayList;
import java.util.Stack;

class MyCalculator implements Calculator {

    public double calculate(String expression) throws ParsingException {
        if (expression == null || expression.length() == 0) {
            throw new ParsingException("Empty expression");
        }
        ArrayList<Token> tokens = reorder(tokenize(expression));
        Stack<Double> st = new Stack<>();
        for (Token t : tokens) {
            if (t.getType() == TokenType.NUMBER) {
                try {
                    st.push(Double.parseDouble(t.getValue()));
                } catch (NumberFormatException e) {
                    throw new ParsingException("Invalid number: " + t.getValue());
                }
            } else if (t.getType() == TokenType.UNARY_OP) {
                if (st.empty()) {
                    throw new ParsingException("Not enough operands");
                }
                st.push(evalUnary(t.getValue(), st.pop()));
            } else if (t.getType() == TokenType.BINARY_OP) {
                if (st.size() < 2) {
                    throw new ParsingException("Not enough operands");
                }
                double b = st.pop();
                double a = st.pop();
                st.push(evalBinary(t.getValue(), a, b));
            }
        }
        if (st.size() != 1) {
            throw new ParsingException("Invalid expression");
        }
        return st.pop();
    }

    protected int getPriority(Token t) {
        if (t.getType() == TokenType.UNARY_OP) {
            return 100;
        }
        if (t.getValue().equals("+") || t.getValue().equals("-")) {
            return 1;
        }
        return 2;
    }

    protected boolean isRightAssoc(Token t) {
        return t.getType() == TokenType.UNARY_OP;
    }

    protected double evalUnary(String op, double a) {
        if (op.equals("+")) {
            return a;
        }
        if (op.equals("-")) {
            return -a;
        }
        return 0;
    }

    protected double evalBinary(String op, double a, double b) {
        if (op.equals("+")) {
            return a + b;
        }
        if (op.equals("-")) {
            return a - b;
        }
        if (op.equals("*")) {
            return a * b;
        }
        if (op.equals("/")) {
            return a / b;
        }
        return 0;
    }

    protected ArrayList<Token> tokenize(String expression) throws ParsingException {
        ArrayList<Token> tokens = new ArrayList<>();
        String number = "";
        TokenType last = TokenType.LEFTPAR;
        int pars = 0;
        for (char c : (expression + ' ').toCharArray()) {
            if (Character.isDigit(c) || c == '.') {
                if (last == TokenType.NUMBER) {
                    throw new ParsingException("Operation expected, number found");
                }
                number += c;
            } else if (c == '+' || c == '-' || c == '*' || c == '/') {
                if (!number.isEmpty()) {
                    tokens.add(new Token(TokenType.NUMBER, number));
                    last = TokenType.NUMBER;
                    number = "";
                }
                if (last == TokenType.LEFTPAR || last == TokenType.UNARY_OP || last == TokenType.BINARY_OP) {
                    if (c != '+' && c != '-') {
                        throw new ParsingException("Invalid unary operation: " + c);
                    }
                    tokens.add(new Token(TokenType.UNARY_OP, "" + c));
                    last = TokenType.UNARY_OP;
                } else {
                    tokens.add(new Token(TokenType.BINARY_OP, "" + c));
                    last = TokenType.BINARY_OP;
                }
            } else if (Character.isWhitespace(c)) {
                if (!number.isEmpty()) {
                    tokens.add(new Token(TokenType.NUMBER, number));
                    last = TokenType.NUMBER;
                    number = "";
                }
            } else if (c == '(') {
                if (!number.isEmpty() || last == TokenType.NUMBER || last == TokenType.RIGHTPAR) {
                    throw new ParsingException("Operation expected, '(' found");
                }
                tokens.add(new Token(TokenType.LEFTPAR, "("));
                last = TokenType.LEFTPAR;
                pars++;
            } else if (c == ')') {
                if (!number.isEmpty()) {
                    tokens.add(new Token(TokenType.NUMBER, number));
                    last = TokenType.NUMBER;
                    number = "";
                }
                if (last == TokenType.LEFTPAR || last == TokenType.BINARY_OP || last == TokenType.UNARY_OP) {
                    throw new ParsingException("Number expected, ')' found");
                }
                if (pars <= 0) {
                    throw new ParsingException("Unpaired parentheses");
                }
                tokens.add(new Token(TokenType.RIGHTPAR, ")"));
                last = TokenType.RIGHTPAR;
                pars--;
            } else {
                throw new ParsingException("Invalid character: " + c);
            }
        }
        if (pars != 0) {
            throw new ParsingException("Unpaired parentheses");
        }
        return tokens;
    }

    protected ArrayList<Token> reorder(ArrayList<Token> tokens) {
        ArrayList<Token> result = new ArrayList<>();
        Stack<Token> stack = new Stack<>();
        for (Token t : tokens) {
            switch (t.getType()) {
                case NUMBER:
                    stack.push(t);
                    break;
                case UNARY_OP:
                case BINARY_OP:
                    while (!stack.empty() && stack.peek().getType() != TokenType.LEFTPAR &&
                            getPriority(stack.peek()) >= getPriority(t) && !isRightAssoc(stack.peek())) {
                        result.add(stack.pop());
                    }
                case LEFTPAR:
                    stack.push(t);
                    break;
                case RIGHTPAR:
                    while (!stack.empty() && stack.peek().getType() != TokenType.LEFTPAR) {
                        result.add(stack.pop());
                    }
                    stack.pop();
                default:
            }
        }
        while (!stack.empty()) {
            result.add(stack.pop());
        }
        return result;
    }
}
