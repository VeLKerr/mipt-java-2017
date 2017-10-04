/**
 * Created by ilya on 02.10.17.
 */
package ru.mipt.java2017.homework.g695.gridasov.task1;

public class ParsingException extends RuntimeException {
    private String message;
    ParsingException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
