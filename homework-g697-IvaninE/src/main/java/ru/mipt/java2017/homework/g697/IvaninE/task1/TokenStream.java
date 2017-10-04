package ru.mipt.java2017.homework.g697.IvaninE.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

public class TokenStream {

  //буффер для считываемого токена
  private Token buffer;

  //номер текущей позиции
  private int curPos;

  //строка которую мы обрабатываем
  private String expression;

  //пустой ли буфер?
  private boolean buffer_is_filled;

  private static final char[] arithmetic_symbols = {'+', '-', '*', '/', '(', ')'};

  //проверка на то является ли символ оператором или скобкой
  private static boolean isOperator(char character) {
    for (char ch : arithmetic_symbols) {
      if (character == ch) {
        return true;
      }
    }
    return false;
  }

  //задание конфигурации
  public void setParsingExpression(String parsingExpression) {
    expression = parsingExpression;
    curPos = 0;
    buffer_is_filled = false;
  }

  //функция получения и обработки текущего токена
  public Token getCurrentToken() throws ParsingException {
    //если в буфере чтото- есть, то возвращаем его содержимое
    if (buffer_is_filled) {
      buffer_is_filled = false;
      return buffer;
    }
    if (curPos >= expression.length()) {
      return new Token('#');
    }
    //получение очередного символа
    char current = expression.charAt(curPos);
    if (isOperator(current)) {
      curPos += 1;
      return new Token(current);
    } else if (Character.isDigit(current)) {
      StringBuilder stringBuilder = new StringBuilder();
      while (curPos < expression.length() && Character.isDigit(expression.charAt(curPos))) {
        stringBuilder.append(expression.charAt(curPos));
        curPos += 1;
      }
      if (curPos < expression.length() && expression.charAt(curPos) == '.') {
        stringBuilder.append(expression.charAt(curPos));
        curPos += 1;
        while (curPos < expression.length() && Character.isDigit(expression.charAt(curPos))) {
          stringBuilder.append(expression.charAt(curPos));
          curPos += 1;
        }
      }
      String strNumber = stringBuilder.toString();
      double value = Double.parseDouble(strNumber);
      return new Token(value);
    } else {
      throw new ParsingException("Unexpected character");
    }
  }

  public void putBackToStream(Token token) throws ParsingException {
    if (buffer_is_filled) {
      throw new ParsingException("Illegal expression");
    }
    buffer = token;
    buffer_is_filled = true;
  }

}
