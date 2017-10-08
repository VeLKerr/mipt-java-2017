package ru.mipt.java2017.homework.g697.vorobev;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 * Calculator based on 2 stacks.
 *
 * @author Vorobev Ilya
 * @link Calculator
 * @since 04.09.17
 */
public class MySimpleCalculator implements Calculator {
  private String text = null;

  public static void main(String[] args) {
    MySimpleCalculator c = new MySimpleCalculator();
    try {
      System.out.println(c.calculate("()(1+1)()"));
    } catch (ParsingException e) {
      System.out.println("EXCEPTION");
    }

  }

  /**
   * Split the string into tokens: numbers, operators and brackets.
   * Tokens are divided by space symbol, bracket or operator.
   * Use Dijkstra algorithm on token sequence.
   *
   * @param expression string with ariphmetical expression to be calculated
   * @return result of calculation
   * @throws ParsingException if expression was invalid
   */
  @Override
  public double calculate(String expression) throws ParsingException {
    if (expression != null) {
      text = expression;
    }

    precheck();
    preprocess();
    StringTokenizer st = new StringTokenizer(text, "+-/*() \t\r\f\n", true);
    Stack<Double> numbers = new Stack<Double>();
    Stack<Character> functions = new Stack<Character>();

    while (st.hasMoreTokens()) {
      String token = st.nextToken();
      if (iSDouble(token)) {
        //'o' symbol is replaced back to -0.0
        numbers.push(token.contentEquals("o") ? (-0.0) : Double.parseDouble(token));
      } else if (isFunc(token) || token.contentEquals("(")) {
        while (canPop(token.charAt(0), functions)) {
          popFunction(functions, numbers);
        }

        functions.push(token.charAt(0));
      } else if (token.contentEquals(")")) {
        try {
          //closing bracket pop all functions until opening bracket is found
          while (functions.peek() != '(') {
            popFunction(functions, numbers);
          }

          functions.pop();
        } catch (EmptyStackException e) {
          throw new ParsingException("Wrong brackets sequence");
        }
      } else if (!isSpace(token)) {
        throw new ParsingException("Unexpected symbol", new Throwable(token));
      }
    }

    //In the end @functions should be empty and @numbers should contain one number - the result
    if (!functions.empty() || numbers.size() != 1) {
      throw new ParsingException("Wrong number of operators or operands");
    }

    return numbers.pop();
  }

  /**
   * Some checks whether the expression is valid
   *
   * @throws ParsingException if not
   */
  protected void precheck() throws ParsingException {
    if (text == null) {
      throw new ParsingException("Null string");
    }

    if (text.isEmpty()) {
      throw new ParsingException("Empty string");
    }

    if (text.charAt(0) == ')' || text.charAt(text.length() - 1) == '(') {
      throw new ParsingException("Wrong brackets order");
    }

    if (text.contains("()")) {
      throw new ParsingException("Empty brackets");
    }
  }

  /**
   * Preparing expression for calculating by adding '(' in the beggining
   * and ')' in the end.
   * This should be done to get the final result in this algoritm -
   * closnig brackets will pop two remaining numbers and operator between them
   * and add the final result to numbers stack.
   * <p>
   * Function also replaces all '-0' numbers with special symbol - 'o',
   * then replaces '-a' (unary minus) with "0 - a". This replacements is correct
   * only if '-0' numbers are not replaces, because (-0) != (0-0).
   *
   * @throws ParsingException if letter 'o' was in string - this symbol is required in algorithm.
   */
  protected void preprocess() throws ParsingException {
    if (text.indexOf('o') != -1) {
      throw new ParsingException("Unexpected symbol", new Throwable("o"));
    }

    StringBuilder sb = new StringBuilder();
    sb.append("(");
    sb.append(text);
    sb.append(")");
    text = sb.toString();
    text = text.replaceAll("([^\\d.])0+\\.?0+(\\D)", "$10$2");
    text = text.replaceAll("\\(-\\s*0(\\D)", "(o$1");
    text = text.replaceAll("\\(\\s*-", "(0-");
  }

  /**
   * @param token in expression
   * @return true if @token is valid operator, false if not
   */
  protected boolean isFunc(String token) {
    if (token.contentEquals("+")
        || token.contentEquals("-")
        || token.contentEquals("*")
        || token.contentEquals("/")) {
      return true;
    }

    return false;
  }

  /**
   * Symbol 'o' means '-0', so it is a valid number.
   *
   * @param token in expression
   * @return true if @token is valid number, false if not
   */
  protected boolean iSDouble(String token) {
    if (token.contentEquals("o")) {
      return true;
    }

    return (token.matches("^\\d+\\.\\d+$")
      || token.matches("^\\d+$"));
  }

  protected boolean isSpace(String token) {
    return token.matches("^\\s+$");
  }

  /**
   * @param ch symbol that is supposed to be operator
   * @return priority of @ch in ariphmetic expression
   * @throws ParsingException if @ch is not valid operator
   */
  protected int getPriority(char ch) throws ParsingException {
    switch (ch) {
      case '+':
      case '-':
        return 2;
      case '*':
      case '/':
        return 1;
      case '(':
        return -1;
      default:
        throw new ParsingException("Wrong operator");
    }
  }

  /**
   * @param operator  current operator
   * @param functions stack with functions
   * @return true if @operator and top of @functions have appropriate priority
   * @throws ParsingException if cannot get priority of @operator or top of @functions
   */
  protected boolean canPop(char operator, Stack<Character> functions) throws ParsingException {
    if (functions.empty()) {
      return false;
    }

    int operatorPriority = getPriority(operator);
    int nextOperatorPriority = getPriority(functions.peek());
    return operatorPriority >= 0 && nextOperatorPriority >= 0 && operatorPriority >= nextOperatorPriority;
  }

  /**
   * Pops two numbers from @numbers stack, one operator from @functions stack and
   * pushes into @numbers stack result of operator applied to two numbers;
   *
   * @param functions stack with functions
   * @param numbers   stack with numbers
   * @throws ParsingException if @numbers or @functions contain not enough elements
   */
  void popFunction(Stack<Character> functions, Stack<Double> numbers) throws ParsingException {
    char op = functions.pop();
    Double leftOperand;
    Double rightOperand;
    try {
      rightOperand = new Double(numbers.pop());
      leftOperand = new Double(numbers.pop());
    } catch (EmptyStackException e) {
      throw new ParsingException("Wrong number of operands or operators");
    }

    switch (op) {
      case '+':
        numbers.push(leftOperand + rightOperand);
        break;
      case '-':
        numbers.push(leftOperand - rightOperand);
        break;
      case '*':
        numbers.push(leftOperand * rightOperand);
        break;
      case '/':
        numbers.push(leftOperand / rightOperand);
        break;
      default:
        throw new ParsingException("Wrong operator");
    }
  }
}

