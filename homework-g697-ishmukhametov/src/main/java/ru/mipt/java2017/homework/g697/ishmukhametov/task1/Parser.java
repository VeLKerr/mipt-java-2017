package ru.mipt.java2017.homework.g697.ishmukhametov.task1;

import java.util.Stack;
import ru.mipt.java2017.homework.base.task1.ParsingException;

public class Parser {

    private String reformated_expression = "";
    private int pos;
    private char current_prev;
    private char reformated_prev;
    private final String NUMBERS = "0123456789.";

    String getRez() {
        return reformated_expression;
    }

    public void Parse(String expression) throws ParsingException {
        if(expression == null) {
            throw new ParsingException("Empty expression", new NullPointerException());
        }
        expression = expression.replace(" ","").
                replace("\n", "").
                replace("\t", "");
        expression = '#' + expression + '#';

        Stack<Character> temp = new Stack<>();
        temp.add('#');

        pos = 1;
        current_prev = '#';
        reformated_prev = current_prev;

        while (!temp.isEmpty()) {
            char curr = expression.charAt(pos);

            if ("*/".indexOf(curr) != -1) {
                FstPriorityHandler(curr, temp);
                continue;
            }

            if ("+-".indexOf(curr)!= -1) {
                SndPriorityHandler(curr, temp);
                continue;
            }

            if (curr == '(' ) {
                temp.add(curr);
                current_prev = curr;
                pos++;
                continue;
            }

            if (curr == ')') {
                CloseBrace(temp);
                continue;
            }

            if (curr == '#') {
                ExitSymbol(temp);
                continue;
            }

            if (NUMBERS.indexOf(curr) != -1) {
                reformated_expression += curr;
                reformated_prev = curr;
                current_prev = curr;
                pos++;
            } else {
                throw new ParsingException("Invalid symbol");
            }
        }
        if (reformated_expression.equals("")) {
            throw new ParsingException("Empty expression");
        }

        String tmp_string = "";
        for (int i = 0; i < reformated_expression.length(); i++) {
            if ("+-".indexOf(reformated_expression.charAt(i)) == -1) {
                while (temp.size() != 1 && !temp.isEmpty()) {
                    char a = temp.pop();
                    char b = temp.pop();
                    if (a == b) {
                        temp.add('+');
                    } else {
                        temp.add('-');
                    }
                }
                if (temp.size() == 1) {
                    tmp_string += temp.pop();
                }
                tmp_string += reformated_expression.charAt(i);
            } else {
                temp.add(reformated_expression.charAt(i));
            }
        }
        reformated_expression = tmp_string;
    }

    private void FstPriorityHandler(char symbol, Stack<Character> temp) throws ParsingException{
        if ("(+-/*#".indexOf(current_prev) != -1) {
                throw new ParsingException("Operators");
        }
        if (NUMBERS.indexOf(reformated_prev) != -1) {
            reformated_expression += " ";
            reformated_prev = ' ';
        }
        if (temp.peek() == '*' || temp.peek() == '/') {
            reformated_prev = ' ' ;
            reformated_expression += temp.pop() + " ";
        } else {
            temp.add(symbol);
            current_prev = symbol;
            pos++;
        }
    }

    private void SndPriorityHandler(char symbol, Stack<Character> temp) throws ParsingException{
        if ("+-*/".indexOf(current_prev)!= -1) {
            throw new ParsingException("Operators");
        }
        if (NUMBERS.indexOf(reformated_prev) != -1) {
            reformated_expression += " ";
            reformated_prev = ' ';
        }
        if ("(#".indexOf(current_prev)!= -1 ) {
            current_prev = symbol;
            reformated_prev = symbol;
            reformated_expression += symbol;
            pos++;
            return;
        }
        if (temp.peek() == '#' || temp.peek() == '(') {
            temp.add(symbol);
            current_prev = symbol;
            pos++;
        } else {
            reformated_prev = ' ';
            reformated_expression += temp.pop() + " ";
        }
    }

    private void CloseBrace(Stack<Character> temp) throws ParsingException {
        if(current_prev == '(') {
            throw new ParsingException("Empty braces");
        }
        if (temp.peek() == '#') {
            throw new ParsingException("Wrong number of braces");
        }
        if (temp.peek() == '(') {
            current_prev = ')';
            pos++;
            temp.pop();
        } else {
            if (NUMBERS.indexOf(reformated_prev) != -1) {
                reformated_expression = reformated_expression.concat(" ");
            }
            reformated_prev = ' ';
            reformated_expression = reformated_expression.concat(temp.pop() + " ");
        }
    }

    private  void ExitSymbol(Stack<Character> temp) throws ParsingException {
        if (temp.peek() == '(') {
            throw new ParsingException("Wrong number of braces");
        }
        if (temp.peek() == '#') {
            temp.pop();
        } else {
            if (NUMBERS.indexOf(reformated_prev) != -1) {
                reformated_expression = reformated_expression.concat(" ");
            }
            reformated_prev = ' ';
            reformated_expression = reformated_expression.concat(temp.pop() + " ");
        }
    }
}
