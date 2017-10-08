package ru.mipt.java2017.homework.g696.shevchenko.task1;

import java.util.LinkedList;
import ru.mipt.java2017.homework.base.task1.ParsingException;
import ru.mipt.java2017.homework.base.task1.Calculator;

/**
 * Class of a simple calculator which supports +,-,*,/ and brackets.
 * Operation priorities are working.
 *
 * @author Alexey Shevchenko
 */

public class Calc implements Calculator
{
  private enum Operations { PLUS, MINUS, MULTIPLY, DIVIDE }
  private static final char space = ' ', left_bracket = '(', right_bracket = ')';
  private String data;

  /**
   * Deletes tabs, spaces, symbols like '\n'. Perfoms checks for the correctness of bracket
   * sequence and some others. May throw ParsingException.
   * @param str - given string.
   * @return string - simplified string ready for parsing.
   */
  private String simplify(String str) throws ParsingException
  {
    try
    {
      if (str.equals(""))
        throw new ParsingException("Empty string");
    } catch (NullPointerException e)
    {
      throw new ParsingException("String is null");
    }

    int bracket_count = 0;
    boolean in_brackets = false, empty_brackets = false, starting_symbol = true;
    StringBuilder new_str = new StringBuilder();
    for (int cp = 0; cp < str.length(); ++cp)
    {
      switch (str.charAt(cp)) {
        case space:
        case '\n':
        case '\t': {
          if (in_brackets)
            empty_brackets = false;
          break;
        }
        case left_bracket: {
          in_brackets = true;
          ++bracket_count;
          empty_brackets = true;
          if (starting_symbol)
            new_str.append('+');
          starting_symbol = true;
          new_str.append(str.charAt(cp));
          break;
        }
        case right_bracket: {
          --bracket_count;
          starting_symbol = false;
          if (empty_brackets || bracket_count < 0)
            throw new ParsingException("");
          new_str.append(str.charAt(cp));
          break;
        }
        default:
        {
          if (in_brackets)
            empty_brackets = false;
          if (starting_symbol)
          {
            starting_symbol = false;
            switch (str.charAt(cp)) {
              case '+':
              {
                new_str.append('+'); //continue;
                break;
              }
              case '-':
              {
                new_str.append('-');
                break;
              }
              case '*':
              {
                throw new ParsingException("Invalid input: symbol * at the beginning.");
              }
              case '/':
                throw new ParsingException("Invalid input: symbol / at the beginning.");
              default:
              {
                if (Character.isDigit(str.charAt(cp)))
                  new_str.append('+');
                new_str.append(str.charAt(cp));
              }
            }
          }
          else
          {
            new_str.append(str.charAt(cp));
          }
        }
      }
    }
    if (bracket_count > 0)
      throw new ParsingException("Problem with brackets");
    if (new_str.length() == 0)
      throw new ParsingException("Nothing");
    return new_str.toString();
  }

  /**
   * Parses data into arrays expression and actions. Recursively calls itself and
   * method double calc(LinkedList<String> expr, LinkedList<Operations> actions).
   * @param start - starting CodePoint of data
   * @param end - ending CodePoint of data
   */
  private void parse(LinkedList<String> expression, LinkedList<Operations> actions,
      int start, int end) throws ParsingException
  {
    boolean last_was_number = false;
    boolean last_was_action = false;
    boolean digits = false; //writing digits at the moment
    boolean empty_braces;
    int brackets_count;
    StringBuilder new_number = new StringBuilder();

    for (int cp = start; cp < end; ++cp)
    {
      if (Character.isDigit(data.charAt(cp)) || data.charAt(cp) == '.')
      {
        if (data.charAt(cp) == '.' && new_number.length() == 0)
          throw new ParsingException("Nothing can start with .");
        else if (last_was_number && !digits)
          throw new ParsingException("Two numbers in a row without action");
        else
        {
          new_number.append(data.charAt(cp));
          digits = true;
          last_was_number = true;
          last_was_action = false;
        }
        continue;
      }

      if (data.charAt(cp) == right_bracket) // This never happens
        System.out.println("My mistake - parser sees right brackets where it shouldn't");

      if (data.charAt(cp) == left_bracket)
      {
        if (new_number.length() > 0)
          expression.add(new_number.toString());
        new_number.setLength(0);
        digits = false;
        last_was_action = false;
        last_was_number = true;
        LinkedList<String> additional = new LinkedList<String>();
        LinkedList<Operations> additional_actions = new LinkedList<Operations>();
        int j = cp + 1;
        brackets_count = 0;
        empty_braces = true;
        while (j < data.length()) {
          if (data.charAt(j) == left_bracket)
            ++brackets_count;
          else if (data.charAt(j) == right_bracket)
            if (brackets_count > 0)
              --brackets_count;
            else
              break;
          else
            empty_braces = false;
          ++j;
        }
        if (empty_braces)
          throw new ParsingException("Invalid input: empty braces.");
        parse(additional, additional_actions, cp + 1, j);
        cp = j;
        expression.add(String.valueOf(calc(additional, additional_actions)));
        continue;
      }

      if (new_number.length() > 0)
        expression.add(new_number.toString());
      new_number.setLength(0);
      digits = false;
      if (last_was_action)
        throw new ParsingException("Two operation signs in a row.");
      last_was_action = true;
      last_was_number = false;
      switch (data.charAt(cp)) {
        case '+': {
          actions.add(Operations.PLUS);
          break;
        }
        case '-': {
          actions.add(Operations.MINUS);
          break;
        }
        case '*': {
          actions.add(Operations.MULTIPLY);
          break;
        }
        case '/': {
          actions.add(Operations.DIVIDE);
          break;
        }
        default:
          throw new ParsingException("Symbol " + data.charAt(cp) + " is not suitable here.");
      }
    }
    if (last_was_action)
      throw new ParsingException("Expression cannot end with an operation sign.");
    if (new_number.length() > 0)
      expression.add(new_number.toString());
  }

  /**
   * Method activates parser and calculations.
   * @param str - given string expression to calculate
   * @return calculated result
   * @throws ParsingException if the str is invalid as an expression
   */
  public double calculate(String str) throws ParsingException
  {
    data = simplify(str);
    LinkedList<String> expression = new LinkedList<String>();
    LinkedList<Operations> operations = new LinkedList<Operations>();
    parse(expression, operations, 0, data.length());
    return calc(expression, operations);
  }

  /**
   * Calculates simple parsed expression with no brackets.
   * Throws MyParsingException if cannot convert expr[i] to double.
   * @param expr - LinkedList that contains double values (as strings)
   * @param actions - LinkedList<Operations>, action[i] stands for the operation before expr[i]
   * @return calculated result
   */
  private double calc(LinkedList<String> expr, LinkedList<Operations> actions) throws ParsingException
  {
    //1-st priority operations:
    try {
      for (int i = 0; i < expr.size(); ++i)
        if (actions.get(i) == Operations.MULTIPLY) {
          expr.set(i - 1,
            String.valueOf(Double.valueOf(expr.get(i - 1)) * Double.valueOf(expr.get(i))));
          expr.remove(i);
          actions.remove(i);
        } else if (actions.get(i) == Operations.DIVIDE) {
          expr.set(i - 1,
            String.valueOf(Double.valueOf(expr.get(i - 1)) / Double.valueOf(expr.get(i))));
          expr.remove(i);
          actions.remove(i--);
        }

      double result;
      if (actions.get(0) == Operations.MINUS)
        result = -Double.valueOf(expr.get(0));
      else
        result = Double.valueOf(expr.get(0));

      //2-nd priority operations:
      for (int i = 1; i < expr.size(); ++i) {
        if (actions.get(i) == Operations.PLUS)
          result += Double.valueOf(expr.get(i));
        else
          result -= Double.valueOf(expr.get(i));
      }
      return result;
    } catch (NumberFormatException e)
    {
      throw new ParsingException("");
    }
  }

}
