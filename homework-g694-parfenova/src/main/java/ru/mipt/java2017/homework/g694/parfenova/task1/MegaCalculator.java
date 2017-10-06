package ru.mipt.java2017.homework.g694.parfenova.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

import ru.mipt.java2017.homework.g694.parfenova.task1.TokenStream.Operator;
import ru.mipt.java2017.homework.g694.parfenova.task1.TokenStream.Brace;
import ru.mipt.java2017.homework.g694.parfenova.task1.TokenStream.Number;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Created by Julia Parfenova on 5/10/17.
 */
public class MegaCalculator implements Calculator {
  @Override
  public final double calculate(String expression) throws ParsingException {
    if (expression == null) {
      throw new ParsingException("Expression cannot be null");
    }
    if (expression.equals("")) {
      throw new ParsingException("Expression cannot be empty");
    }

    expression = prepare(expression);

    TokenStream ts = new TokenStream(expression);
    List<TokenStream.Token> tokenList = new ArrayList<>();
    TokenStream.Token token = ts.getToken();
    while (token != null) {
      tokenList.add(token);
      token = ts.getToken();
    }
    return calculateTokenizedRPN(convertToRPN(tokenList));
  }

  private static ArrayList<TokenStream.Token> convertToRPN(List<TokenStream.Token> input) {

    ArrayList<TokenStream.Token> output = new ArrayList<>();
    Deque<TokenStream.Token> stack = new LinkedList<>();

    for (TokenStream.Token token : input) {
      if (token instanceof Operator) { // If operator
        Operator operator = (Operator) token;

        while (!stack.isEmpty()
          && stack.peek() instanceof Operator
          && (((Operator) stack.peek()).getType().getPriority() >= operator.getType().getPriority())) {
          output.add(stack.pop());
        }
        stack.push(operator);

      } else if (token instanceof Brace) { // if brace
        Brace brace = (Brace) token;
        if (!brace.getType()) { // opening
          stack.push(token);
        } else { // closing
          while (!stack.isEmpty() && !(stack.peek() instanceof Brace)) { // while not '('
            output.add(stack.pop());

          }
          stack.pop();
        }
      } else { // If digit
        output.add(token);
      }
    }
    while (!stack.isEmpty()) {
      output.add(stack.pop());
    }
    return output;
  }

  private double calculateTokenizedRPN(List<TokenStream.Token> rpn) throws ParsingException {

    Stack<TokenStream.Number> numbers = new Stack<>();
    for (TokenStream.Token t : rpn) {
      if (t instanceof TokenStream.Number) {
        numbers.push(((TokenStream.Number) t));
      } else {
        TokenStream.Number op1 = numbers.pop();
        TokenStream.Number op2 = numbers.pop();
        double result = 0;
        TokenStream.Operator op = (TokenStream.Operator) t;
        switch (op.getType()) {
          case OperatorTypePlus:
            result = op1.add(op2);
            break;
          case OperatorTypeMinus:
            result = op1.substract(op2);
            break;
          case OperatorTypeMultiply:
            result = op1.multiply(op2);
            break;
          case OperatorTypeDivide:
            result = op1.divide(op2);
            break;
          default:
            throw new ParsingException("Invalid operation token");

        }
        numbers.push(new Number(result));
      }
    }
    return numbers.peek().getValue();
  }

  private String prepare(String expression) throws ParsingException {

    Pattern noOperator = Pattern.compile("\\d\\s\\d");
    Matcher matcher = noOperator.matcher(expression);
    if (matcher.find()) {
      throw new ParsingException("Two numbers without operator is unacceptable");
    }

    expression = expression.replaceAll("\\s+", "");

    if (!expression.isEmpty() && expression.charAt(0) == '-') {
      expression = '~' + expression.substring(1);
    }

    Pattern unacceptablePairs = Pattern.compile("([+\\-*/]{2})|([(][+\\-*/])|\\(\\)");
    int balance = 0;
    char last = '!';
    char cur;

    for (int i = 0; i < expression.length(); i++) {
      cur = expression.charAt(i);
      if (last == '(' && cur == '-') {
        String finish = (i == expression.length() - 1) ? "" : expression.substring(i + 1);
        expression = expression.substring(0, i) + '~' + finish;
        cur = '~';
      } else if ("+-;/".indexOf(last) >= 0 && cur == '-') {
        String finish = (i == expression.length() - 1) ? "" : expression.substring(i + 1);
        expression = expression.substring(0, i) + '~' + finish;
        cur = '~';
      }
      if (cur == '(') {
        balance++;
      } else if (cur == ')') {
        balance--;
      }

      if (unacceptablePairs.matcher(Character.toString(last) + cur).matches()) {
        throw new ParsingException("Invalid characters position");
      }

      last = cur;
    }

    if (balance != 0) {
      throw new ParsingException("Unbalanced parentheses");
    }
    Pattern invalidCharCheck = Pattern.compile("[~\\d\\(\\)\\+\\-\\*\\/\\.]+");
    if (!invalidCharCheck.matcher(expression).matches()) {
      throw new ParsingException("Invalid characters");
    }
    return expression;
  }
}

class TokenStream {
  public enum OperatorType {
    OperatorTypePlus(1), OperatorTypeMinus(2), OperatorTypeMultiply(3), OperatorTypeDivide(4);
    private final int priority;

    OperatorType(int p) {
      priority = p;
    }

    public int getPriority() {
      return priority;
    }
  }

  private Token buffer;
  private boolean full;
  private String expression;
  private int stringPosition;

  TokenStream(String expr) {
    expression = expr;
    buffer = null;
    full = false;
    stringPosition = 0;
  }

  public Token getToken() throws ParsingException {
    if (expression.length() <= stringPosition) {
      return null;
    }

    if (full) {
      full = false;
      return buffer;
    }
    char c = getChar(stringPosition);
    stringPosition++;

    Token token;
    if ("()".indexOf(c) >= 0) {
      token = new Brace(c);
    } else if ("+-*/".indexOf(c) >= 0) {
      token = new Operator(c);
    } else if ("0123456789~".indexOf(c) >= 0) {
      token = new Number(getNumber(c));
    } else {
      throw new ParsingException("Unexpected symbol " + c);
    }

    return token;
  }

  public void pushToken(Token buf) throws ParsingException {
    if (!full) {
      full = true;
      buffer = buf;
    } else {
      throw new ParsingException("TokenStream buffer already full");
    }
  }

  private char getChar(int pos) throws ParsingException {
    if (pos >= expression.length()) {
      return 0;
    }
    char retval = expression.charAt(pos);

    return retval;
  }

  private double getNumber(char c) throws ParsingException {
    String numberString = (c == '~') ? "-" : Character.toString(c);

    boolean singleDotPresent = false;
    while (expression.length() > stringPosition && ".0123456789".indexOf(expression.charAt(stringPosition)) >= 0) {

      char cur = getChar(stringPosition);
      if (cur == '.') {
        if (singleDotPresent) {
          throw new ParsingException("Multiple dots present in one number");
        }
        singleDotPresent = true;
      }
      numberString += Character.toString(cur);
      stringPosition++;
    }
    if (numberString.equals("-")) {
      numberString = "-1";
      pushToken(new Operator('*'));
    }

    return Double.parseDouble(numberString);
  }

  abstract static class Token {
    public abstract String getVisualRepresentation();
  }

  static class Number extends Token {
    private double value;

    Number(final double val) {
      value = val;
    }

    public double getValue() {
      return value;
    }

    public double add(Number n) {
      return value + n.getValue();
    }

    public double substract(Number n) {
      return n.getValue() - value;
    }

    public double multiply(Number n) {
      return value * n.getValue();
    }

    public double divide(Number n) {
      return n.getValue() / value;
    }

    @Override
    public String getVisualRepresentation() {
      return new Double(value).toString();
    }
  }

  static class Operator extends Token {
    private OperatorType type;
    private char value;

    Operator(final char symbol) throws ParsingException {

      value = symbol;

      switch (symbol) {
        case '+':
          type = OperatorType.OperatorTypePlus;
          break;
        case '-':
          type = OperatorType.OperatorTypeMinus;
          break;
        case '*':
          type = OperatorType.OperatorTypeMultiply;
          break;
        case '/':
          type = OperatorType.OperatorTypeDivide;
          break;
        default:
          throw new ParsingException("Unknown operator symbol");
      }

    }

    public OperatorType getType() {
      return type;
    }

    public String getVisualRepresentation() {
      return Character.toString(value);
    }
  }

  static class Brace extends Token {
    private boolean type; // 0 - opening ; 1 – closing

    Brace(final char symbol) throws ParsingException {

      switch (symbol) {
        case '(':
          type = false;
          break;
        case ')':
          type = true;
          break;
        default:
          throw new ParsingException("Unknown Brace symbol");
      }
    }

    public boolean getType() {
      return type;
    }

    public String getVisualRepresentation() {
      if (!type) {
        return "(";
      } else {
        return ")";
      }
    }
  }
}
