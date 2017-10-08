package ru.mipt.java2017.homework.g695.ogorodnikov.task1.parser;

import static ru.mipt.java2017.homework.g695.ogorodnikov.task1.parser.Token.TokenType.LBRACE;
import static ru.mipt.java2017.homework.g695.ogorodnikov.task1.parser.Token.TokenType.NUMBER;
import static ru.mipt.java2017.homework.g695.ogorodnikov.task1.parser.Token.TokenType.OP;
import static ru.mipt.java2017.homework.g695.ogorodnikov.task1.parser.Token.TokenType.RBRACE;

import java.util.ArrayList;
import java.util.ListIterator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

public abstract class ParsedExpr {

  public static ParsedExpr parse(String expr) throws ParsingException {
    ArrayList<Token> tokenList = tokenize(expr);
    return fromTokenList(tokenList);
  }

  private static ParsedExpr fromTokenList(ArrayList<Token> tokenList) throws ParsingException {
    ListIterator<Token> iter = tokenList.listIterator();
    ParsedExpr expr = parseExpr(iter);
    if (iter.hasNext()) {
      throw new ParsingException("Parse error: has extra tokens left");
    } else {
      return expr;
    }
  }

  private static ParsedExpr parseExpr(ListIterator<Token> iter) throws ParsingException {
    ArrayList<ParsedExpr> terms = new ArrayList<>();
    ArrayList<Token> ops = new ArrayList<>();
    while (iter.hasNext()) {
      terms.add(parseTerm(iter));
      if (iter.hasNext()) {
        Token op = iter.next();
        if (op.getType() == OP && ("+".equals(op.getVal()) || "-".equals(op.getVal()))) {
          ops.add(op);
        } else {
          iter.previous();
          break;
        }
      }
    }
    if (terms.isEmpty()) {
      throw new ParsingException("Empty terms list");
    }
    ParsedExpr expr = terms.get(0);
    for (int i = 1; i < terms.size(); ++i) {
      if ("+".equals(ops.get(i - 1).getVal())) {
        expr = new SumNode(expr, terms.get(i));
      } else {
        expr = new DiffNode(expr, terms.get(i));
      }
    }

    return expr;
  }

  private static ParsedExpr parseTerm(ListIterator<Token> iter) throws ParsingException {
    ArrayList<ParsedExpr> factors = new ArrayList<>();
    ArrayList<Token> ops = new ArrayList<>();
    while (iter.hasNext()) {
      factors.add(parseFactor(iter));
      if (iter.hasNext()) {
        Token op = iter.next();
        if (op.getType() == OP && ("*".equals(op.getVal()) || "/".equals(op.getVal()))) {
          ops.add(op);
        } else {
          iter.previous();
          break;
        }
      }
    }
    if (factors.isEmpty()) {
      throw new ParsingException("Empty factors list");
    }
    ParsedExpr expr = factors.get(0);
    for (int i = 1; i < factors.size(); ++i) {
      if ("*".equals(ops.get(i - 1).getVal())) {
        expr = new MulNode(expr, factors.get(i));
      } else {
        expr = new DivNode(expr, factors.get(i));
      }
    }

    return expr;
  }

  private static ParsedExpr parseSimpleFactor(ListIterator<Token> iter) throws ParsingException {
    if (!iter.hasNext()) {
      throw new ParsingException("Parser error: bad simple_factor");
    }
    Token t1 = iter.next();
    if (t1.getType() == NUMBER) {
      return new NumberNode(Double.parseDouble(t1.getVal()));
    } else if (t1.getType() == LBRACE) {
      ParsedExpr expr = parseExpr(iter);
      if (!iter.hasNext() || iter.next().getType() != RBRACE) {
        throw new ParsingException("Parser error: bad brackets balance");
      }
      return expr;
    } else {
      throw new ParsingException("Parser error: bad simple_factor");
    }
  }

  private static ParsedExpr parseFactor(ListIterator<Token> iter) throws ParsingException {
    if (!iter.hasNext()) {
      throw new ParsingException("Parser error: bad factor");
    }
    Token t1 = iter.next();
    if (t1.getType() == OP) {
      if (!iter.hasNext()) {
        throw new ParsingException("Parser error: bad factor");
      }
      if ("-".equals(t1.getVal())) {
        return new UnaryMinusNode(parseSimpleFactor(iter));
      } else if ("+".equals(t1.getVal())) {
        return new UnaryPlusNode(parseSimpleFactor(iter));
      } else {
        throw new ParsingException("Parser error: bad factor");
      }
    } else {
      iter.previous();
      return parseSimpleFactor(iter);
    }
  }

  private static ArrayList<Token> tokenize(String expr) throws ParsingException {
    if (null == expr) {
      throw new ParsingException("expr == null");
    }
    ArrayList<Token> tokens = new ArrayList<>();

    for (int i = 0; i < expr.length(); ++i) {
      char c = expr.charAt(i);
      // Skip whitespaces
      if (Character.isWhitespace(c)) {
        continue;
      }

      if (c == '(') {
        tokens.add(new Token(LBRACE, "("));
      } else if (c == ')') {
        tokens.add(new Token(RBRACE, ")"));
      } else if ("+/*-".indexOf(c) >= 0) {
        tokens.add(new Token(OP, Character.toString(c)));
      } else if (Character.isDigit(c)) {
        int pos = i + 1;
        boolean hasPoint = false;
        while (pos < expr.length() &&
            (Character.isDigit(expr.charAt(pos)) || (expr.charAt(pos) == '.' && !hasPoint))) {
          if (expr.charAt(pos) == '.') {
            hasPoint = true;
          }
          ++pos;
        }
        tokens.add(new Token(NUMBER, expr.substring(i, pos)));
        i = pos - 1;
      } else {
        throw new ParsingException("Tokenizer error at pos " + i);
      }
    }

    return tokens;
  }

  public abstract double eval();

  public abstract String toString();
}
