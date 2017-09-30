package ru.mipt.java2017.homework.g696.mosyagin.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.io.Serializable;

/**
 * Class that stores information about a single token of Polish notation;
 *
 * @author Mosyagin Mikhail
 * @since 24.09.17
 */
public class Token {
    Token() {
        type = null;
    }

    Token(double value) {
        setValue(value);
    }

    Token(char operation) throws ParsingException {
        switch (operation) {
            case '|':
                setType(TokenType.UNARY_PLUS);
                break;
            case '~':
                setType(TokenType.UNARY_MINUS);
                break;
            case '+':
                setType(TokenType.BINARY_PLUS);
                break;
            case '-':
                setType(TokenType.BINARY_MINUS);
                break;
            case '*':
                setType(TokenType.MULTIPLICATION);
                break;
            case '/':
                setType(TokenType.DIVISION);
                break;
            case ';':
                setType(TokenType.END_OF_EXPRESSION);
                break;
            default:
                throw new ParsingException("Unknown operation " + operation);
        }
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public double getValue() throws ParsingException {
        if (type != TokenType.NUMBER) {
            throw new ParsingException("Cannot get value of a non-number token");
        }
        return value;
    }

    public void setValue(double value) {
        type = TokenType.NUMBER;
        this.value = value;
    }

    private TokenType type;
    private double value;
}
