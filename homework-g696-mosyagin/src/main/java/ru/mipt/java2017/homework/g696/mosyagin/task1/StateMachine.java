package ru.mipt.java2017.homework.g696.mosyagin.task1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

import ru.mipt.java2017.homework.base.task1.ParsingException;

public class StateMachine implements Serializable {
    StateMachine() {
        state = StateTag.BEGIN;
        lastNumber = "";
        polishNotation = new ArrayList<Token>();
        operations = new Stack<Token>();
        subautomata = null;
    }

    public void transit(char character) throws ParsingException {
        if (!isAcceptable(character)) {
            throw new ParsingException("Unacceptable character: " + character);
        }

        if (state == StateTag.END) {
            throw new ParsingException("Cannot parse after the end of the expression");
        } else if (state == StateTag.SUBAUTOMATA) {
            handleSubautomata(character);
        } else if (character == '(') {
            handleOpeningBracket();
        } else if (character == ')') {
            handleClosingBracket();
        } else if (isOperation(character)) {
            handleOperation(character);
        } else if (Character.isDigit(character) || character == '.') {
            handleNumber(character);
        } else {
            handleWhtespace();
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

    private void handleSubautomata(char character) throws ParsingException {
        subautomata.transit(character);
        if (subautomata.hasFinished()) {
            polishNotation.add(new Token(subautomata.evaluateExpression()));
            subautomata = null;
            state = StateTag.NUMBER_END;
        }
    }

    private void handleOpeningBracket() throws ParsingException {
        if (state == StateTag.NUMBER || state == StateTag.NUMBER_END) {
            throw new ParsingException("Cannot parse '(' following a number");
        }
        state = StateTag.SUBAUTOMATA;
        subautomata = new StateMachine();
    }

    private void handleClosingBracket() throws ParsingException {
        if (state == StateTag.BEGIN
            || state == StateTag.BINARY_OPEARTION
            || state == StateTag.UNARY_OPERATION) {
            throw new ParsingException("Cannot parse ')' not following a number");
        }
        pushNumber();
        pushOperation(';');
        state = StateTag.END;
    }

    private void handleOperation(char operation) throws ParsingException {
        if (state == StateTag.UNARY_OPERATION) {
            throw new ParsingException("Cannot parse an operation following a unary operation");
        } else if (state == StateTag.BINARY_OPEARTION || state == StateTag.BEGIN) {
            pushOperation(makeUnary(operation));
            state = StateTag.UNARY_OPERATION;
        } else {
            pushNumber();
            pushOperation(operation);
            state = StateTag.BINARY_OPEARTION;
        }
    }

    private void handleNumber(char number) throws ParsingException {
        if (state == StateTag.NUMBER_END) {
            throw new ParsingException("Cannot handle two consecutive numbers");
        }
        lastNumber += number;
        state = StateTag.NUMBER;
    }

    private void handleWhtespace() throws ParsingException {
        if (state == StateTag.NUMBER) {
            pushNumber();
            state = StateTag.NUMBER_END;
        }
    }

    private void pushNumber() throws ParsingException {
        if (state != StateTag.NUMBER) {
            return;
        }

        try {
            polishNotation.add(new Token(Double.parseDouble(lastNumber)));
        } catch (NumberFormatException exception) {
            throw new ParsingException("Cannot parse number " + lastNumber);
        }

        lastNumber = "";
    }

    private void pushOperation(char operationSymbol) throws ParsingException {
        Token operation = new Token(operationSymbol);
        while (!operations.empty()
            && getOperationPriority(operations.peek()) >= getOperationPriority(operation)) {
            polishNotation.add(operations.pop());
        }
        operations.push(operation);
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

    private char makeUnary(char operation) throws ParsingException {
        if (operation == '*' || operation == '/') {
            throw new ParsingException("No such unary operation: " + operation);
        }

        return operation == '+' ? '|' : '~'; // '|' stands for unary '+', '~' stands for unary '-'
    }

    private boolean isOperation(char character) {
        return character == '+' || character == '-' || character == '*' || character == '/';
    }

    private boolean isAcceptable(char character) {
        return character == '(' || character == ')' || character == '.'
            || Character.isDigit(character) || Character.isWhitespace(character)
            || isOperation(character);
    }

    public boolean hasFinished() {
        return state == StateTag.END;
    }

    enum StateTag {
        BEGIN,
        BINARY_OPEARTION,
        UNARY_OPERATION,
        NUMBER,
        NUMBER_END,
        SUBAUTOMATA,
        END
    }

    private StateTag state;
    private String lastNumber;
    private ArrayList<Token> polishNotation;
    private Stack<Token> operations;
    private StateMachine subautomata;
}
