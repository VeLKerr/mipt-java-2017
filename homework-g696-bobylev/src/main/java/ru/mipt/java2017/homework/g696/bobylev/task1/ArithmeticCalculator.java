package ru.mipt.java2017.homework.g696.bobylev.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.IntPredicate;

/**
 * @author Igor V. Bobylev
 * @since 03.10.17
 *
 * Grammar:
 * <E>  ::= <T> <E’>.
 * <E’> ::= + <T> <E’> | - <T> <E’> | .
 * <T>  ::= <F> <T’>.
 * <T’> ::= * <F> <T’> | / <F> <T’> | .
 * <F>  ::= <number> | ( <E> ) | - <F>.
 */
class Position {

  private String text;
  private int index;

  Position(String text) {
    this(text, 0);
  }

  int getIndex() {
    return this.index;
  }

  String getText() {
    return text;
  }

  int getChar() {
    return index < text.length() ? text.codePointAt(index) : -1;
  }

  boolean satisfies(IntPredicate p) {
    return p.test(getChar());
  }

  void skip() {
    int c = getChar();
    switch (c) {
      case -1:
        return;
      default:
        this.index++;
    }
  }

  Position next() {
    int c = getChar();
    switch (c) {
      case -1:
        return this;
      default:
        return new Position(text, index + 1);
    }
  }

  void skipWhile(IntPredicate p) {
    while (this.satisfies(p)) {
      this.skip();
    }
  }

  public String toString() {
    return String.format("(%d)", index);
  }

  private Position(String text, int index) {
    this.text = text;
    this.index = index;
  }
}

enum Tag {
  NUMBER,
  LPAREN,
  RPAREN,
  END_OF_TEXT,
  ADD,
  SUB,
  MUL,
  DIV;

  public String toString() {
    switch (this) {
      case NUMBER:
        return "number";
      case LPAREN:
        return "'('";
      case RPAREN:
        return "')'";
      case END_OF_TEXT:
        return "end of text";
      case ADD:
        return "+";
      case SUB:
        return "-";
      case MUL:
        return "*";
      case DIV:
        return "|";
      default:
        throw new RuntimeException("unreachable code");
    }
  }
}

class Token {

  private Tag tag;
  private Position start, follow;
  private String word;

  Token(String text) throws ParsingException {
    this(new Position(text));
  }

  Tag getTag() {
    return tag;
  }

  String getWord() {
    return word;
  }

  public String toString() {
    return this.word;
  }

  boolean matches(Tag... tags) {
    return Arrays.stream(tags).anyMatch(t -> tag == t);
  }

  Token next() throws ParsingException {
    return new Token(follow);
  }

  boolean isFinal() {
    return this.tag == Tag.END_OF_TEXT;
  }

  void throwError(String msg) throws ParsingException {
    throw new ParsingException(msg);
  }

  private Token(Position cur) throws ParsingException {
    start = cur;
    start.skipWhile(Character::isWhitespace);
    follow = start.next();
    switch (start.getChar()) {
      case -1:
        tag = Tag.END_OF_TEXT;
        break;
      case '(':
        tag = Tag.LPAREN;
        break;
      case ')':
        tag = Tag.RPAREN;
        break;
      case '-':
        tag = Tag.SUB;
        break;
      case '+':
        tag = Tag.ADD;
        break;
      case '*':
        tag = Tag.MUL;
        break;
      case '/':
        tag = Tag.DIV;
        break;
      default:
        if (start.satisfies(Character::isDigit)) {
          follow.skipWhile(Character::isDigit);
          if (follow.getChar() == '.') {
            follow.skip();
            follow.skipWhile(Character::isDigit);
          }
          tag = Tag.NUMBER;
        } else {
          throwError("invalid character");
        }
    }
    if (start.getIndex() < start.getText().length()) {
      word = start.getText().substring(start.getIndex(), follow.getIndex());
    } else {
      word = "#NOTHING";
    }
  }
}

public class ArithmeticCalculator implements Calculator {

  private static Token countToken;
  private static String text;
  private static int countTokenIndex;
  private static ArrayList<Token> tokens;

  public double calculate(String expression) throws ParsingException {
    try {
      text = expression;
      tokenize();

      countToken = tokens.get(0);
      countTokenIndex = 1;

      //double res = parse();
      //System.out.println(res);
      //System.out.println("success");
      return parse();
    } catch (Exception ex) {
      throw new ParsingException(ex.getMessage());
    }
  }

  private static void nextToken() {
    if (countTokenIndex < tokens.size()) {
      countToken = tokens.get(countTokenIndex);
      countTokenIndex++;
    }
  }

  private static void expect(Tag tag) throws ParsingException {
    if (!countToken.matches(tag)) {
      countToken.throwError(tag.toString() + " expected");
    }
    nextToken();
  }

  private static void tokenize() throws ParsingException {
    tokens = new ArrayList<>();

    Token t = new Token(text);
    tokens.add(t);

    do {
      t = t.next();
      tokens.add(t);
    } while (!t.isFinal());
  }

  private static double parse() throws ParsingException {
    double res = parseExp();

    expect(Tag.END_OF_TEXT);

    return res;
  }

  private static double parseExp() throws ParsingException {
    double res = parseT();
    while (countToken.getTag() == Tag.ADD || countToken.getTag() == Tag.SUB) {
      if (countToken.getTag() == Tag.ADD) {
        nextToken();
        res += parseT();
      } else if (countToken.getTag() == Tag.SUB) {
        nextToken();
        res -= parseT();
      }
    }
    return res;
  }

  private static double parseT() throws ParsingException {
    double res = parseF();
    while (countToken.getTag() == Tag.MUL || countToken.getTag() == Tag.DIV) {
      if (countToken.getTag() == Tag.MUL) {
        nextToken();
        res *= parseF();
      } else if (countToken.getTag() == Tag.DIV) {
        nextToken();
        res /= parseF();
      }
    }
    return res;
  }

  private static double parseF() throws ParsingException {
    if (countToken.getTag() == Tag.NUMBER) {
      double res = Double.parseDouble(countToken.getWord());
      nextToken();
      return res;
    } else if (countToken.getTag() == Tag.LPAREN) {
      nextToken();
      double res = parseExp();
      expect(Tag.RPAREN);
      return res;
    } else if (countToken.getTag() == Tag.SUB) {
      nextToken();
      return -parseF();
    }

    countToken.throwError("ERROR - F");
    return -1;
  }
}