package ru.mipt.java2017.homework.g696.mosyagin.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;

/**
 * State machine that converts a mathematical expressions to Polish notation
 * and finds its value.
 *
 * @author Mosyagin Mikhail
 * @since 24.09.17
 */
public class StateMachine {
    StateMachine() {
        state = StateTag.BEGIN;
        lastNumber = "";
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

    public boolean hasFinished() {
        return state == StateTag.END;
    }

    public PolishNotation getResult() throws ParsingException {
        if (!hasFinished()) {
            throw new ParsingException("Cannot return result before finished");
        }

        return result;
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
            result.pushToken(new Token(Double.parseDouble(lastNumber)));
        } catch (NumberFormatException exception) {
            throw new ParsingException("Cannot parse number " + lastNumber);
        }

        lastNumber = "";
    }

    private void pushOperation(char operationSymbol) throws ParsingException {
        result.pushToken(new Token(operationSymbol));
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
    private PolishNotation result;
    private StateMachine subautomata;
}
