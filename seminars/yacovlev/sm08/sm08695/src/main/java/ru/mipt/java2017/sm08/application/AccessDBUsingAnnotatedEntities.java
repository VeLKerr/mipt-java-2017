package ru.mipt.java2017.sm08.application;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mipt.java2017.sm08.models.Customer;
import ru.mipt.java2017.sm08.models.Good;
import ru.mipt.java2017.sm08.models.Order;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;

import java.util.List;

public class AccessDBUsingAnnotatedEntities {

    private final Logger logger = LoggerFactory.getLogger("database_client");

    // For Native Hibernate API
    private final static SessionFactory sessionFactory;
    private final Session session;

    // For Java Persistence API
    private final static EntityManagerFactory entityManagerFactory;
    private final EntityManager entityManager;

    static {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Customer.class);
        configuration.addAnnotatedClass(Good.class);
        configuration.addAnnotatedClass(Order.class);
        configuration.configure();
        sessionFactory = configuration.buildSessionFactory();
        entityManagerFactory = Persistence.createEntityManagerFactory("mydatabase");
    }

    public AccessDBUsingAnnotatedEntities() {
        session = sessionFactory.openSession();
        entityManager = entityManagerFactory.createEntityManager();
    }

    public void getCustomerById(long id) {
        Customer customer = null; // ....

        customer = session.get(Customer.class, id);

        logger.info("Customer at {} : {} {}", id, customer.getFirstName(), customer.getLastName());
    }

    public void getCutomerByIdUsingJPA(long id) {
        Customer customer = null; // ....
        customer = entityManager.find(Customer.class, id);
    }

    public void getAllGoodsCheaperThanUsingJPA(int priceInRoubles) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Good> query = builder.createQuery(Good.class);
        Predicate lessThan = builder.lessThan(
                query.from(Good.class).get("price"),
                priceInRoubles
        );
        query.where(lessThan);
        List<Good> goods = entityManager.createQuery(query).getResultList();
        for (Good good : goods) {
            logger.info("Good {} - {} < cheaper than {}",
                    good.getName(), good.getPrice(), priceInRoubles);
        }
    }

    public void getAllCustomers() {
        Query<Customer> query = session.createQuery("from Customer", Customer.class);
        List<Customer> customers = query.getResultList();
        for (Customer customer : customers) {
            logger.info("Cusomer: {} {}", customer.getFirstName(), customer.getLastName());
        }
    }


    public static void main(String args[]) {

        AccessDBUsingAnnotatedEntities program = new AccessDBUsingAnnotatedEntities();
        program.getCustomerById(1);
        program.getAllCustomers();
        program.getAllGoodsCheaperThanUsingJPA(1000);

        shutdown();  // Disclaimer: better not main, but signal handler
    }

    public static void shutdown() {
        sessionFactory.close();
        entityManagerFactory.close();
    }

}
