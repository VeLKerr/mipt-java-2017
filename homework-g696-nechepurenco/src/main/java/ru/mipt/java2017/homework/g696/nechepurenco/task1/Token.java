package ru.mipt.java2017.homework.g696.nechepurenco.task1;

class Token {
    //в программе будет всего 2 таких токена, так что неэкономное использование памяти не так страшно
    int type = -1;
    char operand = '!';
    double number = 0;
    Token(char _operand){
        operand = _operand;
        type = 2;
    }
    Token(double value){
        number = value;
        type = 1;
    }
    boolean equal(char _operand){
        return (type == 2 && operand == _operand);
    }
}
