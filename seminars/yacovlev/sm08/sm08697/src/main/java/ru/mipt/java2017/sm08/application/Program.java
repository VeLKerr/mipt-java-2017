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
import javax.persistence.criteria.Root;
import java.util.List;


public class Program {

    private final Logger logger = LoggerFactory.getLogger("database_client");

    // Native API: Sessions, Queries, ....
    private final static SessionFactory sessionFactory; // connection to DB
    private final Session session;

    // Java Persistence API: EntityManager, Entities ...
    private final static EntityManagerFactory entityManagerFactory;
    private final EntityManager entityManager;

    static {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Customer.class);
        configuration.addAnnotatedClass(Good.class);
        configuration.addAnnotatedClass(Order.class);

        configuration.configure(); // read from XML file
        sessionFactory = configuration.buildSessionFactory();

        entityManagerFactory = Persistence.createEntityManagerFactory("mydatabase");
    }

    Program() {
        session = sessionFactory.openSession();
        entityManager = entityManagerFactory.createEntityManager();
    }

    static void shutdown() {
        // Runtime signal handlers
        sessionFactory.close();
        entityManagerFactory.close();
    }

    public void getCustomerById(long id) {
        Customer customer = session.get(Customer.class, id);
        logger.info("Customer at {} : {}", id, customer.getName());
    }

    public void getAllCustomers() {
        Query<Customer> query = session.createQuery("from Customer", Customer.class);
        List<Customer> customerList = query.getResultList();
        for (Customer customer : customerList) {
            logger.info("Customer {} {}", customer.getName(), customer.getAddress());
        }
    }

    public void getCustomerByIdUsingJPA(long id) {
        Customer customer = entityManager.find(Customer.class, id);
        logger.info("Customer at {} : {}", id, customer.getName());
    }

    public void queryAllGoodsCheaperThanUsingJPA(long priceInRoubles) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Good> query = builder.createQuery(Good.class);
        /** select from goods where price < $priceInRoubles */
        Root<Good> root = query.from(Good.class); // from ...
        Predicate predicate = builder.lessThan(
                root.get("price"),  // price field on table goods
                priceInRoubles  // Long, but it casted by Java 8
        );
        query.where(predicate); // where ...
        List<Good> goods = entityManager.createQuery(query).getResultList();
        goods.forEach(good -> {
            logger.info("Good cheaper than {} : {} - {} RUR",
                    priceInRoubles, good.getName(), good.getPrice());
        });
    }

    public static void main(String args[]) {
        Program p = new Program();

        p.getCustomerById(1);
        p.getAllCustomers();
        p.queryAllGoodsCheaperThanUsingJPA(1000);

        // BAD style -- better use shutdown hooks
        shutdown();
    }

}
