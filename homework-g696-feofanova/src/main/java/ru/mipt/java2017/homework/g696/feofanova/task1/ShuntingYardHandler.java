package ru.mipt.java2017.homework.g696.feofanova.task1;

import java.util.Stack;
import ru.mipt.java2017.homework.base.task1.ParsingException;

/**
 * ShuntingYardHandler implements ExpressionHandler. It's a specific implementation of handler, that
 * using shunting-yard algorithm with 2 stacks: for numbers and for operators. We give for each
 * operator the priority and calculate expressions in stacks when it possible.
 * @author Mary Feofanova
 * @since 25.09.17
 */
public class ShuntingYardHandler implements ExpressionHandler {

    /**
     * Constructor.
     * Initialise {@link ShuntingYardHandler#numbersStack},
     * {@link ShuntingYardHandler#functionsStack},
     * and {@link ShuntingYardHandler#isPrevNumber}
     */
  public ShuntingYardHandler() {
    numbersStack = new Stack<Double>();
    functionsStack = new Stack<Character>();
    // checks if previous token is number.
    isPrevNumber = false;
  }

  /**
   * We keep numbers in special stack
   *
   * @param number is a value to be pushed
   * @throws ParsingException if input expression was invalid
   */
  public void pushNumber(double number) throws ParsingException {
    numbersStack.push(number);
    isPrevNumber = true;
  }

  /**
   * Same for operators, but sometimes we should calculate intermediate expressions
   *
   * @param operator is an operator to be pushed
   * @throws ParsingException if input expression was invalid
   */
  public void pushOperator(char operator) throws ParsingException {
    // firstly, to make our task more simple, we convert unary + and - operators to binary.
    // it's only possible if the last token was brace or it was operator with less priority.
    if ((operator == '+' || operator == '-') && !isPrevNumber) {
      switch (functionsStack.peek()) {
        case '(':
          numbersStack.push(0.0);
          break;
        case '/':
        case '*':
          // if it's a minus, it's enough to multiply the previous number by -1.
          if (operator == '-') {
            numbersStack.push(-numbersStack.pop());
          }
          return;
        default:
          // other situations are not allowed.
          throw new ParsingException("Redundant operator");
      }
    }

    // if it's a close brace, we should calculate all expressions in stack until we meet the open
    // brace.
    if (operator == ')') {
      while (!functionsStack.empty() && functionsStack.peek() != '(') {
        operate(numbersStack, functionsStack);
      }
      // if we didn't find the open brace, there is a redundant brace.
      if (functionsStack.empty()) {
        throw new ParsingException("Redundant close brace");
      }
      functionsStack.pop(); // pop open brace.

      // we skip spaces and other unimportant symbols,
      // and calculate expression if it's an operator and we can do this (see canPop)
    } else if (operator != ' ' && operator != '\t' && operator != '\n') {
      while (functionsStack.size() != 0 && canPop(functionsStack.peek(), operator)) {
        operate(numbersStack, functionsStack);
      }
      functionsStack.push(operator);
      isPrevNumber = false;
    }
  }

  /**
   * Func that returns the answer
   *
   * @return correct answer
   * @throws ParsingException if input expression was invalid
   */
  public double getAnswer() throws ParsingException {
    if (numbersStack.size() != 1 || !functionsStack.empty()) {
      // if there isn't only one number or remained some operators, something wrong with expression.
      throw new ParsingException("Invalid expression");
    }
    return numbersStack.pop();
  }

  // returns priority for operator.
  private int getPriority(char operator) throws ParsingException {
    switch (operator) {
        // the highest priority is for + and -, 'cause they are last to calculate.
      case '+':
      case '-':
        return 2;
        // these operators calculate firstly.
      case '*':
      case '/':
        return 1;
        // open brace should be removed only by close brace.
      case '(':
        return 0;
        // other symbols are unknown.
      default:
        throw new ParsingException("Unknown operator");
    }
  }

  // says if it possible to pop the previous operator.
  // Its priority should be less, and it cannot be the open brace.
  private boolean canPop(char firstOperator, char secondOperator) throws ParsingException {
    int firstPriority = getPriority(firstOperator);
    int secondPriority = getPriority(secondOperator);
    return firstPriority > 0 && secondPriority > 0 && firstPriority <= secondPriority;
  }

  // do an action with last 2 numbers and operator.
  private void operate(Stack<Double> numbers, Stack<Character> functions) throws ParsingException {
    char operator = functions.pop();

    // there must be at least 2 numbers.
    if (numbers.size() < 2) {
      throw new ParsingException("Invalid expression");
    }

    double secondNumber = numbers.pop();
    double firstNumber = numbers.pop();
    switch (operator) {
      case '+':
        numbers.push(firstNumber + secondNumber);
        break;
      case '-':
        numbers.push(firstNumber - secondNumber);
        break;
      case '*':
        numbers.push(firstNumber * secondNumber);
        break;
      case '/':
        numbers.push(firstNumber / secondNumber);
        break;
      default:
        throw new ParsingException("Unknown operator");
    }
  }

  private Stack<Double> numbersStack;
  private Stack<Character> functionsStack;
  private boolean isPrevNumber;
}
