package ru.mipt.java2017.homework.g694.kozinov.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;
import java.util.Stack;

public class MyCalculator implements Calculator {
  private Stack<Double> numbers;
  private Stack<Character> operations;

  MyCalculator() {
    numbers = new Stack<>();
    operations = new Stack<>();
  }

  private void process(char operation) throws ParsingException {
    if (numbers.size() < 2) {
      throw new ParsingException("Too much operations");
    }

    double a = numbers.pop();
    double b = numbers.pop();

    switch (CharRecognizer.getOperationKind(operation)) {
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
    numbers.clear();
    operations.clear();
    Parser currentParser = new Parser();
    currentParser.parseString(expression);

    for (int i = 0; i < currentParser.size(); ++i) {
      String currentLexeme = currentParser.getAt(i);

      switch (CharRecognizer.getCharKind(currentLexeme.charAt(0))) {
        case UNKNOWN:
          throw new ParsingException("Find unknown word");

        case OPEN_BRACKET:
          operations.push('(');
          break;

        case NUMBER_PART:
          numbers.push(Double.parseDouble(currentLexeme));
          break;

        case CLOSE_BRACKET:
          while (CharRecognizer.getCharKind(operations.peek()) != CharRecognizer.CharKind.OPEN_BRACKET) {
            process(operations.pop());
          }

          operations.pop();
          break;

        case OPERATION:
          CharRecognizer.OperationKind curop = CharRecognizer.getOperationKind(currentLexeme.charAt(0));
          while (!operations.empty() &&
            CharRecognizer.getOperationKind(operations.peek()).getPriority() >= curop.getPriority()) {
            process(operations.pop());
          }
          operations.push(currentLexeme.charAt(0));
          break;

        default:
          break;
      }
    }
    while (!operations.empty()) {
      process(operations.pop());
    }

    return numbers.peek();
  }
}





