package ru.mipt.java2017.seminars.seminar7;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logs {
    private final static Logger LOGGER = LoggerFactory.getLogger(Logs.class);

    public static void main(String[] args) {
        LOGGER.error("error level {}", 10);
        LOGGER.info("info level");
        LOGGER.debug("debug level");
        if (LOGGER.isTraceEnabled()) {
            System.out.println("trace level enabled");

            LOGGER.trace("trace level");
        }
    }
}
