package ru.mipt.java2017.sm08.application;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.mipt.java2017.sm08.models.Customer;
import ru.mipt.java2017.sm08.models.Good;
import ru.mipt.java2017.sm08.models.Order;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DatabaseAccess {

    // Native API: Sessions, Queries, ....
    private final static SessionFactory sessionFactory; // connection to DB
    final Session session;

    // Java Persistence API: EntityManager, Entities ...
    private final static EntityManagerFactory entityManagerFactory;
    final EntityManager entityManager;

    static {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Customer.class);
        configuration.addAnnotatedClass(Good.class);
        configuration.addAnnotatedClass(Order.class);

        configuration.configure(); // read from XML file
        sessionFactory = configuration.buildSessionFactory();

        entityManagerFactory = Persistence.createEntityManagerFactory("mydatabase");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            sessionFactory.close();
            entityManagerFactory.close();
        }));
    }

    DatabaseAccess() {
        session = sessionFactory.openSession();
        entityManager = entityManagerFactory.createEntityManager();
    }

}
