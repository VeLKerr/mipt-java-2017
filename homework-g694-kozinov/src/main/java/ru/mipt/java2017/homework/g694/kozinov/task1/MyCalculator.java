package ru.mipt.java2017.homework.g694.kozinov.task1;

import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;
import java.util.*;

public class MyCalculator implements Calculator{
    private Stack <Double> num;
    private Stack <Character> op;

    private void process(char operation) {
        double a = num.pop();
        double b = num.pop();

        switch(Detecter.GetOperKind(operation)) {
            case PLUS:
                num.push(a + b);
                break;
            case MINUS:
                num.push(b - a);
                break;
            case MULTI:
                num.push(a * b);
                break;
            case DIVISION:
                num.push(b / a);
                break;
        }
    }

    public double calculate(String expression) throws ParsingException{
        num = new Stack <Double>();
        op = new Stack<Character>();
        Parser current_parser = new Parser();
        current_parser.ParseString(expression);

        for (int i = 0; i < current_parser.size(); ++i) {
            String curr_lexema = current_parser.getAt(i);

            switch(Detecter.GetCharKind(curr_lexema.charAt(0))) {
                case OPEN_BRACKET:
                    op.push('(');
                    break;

                case NUMBER_PART:
                    num.push(Double.parseDouble(curr_lexema));
                    break;

                case CLOSE_BRACKET:
                    while (Detecter.GetCharKind(op.peek()) != Detecter.CharKind.OPEN_BRACKET) {
                        process(op.pop());
                    }
                    op.pop();
                    break;

                case OPERATION:
                    Detecter.OperKind curop = Detecter.GetOperKind(curr_lexema.charAt(0));
                    while (!op.empty() &&
                            Detecter.GetOperKind(op.peek()).prior() >= curop.prior()) {
                        process(op.pop());
                    }
                    op.push(curr_lexema.charAt(0));
                    break;
            }
        }
        while (!op.empty()) {
            process(op.pop());
        }

        return num.peek();
    }
}



class Detecter {
    public enum CharKind {
        IGNORE,
        UNKNOWN,
        OPEN_BRACKET,
        CLOSE_BRACKET,
        NUMBER_PART,
        OPERATION
    }

    public enum OperKind {
        UNKNOWN,
        PLUS,
        MINUS,
        MULTI,
        DIVISION;
        public  int prior() {
            if (this == PLUS || this == MINUS)
                return 1;

            if (this == MULTI || this == DIVISION)
                return 2;

            return -1;
        }
    }

    public CharKind GetCharKind(char c) {
        if (c == ' ')
            return CharKind.IGNORE;

        if (c == '(')
            return CharKind.OPEN_BRACKET;

        if (c == ')')
            return CharKind.CLOSE_BRACKET;

        if (Character.isDigit(c) || c == '.')
            return CharKind.NUMBER_PART;

        if (c == '+' || c == '-' || c == '*' || c == '/')
            return CharKind.OPERATION;

        return CharKind.UNKNOWN;
    }

    public static OperKind GetOperKind(char c) {
        switch (c) {
            case '+':
                return OperKind.PLUS;

            case '-':
                return OperKind.MINUS;

            case '*':
                return OperKind.MULTI;

            case '/':
                return OperKind.DIVISION;

            default:
                return OperKind.UNKNOWN;
        }
    }
}


class Parser {
    private Vector <String> lexem;
    void ParseString(String expression) throws ParsingException {
        int len = expression.length();
        int balance = 0;
        lexem = new Vector <String> ();
        for (int i = 0; i < len; ++i) {
            char curr_char = expression.charAt(i);
            switch (Detecter.GetCharKind(curr_char)) {
                case IGNORE:
                    break;
                case UNKNOWN:
                    throw new ParsingException("Unknown symbol is " + curr_char);

                case NUMBER_PART:
                    StringBuilder next_string = new StringBuilder();
                    while (Detecter.GetCharKind(curr_char) ==
                            Detecter.CharKind.NUMBER_PART) {
                        next_string.append(curr_char);
                        ++i;
                        if (i >= len) {
                            break;
                        }
                        curr_char = expression.charAt(i);
                    }
                    --i;
                    lexem.add(next_string.toString());
                    break;
                case OPEN_BRACKET:
                    ++balance;
                    lexem.add("(");
                    break;
                case CLOSE_BRACKET:
                    --balance;
                    if (balance < 0) {
                        throw new ParsingException("Extra close bracket at position " + i);
                    }
                    lexem.add(")");
                    break;
                default:
                    lexem.add("" + curr_char);
            }
        }

        if (balance != 0) {
            throw new ParsingException("Number of close and open brackets are different");
        }
    }

    void PrintLexems() {
        for (String i: lexem) {
            System.out.println(i);
        }
    }

    public String getAt(int i) {
        return lexem.get(i);
    }

    public int size() {
        return lexem.size();
    }
}


