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


public class DatabaseObserver {

    private final Logger logger = LoggerFactory.getLogger("observer");

    // Native API: Sessions, Queries, ....
    private final Session session;

    // Java Persistence API: EntityManager, Entities ...
    private final EntityManager entityManager;



    DatabaseObserver(DatabaseAccess access) {
        session = access.session;
        entityManager = access.entityManager;
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
        DatabaseObserver p = new DatabaseObserver(new DatabaseAccess());

        p.getCustomerById(1);
        p.getAllCustomers();
        p.queryAllGoodsCheaperThanUsingJPA(1000);

        System.exit(0);
    }

}
