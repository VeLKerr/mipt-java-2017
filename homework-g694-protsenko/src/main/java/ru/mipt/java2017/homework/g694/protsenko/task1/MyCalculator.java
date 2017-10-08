package ru.mipt.java2017.homework.g694.protsenko.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.Stack;


public class MyCalculator implements Calculator {
  @Override
  public double calculate(String s) throws ParsingException {
    // function for calculating the result of an expression
    if (s == null || s.equals("")) {
      throw new ParsingException("expression can not be empty");
    }
    String[] news; // array for postfix writing expression
    news = postfics(s); // postfix calculation
    double r = postint(news); // computing the result of an expression
    return r;
  }

  private double postint(String[] s) throws ParsingException {
    // postfix counting function
    Stack<Double> stack = new Stack<>(); //stack of value counting per piece
    int j = 0;
    double r = 0;
    double first; // The first variable to calculate the operation
    double second; // The second variable to calculate the operation
    double sum; // the value of the result of applying the operator to two variables
    while (!s[j].equals("")) { //pass through all symbols
      String c = s[j]; //passage string
      if ((c.equals("-") || c.equals("+") || c.equals("*") || c.equals("/")) && (stack.size() < 2)) {
        throw new ParsingException("for binary operation need two numbers");
      }
      switch (c) {
        case "+":
          first = stack.pop();
          second = stack.pop();
          sum = first + second;
          stack.push(sum);
          break;
        case "-":
          first = stack.pop();
          second = stack.pop();
          sum = second - first;
          stack.push(sum);
          break;
        case "*":
          first = stack.pop();
          second = stack.pop();
          sum = first * second;
          stack.push(sum);
          break;

        case "/":
          first = stack.pop();
          second = stack.pop();
          sum = second / first;
          stack.push(sum);
          break;
        default:
          try {
            r = Double.parseDouble(c);
          } catch (NumberFormatException d) {
            throw new ParsingException("Wrong format of number");
          }
          stack.push(r);
          break;
      }
      j++;
    }
    return stack.pop();
  }

  private int[] divide(Stack<String> stack, int unarsymblogic, int unarsymb,
                     String[] str, int kol, int i) throws ParsingException {
    // the function of putting character * in postfix record
    int[] r = new int[3]; // array for returning value values ​​from function
    if (unarsymblogic == 1 && unarsymb == 1) {
      throw new ParsingException("not two operation next");
    }
    unarsymblogic = 1; // flag for the possibility of unary '-'
    if (stack.empty()) { // If the operator stack is empty, we put '/'
      stack.push("/");
      r[0] = unarsymblogic;
      r[1] = unarsymb;
      r[2] = kol;
      return r;
    }
    if (stack.peek().equals("+") || stack.peek().equals("-")) {
      // if at the top operation with priority is less, then put '/'
      stack.push("/");
      r[0] = unarsymblogic;
      r[1] = unarsymb;
      r[2] = kol;
      return r;
    }
    if (stack.peek().equals("*") || stack.peek().equals("/")) {
      // If at the top of the operation with the same priority, then shift operations from the stack to the string
      while (!stack.peek().equals("(") || !stack.peek().equals("+") || !stack.peek().equals("-")) {
        str[kol] = stack.pop();
        kol++;
        if (stack.empty()) {
          break;
        }
      }
      stack.push("/"); // after we put the operation '/' on the stack
      r[0] = unarsymblogic;
      r[1] = unarsymb;
      r[2] = kol;
      return r;
    }
    if (stack.empty() || stack.peek().equals("(")) {
      //if there was nothing on the stack or the last one was a '(', then put '/' on the stack
      stack.push("/");
    }
    return r;
  }

  private int[] plus(Stack<String> stack, int unarsymblogic, int unarsymb,
                     String[] str, int kol, int i) throws ParsingException {
    int[] r = new int[3];
    if (unarsymblogic == 1 && unarsymb == 1) {
      throw new ParsingException("not two operation next");
    }
    if (unarsymblogic == 1 || i == 0) {
      unarsymblogic = 1;
      unarsymb = 1;
    }
    if (stack.empty()) { // if stack empty then push '+' to stack
      stack.push("+");
      r[0] = unarsymblogic;
      r[1] = unarsymb;
      r[2] = kol;
      return r;
    }
    if (stack.peek().equals("-") || stack.peek().equals("+") ||
        stack.peek().equals("*") || stack.peek().equals("/")) {
      // if at the top operation with priority is same or great, then shift operations from the stack to the string
      while (!stack.peek().equals("(")) {
        str[kol] = stack.pop();
        kol++;
        if (stack.empty()) {
          break;
        }
      }
      stack.push("+"); //push '+' to stack
      r[0] = unarsymblogic;
      r[1] = unarsymb;
      r[2] = kol;
      return r;
    }
    if (stack.empty() || stack.peek().equals("(")) {
      stack.push("+"); // if stack empty or last operation was '(' then push '+' to stack
    }
    r[0] = unarsymblogic;
    r[1] = unarsymb;
    r[2] = kol;
    return r;
  }

  private int[] minus(Stack<String> stack, int unarsymblogic, int unarsymb,
                      String[] str, int kol, int i, String s) throws ParsingException {
    int[] r = new int[3];
    if (unarsymblogic == 1 && unarsymb == 1) {
      throw new ParsingException("not two operation next");
    }
    if (unarsymblogic == 1 || i == 0) {
      unarsymblogic = 1;
      unarsymb = 1;
      if (i + 1 < s.length() && Character.isDigit(s.charAt(i + 1))) {
        //if the next character is a digit and you are able to tolerate a unary minus
        stack.push("-");
        r[0] = unarsymblogic;
        r[1] = unarsymb;
        r[2] = kol;
        return r;
      }
    }
    if (stack.empty()) {
      //if stack empty then push '-' to stack
      stack.push("-");
      r[0] = unarsymblogic;
      r[1] = unarsymb;
      r[2] = kol;
      return r;
    }
    if (stack.peek().equals("-") || stack.peek().equals("+")
        || stack.peek().equals("*") || stack.peek().equals("/")) {
      // if at the top operation with priority is same or great, then shift operations from the stack to the string
      while (!stack.peek().equals("(")) {
        str[kol] = stack.pop();
        kol++;
        if (stack.empty()) {
          break;
        }
      }
      stack.push("-"); // push '-' to stack
      r[0] = unarsymblogic;
      r[1] = unarsymb;
      r[2] = kol;
      return r;
    }
    if (stack.empty() || stack.peek().equals("(")) { // if stack empty or last operation was '(' then push '+' to stack
      stack.push("-");
    }
    r[0] = unarsymblogic;
    r[1] = unarsymb;
    r[2] = kol;
    return r;
  }

  private int[] multiply(Stack<String> stack, int unarsymblogic,
                         int unarsymb, String[] str, int kol, int i) throws ParsingException {
    int[] r = new int[3];
    if (unarsymblogic == 1 && unarsymb == 1) {
      throw new ParsingException("not two operation next");
    }
    unarsymblogic = 1; // flag for the possibility of unary '-'
    if (stack.empty()) { // If the operator stack is empty, we put '*'
      stack.push("*");
      r[0] = unarsymblogic;
      r[1] = unarsymb;
      r[2] = kol;
      return r;
    }
    if (stack.peek().equals("+") || stack.peek().equals("-")) {
      stack.push("*"); // if at the top operation with priority is less, then put '*'
      r[0] = unarsymblogic;
      r[1] = unarsymb;
      r[2] = kol;
      return r;
    }
    if ((stack.peek()).equals("*") || stack.peek().equals("/")) {
      // If at the top of the operation with the same priority, then shift operations from the stack to the string
      while (!stack.peek().equals("(") || !stack.peek().equals("+") || !stack.peek().equals("-")) {
        str[kol] = stack.pop();
        kol++;
        if (stack.empty()) {
          break;
        }
      }
      stack.push("*"); //push '*' to stack
      r[0] = unarsymblogic;
      r[1] = unarsymb;
      r[2] = kol;
      return r;
    }
    if (stack.empty() || stack.peek().equals("(")) {
      //if there was nothing on the stack or the last one was a '(', then put '/' on the stack
      stack.push("*");
    }
    r[0] = unarsymblogic;
    r[1] = unarsymb;
    r[2] = kol;
    return r;
  }

  private int[] default1(Stack<String> stack, int unarsymblogic, int unarsymb,
                       String[] str, int kol, char c, int logic) throws ParsingException {
    int[] r = new int[4];
    if (c != '.' && !Character.isDigit(c)) {
      throw new ParsingException("must be only ' ', '(', ')', '.', '+'," +
                " '-', '/', '*', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' ");
    }
    if (unarsymb == 1 && (stack.peek().equals("+") || stack.peek().equals("-"))) {
      //if a unary sign is allowed, then set
      if (stack.peek().equals("-")) {
        str[kol] = "-" + c;
        kol++;
        stack.pop();
        r[0] = 0;
        r[1] = 0;
        r[2] = kol;
        r[3] = 0;
        return r;
      } else {
        str[kol] = "" + c;
        kol++;
        stack.pop();
        r[0] = 0;
        r[1] = 0;
        r[2] = kol;
        r[3] = 0;
        return r;
      }
    }
    if (kol > 0 && (logic == 0)) {
      if (!str[kol - 1].equals("+") && !str[kol - 1].equals("-")
          && !str[kol - 1].equals("*") && !str[kol - 1].equals("/")) {
        str[kol - 1] = str[kol - 1] + c; //we compose a number from a string, if the symbol before that is a number
      } else { //If we could not make a number, then we add a new
        str[kol] = String.valueOf(c);
        kol++;
      }
    } else { // If we could not make a number, then we add a new
      str[kol] = String.valueOf(c);
      kol++;
      logic = 0;
    }
    r[0] = 0;
    r[1] = 0;
    r[2] = kol;
    r[3] = logic;
    return r;
  }

  private String[] postfics(String s) throws ParsingException {
    Stack<String> stack = new Stack<>();
    String[] str = new String[s.length() + 2];
    for (int i = 0; i < s.length() + 2; i++) {
      str[i] = "";
    }
    int openbracket = 0; //open and closed parentheses
    int minusscob = 0; //flag for operation '-' before bracket
    int chislologic = 0; //flag for the presence of a number in the expression
    int logic = 0; //continuation flag
    int kol = 0; //line counter for postfix recording
    int unarsymblogic = 0; //tolerance to unary '-' to '/', '*', '('
    int unarsymb = 0; //admission to the sign of a number by a unary operator
    int[] mas;
    for (int i = 0; i < s.length(); i++) { // pass on the given line
      char c = s.charAt(i);
      switch (c) { //depending on the symbol, there is an addition
        case '+':
          if (minusscob == 0 && i + 1 <= s.length()  && s.charAt(i + 1) == '(') {
            str[kol] = "0";
            kol++;
          }
          logic = 1; //prohibition on the continuation of the number
          mas = plus(stack, unarsymblogic, unarsymb, str, kol, i);
          kol = mas[2];
          unarsymblogic = mas[0];
          unarsymb = mas[1];
          break;
        case '-':
          if (minusscob == 0 && i + 1 <= s.length()  && s.charAt(i + 1) == '(') {
            str[kol] = "0";
            kol++;
          }
          logic = 1; //prohibition on the continuation of the number
          mas = minus(stack, unarsymblogic, unarsymb, str, kol, i, s);
          kol = mas[2];
          unarsymblogic = mas[0];
          unarsymb = mas[1];
          minusscob = -1;
          break;
        case '*':
          logic = 1; //prohibition on the continuation of the number
          minusscob = -1;
          mas = multiply(stack, unarsymblogic, unarsymb, str, kol, i);
          kol = mas[2];
          unarsymblogic = mas[0];
          unarsymb = mas[1];
          break;
        case '/':
          unarsymblogic = 1;
          minusscob = -1;
          logic = 1; //prohibition on the continuation of the number
          mas = divide(stack, unarsymblogic, unarsymb, str, kol, i);
          kol = mas[2];
          unarsymblogic = mas[0];
          unarsymb = mas[1];
          break;
        case '(':
          logic = 1; //prohibition on the continuation of the number
          minusscob = 0; //opening a new parenthesis
          openbracket++; //the increase of not enclosed brackets '('
          if (unarsymb == 1) { //admission to a unary operator
            unarsymblogic = 1;
            unarsymb = 0;
          } else {
            unarsymblogic = 1;
          }
          logic = 1; //prohibition on the continuation of the number
          stack.push("(");
          break;
        case ')':
          openbracket--; //reduction of not enclosed brackets '('
          unarsymblogic = 0; //ban on unary operator
          unarsymb = 0; //ban on unary operator
          logic = 1; //prohibition on the continuation of the number
          if (stack.empty()) {
            throw new ParsingException("if ')' must be '(' for this ");
          }
          while (!stack.peek().equals("(")) {
            str[kol] = stack.pop();
            if (stack.empty() && !str[kol].equals("(")) {
              throw new ParsingException("if ')' must be '(' for this ");
            }
            kol++;
          }
          stack.pop();
          break;
        default:
          if (c == ' ' || c == '\n' || c == '\t' || c == '\r' || c == '\f') {
            continue;
          }
          minusscob = 1;
          chislologic = 1;
          int[] r = default1(stack, unarsymblogic, unarsymb, str, kol, c, logic);
          unarsymblogic = r[0];
          unarsymb = r[1];
          kol = r[2];
          logic = r[3];
      }
    }
    while (!stack.empty()) {
      str[kol] = stack.pop();
      kol++; ////Лажа
    }
    if (openbracket != 0) {
      throw new ParsingException("count '(' must = count ')'");
    }
    if (chislologic == 0) {
      throw new ParsingException("must be minimal one number");
    }
    return str;
  }
}
