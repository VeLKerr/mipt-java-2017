package ru.mipt.java2017;

public class ClassA {
    public static int a = 123;
    public static String s = "sdfsdf";
    public int b = 34234;
    static {

    }
}

class B {
    void doSmthing() {
        new ClassA().b = 124;
        ClassA.a = 789;
    }
}
