package ru.mipt.java2017.homework.g695.starostin.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.EmptyStackException;
import java.util.Stack;

/**
 * @author Ivan Starostin
 */

public class MyCalculator implements Calculator {

  @Override
  public double calculate(String expression) throws ParsingException {
    return calculateExpression(parseExpression(expression));
  }

  //p denotes unary +, n denotes unary -
  private char convertOperator(char operator, boolean isBinary) throws ParsingException {
    if (!isBinary) {
      switch (operator) {
        case '+':
          return 'p';
        case '-':
          return 'n';
        default:
          throw new ParsingException("Wrong usage of operator: " + String.valueOf(operator));
      }
    }
    return operator;
  }

  private int getOperatorPriority(char operator) {
    switch (operator) {
      case '+':
        return 1;
      case '-':
        return 2;
      case '*':
        return 3;
      case '/':
        return 4;
      case 'n':
        return 5;
      case 'p':
        return 5;
      default:
        return -1;
    }
  }

  private boolean isDigit(char c) {
    return c >= '0' && c <= '9' || c == '.';
  }

  private boolean isOperator(char c) {
    return c == '+' || c == '-' || c == '*' || c == '/';
  }

  private String parseExpression(String expression) throws ParsingException {
    if (expression == null) {
      throw new ParsingException("Argument null");
    }
    Stack<Character> operatorStack = new Stack<>();
    String postfixExpression = "";
    expression = expression.replaceAll("\\s", "");
    expression = expression.replaceAll("\t", "");
    expression = expression.replaceAll("\n", "");
    boolean nextOperatorBinary = false;
    boolean operatorAllowed = true;
    for (int i = 0; i < expression.length(); ++i) {
      char currentCharacter = expression.charAt(i);
      if (isDigit(currentCharacter)) {
        operatorAllowed = true;
        nextOperatorBinary = true;
        postfixExpression += currentCharacter;
        if (i + 1 >= expression.length() || !isDigit(expression.charAt(i + 1))) {
          postfixExpression += ' ';
        }
      } else if (currentCharacter == '(') {
        operatorAllowed = true;
        nextOperatorBinary = false;
        operatorStack.push('(');
        operatorAllowed = true;
      } else if (currentCharacter == ')') {
        operatorAllowed = true;
        nextOperatorBinary = true;
        try {
          for (char topOperator = operatorStack.pop(); topOperator != '(';
              topOperator = operatorStack.pop()) {
            postfixExpression += topOperator;
            postfixExpression += ' ';
          }
        } catch (EmptyStackException e) {
          throw new ParsingException("Too few braces");
        }
      } else if (isOperator(currentCharacter)) {
        if (!nextOperatorBinary) {
          if (!operatorAllowed) {
            throw new ParsingException("Double unary operators are not permitted");
          }
          operatorAllowed = false;
        }
        currentCharacter = convertOperator(currentCharacter, nextOperatorBinary);
        while (!operatorStack.isEmpty() && operatorStack.peek() != '('
          && getOperatorPriority(operatorStack.peek()) >= getOperatorPriority(currentCharacter)) {
          postfixExpression += operatorStack.pop();
          postfixExpression += ' ';
        }
        nextOperatorBinary = false;
        operatorStack.push(currentCharacter);
      } else {
        throw new ParsingException("Unrecognized character: " + String.valueOf(currentCharacter));
      }
    }
    while (!operatorStack.isEmpty()) {
      if (operatorStack.peek() == '(') {
        throw new ParsingException("Too many opening braces");
      }
      postfixExpression += operatorStack.pop();
      postfixExpression += ' ';
    }
    return postfixExpression;
  }

  private void executeOperation(Stack<Double> operandStack, char operator) throws ParsingException {
    double value = operandStack.pop();
    switch (operator) {
      case 'n':
        operandStack.push(-1.0 * value);
        break;
      case 'p':
        operandStack.push(value);
        break;
      case '+':
        operandStack.push(operandStack.pop() + value);
        break;
      case '-':
        operandStack.push(operandStack.pop() - value);
        break;
      case '*':
        operandStack.push(operandStack.pop() * value);
        break;
      case '/':
        operandStack.push(operandStack.pop() / value);
        break;
      default:
        throw new ParsingException("Unknown operator");
    }
  }

  private double calculateExpression(String postfixExpression) throws ParsingException {
    Stack<Double> operandStack = new Stack<>();
    for (int i = 0; i < postfixExpression.length(); ++i) {
      char currentCharacter = postfixExpression.charAt(i);
      if (isDigit(currentCharacter)) { //read the whole number
        int j = i;
        while (isDigit(postfixExpression.charAt(j))) {
          ++j;
        }
        try {
          operandStack.push(Double.parseDouble(postfixExpression.substring(i, j)));
        } catch (NumberFormatException e) {
          throw new ParsingException("Wrong number format");
        }
        i = j;
      }
      if (isOperator(currentCharacter) || currentCharacter == 'n' || currentCharacter == 'p') {
        executeOperation(operandStack, currentCharacter);
      }
    }
    try {
      return operandStack.pop();
    } catch (EmptyStackException e) {
      throw new ParsingException("Wrong expression format");
    }
  }
}
