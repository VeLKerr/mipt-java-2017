package ru.mipt.java2017.homework.g697.inyutin.task1.task1;

import java.util.Collections;
import java.util.Stack;
import java.util.StringTokenizer;
import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

/**
 * Реализация Парсинга калькулятора с помощью алгоритма Сортирующей станции
 *
 * @author Inyutin Dmitry
 * @since 21.09.17
 */

public class AwesomeCalculator implements Calculator {
  public double calculate(String expression) throws ParsingException {

    try {
      operations.clear();    // Отчищаем стек операций
      expressions.clear();   //  Отчищаем стек Выражений

      // Сделаем некоторые приготовления
      expression = expression.replace(" ", "").replace("(-", "(0-")
        .replace(".-", ".0-")
        .replace("\n", "").replace("\t", "");
      if (expression.charAt(0) == '-') {
        expression = "0" + expression;
      }

      if (expression.contains("/-0")) {
        return Double.NEGATIVE_INFINITY;
      }

      // Разобьем строку на токены
      StringTokenizer stringTokenizer = new StringTokenizer(expression,
          OPERATORS + "()", true);

      // Алгоритм Сортирующей станции
      while (stringTokenizer.hasMoreTokens()) {   // Пока ещё есть токены
        String token = stringTokenizer.nextToken();     // Берем некоторый токен

        if (isOpenBracket(token)) { // Если это открывающая скобка
          operations.push(token);
        } else if (isCloseBracket(token)) {   // Если это закрывающая скобка
          while (!operations.empty() && !isOpenBracket(operations.lastElement())) {
            expressions.push(operations.pop());
          }
          operations.pop();
        } else if (isOperator(token)) {   // Если это математический оператор
          while (!operations.empty() && isOperator(operations.lastElement())
            && getPrecedence(token) <= getPrecedence(operations.lastElement())) {
            expressions.push(operations.pop());
          }
          operations.push(token);
        } else {
          expressions.push(token);
        }
      }

      while (!operations.empty()) {
        expressions.push(operations.pop());
      }

      //Делаем реверс
      Collections.reverse(expressions);

      String[] exp = new String[expressions.size()];

      int j = 0;
      while (!expressions.empty()) {
        exp[j] = expressions.pop();
        j++;
      }

      Stack<Double> stack = new Stack<>();
      for (int i = 0; i < exp.length; i++) {
        if (!isOperator(exp[i])) {
          stack.push(Double.parseDouble(exp[i]));
        } else {
          double op2 = Double.valueOf(stack.pop());
          double op1 = Double.valueOf(stack.pop());
          double result = 0;
          String operator = exp[i];
          if (operator.equals("+")) {
            result = op1 + op2;
          } else if (operator.equals("-")) {
            result = op1 - op2;
          } else if (operator.equals("*")) {
            result = op1 * op2;
          } else if (operator.equals("/")) {
            result = op1 / op2;
          }
          stack.push(result);
        }
      }
      return stack.pop();
    } catch (Exception e) {
      throw new ParsingException(e.getMessage());
    }
  }

  private static final String OPERATORS = "+-*/";    // Все возможные математические операторы

  // Методы для парсинга
  private boolean isOpenBracket(String token) {
    return token.equals("(");
  }

  private boolean isCloseBracket(String token) {
    return token.equals(")");
  }

  private boolean isOperator(String token) {
    return OPERATORS.contains(token);
  }

  private byte getPrecedence(String token) {
    if (token.equals("+") || token.equals("-")) {
      return 1;
    }
    return 2;
  }

  private Stack<String> operations = new Stack<>();   // Cтэк операций
  private Stack<String> expressions = new Stack<>();  // Стэк для хранений выражения в обратной польской нотации
}
