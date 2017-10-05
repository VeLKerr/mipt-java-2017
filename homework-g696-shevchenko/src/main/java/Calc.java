import java.io.IOException;
import java.util.LinkedList;
import ru.mipt.java2017.homework.base.task1.ParsingException;
import java.util.Scanner;

public class Calc {
  private enum Operations { PLUS, MINUS, MULTIPLY, DIVIDE }
  private static final char space = ' ', left_bracket = '(', right_bracket = ')';

  public static void main()
  {
    System.out.println("My Calculator.\n");
    Scanner in = new Scanner(System.in);
    String expression = in.nextLine();
    Calc c = new Calc(expression);
    System.out.println(c.calculate());
  }

  private String data;

  //method string.equals()

  public Calc(String str)
  {
    data = simplify(str);
  }

  /**
   * Deletes tabs, spaces; checks if there are symbols that cannot be interpreted.
   * Checks the correctness of bracket sequence.
   * @param str - given string.
   * @return string - simplified string ready for parsing.
   */
  private String simplify(String str)
  {
    StringBuilder new_str;
    for (int cp = 0; cp < str.length(); ++cp)
      //if () TODO!!!
  }

  //FIXME
  /**
   * Parses data into arrays expression and actions. Recursively calls itself and
   * method double calc(LinkedList<String> expr, LinkedList<Operations> actions).
   * @param start - starting CodePoint of data
   * @param end - ending CodePoint of data
   */
  private void parse(LinkedList<String> expression, LinkedList<Operations> actions,
      int start, int end) throws MyParsingException
  {
    boolean last_was_number = false;
    boolean last_was_action = false;
    boolean digits = false; //writing digits at the moment
    boolean empty_braces = true;
    int brackets_count = 0;
    StringBuilder new_number = new StringBuilder();


    //int count = 1;
    actions.set(0, Operations.PLUS);
    for (int cp = data.codePointAt(start); cp < end; ++cp)
    {
      if (Character.isDigit(data.charAt(cp)) || data.charAt(cp) == '.')
      {
        if (data.charAt(cp) == '.' && new_number.length() == 0) // nothing can start with .
          throw new MyParsingException;
        else if (last_was_number && digits)//two numbers in a row without action
          throw new MyParsingException;
        else
        {
          new_number.append(data.charAt(cp));
          digits = true;
          last_was_number = true;
          last_was_action = false;
        }
        continue;
      }

      if (data.charAt(cp) == right_bracket)
      {
        //digits = false;
        System.out.println("My mistake - parser sees right brackets where it shouldn't");
        //continue;
      }

      if (data.charAt(cp) == left_bracket)
      {
        if (new_number.length() > 0)
          expression.add(new_number.toString());
        digits = false;
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
          throw new MyParsingException;
        parse(additional, additional_actions, cp + 1, j);
        cp = j;
        expression.add(String.valueOf(calc(additional, additional_actions)));
        continue;
      }

      //data.charAt(cp) is an operation sign
      if (new_number.length() > 0)
        expression.add(new_number.toString());
      digits = false;
      if (last_was_action)
        throw new MyParsingException;
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
          throw new MyParsingException;
      }

    }
    if (last_was_action)
      throw new ParsingException("Invalid input");
    if (new_number.length() > 0)
      expression.add(new_number.toString());
  }

  public double calculate()
  {
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
  private double calc(LinkedList<String> expr, LinkedList<Operations> actions)
  {
    try {
      //1-st priority operations:

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
          actions.remove(i);
        }

      double result = 0;
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
    } catch (IOException e)
    {
      throw new MyParsingException;
    }
  }
}
