import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.ArrayList;

public class Foo implements Calculator {

  @Override
  public double calculate(String s) throws ParsingException {
    if (s == null) {
      throw new ParsingException("wrong expression");
    }
    return calculate(s, 0, s.length());
  }

  private double calculate(String s, int from, int to) throws ParsingException {
    if (from == to) 
      throw new ParsingException("wrong expression");
    ArrayList<Double> terms = new ArrayList<>();
    terms.add(0.0);
    Double nextTerm = 1.0;
    Double nextNumber = 0.0;
    Boolean isLastMult = true;
    Boolean noNumber = false;
    while (((s.charAt(from) == ' ') || (s.charAt(from) == '\t') || (s.charAt(from) == '\n'))) {
      from++;
      if (from == to) {
        throw new ParsingException("wrong expression");
      }
    }
    if (s.charAt(from) == '+') {
      from++;
    } else if (s.charAt(from) == '-') {
      nextTerm = -1.0;
      from++;
    }
    while (((s.charAt(from) == ' ') || (s.charAt(from) == '\t') || (s.charAt(from) == '\n'))) {
      from++;
      if (from == to) {
        throw new ParsingException("wrong expression");
      }
    }
    while (from < to) {
      if (s.charAt(from) == '-') {
        nextTerm = -1.0;
        from++;
      }
      while (((s.charAt(from) == ' ') || (s.charAt(from) == '\t') || (s.charAt(from) == '\n'))) {
        from++;
        if (from == to) {
          throw new ParsingException("wrong expression");
        }
      }
      if (s.charAt(from) == '(') {
        int start = from + 1;
        int balance = 1;
        while (balance > 0) {
          from++;
          if (from == to) {
            throw new ParsingException("wrong expression");
          }
          if (s.charAt(from) == '(') {
            balance++;
          }
          if (s.charAt(from) == ')') {
            balance--;
          }
        }
        nextNumber = calculate(s, start, from);
        from++;
        if (from < to) {
          while (((s.charAt(from) == ' ') || (s.charAt(from) == '\t') || (s.charAt(from)
              == '\n'))) {
            from++;
            if (from == to) {
              break;
            }
          }
        }
      } else if (((s.charAt(from) >= '0') && (s.charAt(from) <= '9')) || s.charAt(from) == '.') {
        while (
            (((s.charAt(from) >= '0') && (s.charAt(from) <= '9')) || (((s.charAt(from) == ' ') || (
                s.charAt(from) == '\t') || (s.charAt(from) == '\n')))) && (from < to)) {
          if ((s.charAt(from) != ' ') && (s.charAt(from) != '\n') && (s.charAt(from) != '\t')) {
            noNumber = false;
            nextNumber = nextNumber * 10 + s.charAt(from) - '0';
          }
          from++;
          if (from == to) {
            break;
          }
        }
        if (from < to) {
          if (s.charAt(from) == '.') {
            Double dot = 0.1;
            from++;
            while (((s.charAt(from) == ' ') || (s.charAt(from) == '\t') || (s.charAt(from)
                == '\n'))) {
              from++;
            }

            while (
                (((s.charAt(from) >= '0') && (s.charAt(from) <= '9')) || ((
                    (s.charAt(from) == ' ')
                        || (s.charAt(from) == '\t') || (s.charAt(from) == '\n')))) && (from
                    < to)) {
              if ((s.charAt(from) != ' ') && (s.charAt(from) != '\n') && (s.charAt(from)
                  != '\t')) {
                nextNumber = nextNumber + (dot * (s.charAt(from) - '0'));
                noNumber = false;
                dot = dot / 10;
              }
              from++;
              if (from == to) {
                break;
              }
            }
          }
        }
        if (noNumber) {
          throw new ParsingException("wrong expression");
        }
        noNumber = true;
      } else {
        throw new ParsingException("wrong expression");
      }
      if (isLastMult) {
        nextTerm = nextTerm * nextNumber;
      } else {
        nextTerm = nextTerm / nextNumber;
      }
      nextNumber = 0.0;
      if (from == to) {
        for (int i = 0; i < terms.size(); i++) {
          nextTerm = nextTerm + terms.get(i);
        }
        return nextTerm;
      }
      switch (s.charAt(from)) {
        case '+':
          terms.add(nextTerm);
          nextTerm = 1.0;
          isLastMult = true;
          break;
        case '-':
          terms.add(nextTerm);
          nextTerm = -1.0;
          isLastMult = true;
          break;
        case '*':
          isLastMult = true;
          break;
        case '/':
          isLastMult = false;
          break;
        default:
          throw new ParsingException("wrong expression");
      }
      from++;
      while (((s.charAt(from) == ' ') || (s.charAt(from) == '\t') || (s.charAt(from) == '\n'))) {
        from++;
      }
    }
    throw new ParsingException("wrong expression");
  }
}

