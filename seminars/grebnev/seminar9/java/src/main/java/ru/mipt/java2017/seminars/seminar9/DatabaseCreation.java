package ru.mipt.java2017.seminars.seminar9;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.io.Resources.getResource;

public class DatabaseCreation {
    public static void main(String[] args) throws Exception {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:postgresql:postgres", "postgres", "123456")) {
            try (Statement statement = connection.createStatement()) {

                statement.execute("DROP DATABASE IF EXISTS db_seminar9;");
                statement.execute("CREATE DATABASE db_seminar9;");
            }
        }

        try (Connection connection = DriverManager.getConnection(
                "jdbc:postgresql:db_seminar9", "postgres", "123456")){
            try (Statement statement = connection.createStatement()) {

                statement.execute(Resources.toString(
                        getResource("create_schema.sql"), UTF_8));

                statement.execute(Resources.toString(
                        getResource("fill_database.sql"), UTF_8));
            }
        }
    }
}
