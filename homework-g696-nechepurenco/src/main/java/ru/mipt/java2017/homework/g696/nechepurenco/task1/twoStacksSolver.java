package ru.mipt.java2017.homework.g696.nechepurenco.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;
import java.util.Stack;

class twoStacksSolver {
  private String sample;
  private Token currentToken = new Token('b');
  private Token previousToken = new Token('b');
  private Stack<Double> numbers;
  private Stack<Character> operators;
  private int pos = 0;
  private double answer = 0;
  private boolean wasSpace = false; //нужно для обработки ошибки двух чисел через пробел
  private boolean wasUnaryMinus = false;
  private boolean weFinished = false; //для обработки случая, когда скобочный балланс выражения становится < 0
  //при достижении ск-м баллансом 0 мы выставляем эту переменную в true, и дальше проверяем, что остаток строки состоит
  //только из эквивалентных пробелу символов

  twoStacksSolver(String givenString) throws ParsingException {
    if (givenString == null) {
      throw new ParsingException("Empty string");
    }
    sample = '(' + givenString + ')';
    numbers = new Stack();
    operators = new Stack();
  }

  boolean numberOnCurrentPosition() {
    return sample.charAt(pos) <= '9' && sample.charAt(pos) >= '0';
  }

  private Double readDouble() throws ParsingException {
    double d = 0; //целая часть
    while (pos < sample.length()) {
      if (!numberOnCurrentPosition()) {
        break;
      }
      d = d * 10 + sample.charAt(pos) - '0';
      pos++;
    }
    if (pos < sample.length()) {
      if (sample.charAt(pos) == '.') {
        d += readDoubleAfterPoint();
      }
    }
    pos--;
    return d;
  }

  private Double readDoubleAfterPoint() throws ParsingException {
    pos++; //проходим точку
    double d = 0; //дробная часть
    double exp = 0.1;
    while (pos < sample.length()) {
      if (!numberOnCurrentPosition()) { //засунь ты это в отдельную функцию
        break;
      }
      d = d + (sample.charAt(pos) - '0') * exp;
      exp /= 10;
      pos++;
    }
    if (exp == 0.1) {
      throw new ParsingException("Incorrect number");
    }
    return d;
  }

  private int getPriority(char operand) throws ParsingException {
    switch (operand) {
      case '(':
        return -1;
      case '+':
      case '-':
        return 2;
      case '*':
      case '/':
        return 1;
      default:
        throw new ParsingException("This mustn't happen!");
    }
  }

  private boolean canOperate() throws ParsingException {
    if (operators.size() == 0) {
      return false;
    }
    int p1 = getPriority(currentToken.getOperand());
    int p2 = getPriority(operators.peek());
    return p2 >= 0 && p1 >= p2;
  }

  private void operate() throws ParsingException { //подумай над названием
    if (operators.size() == 0 || numbers.size() < 2) {
      throw new ParsingException("Something is wrong");
    }
    double op2 = numbers.pop();
    char operator = operators.pop();
    double op1 = numbers.pop();
    double result;
    switch (operator) {
      case '+':
        result = op1 + op2;
        break;
      case '-':
        result = op1 - op2;
        break;
      case '*':
        result = op1 * op2;
        break;
      case '/':
        result = op1 / op2;
        break;
      default:
        throw new ParsingException("this can't happen, I bet");
    }
    numbers.push(result);
  }

  private Token getNextToken() throws ParsingException {
    Token t = new Token('f');
    if (numberOnCurrentPosition()) {
      if (wasSpace && currentToken.getType() == 1) {
        throw new ParsingException("Useless space");
      }
      wasSpace = false;
      double d = readDouble();
      t = new Token(d);
    } else {
      Character ch = sample.charAt(pos);
      switch (ch) {
        case '-':
          if (previousToken.equal('(')) {
            numbers.push(0.0);
          }
          previousToken = new Token(0);
          if (operators.peek() == '/') { //мы можем рассматривать и 2 оператора подряд, с такой хитростью: не делим на
          //отрицательное число, а сначала умножаем делимое на -1. С умножением такое рассматривать,
          // мне кажется, не обязательно
            double d = numbers.pop();
            numbers.push(-d);
            wasUnaryMinus = true;
          }
        case '+':
        case '/':
        case '*':
          if (previousToken.getType() == 2 && previousToken.getOperand() != ')') {
            throw new ParsingException("Invalid string");
          }
        case ')':
        case '(':
          t = new Token(ch);
          wasSpace = false;
          break;
        case ' ':
        case '\n':
        case '\t':
          wasSpace = true;
          break;
        default:
          throw new ParsingException("Unexpected symbol");
      }
    }
    return t;
  }

  void parse() throws ParsingException {
    if (sample.length() == 2) { //ма вначале добавили пару скобок
      throw new ParsingException("Empty buckets");
    }
    operators.push('(');
    for (; pos < sample.length(); ++pos) {
      currentToken = getNextToken();
      if (currentToken.equal('f')) { //there was a space
        continue;
      }
      if (weFinished) {
        throw new ParsingException("Too many closing brackets");
      }
      if (currentToken.getType() == 1) {
        numbers.push(currentToken.getNumber());
      } else {
        if (wasUnaryMinus) {
          wasUnaryMinus = false;
          continue;
        }
        wasUnaryMinus = false;
        if (previousToken.equal('(') && (/*currentToken.equal('+')||*/ currentToken.equal('-'))) {
          numbers.push(0.0);
        }
        if (currentToken.equal(')')) {
          while (operators.size() > 0 && operators.peek() != '(') {
            operate();
          }
          if (operators.peek() != '(') {
            throw new ParsingException("Too many closing brackets");
          }
          operators.pop();
          if (operators.size() == 1) {
            weFinished = true;
          }
        } else {
          while (canOperate()) {
            operate();
          }
          operators.push(currentToken.getOperand());
        }
      }
      previousToken = currentToken;
    }
    if (numbers.size() != 1) {
      throw new ParsingException("Incorrect statement");
    }
    answer = numbers.get(0);
  }

  double getAnswer() throws ParsingException {
    return answer;
  }
}