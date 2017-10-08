package ru.mipt.java2017.homework.g696.mikhaylov.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;
import ru.mipt.java2017.homework.base.task1.Calculator;

import java.util.ArrayList;
import java.util.Stack;

class MyCalculator implements Calculator {

  /**
   * Парсит выражение в обратную польскую запись, а потом вычиляет его значение
   *
   * @param expression выражение, значение которого необходимо вычислить
   * @return значение выражения
   * @throws ParsingException невозможно выполнить одну из операций или есть некорректное число
   */

  @Override
  public double calculate(String expression) throws ParsingException {
    MyParser parser = new MyParser();
    ArrayList<Lexeme> parsedExpression;
    Stack<Double> stack = new Stack<>();
    parsedExpression = parser.parse(expression);
    MyCalculatorFunctions functions = new MyCalculatorFunctions();

    for (Lexeme item: parsedExpression) {
      switch (item.getType()) {
        case NUMBER:
          try {
            stack.push(Double.parseDouble(item.getValue()));
          } catch (NumberFormatException e) {
            throw new ParsingException("Invalid number: " + item.getValue());
          }
          break;
        case BINARY_OPERATION:
          if (stack.size() < 2) {
            throw new ParsingException("Binary operation fault");
          }
          double second = stack.pop();
          double first = stack.pop();
          stack.push(functions.binaryOperations(item, first, second));
          break;
        case UNARY_OPERATION:
          if (stack.empty()) {
            throw new ParsingException("Unary operation fault");
          }
          double value = stack.pop();
          stack.push(functions.unaryOperations(item, value));
          break;
        default:
          break;
      }
    }
    return stack.pop();
  }
}
