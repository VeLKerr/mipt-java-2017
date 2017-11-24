package ru.mipt.java2017.seminars.seminar11;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.mipt.java2017.seminars.seminar11.model.Good;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class GoodManagement {

    private static final String GOODS_TABLE_FILE_NAME = "goods.xlsx";

    public static void main(String[] args) throws IOException {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("mydatabase");
        try {

            EntityManager entityManager = entityManagerFactory.createEntityManager();
            try {
                printPriceList(entityManager);

//                doublePrice(entityManager);
                updateGoods(entityManager);

                System.out.println();
                printPriceList(entityManager);

//                unloadGoods(entityManager);
            } finally {
                entityManager.close();
            }
        } finally {
            entityManagerFactory.close();
        }
    }

    private static void printPriceList(EntityManager entityManager) {
        List result = entityManager.createQuery("select name, price from Good order by id").getResultList();

        System.out.format("%35s%15s\n", "Наименование", "Цена");
        System.out.println("--------------------------------------------------");
        for (Object obj : result) {

            Object[] row = (Object[]) obj;
            System.out.format("%35s%15s\n", row[0], row[1]);
        }
    }

    private static void doublePrice(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        List<Good> goods = entityManager.createQuery("from Good", Good.class).getResultList();
        for (Good good : goods) {
            good.setPrice(good.getPrice().multiply(BigDecimal.valueOf(2)));
        }

        entityManager.getTransaction().commit();
    }

    private static void unloadGoods(EntityManager entityManager) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet();

            List<Good> goods = entityManager.createQuery("from Good", Good.class).getResultList();
            int i = 0;
            for (Good good : goods) {
                Row row = sheet.createRow(i);
                i++;

                row.createCell(0).setCellValue(good.getId());
                row.createCell(1).setCellValue(good.getName());
                row.createCell(2).setCellValue(good.getPrice().toString());
            }

            try (FileOutputStream fos = new FileOutputStream(GOODS_TABLE_FILE_NAME)) {
                workbook.write(fos);
            }
        }
    }

    private static void updateGoods(EntityManager entityManager) throws IOException {
        entityManager.getTransaction().begin();

        try (FileInputStream fis = new FileInputStream(GOODS_TABLE_FILE_NAME)) {
            try (Workbook workbook = new XSSFWorkbook(fis)) {
                Sheet sheet = workbook.getSheetAt(0);

                for (Row row : sheet) {
                    int id = new Double(row.getCell(0).getNumericCellValue()).intValue();

                    Good good = new Good();

                    if (id != 0) {
                        good.setId(id);
                    }
                    good.setName(row.getCell(1).getStringCellValue());
                    good.setPrice(BigDecimal.valueOf(row.getCell(2).getNumericCellValue()));

                    entityManager.merge(good);
                }
            }
        }

        entityManager.getTransaction().commit();
    }
}
