/**
 * Created by ilya on 02.10.17.
 */
package ru.mipt.java2017.homework.g695.gridasov.task1;

import ru.mipt.java2017.homework.base.task1.ParsingException;

public interface AbstractParser {
    Operation parse(String expression) throws ParsingException;
}
