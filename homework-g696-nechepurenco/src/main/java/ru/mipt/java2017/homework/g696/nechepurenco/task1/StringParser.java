package ru.mipt.java2017.homework.g696.nechepurenco.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.Stack;

class StringParser {
  private String sample;
  private Token currentToken = new Token('s');
  private Token previousToken = new Token('s');
  private Stack<Double> numbers;
  private Stack<Character> operators;
  private int pos = 0;
  private double answer = 0;
  private boolean wasSpace = false;
  private boolean wasUnaryMinus = false;
  private boolean weFinished = false;

  StringParser(String _sample) throws ParsingException {
    if (_sample == null) {
      throw new ParsingException("Empty string");
    }
    sample = '(' + _sample + ')';
    numbers = new Stack();
    operators = new Stack();
  }

  private Double readDouble() throws ParsingException {
    double d = 0;
    while (pos < sample.length()) {
      if (sample.charAt(pos) > '9' || sample.charAt(pos) < '0') {
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
    pos++;//проходим точку
    double d = 0;
    double exp = 0.1;
    while (pos < sample.length()) {
      if (sample.charAt(pos) > '9' || sample.charAt(pos) < '0') {//засунь ты это в отдельную функцию
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
    }
    return -1;
  }

  private boolean canPop() throws ParsingException {
    if (operators.size() == 0){
      return false;
    }
    int p1 = getPriority(currentToken.operand);
    int p2 = getPriority(operators.peek());
    return p2 >= 0 && p1 >= p2;
  }

  private void Pop() throws ParsingException {//подумай над названием
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
    Character ch = sample.charAt(pos);
    if (ch <= '9' && ch >= '0') {
      if (wasSpace && currentToken.type == 1) {
        throw new ParsingException("Useless space");
      }
      wasSpace = false;
      double d = readDouble();
      t = new Token(d);
    } else {
      switch (ch) {
        case '-':
          if (previousToken.equal('(')) {
            numbers.push(0.0);
          }
          previousToken = new Token(0);
          if (operators.peek() == '/') {
            System.out.print("!");
            double d = numbers.pop();
            numbers.push(-d);
            wasUnaryMinus = true;
          }
        case '+':
        case '/':
        case '*':
          if (previousToken.type == 2 && previousToken.operand != ')') {
            System.out.print(previousToken.operand);
            throw new ParsingException("Invalid string");
          }
        case ')':
        case '(':
          t = new Token(ch);
          break;
        case ' ':
        case '\n':
        case '\t':
          wasSpace = true;
          break;
        default:
          System.out.print(ch);
          throw new ParsingException("Unexpected symbol");
      }
    }
    return t;
  }

  void Parse() throws ParsingException {
    if (sample.length() == 2)//ма вначале добавили пару скобок
      throw new ParsingException("Empty buckets");
    operators.push('(');
    for (; pos < sample.length(); ++pos) {
      currentToken = getNextToken();
      if (currentToken.equal('f')) {//there was a space
        continue;
      }
      if (weFinished) {
        throw new ParsingException("Too many closing brackets");
      }
      if (currentToken.type == 1) {
        numbers.push(currentToken.number);
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
            Pop();
          }
          if (operators.peek() != '(') {
            throw new ParsingException("Too many closing brackets");
          }
          operators.pop();
          if (operators.size() == 1) {
            System.out.println(pos);
            System.out.print("We finished");
            weFinished = true;
          }
        } else {
          while (canPop()) {
            Pop();
          }
          operators.push(currentToken.operand);
        }
      }
      previousToken = currentToken;
    }
    //!!!
    if (numbers.size() != 1) {
      for (char ch : operators) {
        System.out.print(ch);
      }
      for (double d : numbers) {
        System.out.print(d);
      }
      throw new ParsingException("Incorrect statement");
    }
    answer = numbers.get(0);
  }

  double getAnswer() throws ParsingException {
    return answer;
  }
}