package ru.mipt.java2017.homework.g696.semenov.task1;

import java.util.EmptyStackException;
import java.util.Stack;
import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;


public class MyCalculator implements Calculator {

  private Stack<Operator> operators;
  private Stack<Double> numbers;

  MyCalculator() {
    operators = new Stack<>();
    numbers = new Stack<>();
  }

  private class Operator {

    private Character operator;
    private int valence; // 1 - unary, 2 -binary

    Operator(char sign, int valence) {
      this.operator = sign;
      this.valence = valence;
    }

    Character getOperator() {
      return operator;
    }

    int getValence() {
      return valence;
    }
  }

  private void executeAnOperation() throws ParsingException {
    Operator currentOperator = operators.pop();
    if (currentOperator.getValence() == 1) {
      Double first;
      try {
        first = numbers.pop();
      } catch (EmptyStackException e) {
        throw new ParsingException(
          "The lack of arguments for a unary operator " + currentOperator.getOperator());
      }
      switch (currentOperator.getOperator()) {
        case '+':
          numbers.push(first);
          break;
        case '-':
          numbers.push((-first));
          break;
        default:
          throw new ParsingException(
            "Unary operator " + currentOperator.getOperator() + " unknown");
      }
    } else {
      Double first;
      Double second;
      try {
        first = numbers.pop();
        second = numbers.pop();
      } catch (EmptyStackException e) {
        throw new ParsingException(
          "The lack of arguments for a binary operator " + currentOperator.getOperator());
      }
      switch (currentOperator.getOperator()) {
        case '+':
          numbers.push(second + first);
          break;
        case '-':
          numbers.push(second - first);
          break;
        case '*':
          numbers.push(second * first);
          break;
        case '/':
          numbers.push(second / first);
          break;
        default:
          throw new ParsingException(
            "Binary operator " + currentOperator.getOperator() + " unknown");
      }
    }
  }

  private int getPriorityOfOperation(Operator currentOperator) throws ParsingException {
    if (currentOperator.valence == 1) {
      return 3;
    }
    switch (currentOperator.getOperator()) {
      case '+':
      case '-':
        return 1;
      case '/':
      case '*':
        return 2;
      case '(':
        return 0;
      default:
        throw new ParsingException("Operator " + currentOperator.getOperator() + " unknown");
    }
  }

  @Override
  public double calculate(String expression) throws ParsingException {

    boolean mayUnary = true;
    Operator currentOperator;

    if (expression == null || expression.isEmpty()) {
      throw new ParsingException("Expression is empty");
    }

    for (int currentPosition = 0; currentPosition < expression.length(); ++currentPosition) {
      if (expression.charAt(currentPosition) == '\n'
          || expression.charAt(currentPosition) == ' '
          || expression.charAt(currentPosition) == '\t') {
        continue;
      }
      switch (expression.charAt(currentPosition)) {
        case '(':
          operators.push(new Operator('(', 2));
          mayUnary = true;
          break;
        case ')':
          while (operators.lastElement().getOperator() != '(') {
            try {
              executeAnOperation();
              if (operators.size() == 0) {
                throw new ParsingException(
                  "Invalid arithmetic expression");
              }
            } catch (EmptyStackException e) {
              throw new ParsingException(
                "Invalid arithmetic expression");
            }
          }
          try {
            operators.pop();
          } catch (EmptyStackException e) {
            throw new ParsingException(
              "Invalid arithmetic expression");
          }
          mayUnary = false;
          break;
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
          String currentNumber = "";
          while (currentPosition < expression.length()
            && (Character.isDigit(expression.charAt(currentPosition))
            || expression.charAt(currentPosition) == '.')) {
            currentNumber += expression.charAt(currentPosition);
            ++currentPosition;
          }
          --currentPosition;
          try {
            double number = Double.parseDouble(currentNumber);
            numbers.push(number);
            mayUnary = false;
          } catch (NumberFormatException e) {
            throw new ParsingException("Invalid number format " + currentNumber);
          }
          break;
        case '-':
        case '+':
          currentOperator = new Operator(expression.charAt(currentPosition),
              mayUnary ? 1 : 2);
          while (!operators.empty()
            && getPriorityOfOperation(operators.lastElement()) >= getPriorityOfOperation(
            currentOperator)) {
            executeAnOperation();
          }
          operators.push(currentOperator);
          mayUnary = true;
          break;
        case '/':
        case '*':
          currentOperator = new Operator(expression.charAt(currentPosition), 2);
          while (!operators.empty()
            && getPriorityOfOperation(operators.lastElement()) >= getPriorityOfOperation(
            currentOperator)) {
            executeAnOperation();
          }
          operators.push(currentOperator);
          mayUnary = true;
          break;
        default:
          throw new ParsingException("Unknown symbol " + expression.charAt(currentPosition));
      }
    }

    while (!operators.isEmpty()) {
      executeAnOperation();
    }

    if (numbers.size() != 1) {
      throw new ParsingException("Invalid arithmetic expression");
    } else {
      return numbers.lastElement();
    }
  }
}
