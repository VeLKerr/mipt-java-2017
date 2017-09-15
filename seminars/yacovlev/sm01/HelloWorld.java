import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class HelloWorld {
    public static void main(String[] args) {
        MyClazz object1 = new MyClazz();
        MyClazz object2 = new MyClazz("Cat");
        MyClazz object3 = new MyClazz("Dog");

        PrintStream out;
        try {
            FileOutputStream fos = new FileOutputStream("out.txt");
            out = new PrintStream(fos);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            out = System.out;
        }


        out.println(object1);
        out.println(object2);
        out.println(object3);

        int a, b ,c;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("input.txt");
        } catch (FileNotFoundException e) {
            System.err.println("Файл не найден");
        }

        Scanner scanner = new Scanner(fis);
        a = scanner.nextInt();
        b = scanner.nextInt();
        c = scanner.nextInt();
        System.out.println(a+b+c);

    }
}

class MyClazz {
    private String myData = "unknown";
    private String myAnotherData = "abrakadabra";

    public MyClazz(String myData) {
        this.myData = myData;
    }

    public MyClazz() {

    }

    @Override
    public String toString() {
        while (true) {
            if (false) break;
        }
        return "Object of " + myData;
    }
}