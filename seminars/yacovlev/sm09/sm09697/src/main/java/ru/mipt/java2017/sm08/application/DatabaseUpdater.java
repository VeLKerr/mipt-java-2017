package ru.mipt.java2017.sm08.application;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mipt.java2017.sm08.models.Good;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.List;

public class DatabaseUpdater {

    private final Logger logger = LoggerFactory.getLogger("updater");
    private final Session session;
    private final EntityManager entityManager;
    private final CriteriaBuilder builder;

    DatabaseUpdater(DatabaseAccess access) {
        session = access.session;
        entityManager = access.entityManager;
        builder = entityManager.getCriteriaBuilder();
    }

    private void addGood(Good good) {
        entityManager.persist(good);
    }

    private Good saveModifiedValue(Good good) {
        return entityManager.merge(good);
    }

    private void deleteEntry(Good good) {
        entityManager.remove(good);
    }

    public void addGoods(List<Good> goods) {
        entityManager.getTransaction().begin();
        goods.forEach(this::addGood);
        boolean success = true; // might be 'false'...
        if (success) {
            entityManager.getTransaction().commit();
        }
        else {
            entityManager.getTransaction().rollback();
        }
    }

    public void inflatePrice() {
        CriteriaQuery<Good> query = builder.createQuery(Good.class);
        Root<Good> table = query.from(Good.class);
        Predicate lessThan10K = builder.lessThan(
                table.get("price"),
                10000
        ); // SQL: goods.price < 10000
        query.where(lessThan10K);

        entityManager.getTransaction().begin();
        List<Good> goods = entityManager.createQuery(query).getResultList();

        goods.forEach((entry) -> {
            entry.setPrice(entry.getPrice().multiply(BigDecimal.valueOf(1.1)));
        });
        entityManager.getTransaction().commit();
    }

    public void removeAllWithoutDescription() {
        CriteriaQuery<Good> query = builder.createQuery(Good.class);
        Root<Good> table = query.from(Good.class);
        Predicate emptyDescription = builder.isNull(table.get("description"));
        query.where(emptyDescription);

        entityManager.getTransaction().begin();
        List<Good> goods = entityManager.createQuery(query).getResultList();

        goods.forEach((entry) -> {
            deleteEntry(entry);
        });
        entityManager.getTransaction().commit();

    }



    public static void main(String args[]) {
        DatabaseAccess access = new DatabaseAccess();
        DatabaseUpdater updater = new DatabaseUpdater(access);
        ExcelDataSource excelDataSource = ExcelDataSource.createExcelDataSource(args[0]);
        if (excelDataSource!=null) {
//            updater.addGoods(excelDataSource.getGoods());
        }
        updater.inflatePrice();
        updater.removeAllWithoutDescription();
        System.exit(0);
    }

}
