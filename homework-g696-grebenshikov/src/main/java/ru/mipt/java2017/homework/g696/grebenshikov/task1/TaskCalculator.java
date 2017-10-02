package ru.mipt.java2017.homework.g696.grebenshikov.task1;

import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.Stack;
import java.util.StringTokenizer;
import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

/**
 * @author Ivan Grebenshikov
 * @since 01.10.2017
 */
public class TaskCalculator implements Calculator {

  //& - is unary -, | - is unary plus
  private final String operators = "+-*/&|";
  private final String incorrectDigitsSequenceRegex = "\\d\\s*\\d$";
  private final String correctCharactersRegex = "[().\\-+*/0-9\\s]*";

  /**
   * @param s - string
   * @return - true if s is double in string, otherwise - false
   */
  private boolean isNumber(String s) {
    try {
      Double.parseDouble(s);
      return true;
    } catch (NumberFormatException | NullPointerException e) {
      return false;
    }
  }


  /**
   * Possible operators: + - * /
   *
   * @param s - string
   * @return - true if s is operator in string, otherwise - false
   */
  private boolean isOperator(String s) {
    return s.length() == 1 && operators.contains(s);
  }

  /**
   * Return priority of operation: 0 for unary +, -, 1 for *, /, 2 for binary +, -. If @param isn't
   * operator in string, it will throws the IllegalArgumentException.
   *
   * @param operator - MUST be operator in string
   * @return value of priority
   */
  private int getPriority(String operator) {
    if (!isOperator(operator)) {
      throw new IllegalArgumentException("Not operator, it doesn't have priority.");
    }
    if (operator.equals("+") || operator.equals("-")) {
      return 2;
    }
    if (operator.equals("*") || operator.equals("/")) {
      return 1;
    }
    return 0;
  }

  /**
   * Generate postfix notation of expression. Every element of list is double in string or operator
   * in string
   *
   * @param expression - math expression with +,-,*,/,(,) without unary - and unary +
   * @return - Postfix notation of the @param split by operands and operators
   * @throws ParsingException - throw when the balance of braces is wrong
   */
  private LinkedList<String> generatePostfixNotation(String expression) throws ParsingException {
    LinkedList<String> output = new LinkedList<String>();
    Stack<String> operatorsStack = new Stack<String>();
    String lastToken = null;
    String token = null;

    StringTokenizer st = new StringTokenizer(expression, "()" + this.operators, true);
    while (st.hasMoreTokens()) {
      lastToken = token;
      token = st.nextToken();
      if (token.equals("(")) {
        operatorsStack.push(token);
      } else if (token.equals(")")) {
        try {
          while (!operatorsStack.peek().equals("(")) {
            output.addLast(operatorsStack.pop());
          }
          operatorsStack.pop();
        } catch (EmptyStackException e) {
          throw new ParsingException("Invalid expression");
        }
      } else if (isNumber(token)) {
        output.addLast(token);
      } else if (isOperator(token)) {
        if ((lastToken != null) && isOperator(lastToken) && (getPriority(lastToken) == 1)
            && (getPriority(token) == 2)) {
          if (token.equals("-")) {
            token = "&";
          } else {
            token = "|";
          }
        }
        while (!operatorsStack.isEmpty() &&
            isOperator(operatorsStack.peek()) &&
            getPriority(token) >= getPriority(operatorsStack.peek())) {
          output.addLast(operatorsStack.pop());
        }
        operatorsStack.push(token);
      }
    }

    while (!operatorsStack.isEmpty()) {
      String oper = operatorsStack.pop();
      if (isOperator(oper)) {
        output.addLast(oper);
      } else {
        throw new ParsingException("Invalid expression");
      }
    }
    return output;
  }

  /**
   * Calculate value of expression in postfix notation.
   *
   * @param postfixNotation - the postfix notation of math expression split by numbers and
   * operators
   * @return - expression value
   * @throws ParsingException - throw when expression contains too much or too few arguments in
   * postfix notation
   */
  private double calculateValueOfPostfixExpression(LinkedList<String> postfixNotation)
      throws ParsingException {
    Stack<Double> operands = new Stack<Double>();
    String token;
    try {
      while (!postfixNotation.isEmpty()) {
        token = postfixNotation.pollFirst();
        if (isOperator(token)) {
          Double arg = operands.pop();
          switch (token) {
            case "+":
              arg = operands.pop() + arg;
              break;
            case "-":
              arg = operands.pop() - arg;
              break;
            case "*":
              arg = operands.pop() * arg;
              break;
            case "/":
              arg = operands.pop() / arg;
              break;
            case "&":
              arg = -arg;
              break;
            case "|":
              break;
            default:
              break;
          }
          operands.push(arg);
        } else if (isNumber(token)) {
          operands.push(Double.parseDouble(token));
        } else {
          throw new IllegalArgumentException("Bad postfix expression");
        }
      }
    } catch (EmptyStackException e) {
      throw new ParsingException("Invalid expression");
    }

    if (operands.size() != 1) {
      throw new ParsingException("Invalid Expression");
    }

    return operands.peek();
  }


  /**
   * Принимает строку с валидным арифметическим выражением. Возвращает результат выполнения этого
   * выражения. Выражение может содержать числа десятичного формата, операторы +, -, *, / и
   * операторы приоритета (, ). В выражении допустимы любые space-символы.
   *
   * @param expression строка с арифметическим выражением
   * @return результат расчета выражения
   * @throws ParsingException не удалось распознать выражение
   */
  @Override
  public double calculate(String expression) throws ParsingException {
    if (expression == null) {
      throw new ParsingException("Null expression");
    }
    if (!expression.matches(correctCharactersRegex)) {
      throw new ParsingException("Invalid expression");
    }
    if (expression.matches(incorrectDigitsSequenceRegex)) {
      throw new ParsingException("Invalid expression");
    }
    String expr = "(" + expression + ")";
    expr = expr.replaceAll("\\s", "").replaceAll("\\(\\-", "(0-").replaceAll("\\(\\+", "(0+");

    return calculateValueOfPostfixExpression(generatePostfixNotation(expr));
  }
}
