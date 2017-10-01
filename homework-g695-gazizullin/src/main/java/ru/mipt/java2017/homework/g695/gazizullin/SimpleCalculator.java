package ru.mipt.java2017.homework.g695.gazizullin;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.Stack;

import static ru.mipt.java2017.homework.g695.gazizullin.Parser.*;

/**
 * @author Malik R. Gazizullin
 * @since 01.10.17
 */
public class SimpleCalculator implements Calculator {
    /**
     * Get valid string expressions.
     * Returns result of calculations
     * Expression can contain decimal double numbers,
     *  binary operators +, -, *, / and unary operators +, -
     *  and priroty operations (, ).
     * Expression can contain sny space symbols
     *
     * @param expression with arithmetical operations and numbers
     * @return result of calculations
     * @throws ParsingException cannot parse expression
     *
     */
    @Override
    public double calculate(String expression) throws ParsingException {
        expression = addUsefulSpaces("(" + expression + ")");
        expression = remakeUnaryToBinary(expression);
        String[] expressionWithoutSpace = expression.trim().split("[\\s]+");

        Stack<Double> numbers = new Stack<Double>();
        Stack<String> operators = new Stack<String>();

        for (int i = 0; i < expressionWithoutSpace.length; ++i) {
            if (isNumber(expressionWithoutSpace[i])) {
                numbers.push(getNumber(expressionWithoutSpace[i]));
            } else if (isOperator(expressionWithoutSpace[i])) {
                if (isBracket(expressionWithoutSpace[i])) {
                    if (isCloseBracket(expressionWithoutSpace[i])) {
                        while (!operators.empty() && !isOpenBracket(operators.peek())) {
                            calculateBinary(numbers, operators);
                        }
                        if (operators.empty()) {
                            throw new ParsingException("No open Brasket");
                        } else {
                            operators.pop();
                        }
                    } else {
                        operators.push(expressionWithoutSpace[i]);
                    }
                } else {
                    while (!operators.empty() && isLesserThan(operators.peek(), expressionWithoutSpace[i])) {
                        calculateBinary(numbers, operators);
                    }
                    operators.push(expressionWithoutSpace[i]);
                }
            } else {
                throw new ParsingException("Something unusual!");
            }
        }
        if (numbers.isEmpty()) {
            throw new ParsingException("Empty???");
        }
        double resultOfExpression = numbers.pop();
        if (numbers.isEmpty()) {
            return resultOfExpression;
        } else {
            throw new ParsingException("Some numbers are in Stack!!!");
        }
    }


    /**
     *
     * Calculates some binary operation with two operands and push it into numbers double stack
     * @param numbers - double stack with two numbers in top to calculate
     * @param operators - stack with one opertaor on top to calculate
     * @throws ParsingException if cannot calculate or there is no numbers there
     */
    private void calculateBinary(Stack<Double> numbers, Stack<String> operators) throws ParsingException {
        if (numbers.size() >= 2) {
            double secondNumber = numbers.pop();
            double firstNumber = numbers.pop();
            String operator = operators.pop();
            if (isPlus(operator)) {
                numbers.push(firstNumber + secondNumber);
            } else if (isMinus(operator)) {
                numbers.push(firstNumber - secondNumber);
            } else if (isMultiply(operator)) {
                numbers.push(firstNumber * secondNumber);
            } else if (isDivide(operator)) {
                // INfiny.positive Infinity Negative
                numbers.push(firstNumber / secondNumber);
            } else {
                throw new ParsingException("It is not an operator!!!");
            }
        } else {
            throw new ParsingException("Operands are necessary");
        }
    }
}
