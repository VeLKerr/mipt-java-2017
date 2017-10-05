package ru.mipt.java2017.homework.g696.marchenko.task1.MyParser;

import static ru.mipt.java2017.homework.g696.marchenko.task1.MyParser.MyToken.TokenType.LEFTB;
import static ru.mipt.java2017.homework.g696.marchenko.task1.MyParser.MyToken.TokenType.NUMBER;
import static ru.mipt.java2017.homework.g696.marchenko.task1.MyParser.MyToken.TokenType.OPERATION;
import static ru.mipt.java2017.homework.g696.marchenko.task1.MyParser.MyToken.TokenType.RIGHTB;

import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.ArrayList;
import java.util.ListIterator;

public abstract class MyExpression {

  public static MyExpression parse(String expression) throws ParsingException {
    ArrayList<MyToken> MyTokenList = toTokenList(expression);
    return fromTokenList(MyTokenList);
  }

  private static ArrayList<MyToken> toTokenList(String expression) throws ParsingException {
    ArrayList<MyToken> myTokens = new ArrayList<>();

    if (expression == null) {
      throw new ParsingException("expression is null");
    }

    for (int i = 0; i < expression.length(); ++i) {
      char c = expression.charAt(i);

      if (Character.isWhitespace(c)) {
        continue;
      }

      if (c == '(') {
        myTokens.add(new MyToken(LEFTB, "("));
      } else if (c == ')') {
        myTokens.add(new MyToken(RIGHTB, ")"));
      } else if (c == '+' || c == '-' || c == '*' || c == '/') {
        myTokens.add(new MyToken(OPERATION, Character.toString(c)));
      } else if (Character.isDigit(c)) {
        int posit = i + 1;
        boolean point = false;
        while (posit < expression.length() && (Character.isDigit(expression.charAt(posit)) ||
          (expression.charAt(posit) == '.' && !point))) {
          if (expression.charAt(posit) == '.') {
            point = true;
          }
          ++posit;
        }
        myTokens.add(new MyToken(NUMBER, expression.substring(i, posit)));
        i = posit - 1;
      } else {
        throw new ParsingException("invalid expression (position " + i + ")");
      }
    }

    return myTokens;
  }

  private static MyExpression fromTokenList(ArrayList<MyToken> myTokens) throws ParsingException {
    ListIterator<MyToken> iterator = myTokens.listIterator();
    MyExpression expression = parseExpression(iterator);
    if (iterator.hasNext()) {
      throw new ParsingException("invalid expression (extra tokens)");
    }
    return expression;
  }

  private static MyExpression parseExpression(ListIterator<MyToken> iterator) throws ParsingException {
    ArrayList<MyExpression> myTerms = new ArrayList<>();
    ArrayList<MyToken> operations = new ArrayList<>();

    while (iterator.hasNext()) {
      myTerms.add(parseTerm(iterator));
      if (iterator.hasNext()) {
        MyToken operation = iterator.next();
        if (operation.getType() == OPERATION && ("+".equals(operation.getValue()) ||
          "-".equals(operation.getValue()))) {
          operations.add(operation);
        } else {
          iterator.previous();
          break;
        }
      }
    }
    if (myTerms.isEmpty()) {
      throw new ParsingException("invalid expression (No terms)");
    }

    MyExpression expression = myTerms.get(0);
    for (int i = 1; i < myTerms.size(); ++i) {
      if("+".equals(operations.get(i - 1).getValue())) {
        expression = new PlusNode(expression, myTerms.get(i));
      } else {
        expression = new MinusNode(expression, myTerms.get(i));
      }
    }

    return expression;
  }

  private static MyExpression parseTerm(ListIterator<MyToken> iterator) throws ParsingException {
    ArrayList<MyExpression> myFactors = new ArrayList<>();
    ArrayList<MyToken> operations = new ArrayList<>();

    while (iterator.hasNext()) {
      myFactors.add(parseFactor(iterator));
      if (iterator.hasNext()) {
        MyToken operation = iterator.next();
        if (operation.getType() == OPERATION && ("*".equals(operation.getValue()) ||
          "/".equals(operation.getValue()))) {
          operations.add(operation);
        } else {
          iterator.previous();
          break;
        }
      }
    }
    if (myFactors.isEmpty()) {
      throw new ParsingException("invalid expression (No factors)");
    }

    MyExpression expression = myFactors.get(0);
    for (int i = 1; i < myFactors.size(); ++i) {
      if("*".equals(operations.get(i - 1).getValue())) {
        expression = new MultNode(expression, myFactors.get(i));
      } else {
        expression = new DivNode(expression, myFactors.get(i));
      }
    }

    return expression;
  }

  private static MyExpression parseFactor(ListIterator<MyToken> iterator) throws ParsingException {
    if (!iterator.hasNext()) {
      throw new ParsingException("invalid factor");
    }

    MyToken factorStart = iterator.next();
    MyExpression result;
    if (factorStart.getType() == OPERATION) {
      if("+".equals(factorStart.getValue())) {
        result = new UnPlusNode(parseUnsignedFactor(iterator));
      } else if ("-".equals(factorStart.getValue())) {
        result = new UnMinusNode(parseUnsignedFactor(iterator));
      } else {
        throw new ParsingException("invalid factor");
      }
    } else {
      iterator.previous();
      result = parseUnsignedFactor(iterator);
    }
    return result;
  }

  private static MyExpression parseUnsignedFactor(ListIterator<MyToken> iterator) throws ParsingException {
    if (!iterator.hasNext()) {
      throw new ParsingException("invalid unsigned factor");
    }

    MyToken factorStart = iterator.next();
    if (factorStart.getType() == LEFTB) {
      MyExpression expression = parseExpression(iterator);
      if (!iterator.hasNext() || iterator.next().getType() != RIGHTB) {
        throw new ParsingException("No brackets balance");
      }
      return expression;
    } else if (factorStart.getType() == NUMBER) {
      return new NumberNode(Double.parseDouble(factorStart.getValue()));
    } else {
      throw new ParsingException("invalid unsigned factor");
    }
  }

  public abstract double count();
}
