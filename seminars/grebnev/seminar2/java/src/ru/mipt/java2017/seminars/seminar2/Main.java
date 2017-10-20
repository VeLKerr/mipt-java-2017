package ru.mipt.java2017.seminars.seminar2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws Exception {
        String fileName = "test.txt";

        try (FileInputStream fis = new FileInputStream(fileName);
             FileInputStream fis2 = new FileInputStream("")) {
            fis.read();
            System.out.println("1");
        }
    }
}
