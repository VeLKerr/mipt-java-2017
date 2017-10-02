package ru.mipt.java2017.homework.g696.pak.task1;

import sun.security.pkcs.ParsingException;

import java.util.*;

public class Parser implements ExpressionParser {

    private PrimaryExpressionParser primaryExpressionParser_;

    private boolean isSeparationSign(char symbol) {
        return symbol == ' ' || symbol == '\t' || symbol == '\n';
    }

    private boolean isDigit(char symbol) {
        return (symbol >= '0' && symbol <= '9');
    }

    private boolean isBracket(char symbol) {
        return (symbol == ')' || symbol == '(');
    }

    private boolean isOperator(char symbol) {
        return symbol == '+' || symbol == '-' || symbol == '/' || symbol == '*';
    }

    //check if valid except amount of operators and operands
    private boolean checkIfValid_(String expression) throws ParsingException {
        int balance = 0;
        int amountOfDots = 0;
        if (expression == null || expression.length() == 0)
            throw new ParsingException();
        char symbol = expression.charAt(0);

        if (symbol == '(')
            ++balance;
        if (symbol == ')')
            throw new ParsingException();

        if (!(isBracket(symbol) || isDigit(symbol) || symbol == '+' || symbol == '-'
            || isSeparationSign(symbol)))
            throw new ParsingException();

        for (int iter = 1; iter < expression.length() - 1; ++iter) {
            symbol = expression.charAt(iter);

            if (!(isBracket(symbol) || isDigit(symbol) || isOperator(symbol) ||
                isSeparationSign(symbol) || symbol == '.'
            ))
                throw new ParsingException();

            if (expression.charAt(iter) == '.')
                ++amountOfDots;

            if (isOperator(symbol) || isBracket(symbol))
                amountOfDots = 0;

            if (amountOfDots > 1)
                throw new ParsingException();

            if (expression.charAt(iter) == '.' && !(isDigit(expression.charAt(iter + 1)) &&
                isDigit(expression.charAt(iter - 1))))
                throw new ParsingException();

            if (expression.charAt(iter) == '(')
                balance += 1;
            if (expression.charAt(iter) == ')')
                balance -= 1;
            if (balance < 0)
                throw new ParsingException();

        }

        symbol = expression.charAt(expression.length() - 1);

        if (symbol == ')')
            --balance;
        if (symbol == '(' || balance != 0)
            throw new ParsingException();
        if (!(isBracket(symbol) || isDigit(symbol) || isSeparationSign(symbol)))
            throw new ParsingException();

        return true;
    }

    Parser() {
        primaryExpressionParser_ = new PrimaryExpressionParser();
    }

    @Override
    public Calculable parse(String expression) throws ParsingException {
        if (!checkIfValid_(expression))
            throw new ParsingException(); // check if expression is valid
        Stack<String> stack = new Stack<String>();
        LinkedList<ExpressionForCalculation> queue = new LinkedList<ExpressionForCalculation>();
        boolean isUnaryOperationAvailable = true;

        for (int iter = 0; iter < expression.length(); ++iter) { //parsing the expression

            if (expression.charAt(iter) == '(') {
                stack.push("(");
                isUnaryOperationAvailable = true;
            } else if (expression.charAt(iter) == ')') {
                while (!stack.peek().equals("(")) {
                    queue.addLast(new Operator(stack.pop()));
                }

                isUnaryOperationAvailable = false;
                stack.pop();

            } else if (expression.charAt(iter) == '+' || expression.charAt(iter) == '-') {
                if (isUnaryOperationAvailable && (expression.charAt(iter) == '-'
                    || expression.charAt(iter) == '+')) {
                    if (expression.charAt(iter) == '-') {
                        stack.push("*");
                        queue.addLast(new Constant(-1));
                    }
                } else {
                    while (!stack.empty() && (stack.peek().equals("+") || stack.peek().equals("-")
                        || stack.peek().equals("*") || stack.peek().equals("/"))) {
                        queue.addLast(new Operator(stack.pop()));
                    }
                    stack.push(expression.charAt(iter) + "");
                }

                if (expression.charAt(iter) == '*' || expression.charAt(iter) == '/')
                    isUnaryOperationAvailable = true;
                else
                    isUnaryOperationAvailable = false;

            } else if (expression.charAt(iter) == '*' || expression.charAt(iter) == '/') {
                while (!stack.empty() && (stack.peek().equals("*") || stack.peek().equals("/"))) {
                    queue.addLast(new Operator(stack.pop()));
                }
                stack.push(expression.charAt(iter) + "");
                isUnaryOperationAvailable = true;
            } else {
                while (iter < expression.length() &&
                    !((expression.charAt(iter) >= '0' && expression.charAt(iter) <= '9') ||
                        expression.charAt(iter) == '(' || expression.charAt(iter) == ')' ||
                        expression.charAt(iter) == '+' || expression.charAt(iter) == '-' ||
                        expression.charAt(iter) == '*' || expression.charAt(iter) == '/')
                    )
                    ++iter;

                StringBuffer s = new StringBuffer();
                while (iter < expression.length() && expression.charAt(iter) >= '0'
                    && expression.charAt(iter) <= '9') {
                    s.append(expression.charAt(iter));
                    ++iter;
                }

                if (iter < expression.length() && expression.charAt(iter) == '.') {
                    s.append('.');
                    ++iter;
                    while (iter < expression.length() && expression.charAt(iter) >= '0'
                        && expression.charAt(iter) <= '9') {
                        s.append(expression.charAt(iter));
                        ++iter;
                    }
                }

                --iter;
                if (s.length() > 0) {
                    queue.addLast((Constant) primaryExpressionParser_.parse(s.toString()));
                    isUnaryOperationAvailable = false;
                }
            }
        }
        while (!stack.empty())
            queue.addLast(new Operator(stack.pop()));

        return new CalculableExpression(queue);
    }
}
