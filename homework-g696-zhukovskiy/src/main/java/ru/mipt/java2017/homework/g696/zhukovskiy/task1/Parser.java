package ru.mipt.java2017.homework.g696.zhukovskiy.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.Stack;

class Parser {

  /**
   * Initializes Parser and creates it's inner state
   *
   * @param expressionWithSpaces expression to create RPN from
   * @return {@code Parser} object
   */
  Parser(String expressionWithSpaces) {
    expression = expressionWithSpaces.replaceAll("\\s+", "");
    rpnExpression = new StringBuilder();
    operatorStack = new Stack<>();
  }

  private String expression;
  private StringBuilder rpnExpression;
  private Stack<Character> operatorStack;

  /**
   * Indicates if given character is arithmetic operator
   *
   * @param currentChar character
   * @return {@code true} if character is operator {@code false} instead
   */
  private boolean symbolIsOperator(char currentChar) {
    return (currentChar == '+' || currentChar == '-' || currentChar == '*' || currentChar == '/');
  }

  /**
   * Returns priority of some operator or bracket
   *
   * @param operator character
   * @return {@code int} it's priority
   * @throws ParsingException if given character isn't operation but supposed to be so
   */
  private int getPriority(char operator) throws ParsingException {
    switch (operator) {
      case '(':
        return 0;
      case ')':
        return 0;
      case '+':
        return 1;
      case '-':
        return 1;
      case '*':
        return 2;
      case '/':
        return 2;
      case '$': // замена унарного минуса
        return 3;
      default:
        throw new ParsingException("Broken expression (given character isn't operation)");
    }
  }

  /**
   * Clears out an inner stack at the end of parsing
   * to keep operations that were left out of string
   *
   * @throws ParsingException if there is something else than operator at the end of stack
   */
  private void clearStack() throws ParsingException {
    // Опустошение остатков стека
    while (!operatorStack.isEmpty()) {
      char currentChar = operatorStack.pop();
      if (symbolIsOperator(currentChar) || currentChar == '$') {
        rpnExpression.append(currentChar);
      } else {
        throw new ParsingException("Broken expression (operator at the end of sequence)");
      }
    }
  }

  /**
   * Parses an expression in it's inner state
   * and returns RPN expression made of it
   *
   * @return {@code String} RPN expression
   * @throws ParsingException if expression is not valid
   */
  String parseRPN() throws ParsingException {

    boolean unary = true;

    for (char currentChar : expression.toCharArray()) {

      if (symbolIsOperator(currentChar)) {
        rpnExpression.append('\\');
        if (unary) {
          // Пришел унарный оператор
          if (currentChar == '-') {
            operatorStack.push('$');
          } else {
            throw new ParsingException("Broken expression (unary operator isn't \"-\")");
          }
        } else {
          // Пришел обычный оператор
          while (!operatorStack.isEmpty()) {
            char curOperator = operatorStack.pop();
            if (getPriority(currentChar) <= getPriority(curOperator)) {
              rpnExpression.append(curOperator).append('\\');
            } else {
              operatorStack.push(curOperator);
              break;
            }
          }
          unary = true;
          operatorStack.push(currentChar);
        }
        // Обработка скобочных последовательностей
      } else if (currentChar == '(') {
        operatorStack.push(currentChar);
        unary = true;
      } else if (currentChar == ')') {
        while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
          rpnExpression.append(operatorStack.pop());
        }
        if (operatorStack.isEmpty()) {
          throw new ParsingException("Broken expression (not RBS)");
        }
        operatorStack.pop();
        unary = false;
        // Обработка чисел
      } else if (Character.isDigit(currentChar) || currentChar == '.') {
        rpnExpression.append(currentChar);
        unary = false;
      } else {
        throw new ParsingException("Broken expression (unexpected symbol)");
      }
    }

    clearStack();

    return rpnExpression.toString();
  }
}