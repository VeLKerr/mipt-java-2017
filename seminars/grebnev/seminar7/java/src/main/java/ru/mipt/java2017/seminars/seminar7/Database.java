package ru.mipt.java2017.seminars.seminar7;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.sql.*;

public class Database {
    private final static Logger LOGGER = LoggerFactory.getLogger(Database.class);

    public static void main(String[] args) {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        String databaseUrl = "jdbc:sqlite:" + args[0];
        dataSource.setUrl(databaseUrl);

        try (Connection connection = dataSource.getConnection()) {
            LOGGER.info("Connection to {} created", databaseUrl);

            try (Statement statement = connection.createStatement()) {

//                int max = 0;
//                try (ResultSet resultSet = statement.executeQuery(
//                        "SELECT MAX(test_column) max_value FROM test_table")) {
//
//                    if (resultSet.next()) {
//                        max = resultSet.getInt("max_value");
//                    }
//                }
//
//                for (int i = 0; i < 10; i++) {
//                    statement.execute("INSERT INTO test_table (test_column) " +
//                            "VALUES (" + (max + 1 + i) + ")");
//                }

//                for (int i = 1; i < args.length; i++) {
//                    String query = "INSERT INTO test_table (test_column) " +
//                            "VALUES ('" + args[i] + "');";
//
//                    LOGGER.debug("Execute query: {}", query);
//                    statement.executeUpdate(query);
//                }

                String query = "INSERT INTO test_table (test_column) " +
                        "VALUES (?);";
                try (PreparedStatement prepared = connection.prepareStatement(query)) {
                    for (int i = 1; i < args.length; i++) {
                        prepared.setString(1, args[i]);

                        LOGGER.debug("Execute query: {}", query);
                        prepared.executeUpdate();
                    }
                }

                try (ResultSet resultSet = statement.executeQuery(
                        "SELECT test_column FROM test_table")) {
                    int rowCount = 0;
                    while (resultSet.next()) {
                        // int columnValue = resultSet.getInt(1);
                        String columnValue = resultSet.getString(1);
                        System.out.println("Значение в таблице: " + columnValue);

                        rowCount++;
                    }

                    System.out.println("Количество строк в ResultSet: " + rowCount);
                }
            }
        } catch (SQLException e) {
            //LOGGER.error("Database connection error {}", e);

            throw new RuntimeException(e);
            // System.exit(1);
        }
    }
}
