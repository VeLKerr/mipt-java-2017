package ru.mipt.java2017.homework.g694.kozinov.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.Stack;

public class MyCalculator implements Calculator {
  private Stack<Double> numbers;
  private Stack<CharRecognizer.OperationKind> operations;

  MyCalculator() {
    numbers = new Stack<>();
    operations = new Stack<>();
  }

  private void process(CharRecognizer.OperationKind operation) throws ParsingException {
    if (operation.isUnary()) {
      if (numbers.empty()) {
        throw new ParsingException("Operation without operand");
      }

      double a = numbers.pop();
      switch (operation) {
        case UNARY_PLUS:
          numbers.push(a);
          break;
        case UNARY_MINUS:
          numbers.push(-a);
          break;
        default:
          throw new ParsingException("Find unknown operation (Internal error)");
      }
      return;
    }

    double a;
    double b;
    if (numbers.size() < 2) {
      throw new ParsingException("Operation without operands");
    }
    a = numbers.pop();
    b = numbers.pop();

    switch (operation) {
      case PLUS:
        numbers.push(a + b);
        break;
      case MINUS:
        numbers.push(b - a);
        break;
      case MULTI:
        numbers.push(a * b);
        break;
      case DIVISION:
        numbers.push(b / a);
        break;
      default:
        throw new ParsingException("Find unknown operation");
    }
  }

  @Override
  public double calculate(String expression) throws ParsingException {
    if (expression == null) {
      throw new ParsingException("Pass a null string");
    }
    numbers.clear();
    operations.clear();
    Boolean mayUnary = true;
    Parser currentParser = new Parser();
    currentParser.parseString(expression);
    currentParser.checkLexemes();

    for (int i = 0; i < currentParser.size(); ++i) {
      String currentLexeme = currentParser.getAt(i);
      CharRecognizer.OperationKind curop = CharRecognizer.getOperationKind(currentLexeme.charAt(0));

      switch (CharRecognizer.getCharKind(currentLexeme.charAt(0))) {
        case UNKNOWN:
          throw new ParsingException("Find unknown word");

        case OPEN_BRACKET:
          operations.push(curop);
          mayUnary = true;
          break;

        case NUMBER_PART:
          try {
            numbers.push(Double.parseDouble(currentLexeme));
          } catch (java.lang.NumberFormatException e) {
            throw new ParsingException("You write not a number: " + currentLexeme);
          }
          mayUnary = false;
          break;

        case CLOSE_BRACKET:
          while (operations.peek() != CharRecognizer.OperationKind.OPEN) {
            process(operations.pop());
          }

          operations.pop();
          mayUnary = false;
          break;

        case OPERATION:
          if (mayUnary) {
            curop = curop.makeUnary();
          }
          while (!operations.empty() && (
               !curop.isUnary() &&
              operations.peek().getPriority() >= curop.getPriority() ||
                curop.isUnary() &&
              operations.peek().getPriority() > curop.getPriority())) {
            process(operations.pop());
          }
          operations.push(curop);
          mayUnary = true;
          break;

        default:
          break;
      }
    }
    while (!operations.empty()) {
      process(operations.pop());
    }

    if (numbers.isEmpty()) {
      throw new ParsingException("Pass the expression without numbers");
    }
    return numbers.peek();
  }
}





