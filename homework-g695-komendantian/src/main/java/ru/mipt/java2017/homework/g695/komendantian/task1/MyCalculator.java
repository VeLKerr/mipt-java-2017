package ru.mipt.java2017.homework.g695.komendantian.task1;

import java.util.ArrayList;
import java.util.Stack;
import ru.mipt.java2017.homework.base.task1.ParsingException;
import ru.mipt.java2017.homework.base.task1.Calculator;

class MyCalculator implements Calculator {

  /**
   * Бросает исключение, если выражение верно
   *
   * @param condition выражение
   * @param message сообщение, передаваемое вместе с исключением
   * @throws ParsingException выражение верно
   */

  private void throwIf(boolean condition, String message) throws ParsingException {
    if (condition) {
      throw new ParsingException(message);
    }
  }

  /**
   * Принимает стек чисел и бинарную операцию. Удаляет два последних числа из стека, применяет к ним
   * операцию и добавляет результат в стек
   *
   * @param numbers стек чисел
   * @param operation бинарная операция
   */

  private void processOperation(Stack<Double> numbers, String operation) {
    Double second = numbers.peek();
    numbers.pop();
    Double first = numbers.peek();
    numbers.pop();
    if (operation.equals("+")) {
      numbers.push(first + second);
    } else if (operation.equals("-")) {
      numbers.push(first - second);
    } else if (operation.equals("*")) {
      numbers.push(first * second);
    } else {
      numbers.push(first / second);
    }
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
    throwIf(expression == null, "Expression is null");
    ArrayList<String> tokens = tokenize(expression);
    checkCorrectness(tokens);

    Stack<Double> numbers = new Stack<>();
    Stack<String> operations = new Stack<>();
    for (String token : tokens) {
      if (token.equals(TokenUtils.UNARY_MINUS)) {
        operations.push(token);
      } else if (token.equals("(")) {
        operations.push(token);
      } else if (token.equals(")")) {
        while (!operations.peek().equals("(")) {
          processOperation(numbers, operations.peek());
          operations.pop();
        }
        operations.pop();
      } else if (TokenUtils.isNumber(token)) {
        numbers.push(Double.valueOf(token));
      } else {
        while (!operations.empty() && TokenUtils.getPriority(operations.peek()) >=
          TokenUtils.getPriority(token)) {
          processOperation(numbers, operations.peek());
          operations.pop();
        }
        operations.push(token);
      }
      if (!token.equals(TokenUtils.UNARY_MINUS) &&
          !operations.empty() && operations.peek().equals(TokenUtils.UNARY_MINUS)) {
        Double last = numbers.peek();
        numbers.pop();
        numbers.push(-last);
        operations.pop();
      }
    }
    while (!operations.empty()) {
      processOperation(numbers, operations.peek());
      operations.pop();
    }
    return numbers.peek();
  }

  /**
   * Принимает токены. Бросает исключение, если не существует валидного арифметического выражения
   *
   * @param tokens токены
   * @throws ParsingException выражение не валидно
   */

  private void checkCorrectness(ArrayList<String> tokens) throws ParsingException {
    boolean needNumber = true;
    int balance = 0;
    for (String token : tokens) {
      if (token.equals(TokenUtils.UNARY_MINUS)) {
        continue;
      }
      if (needNumber) {
        if (TokenUtils.isNumber(token)) {
          needNumber = false;
        } else if (token.equals("(")) {
          ++balance;
        } else {
          throw new ParsingException("Syntax Error");
        }
      } else {
        if (token.equals(")")) {
          --balance;
          throwIf(balance < 0, "Negative balance of brackets");
        } else if (TokenUtils.isOperator(token)) {
          needNumber = true;
        } else {
          throw new ParsingException("Syntax Error");
        }
      }
    }
    throwIf(balance > 0, "Balance of brackets must be equal to zero");
    throwIf(needNumber, "Need number in the end of expression");
  }

  /**
   * Принимает строку с арифметическим выражением. Разбивает ее на токены и проверяет на валидность
   * токены и расположение унарных операторов.
   *
   * @param expression строка с арифметическим выражением
   * @return токены, полученные после разбиения выражения
   * @throws ParsingException токены или унарные операторы невалидны
   */

  private ArrayList<String> tokenize(String expression) throws ParsingException {
    for (String op : TokenUtils.CONTROL_SYMBOLS) {
      expression = expression.replace(op, " " + op + " ");
    }
    boolean wasUnary = false;
    ArrayList<String> tokens = new ArrayList<>();
    for (String token : expression.split("\\s")) {
      if (token.isEmpty()) {
        continue;
      }
      if (!TokenUtils.isControlSymbol(token) && !TokenUtils.isNumber(token)) {
        throw new ParsingException("Bad token");
      }
      if (wasUnary) {
        throwIf(!TokenUtils.isNumber(token) && !token.equals("("),
            "There must be number or opening bracket after unary operation");
        tokens.add(token);
        wasUnary = false;
      } else {
        String last = tokens.isEmpty() ? "" : tokens.get(tokens.size() - 1);
        tokens.add(token);
        if (token.equals("+") || token.equals("-")) {
          if (last.isEmpty() || last.equals("(") || last.equals("*") || last.equals("/")) {
            tokens.remove(tokens.size() - 1);
            if (token.equals("-")) {
              tokens.add(TokenUtils.UNARY_MINUS);
            }
            wasUnary = true;
          }
        }
      }
    }
    throwIf(wasUnary, "Unary operator in the end of expression");
    return tokens;
  }

}