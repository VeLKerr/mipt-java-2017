package ru.mipt.java2017.homework.g694.yusupov.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Non-recursive calculator realization using stack
 *
 * @author Nikita O. Yusupov
 * @since 01.10.17
 */

public class MyCalc implements Calculator {

  private String expression;

  private Stack<Integer> operations = new Stack<>();
  private Stack<Double> operands = new Stack<>();

  private boolean isUnary;

  /**
   * @param exp Arithmetic expression to calculate
   * @return the value of the arithmetic expression
   * @throws ParsingException exception thrown in case of invalid expression
   */

  public double calculate(String exp) throws ParsingException {
    expression = exp;

    checkNotNull();
    deleteSpaces();
    checkNotEmpty();
    checkInvalidCharacters();
    checkBracketBalance();

    return solve();
  }

  /**
   * Checks if the expression is null
   *
   * @throws ParsingException the exception is thrown if expression is null
   */

  private void checkNotNull() throws ParsingException {
    if (expression == null) {
      throw new ParsingException("Expression is null");
    }
  }

  /**
   * Removes all spaces
   */

  private void deleteSpaces() {
    expression = expression.replaceAll("\\s", "");
  }

  /**
   * Checks if the expression contains invalid characters
   *
   * @throws ParsingException the exception is thrown if expression contains invalid characters
   */

  private void checkInvalidCharacters() throws ParsingException {
    String buffer = expression.replaceAll("[^.()*/0-9+-]", "");
    if (buffer.length() != expression.length()) {
      throw new ParsingException("Invalid characters");
    }
  }

  /**
   * Checks if the expression is empty
   *
   * @throws ParsingException the exception is thrown if expression is empty
   */

  private void checkNotEmpty() throws ParsingException {
    if (expression.isEmpty()) {
      throw new ParsingException("String is empty");
    }
  }

  /**
   * Checks the bracket balance of the expression
   *
   * @throws ParsingException the exception is thrown if the bracket balance of the expression is
   * invalid
   */

  private void checkBracketBalance() throws ParsingException {
    int balance = 0;
    boolean ok = true;
    for (int i = 0; i < expression.length(); i++) {
      if (expression.charAt(i) == '(') {
        balance++;
      } else if (expression.charAt(i) == ')') {
        balance--;
      }
      if (balance < 0) {
        ok = false;
      }
    }
    if (balance != 0) {
      ok = false;
    }
    if (!ok) {
      throw new ParsingException("Invalid bracket balance");
    }
  }

  /**
   * Calculates value of arithmetic expression with the only one operation
   *
   * @throws ParsingException exception thrown in case of invalid expression
   */

  private void calculateOneOperation() throws ParsingException {
    Integer operation;
    try {
      operation = operations.pop();
    } catch (EmptyStackException e) {
      throw new ParsingException("Wrong number of operations");
    }
    if (operation >= 7) {
      Double operand;
      try {
        operand = operands.pop();
      } catch (EmptyStackException e) {
        throw new ParsingException("Wrong number of operands");
      }
      switch (operation) {
        case 7:
          operands.push(operand);
          break;
        case 8:
          operands.push(-operand);
          break;

        default:
          break;
      }
    } else {
      Double rightOperand;
      Double leftOperand;
      try {
        rightOperand = operands.pop();
        leftOperand = operands.pop();
      } catch (EmptyStackException e) {
        throw new ParsingException("Wrong number of operands");
      }
      switch (operation) {
        case 3:
          operands.push(leftOperand + rightOperand);
          break;
        case 4:
          operands.push(leftOperand - rightOperand);
          break;
        case 5:
          operands.push(leftOperand * rightOperand);
          break;
        case 6:
          operands.push(leftOperand / rightOperand);
          break;

        default:
          break;
      }
    }
  }

  /**
   * Calculates code of operation
   *
   * @param operation operation to calculate code of
   * @return Code of operation
   */

  private Integer operationCode(char operation) {
    switch (operation) {
      case '(':
        return 1;
      case ')':
        return 2;

      case '*':
        return 5;
      case '/':
        return 6;

      default:
        break;
    }
    if (!isUnary) {
      switch (operation) {
        case '+':
          return 3;
        case '-':
          return 4;

        default:
          break;
      }
    }
    if (isUnary) {
      switch (operation) {
        case '+':
          return 7;
        case '-':
          return 8;

        default:
          break;
      }
    }
    return -1;
  }

  /**
   * Checks if character corresponds to any operation or not
   *
   * @param character character to check
   * @return true if character corresponds to any operation, false otherwise
   */

  private boolean isOperation(char character) {
    return character == '+' || character == '-' ||
        character == '*' || character == '/' ||
        character == '(' || character == ')';
  }

  /**
   * Calculates priority of operation
   *
   * @param operation Operation to calculate priority of
   * @return the number corresponding to the priority of the operation
   */

  private int operationPriority(Integer operation) {
    if (operation <= 2) {
      return 0;
    }
    if (operation <= 4) {
      return 1;
    }
    if (operation <= 6) {
      return 2;
    }
    if (operation <= 8) {
      return 3;
    }
    return -1;
  }

  /**
   * The main algorithm to calculate value of arithmetic expression
   *
   * @return the value of the arithmetic expression
   * @throws ParsingException exception thrown in case of invalid expression
   */

  private double solve() throws ParsingException {
    isUnary = true;
    for (int i = 0; i < expression.length(); i++) {
      char current = expression.charAt(i);
      if (current == '(') {
        operations.push(operationCode(current));
        isUnary = true;
      } else if (current == ')') {
        while (!operations.peek().equals(operationCode('('))) {
          calculateOneOperation();
        }
        operations.pop();
        isUnary = false;
      } else if (isOperation(current)) {
        Integer operation = operationCode(current);
        while (!operations.empty()
            && operationPriority(operations.peek()) >= operationPriority(operation)) {
          calculateOneOperation();
        }
        operations.push(operation);
        isUnary = true;
      } else {
        StringBuilder builder = new StringBuilder();
        while (i < expression.length() && !isOperation(expression.charAt(i))) {
          builder.append(expression.charAt(i));
          i++;
        }
        i--;
        String buffer = builder.toString();
        try {
          Double operand = new Double(buffer);
          operands.push(operand);
        } catch (NumberFormatException e) {
          throw new ParsingException(e.getMessage());
        }
        isUnary = false;
      }
    }
    while (!operations.empty()) {
      calculateOneOperation();
    }
    if (operands.size() != 1) {
      throw new ParsingException("Parsing error");
    }
    return operands.peek();
  }
}