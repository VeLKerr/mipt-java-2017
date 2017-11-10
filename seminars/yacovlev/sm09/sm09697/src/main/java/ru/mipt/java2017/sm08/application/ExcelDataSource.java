package ru.mipt.java2017.sm08.application;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mipt.java2017.sm08.models.Good;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ExcelDataSource {
    private final static Logger logger = LoggerFactory.getLogger("excel");
    private final Workbook workbook;

    public List<Good> getGoods() {
        Sheet sheet = workbook.getSheetAt(0);
        int maxRowNumber = sheet.getLastRowNum();
        List<Good> goods = new ArrayList<>(maxRowNumber);
        for (int i=1; i<=maxRowNumber; ++i) {
            Row row = sheet.getRow(i);
            Cell nameCell = row.getCell(0);
            Cell descrCell = row.getCell(1);
            Cell priceCell = row.getCell(2);
            Good good = new Good();
            good.setName(nameCell.getStringCellValue());
            good.setPrice(BigDecimal.valueOf(priceCell.getNumericCellValue()));
            if (descrCell!=null) {
                good.setDescription(descrCell.getStringCellValue());
            }
            goods.add(good);
        }
        return goods;
    }

    public static ExcelDataSource createExcelDataSource(String fileName) {
        try {
            return new ExcelDataSource(fileName);
        } catch (FileNotFoundException e) {
            logger.error("File not found: {}", fileName);
            return null;
        } catch (IOException e) {
            logger.error("IOError: {}", e.getMessage());
            return null;
        }
    }

    private ExcelDataSource(String fileName) throws FileNotFoundException, IOException {
        FileInputStream fileInputStream = new FileInputStream(
                new File(fileName)
        );
        workbook = new XSSFWorkbook(fileInputStream);
    }

}
