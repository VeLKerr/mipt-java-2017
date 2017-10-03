package ru.mipt.java2017.homework.g696.nechepurenco.task1;

public class Token {
    //в программе будет всего 2 таких токена, так что неэкономное использование памяти не так страшно
    int type = -1;
    char operand = '!';
    double number = 0;
    public Token(char _operand){
        operand = _operand;
        type = 2;
    }
    public Token(double value){
        number = value;
        type = 1;
    }
    boolean equal(char _operand){
        return (type == 2 && operand == _operand);
    }
    boolean equal(double value){
        return (type == 1 && value == number);
    }
}
