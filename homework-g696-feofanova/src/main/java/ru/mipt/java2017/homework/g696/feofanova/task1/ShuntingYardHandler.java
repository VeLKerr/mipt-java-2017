package ru.mipt.java2017.homework.g696.feofanova.task1;

import java.util.Stack;
import ru.mipt.java2017.homework.base.task1.ParsingException;

/**
 * ShuntingYardHandler implements ExpressionHandler.
 * It's a specific implementation of handler, that using shunting-yard algorithm with 2 stacks:
 * for numbers and for operators. We give for each operator the priority and calculate expressions
 * in stacks when it possible.
 */

public class ShuntingYardHandler implements ExpressionHandler {

  //constructor.
  public ShuntingYardHandler() {
    numbersStack = new Stack<Double>();
    functionsStack = new Stack<Character>();
    //checks if previous token is number.
    isPrevNumber = false;
  }

  //we keep numbers in special stack.
  public void pushNumber(double number) throws ParsingException {
    numbersStack.push(number);
    isPrevNumber = true;
  }

  //same for operands, but sometimes we should calculate intermediate expressions.
  public void pushOperand(char operand) throws ParsingException {
    //firstly, to make our task more simple, we convert unary + and - operations to binary.
    //it's only possible if the last token was brace or it was operator with less priority.
    if ((operand == '+' || operand == '-') && !isPrevNumber) {
      switch (functionsStack.peek()) {
        case '(':
          numbersStack.push(0.0);
          break;
        case '/': case '*':
          //if it's a minus, it's enough to multiply the previous number by -1.
          if (operand == '-') {
            numbersStack.push(-numbersStack.pop());
          }
          return;
        default:
          //other situations are not allowed.
          throw new ParsingException("Redundant operand");
      }
    }

    //if it's a close brace, we should calculate all expressions in stack until we meet the open brace.
    if (operand == ')') {
      while (!functionsStack.empty() && functionsStack.peek() != '(') {
        operate(numbersStack, functionsStack);
      }
      //if we didn't find the open brace, there is a redundant brace.
      if (functionsStack.empty()) {
        throw new ParsingException("Redundant close brace");
      }
      functionsStack.pop(); //pop open brace.

      //we skip spaces and other unimportant symbols,
      //and calculate expression if it's an operator and we can do this (see canPop)
    } else if (operand != ' ' && operand != '\t' && operand != '\n') {
      while (functionsStack.size() != 0 && canPop(functionsStack.peek(), operand)) {
        operate(numbersStack, functionsStack);
      }
      functionsStack.push(operand);
      isPrevNumber = false;
    }
  }

  //func that returns the answer.
  public double getAnswer() throws ParsingException {
    if (numbersStack.size() != 1 || !functionsStack.empty()) {
      //if there isn't only one number or remained some operators, something wrong with expression.
      throw new ParsingException("Invalid expression");
    }
    return numbersStack.pop();
  }

  //returns priority for operator.
  private int getPriority(char operand) throws ParsingException {
    switch (operand) {
      //the highest priority is for + and -, 'cause they are last to calculate.
      case '+': case '-':
        return 2;
      //these operators calculate firstly.
      case '*': case '/':
        return 1;
      //open brace should be removed only by close brace.
      case '(':
        return 0;
      //other symbols are unknown.
      default:
        throw new ParsingException("Unknown operand");
    }
  }

  //says if it possible to pop the previous operator.
  //Its priority should be less, and it cannot be the open brace.
  private boolean canPop(char firstOperand, char secondOperand) throws ParsingException {
    int firstPriority = getPriority(firstOperand);
    int secondPriority = getPriority(secondOperand);
    return firstPriority > 0 && secondPriority > 0 && firstPriority <= secondPriority;
  }

  //do an action with last 2 numbers and operator.
  private void operate(Stack<Double> numbersStack, Stack<Character> functionsStack) throws ParsingException {
    char operand = functionsStack.pop();

    //there must be at least 2 numbers.
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