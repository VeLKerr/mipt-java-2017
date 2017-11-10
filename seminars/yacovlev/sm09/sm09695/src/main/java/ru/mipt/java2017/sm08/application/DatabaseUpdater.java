package ru.mipt.java2017.sm08.application;

import org.hibernate.Criteria;
import ru.mipt.java2017.sm08.models.Good;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.List;

public class DatabaseUpdater {
    private final EntityManager entityManager;
    private final CriteriaBuilder builder;

    public DatabaseUpdater(DatabaseAccess access) {
        entityManager = access.entityManager;
        builder = entityManager.getCriteriaBuilder();
    }

    private void addEntryToGoods(Good good) {
        entityManager.persist(good);
    }

    private void removeEntry(Good good) {
        entityManager.remove(good);
    }

    private Good updateEntry(Good good) {
        return entityManager.merge(good);
    }

    public void addEntriesToGoods(List<Good> goods) {
        entityManager.getTransaction().begin();
        goods.forEach(this::addEntryToGoods);
        boolean success = false; // might be false in some cases...
        if (success) {
            entityManager.getTransaction().commit();
        }
        else {
            entityManager.getTransaction().rollback();
        }
    }

    public void removeGoodsWithNoDescription() {
        CriteriaQuery<Good> query = builder.createQuery(Good.class);
        Root<Good> table = query.from(Good.class);
        Predicate emptyDescr = builder.isNull(table.get("description"));
        query.where(emptyDescr);
        entityManager.getTransaction().begin();
        List<Good> goods = entityManager.createQuery(query).getResultList();
        goods.forEach(this::removeEntry);
        entityManager.getTransaction().commit();
    }

    public void doubleAllPrices() {
        CriteriaQuery<Good> query = builder.createQuery(Good.class);
        query.from(Good.class);
        entityManager.getTransaction().begin();
        List<Good> goods = entityManager.createQuery(query).getResultList();
        goods.forEach((Good entry) -> {
            entry.setPrice(entry.getPrice().multiply(BigDecimal.valueOf(2)));
        });
        entityManager.getTransaction().commit();
    }


    public static void main(String args[]) {
        String fileName = args[0];
        DatabaseUpdater updater = new DatabaseUpdater(new DatabaseAccess());
        ExcelDataProvider provider = ExcelDataProvider.createProvider(fileName);
        if (provider != null) {
            updater.addEntriesToGoods(provider.getGoods());
        }
        updater.removeGoodsWithNoDescription();
        updater.doubleAllPrices();
        System.exit(0);
    }

}
