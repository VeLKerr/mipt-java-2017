package ru.mipt.java2017.homework.g696.feofanova.task1;

import java.util.Stack;
import ru.mipt.java2017.homework.base.task1.ParsingException;

public class ShuntingYardHandler implements ExpressionHandler {

  public ShuntingYardHandler() {
    numbersStack = new Stack<Double>();
    functionsStack = new Stack<Character>();
    functionsStack.push('(');
    isPrevNumber = false;
  }

  public void pushNumber(double number) throws ParsingException {
    numbersStack.push(number);
    isPrevNumber = true;
  }

  public void pushOperand(char operand) throws ParsingException {
    if ((operand == '+' || operand == '-') && !isPrevNumber) {
      switch (functionsStack.peek()) {
        case '(':
          numbersStack.push(0.0);
          break;
        case '/': case '*':
          if (operand == '-') {
            numbersStack.push(-numbersStack.pop());
          }
          return;
        default:
          throw new ParsingException("Redundant operand");
      }
    }

    if (operand == ')') {
      while (!functionsStack.empty() && functionsStack.peek() != '(') {
        operate(numbersStack, functionsStack);
      }
      if (functionsStack.empty()) {
        throw new ParsingException("Redundant close brace");
      }
      functionsStack.pop();
    } else if (operand != ' ' && operand != '\t' && operand != '\n') {
      while (functionsStack.size() != 0 && canPop(functionsStack.peek(), operand)) {
        operate(numbersStack, functionsStack);
      }
      functionsStack.push(operand);
      isPrevNumber = false;
    }
  }

  public double getAnswer() throws ParsingException {
    if (numbersStack.size() != 1 || !functionsStack.empty()) {
      throw new ParsingException("Invalid expression");
    }
    return numbersStack.peek();
  }

  private int getPriority(char operand) throws ParsingException {
    switch (operand) {
      case ')':
        return Integer.MAX_VALUE;
      case '+': case '-':
        return 2;
      case '*': case '/':
        return 1;
      case '(':
        return 0;
      default:
        throw new ParsingException("Unknown operand");
    }
  }

  private boolean canPop(char firstOperand, char secondOperand) throws ParsingException {
    int firstPriority = getPriority(firstOperand);
    int secondPriority = getPriority(secondOperand);
    return firstPriority > 0 && secondPriority > 0 && firstPriority <= secondPriority;
  }

  private void operate(Stack<Double> numbersStack, Stack<Character> functionsStack) throws ParsingException {
    char operand = functionsStack.pop();

    if (numbersStack.size() < 2) {
      throw new ParsingException("Invalid expression");
    }

    double secondNumber = numbersStack.pop();
    double firstNumber = numbersStack.pop();
    switch (operand) {
      case '+':
        numbersStack.push(firstNumber + secondNumber);
        break;
      case '-':
        numbersStack.push(firstNumber - secondNumber);
        break;
      case '*':
        numbersStack.push(firstNumber * secondNumber);
        break;
      case '/':
        numbersStack.push(firstNumber / secondNumber);
        break;
      default:
        throw new ParsingException("Unknown operand");
    }
  }

  private Stack<Double> numbersStack;
  private Stack<Character> functionsStack;
  private boolean isPrevNumber;
}