package ru.mipt.java2017.homework.g697.vorobev;

//import ru.mipt.java2017.homework.base.task1.Calculator;
//import ru.mipt.java2017.homework.base.task1.ParsingException;

import jdk.nashorn.internal.runtime.ECMAException;

import javax.crypto.ExemptionMechanismException;
import javax.xml.crypto.dom.DOMCryptoContext;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.zip.DeflaterOutputStream;

public class MySimpleCalculator {
  private String text = null;

  public double calculate(String str) throws Exception {
    if (str != null) {
      text = str;
    }
    else
    {
      throw new Exception();
    }

    preprocess();
    StringTokenizer st = new StringTokenizer(text, "+-/*() \t\r\f\n", true);
    Stack<Double> numbers = new Stack<Double>();
    Stack<Character> functions = new Stack<Character>();

    while (st.hasMoreTokens()) {
      String token = st.nextToken();
      if(iSDouble(token))
      {
        numbers.push(token.contentEquals("o") ? -0.0 :  Double.parseDouble(token));
      }
      else if(isFunc(token) || token.contentEquals("("))
      {
        while(canPop(token.charAt(0), functions))
        {
          popFunction(functions, numbers);
        }

        functions.push(token.charAt(0));
      }
      else if(token.contentEquals(")"))
      {
        try
        {
          while (functions.peek() != '(')
          {
            popFunction(functions, numbers);
          }

          functions.pop();
        }
        catch (EmptyStackException e)
        {
          throw new Exception();
        }
      }
      else if(isSpace(token)) {}
      else
      {
        throw new Exception();
      }
    }

    if(!functions.empty() || numbers.size() != 1)
    {
      throw new Exception();
    }

    return numbers.pop();
  }

  private void preprocess() throws Exception {
    if (text.indexOf('o') != -1) {
      throw new Exception();
    }

    StringBuilder sb = new StringBuilder();
    sb.append("(");
    sb.append(text);
    sb.append(")");
    text = sb.toString();
    text = text.replaceAll("([^\\d.])0+.?0+(\\D)", "$10$2");
    text = text.replaceAll("\\(-\\s*0(\\D)", "(o$1");
    text = text.replaceAll("\\(\\s*-", "(0-");
  }

  boolean isFunc(String token) {
    if (token.contentEquals("+")
      || token.contentEquals("-")
      || token.contentEquals("*")
      || token.contentEquals("/")) {
      return true;
    }

    return false;
  }

  boolean iSDouble(String token) {
    if (token.contentEquals("o")) {
      return true;
    }

    return (token.matches("^\\d+.\\d+$")
      || token.matches("^\\d+$"));
  }

  boolean isSpace(String token)
  {
   return token.matches("^\\s+$");
  }

  int getPriority(char ch){
    switch (ch) {
      case '+':
      case '-':
        return 2;
      case '*':
      case '/':
        return 1;
      case '(':
        return -1;
    }

    return 0;
  }

  boolean canPop(char operation, Stack<Character> functions)
  {
    if(functions.empty())
    {
      return false;
    }

    int p1 = getPriority(operation);
    int p2 = getPriority(functions.peek());
    return p1 >= 0 && p2 >= 0 && p1 >= p2;
  }

  void popFunction(Stack<Character> functions, Stack<Double> numbers) throws Exception
  {
    char op = functions.pop();
    Double A, B;
    try
    {
      B = new Double(numbers.pop());
      A = new Double(numbers.pop());
    }
    catch (EmptyStackException e)
    {
      throw new Exception();
    }

    switch (op)
    {
      case '+':
        numbers.push(A + B);
        break;
      case '-':
        numbers.push(A - B);
        break;
      case '*':
        numbers.push(A * B);
        break;
      case '/':
        numbers.push(A / B);
    }
  }
}

