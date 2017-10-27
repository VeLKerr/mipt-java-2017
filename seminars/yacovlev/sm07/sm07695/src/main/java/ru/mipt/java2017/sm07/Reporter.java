package ru.mipt.java2017.sm07;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

public class Reporter {
    final private Logger logger = LoggerFactory.getLogger(Reporter.class);
    final private Connection connection;

    final private static String QUERY_ALL_CUSTOMERS =
            "select first_name, last_name from customers;";
    final private static String QUERY_GOODS_CHEAPER_THEN =
            "select name, description, price from goods WHERE price<?;";
    final private static String QUERY_ORDERS = "select\n" +
            "  first_name,\n" +
            "  last_name,\n" +
            "  address,\n" +
            "  sum(orders.quantity * goods.price) as total\n" +
            "from customers, orders\n" +
            "  JOIN goods ON goods.id=orders.goods_id\n" +
            "WHERE orders.customers_id=customers.id\n" +
            "GROUP BY orders.customers_id;";


    public Reporter(String databaseUrl) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(databaseUrl);
        } catch (SQLException e) {
            logger.error("Can't connect to DB: {}", e.getMessage());
        }
        this.connection = connection;
    }

    public void getAllCustomers() {
        if (connection==null) {
            logger.error("Not connected to DB");
            return;
        }
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(QUERY_ALL_CUSTOMERS);
            while (result.next()) {
                String firstName = result.getString("first_name");
                String lastName = result.getString("last_name");
                logger.info("Customer: {} {}", firstName, lastName);
            }

        } catch (SQLException e) {
            logger.error("SQL ERROR: {}", e.getMessage());
        }
    }

    public void getAllGoodsCheaperThan(int priceInRoubles) {
        if (connection==null) {
            logger.error("Not connected to DB");
            return;
        }
        try {
            PreparedStatement statement = connection.prepareStatement(QUERY_GOODS_CHEAPER_THEN);
            statement.setInt(1, priceInRoubles);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                String firstName = result.getString("first_name");
                String lastName = result.getString("last_name");
                String address = result.getString("address");
                double total = result.getDouble("total");
                logger.info("Order: {} {} - {} - Total: {}", firstName, lastName, address, total);
            }

        } catch (SQLException e) {
            logger.error("SQL ERROR: {}", e.getMessage());
        }
    }

    public void getAllOrders(XSSFSheet sheet) {
        if (connection==null) {
            logger.error("Not connected to DB");
            return;
        }
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(QUERY_ORDERS);
            int rowNumber = 1;
            while (result.next()) {
                String firstName = result.getString("first_name");
                String lastName = result.getString("last_name");
                String address = result.getString("address");
                double total = result.getDouble("total");
//                logger.info("Order: {} {} - {} - {}", firstName, lastName, address, total);
                Row row = sheet.createRow(rowNumber++);
                Cell name = row.createCell(0);
                name.setCellValue(firstName+" "+lastName);
                Cell totalCell = row.createCell(1);
                totalCell.setCellValue(total);
            }

        } catch (SQLException e) {
            logger.error("SQL ERROR: {}", e.getMessage());
        }
    }

    public static void main(String args[]) throws IOException {
        String url = args[0];
        Reporter reporter = new Reporter(url);
//        reporter.getAllCustomers();
//        reporter.getAllGoodsCheaperThan(1000);

        XSSFWorkbook book = new XSSFWorkbook();
        XSSFSheet sheet = book.createSheet("Orders");

        Row heading = sheet.createRow(0);
        Cell h1 = heading.createCell(0);
        Cell h2 = heading.createCell(1);
        h1.setCellValue("Name");
        h2.setCellValue("Total");
        CellStyle style = book.createCellStyle();
        Font font = book.createFont();
        font.setBold(true);
        style.setFont(font);
        heading.setRowStyle(style);
        sheet.setColumnWidth(0, 10000);

        reporter.getAllOrders(sheet);

        File outFile = new File(args[1]);
        FileOutputStream fileOutputStream = new FileOutputStream(outFile);
        book.write(fileOutputStream);
    }
}
