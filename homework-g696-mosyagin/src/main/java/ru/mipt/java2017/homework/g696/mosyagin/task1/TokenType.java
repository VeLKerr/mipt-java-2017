package ru.mipt.java2017.homework.g696.mosyagin.task1;

/**
 * Token types enumeration.
 *
 * @author Mikhail Mosyagin
 */
public enum TokenType {
    /**
     * Unary {@code +} operator, internally represented by '|'
     */
    UNARY_PLUS,
    /**
     * Unary {@code -} operator, internally represented by '~'
     */
    UNARY_MINUS,
    /**
     * Binary {@code +} operator
     */
    BINARY_PLUS,
    /**
     * Binary {@code -} operator
     */
    BINARY_MINUS,
    /**
     * Binary {@code *} operator
     */
    MULTIPLICATION,
    /**
     * Binary {@code /} operator
     */
    DIVISION,
    /**
     * Marks the end of an expression, internally represented by ';'
     */
    END_OF_EXPRESSION,
    /**
     * A decimal number, internally represented by a {@code double} value
     */
    NUMBER
}
