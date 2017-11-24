package ru.mipt.java2017.seminars.seminar11;

import ru.mipt.java2017.seminars.seminar11.model.Customer;
import ru.mipt.java2017.seminars.seminar11.model.CustomerOrder;
import ru.mipt.java2017.seminars.seminar11.model.Good;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;

public class DatabaseFilling {

    public static void main(String[] args) {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("mydatabase");
        try {

            EntityManager entityManager = entityManagerFactory.createEntityManager();
            try {
                entityManager.getTransaction().begin();
                
                entityManager.createQuery("from CustomerOrder", CustomerOrder.class)
                    .getResultList().forEach(entityManager::remove);
                entityManager.createQuery("from Customer", Customer.class)
                    .getResultList().forEach(entityManager::remove);
                entityManager.createQuery("from Good", Good.class)
                    .getResultList().forEach(entityManager::remove);

                entityManager.getTransaction().commit();

                entityManager.getTransaction().begin();

                // Покупателей
                Customer ivanov = new Customer("Иван", "Иванов", "ул. Ленина, д. 1");
                Customer petrov = new Customer("Петр", "Петров", "пр. Мира, д. 10");
                Customer sidorov = new Customer("Сидр", "Сидоров", "Ленинградский проспект, д. 67");
                entityManager.persist(ivanov);
                entityManager.persist(petrov);
                entityManager.persist(sidorov);

                // Товары
                Good table = new Good("Стол", new BigDecimal("100.05"));
                Good chair = new Good("Стул", new BigDecimal("25.37"));
                Good dresser = new Good("Комод", new BigDecimal("25.37"));
                Good cellPhone = new Good("Мобильный телефон", new BigDecimal("2953.65"));
                entityManager.persist(table);
                entityManager.persist(chair);
                entityManager.persist(dresser);
                entityManager.persist(cellPhone);

                // Заказы
                entityManager.persist(new CustomerOrder(ivanov)
                    .addGood(chair, 5));
                entityManager.persist(new CustomerOrder(ivanov)
                    .addGood(table, 1)
                    .addGood(chair, 2));
                entityManager.persist(new CustomerOrder(petrov)
                    .addGood(table, 1)
                    .addGood(chair, 4)
                    .addGood(dresser, 1));
                entityManager.persist(new CustomerOrder(sidorov)
                    .addGood(cellPhone, 2)
                    .addGood(table, 1));

                entityManager.getTransaction().commit();
            } finally {
                entityManager.close();
            }
        } finally {
            entityManagerFactory.close();
        }
    }
}
