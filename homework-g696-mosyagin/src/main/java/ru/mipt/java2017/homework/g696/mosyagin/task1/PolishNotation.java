package ru.mipt.java2017.homework.g696.mosyagin.task1;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

public class PolishNotation {
    PolishNotation() {
        polishNotation = new ArrayList<Token>();
        operations = new Stack<Token>();
    }

    public void pushToken(Token token) {
        if (token.getType() == TokenType.NUMBER) {
            polishNotation.add(token);
        } else {
            // the token is either an operation or the end of the expression
            while (!operations.empty()
                && getOperationPriority(operations.peek()) >= getOperationPriority(operation)) {
                polishNotation.add(operations.pop());
            }
            operations.push(operation);
        }
    } 

    public double evaluateExpression() throws ParsingException {
        Stack<Double> results = new Stack<Double>();

        for (Token token : polishNotation) {
            try {
                switch (token.getType()) {
                    case UNARY_PLUS:
                        break;
                    case UNARY_MINUS:
                        results.push(-results.pop());
                        break;
                    case BINARY_PLUS:
                        results.push(results.pop() + results.pop());
                        break;
                    case BINARY_MINUS:
                        results.push(-results.pop() + results.pop());
                        break;
                    case MULTIPLICATION:
                        results.push(results.pop() * results.pop());
                        break;
                    case DIVISION:
                        results.push(1 / results.pop() * results.pop());
                        break;
                    case NUMBER:
                        results.push(token.getValue());
                        break;
                    default:
                        throw new ParsingException("Unexpected token in Polish notation");
                }
            } catch (EmptyStackException exception) {
                throw new ParsingException("Not enough operands for operation");
            }
        }

        if (results.size() != 1) {
            throw new ParsingException("Failed to evaluate expression");
        }
        return results.pop();
    }

    private int getOperationPriority(Token operation) throws ParsingException {
        switch (operation.getType()) {
            case END_OF_EXPRESSION:
                return Integer.MIN_VALUE;
            case BINARY_MINUS:
            case BINARY_PLUS:
                return 0;
            case DIVISION:
            case MULTIPLICATION:
                return 1;
            case UNARY_MINUS:
            case UNARY_PLUS:
                return 2;
            default:
                throw new ParsingException("Unknown operation " + operation);
        }
    }

    private ArrayList<Token> polishNotation;
    private Stack<Token> operations;
}
