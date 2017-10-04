package ru.mipt.java2017.homework.g694.perchuk.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;
import java.util.Stack;


/**
 * Implements calculation.
 */
class Evaluator {

  private Stack<Double> operands = new Stack<>();
  private Stack<Character> operations = new Stack<>();

  private void popOperation() throws ParsingException {
    if ((operations.isEmpty()) || (operands.size() < 2)) {
      throw new ParsingException("in operating");
    }

    double num2 = operands.pop();
    double num1 = operands.pop();

    switch (operations.pop()) {
      case '+':
        operands.push(num1 + num2);
        break;
      case '-':
        operands.push(num1 - num2);
        break;
      case '/':
        operands.push(num1 / num2);
        break;
      case '#':
      case '*':
        operands.push(num1 * num2);
        break;
      default:
        throw new ParsingException("in operating");
    }
  }

  private short priority(Character ch) throws ParsingException {
    switch (ch) {
      case '(':
        return 3;
      case '+':
      case '-':
        return 2;
      case '*':
      case '/':
        return 1;
      case '#':
        return 0;
      default:
        throw new ParsingException("invalid symbol");
    }
  }

  void addLeftBrace() {
    operations.push('(');
  }

  void addRightBrace() throws ParsingException {
    while ((!operations.empty()) && (operations.peek() != '(')) {
      popOperation();
    }
    if (operations.empty()) {
      throw new ParsingException("missing brace");
    }
    operations.pop();
  }

  void addDouble(Double num) {
    operands.add(num);
  }

  void addOperation(Character op) throws ParsingException {
    while ((!operations.empty()) && (priority(op) >= priority(operations.peek()))) {
      popOperation();
    }
    operations.push(op);
  }

  Double completeEvaluation() throws ParsingException {
    while (!operations.empty()) {
      popOperation();
    }
    if (operands.size() != 1) {
      throw new ParsingException("in evaluating");
    }
    return operands.pop();
  }
}
