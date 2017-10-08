package ru.mipt.java2017.homework.g695.polyanskiy.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;
import java.util.ArrayList;

import static java.lang.Character.isDigit;
import static ru.mipt.java2017.homework.g695.polyanskiy.task1.MyCalculator.*;

public class MyParser {

  public static ArrayList<Token> parsingSpace(String expression) throws ParsingException {
    if (expression == null) {
      throw new ParsingException("null");
    }
    ArrayList<Token> list = new ArrayList<>();
    TokenEnums.Tokens last = TokenEnums.Tokens.LEFTBR;
    String variable = "";
    boolean onePoint = false;
    boolean mayUnary = true;
    int bracesBalanc = 0;
    for (int i = 0; i < expression.length(); ++i) {
      char curSymb = expression.charAt(i);
      Token token;
      if (curSymb == '.' && variable.equals("")) {
        throw new ParsingException("bad operations");
      }
      if (isDigit(curSymb) || (curSymb == '.' && !onePoint)) {
        variable += curSymb;
        if (curSymb == '.') {
          onePoint = true;
        }
      } else {
        if (!variable.isEmpty()) {
          list.add(new Token(TokenEnums.Tokens.VARIABLE, variable));
          last = TokenEnums.Tokens.VARIABLE;
          variable = "";
          onePoint = false;
          mayUnary = false;
        }
        if (curSymb == '(') {
          if (last == TokenEnums.Tokens.VARIABLE || last == TokenEnums.Tokens.RIGHTBR) {
            throw new ParsingException("bad operations");
          }
          token = new Token(TokenEnums.Tokens.LEFTBR, String.valueOf(curSymb));
          list.add(token);
          last = TokenEnums.Tokens.LEFTBR;
          mayUnary = true;
          bracesBalanc++;
          continue;
        }
        if (curSymb == ')') {
          if (bracesBalanc == 0 || last == TokenEnums.Tokens.LEFTBR ||
              last == TokenEnums.Tokens.UNARY ||
              last == TokenEnums.Tokens.BINARY) {
            throw new ParsingException("bad operations");
          }
          token = new Token(TokenEnums.Tokens.RIGHTBR, String.valueOf(curSymb));
          list.add(token);
          last = TokenEnums.Tokens.RIGHTBR;
          mayUnary = false;
          bracesBalanc--;
          continue;
        }
        if (isOperation(curSymb)) {
          if (last == TokenEnums.Tokens.UNARY) {
            throw new ParsingException("bad operations");
          }
          if (mayUnary) {
            if (curSymb == '*' || curSymb == '/') {
              throw new ParsingException("bad operations");
            }
            token = new Token(TokenEnums.Tokens.UNARY, String.valueOf(curSymb));
            last = TokenEnums.Tokens.UNARY;
            mayUnary = false;
          } else {
            token = new Token(TokenEnums.Tokens.BINARY, String.valueOf(curSymb));
            last = TokenEnums.Tokens.BINARY;
            mayUnary = true;
          }
          list.add(token);
          continue;
        }
        if (curSymb == '\n' || curSymb == '\t') {
          continue;
        }
        if (curSymb == ' ') {
          continue;
        }
        throw new ParsingException("bad symbol in expression");
      }
    }
    if (!variable.isEmpty()) {
      list.add(new Token(TokenEnums.Tokens.VARIABLE, variable));
    }
    if (list.size() == 0) {
      throw new ParsingException("no expression");
    }
    return list;
  }
}
