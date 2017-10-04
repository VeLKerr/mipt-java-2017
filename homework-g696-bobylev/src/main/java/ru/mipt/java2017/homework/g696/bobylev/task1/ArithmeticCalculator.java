package ru.mipt.java2017.homework.g696.bobylev.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.ArrayList;

/**
 * @author Igor V. Bobylev
 * @since 03.10.17
 *
 * Grammar:
 * <E>  ::= <T> <E’>
 * <E’> ::= + <T> <E’> | - <T> <E’> | .
 * <T>  ::= <F> <T’>
 * <T’> ::= * <F> <T’> | / <F> <T’> | .
 * <F>  ::= <number> | ( <E> ) | - <F>
 */

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