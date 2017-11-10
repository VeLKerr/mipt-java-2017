package ru.mipt.java2017.sm08.application;

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
import java.util.LinkedList;
import java.util.List;

public class ExcelDataProvider {
    private static Logger logger = LoggerFactory.getLogger("excel");

    private List<Good> goods;

    public List<Good> getGoods() {
        return goods;
    }

    public static ExcelDataProvider createProvider(String fileName) {
        try {
            return new ExcelDataProvider(fileName);
        }
        catch (FileNotFoundException e) {
            logger.error("FIle not found: {}", fileName);
            return null;
        } catch (IOException e) {
            logger.error("Read error: {}", e.getMessage());
            return null;
        }
    }

    private ExcelDataProvider(String fileName) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(fileName));
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0);
        int maxRow = sheet.getLastRowNum();
        goods = new LinkedList<>();
        for (int i=1; i<=maxRow; ++i) {
            Good good = new Good();
            Row row = sheet.getRow(i);
            good.setPrice(BigDecimal.valueOf(
                     row.getCell(2).getNumericCellValue()
            ));
            good.setName(row.getCell(0).getStringCellValue());
            String descr = row.getCell(1)==null
                    ? "" : row.getCell(1).getStringCellValue();
            if (!descr.isEmpty()) { // might be nullable id DB schema
                good.setDescription(descr);
            }
            goods.add(good);
        }
    }
}
