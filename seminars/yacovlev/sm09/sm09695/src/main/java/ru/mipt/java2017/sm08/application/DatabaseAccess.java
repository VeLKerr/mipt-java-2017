package ru.mipt.java2017.sm08.application;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mipt.java2017.sm08.models.Customer;
import ru.mipt.java2017.sm08.models.Good;
import ru.mipt.java2017.sm08.models.Order;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DatabaseAccess {

    private final Logger logger = LoggerFactory.getLogger("database");

    // For Native Hibernate API
    private final static SessionFactory sessionFactory;
    final Session session;

    // For Java Persistence API
    private final static EntityManagerFactory entityManagerFactory;
    final EntityManager entityManager;

    DatabaseAccess() {
        session = sessionFactory.openSession();
        entityManager = entityManagerFactory.createEntityManager();
    }

    static {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Customer.class);
        configuration.addAnnotatedClass(Good.class);
        configuration.addAnnotatedClass(Order.class);
        configuration.configure();
        sessionFactory = configuration.buildSessionFactory();
        entityManagerFactory = Persistence.createEntityManagerFactory("mydatabase");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            sessionFactory.close();
            entityManagerFactory.close();
        }));
    }

}
