package ru.mipt.java2017.homework.g694.cherepkov.task1;

import java.util.EmptyStackException;
import java.util.NoSuchElementException;
import java.util.Stack;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

/**
 * @author Cherepkov Anton
 */
public class MyCalculator implements Calculator {

  private Stack<Operation> operations;
  private Stack<Double> numbers;

  /**
   * Constructor
   */
  MyCalculator() {
    operations = new Stack<>();
    numbers = new Stack<>();
  }

  /**
   * Clears stacks (operations, numbers) for algorithm to be done correctly
   */
  private void initialize() {
    operations.clear();
    numbers.clear();
  }

  /**
   * @param operation current operation to be proceeded
   * @throws ParsingException in case operation can not be done (e.g. missing numbers in
   * expression)
   */
  private void proceedOperation(Operation operation) throws ParsingException {
    Double leftNumber;
    Double rightNumber;
    Double resultNumber;

    if (operation.isUnary()) {
      try {
        leftNumber = numbers.pop();
      } catch (EmptyStackException e) {
        throw new ParsingException("Missing number for unary operator");
      }
      switch (operation.getSign()) {
        case '+':
          resultNumber = leftNumber;
          break;
        case '-':
          resultNumber = -leftNumber;
          break;
        default:
          throw new ParsingException("Unknown unary operator");
      }
    } else {
      try {
        rightNumber = numbers.pop();
        leftNumber = numbers.pop();
      } catch (EmptyStackException e) {
        throw new ParsingException("Missing number for binary operator");
      }
      switch (operation.getSign()) {
        case '+':
          resultNumber = leftNumber + rightNumber;
          break;
        case '-':
          resultNumber = leftNumber - rightNumber;
          break;
        case '*':
          resultNumber = leftNumber * rightNumber;
          break;
        case '/':
          resultNumber = leftNumber / rightNumber;
          break;
        default:
          throw new ParsingException("Unknown binary operator");
      }
    }

    numbers.push(resultNumber);
  }

  /**
   * @param expression string containing arithmetic expression
   * @return result of expression
   * @throws ParsingException in case of parsing error
   */
  @Override
  public double calculate(String expression) throws ParsingException {

    if (expression == null || expression.isEmpty()) {
      throw new ParsingException("Expression is empty");
    }
    initialize();

    boolean unaryAllowedHere = true;

    for (int position = 0; position < expression.length(); ++position) {

      if ((int) expression.charAt(position) <= 32) {
        continue;
      }

      if (expression.charAt(position) == '(') {
        operations.push(new Operation('(', false));
        unaryAllowedHere = true;
      } else if (expression.charAt(position) == ')') {
        try {
          while (operations.lastElement().getSign() != '(') {
            proceedOperation(operations.pop());
          }
          operations.pop();
          unaryAllowedHere = false;
        } catch (NoSuchElementException e) {
          throw new ParsingException("Missing bracket");
        }
      } else if (Operation.isOperation(expression.charAt(position))) {
        Operation newOperation = new Operation(expression.charAt(position),
            unaryAllowedHere && Operation.canBeUnary(expression.charAt(position)));
        while (!operations.isEmpty() && operations.lastElement().getPriority() >= newOperation
            .getPriority()) {
          proceedOperation(operations.pop());
        }
        operations.push(newOperation);
        unaryAllowedHere = true;
      } else if (Character.isDigit(expression.charAt(position))) {
        String stringNumber = "";

        while (position < expression.length()
            && (Character.isDigit(expression.charAt(position))
            || expression.charAt(position) == '.')) {
          stringNumber += expression.charAt(position);
          ++position;
        }
        --position;
        try {
          numbers.push(Double.parseDouble(stringNumber));
          unaryAllowedHere = false;
        } catch (NumberFormatException e) {
          throw new ParsingException("Invalid number format");
        }
      } else {
        throw new ParsingException("Unknown character");
      }

    }

    while (!operations.isEmpty()) {
      proceedOperation(operations.pop());
    }

    if (numbers.size() != 1) {
      throw new ParsingException("Unknown error");
    } else {
      return numbers.lastElement();
    }

  }

}