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

    private final Logger logger = LoggerFactory.getLogger(Reporter.class);
    private final Connection connection;

    private final static String QUERY_ALL_CUSTOMERS = "SELECT name, address from customers;";
    private final static String QUERY_GOODS_CHEAPER_THAN =
            "SELECT name, description, price " +
            "from goods WHERE price<?";
    private final static String QUERY_ORDERS = "SELECT\n" +
            "  customers.name, address,\n" +
            "  sum(orders.quantity * goods.price) as total \n" +
            "FROM customers, orders\n" +
            "  JOIN goods ON goods.id=orders.goods\n" +
            "WHERE orders.customers_id=customers.id\n" +
            "GROUP BY orders.customers_id";

    public Reporter(String url) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            logger.error("Cant connect to DB {}: {}", url, e.getMessage());
        }
        this.connection = connection;
    }

    public void queryAllCustomers() {
        if (connection==null) {
            logger.error("Not connected");
            return;
        }
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(QUERY_ALL_CUSTOMERS);
            while (result.next()) {
                String name = result.getString("name");
                String address = result.getString("address");
                logger.info("Customer: {} - {}", name, address);
            }
        } catch (SQLException e) {
            logger.error("SQL Error: {}", e.getMessage());
        }
    }

    public void queryAllGoodsCheaperThan(int priceInRoubles) {
        if (connection==null) {
            logger.error("Not connected");
            return;
        }
        try {
            PreparedStatement statement = connection.prepareStatement(QUERY_GOODS_CHEAPER_THAN);
            statement.setInt(1, priceInRoubles);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                String name = result.getString("name");
                String description = result.getString("description");
                double price = result.getDouble("price");
                logger.info("Good: {} ({}) : {} RUR", name, description, price);
            }
        } catch (SQLException e) {
            logger.error("SQL Error: {}", e.getMessage());
        }
    }

    public static void main(String args[]) throws IOException {
        String url = args[0];
        Reporter reporter = new Reporter(url);
        reporter.queryAllCustomers();
        reporter.queryAllGoodsCheaperThan(1000);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet  = workbook.createSheet("Orders");
        reporter.queryOrders(sheet);

        Row headingRow = sheet.createRow(0);
        Cell headName = headingRow.createCell(0);
        Cell headAddr = headingRow.createCell(1);
        Cell headTotal = headingRow.createCell(2);
        headName.setCellValue("Имя клиента");
        headAddr.setCellValue("Адрес доставки");
        headTotal.setCellValue("Сумма заказа");

        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        headingRow.setRowStyle(style);
        sheet.setColumnWidth(0, 7000);
        sheet.setColumnWidth(1, 7000);
        sheet.setColumnWidth(2, 5000);



        File file = new File(args[1]);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        workbook.write(fileOutputStream);
    }

    public void queryOrders(XSSFSheet sheet) {
        if (connection==null) {
            logger.error("Not connected");
            return;
        }
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(QUERY_ORDERS);
            int rowNumber = 1;
            while (result.next()) {
                String name = result.getString("name");
                String address = result.getString("address");
                double total = result.getDouble("total");
                logger.info("Order: {} - {} - {}", name, address, total);
                Row row = sheet.createRow(rowNumber++);
                Cell nameCell = row.createCell(0);
                Cell addrCell = row.createCell(1);
                Cell totalCell = row.createCell(2);
                nameCell.setCellValue(name);
                addrCell.setCellValue(address);
                totalCell.setCellValue(total);
            }
        } catch (SQLException e) {
            logger.error("SQL Error: {}", e.getMessage());
        }
    }
}
