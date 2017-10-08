
package ru.mipt.java2017.homework.g695.loginov.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.ArrayList;
import java.util.Stack;

import static java.lang.Character.*;

/**
 * @author Roman Loginov, DIHT 695
 * @since 1.0
 * Реализует интерфейс Calculator на основе преобразования инфиксной формы записи выражения
 */
public class MyCalculator implements Calculator {

  /**
   * Абстрактный элемент выражения
   */
  private interface Token {
  }

  /**
   * Элемент с возможностью вычисления выражения
   */
  private interface CalculableToken extends Token {

    Double getValue();
  }

  /**
   * Вспомогательные элементы (скобки, операторы)
   */
  private class SyntaxToken implements Token {

    private char type;

    SyntaxToken(char type) {
      this.type = type;
    }

    public char type() {
      return type;
    }

    @Override
    public String toString() {
      return "SyntaxToken<" + type + ">";
    }
  }

  private class Number implements CalculableToken {

    private double value;

    Number(double value) {
      this.value = value;
    }

    @Override
    public Double getValue() {
      return value;
    }

    @Override
    public String toString() {
      return "Number<" + getValue().toString() + ">";
    }
  }

  private class Brace extends SyntaxToken {

    Brace(char type) {
      super(type);
    }
  }

  private boolean isOperator(char ch) {
    return (ch == '+' || ch == '-' || ch == '*' || ch == '/');
  }

  /**
   * Проверка унарности оператора на этапе синтаксического анализа
   * @param lastToken Последний прочитанный элемент
   * @return Может ли оператор, следующий за @lastToken быть унарным
   */
  private boolean isOperatorUnary(Token lastToken) {
    if (lastToken instanceof Brace) {
      Brace br = (Brace) lastToken;
      return br.type() == '(';
    } else {
      return lastToken instanceof Operator;
    }
  }

  private class Operator extends SyntaxToken {

    private boolean isUnary;
    private int priority;

    Operator(char type, boolean isUnary) {
      super(type);
      this.isUnary = isUnary;

      if (isUnary) {
        priority = 3;
      } else {
        if (type == '+' || type == '-') {
          priority = 1;
        } else {
          priority = 2;
        }
      }
    }

    public int priority() {
      return priority;
    }

    public boolean isUnary() {
      return isUnary;
    }

    @Override
    public String toString() {
      if (isUnary) {
        return "Operator<" + type() + ", un>";
      } else {
        return "Operator<" + type() + ", bin>";
      }
    }
  }

  private boolean isUnaryOperator(Token token) {
    if (!(token instanceof Operator)) {
      return false;
    }

    Operator op = (Operator) token;
    return op.isUnary();
  }

  private boolean isBinaryOperator(Token token) {
    return (token instanceof Operator) && !isUnaryOperator(token);
  }

  private boolean isOpeningBrace(Token token) {
    if (!(token instanceof Brace)) {
      return false;
    }

    Brace brace = (Brace) token;
    return brace.type() == '(';
  }

  private boolean isClosingBrace(Token token) {
    return (token instanceof Brace) && !isOpeningBrace(token);
  }

  private boolean checkStartToken(Token token) {
    return isUnaryOperator(token) || isOpeningBrace(token) || token instanceof Number;
  }

  private boolean checkFinishToken(Token token) {
    return isClosingBrace(token) || token instanceof Number;
  }

  /**
   * Проверка корректности порядка элементов (на основе соседних)
   * @param tokens Выделенный массив элементов
   * @throws ParsingException
   */
  private void checkTokens(ArrayList<Token> tokens) throws ParsingException {
    Integer errorIndex = -1;

    if (tokens.size() < 1) {
      throw new ParsingException("No tokens to read from the expression");
    }

    for (int i = 0; i + 1 < tokens.size(); ++i) {
      Token nextTok = tokens.get(i + 1);
      if (tokens.get(i) instanceof Operator) {
        if (isUnaryOperator(tokens.get(i))) {
          if (!isOpeningBrace(nextTok) && !(nextTok instanceof Number) && !isUnaryOperator(
              nextTok)) {
            errorIndex = i;
          }
        } else {
          if (!isOpeningBrace(nextTok) && !(nextTok instanceof Number) && !isUnaryOperator(
              nextTok)) {
            errorIndex = i;
          }
        }
      } else if (isOpeningBrace(tokens.get(i))) {
        if (!isOpeningBrace(nextTok) && !(nextTok instanceof Number) &&
            !isUnaryOperator(nextTok)) {
          errorIndex = i;
        }
      } else if (isClosingBrace(tokens.get(i))) {
        if (!isBinaryOperator(nextTok) && !isClosingBrace(nextTok)) {
          errorIndex = i;
        }
      } else if (tokens.get(i) instanceof Number) {
        if (!isBinaryOperator(nextTok) && !isClosingBrace(nextTok)) {
          errorIndex = i;
        }
      }
    }

    if (errorIndex != -1) {
      throw new ParsingException("Incorrect token after token " + errorIndex.toString());
    }

    if (!checkStartToken(tokens.get(0))) {
      throw new ParsingException("Incorrect token at the beginning");
    }

    if (!checkFinishToken(tokens.get(tokens.size() - 1))) {
      throw new ParsingException("Incorrect token in the end");
    }
  }

  private int counter = 0;

  /**
   * Выделение числа и его парсинг из строки в @Double
   * @param expression Выражение с числом-префиксом
   * @return Объект класса Number, хранящий вычисенное значение
   * @throws ParsingException
   */
  private Number getNumber(String expression) throws ParsingException {
    StringBuilder stringNumber = new StringBuilder();
    int nDots = 0;

    while (counter < expression.length() && (isDigit(expression.charAt(counter)) ||
        expression.charAt(counter) == '.') && nDots < 2) {
      stringNumber.append(expression.charAt(counter));
      if (expression.charAt(counter) == '.') {
        ++nDots;
      }
      ++counter;
    }

    double doubleRes;

    try {
      doubleRes = Double.parseDouble(stringNumber.toString());
    } catch (NumberFormatException e) {
      throw new ParsingException(e.toString());
    }

    return new Number(doubleRes);
  }

  /**
   * Синтаксический анализ выражения
   * @param expression Строка в инфиксной форме
   * @return Массив синтаксических элементов (токенов) в порядке их следования в строке
   * @throws ParsingException
   */
  private ArrayList<Token> getTokensList(String expression) throws ParsingException {
    ArrayList<Token> tokens = new ArrayList<>();

    while (counter < expression.length()) {
      char sym = expression.charAt(counter);

      if (isDigit(sym)) {
        tokens.add(getNumber(expression));
      } else if (sym == '(' || sym == ')') {
        tokens.add(new Brace(sym));
        counter++;
      } else if (isOperator(sym)) {
        tokens.add(
            new Operator(sym, tokens.isEmpty() || isOperatorUnary(tokens.get(tokens.size() - 1))));
        counter++;
      } else if (sym == '.') {
        throw new ParsingException("Unavailable to deal with \'.\'");
      } else if (isWhitespace(sym)) {
        counter++;
      } else {
        throw new ParsingException("Prohibited symbol found in expression");
      }
    }

    return tokens;
  }

  /**
   * Построение обратной польской нотации по синтаксическим элементам в инфиксной форме
   * @param tokens Массив синтаксических элементов
   * @return Массив синтаксических элементов в польской нотации
   * @throws ParsingException
   */
  private ArrayList<Token> getReversedPolishNotation(ArrayList<Token> tokens)
      throws ParsingException {
    ArrayList<Token> result = new ArrayList<>();
    Stack<SyntaxToken> syntaxTokens = new Stack<>();

    for (int i = 0; i < tokens.size(); ++i) {
      Token currToken = tokens.get(i);

      if (currToken instanceof Number) {
        result.add(currToken);
      } else if (isOpeningBrace(currToken)) {
        syntaxTokens.push((SyntaxToken) currToken);
      } else if (isClosingBrace(currToken)) {
        while (!syntaxTokens.empty() && syntaxTokens.peek().type() != '(') {
          result.add(syntaxTokens.pop());
        }

        if (!syntaxTokens.empty()) {
          syntaxTokens.pop();
        } else {
          throw new ParsingException("Incorrect brace balance");
        }
      } else if (currToken instanceof Operator) {
        Operator op = (Operator) currToken;

        while (!syntaxTokens.empty() && syntaxTokens.peek() instanceof Operator) {
          Operator prevOp = (Operator) syntaxTokens.peek();
          if (prevOp.isUnary() || (!op.isUnary() && op.priority() <= prevOp.priority())) {
            result.add(syntaxTokens.pop());
          } else {
            break;
          }
        }

        syntaxTokens.push(op);
      }
    }

    while (!syntaxTokens.empty()) {
      if (syntaxTokens.peek() instanceof Operator) {
        result.add(syntaxTokens.pop());
      } else {
        throw new ParsingException("Incorrect brace balance");
      }
    }

    return result;
  }

  /**
   * Вычисление значения выражения в обратной польской нотации
   * @param polishNotation
   * @return
   * @throws ParsingException
   */
  private double calculatePolishNotation(ArrayList<Token> polishNotation) throws ParsingException {
    Stack<Double> values = new Stack<>();

    for (int i = 0; i < polishNotation.size(); ++i) {
      if (polishNotation.get(i) instanceof CalculableToken) {
        values.push(((CalculableToken) polishNotation.get(i)).getValue());
      } else if (polishNotation.get(i) instanceof Operator) {
        Operator op = (Operator) polishNotation.get(i);

        if (op.isUnary() && values.size() < 1 || !op.isUnary() && values.size() < 2) {
          throw new ParsingException("Too few arguments for operator " + op.toString());
        }

        double arg1 = values.pop();
        double arg2 = 0;

        if (!op.isUnary()) {
          arg2 = values.pop();
        }

        if (op.isUnary()) {
          if (op.type() == '-') {
            values.push(arg1 * -1.0);
          } else if (op.type() == '+') {
            values.push(arg1);
          } else {
            throw new ParsingException("Unknown unary operator");
          }
        } else {
          switch (op.type()) {
            case '+':
              values.push(arg2 + arg1);
              break;
            case '-':
              values.push(arg2 - arg1);
              break;
            case '*':
              values.push(arg2 * arg1);
              break;
            case '/':
              values.push(arg2 / arg1);
              break;
            default:
              break;
          }
        }
      }
    }

    if (values.size() != 1) {
      throw new ParsingException("Too many arguments for operator");
    }

    return values.pop();
  }

  /**
   * Принимает строку с валидным арифметическим выражением. Возвращает результат выполнения этого
   * выражения. Выражение может содержать числа десятичного формата, операторы +, -, *, / и
   * операторы приоритета (, ). В выражении допустимы любые space-символы.
   *
   * @param expression Строка с арифметическим выражением
   * @return Результат расчета выражения
   * @throws ParsingException Не удалось распознать выражение
   */
  @Override
  public double calculate(String expression) throws ParsingException {
    if (expression == null) {
      throw new ParsingException("String is null");
    }

    ArrayList<Token> tokens = getTokensList(expression);
    checkTokens(tokens);
    ArrayList<Token> polishNotation = getReversedPolishNotation(tokens);

    return calculatePolishNotation(polishNotation);
  }
}