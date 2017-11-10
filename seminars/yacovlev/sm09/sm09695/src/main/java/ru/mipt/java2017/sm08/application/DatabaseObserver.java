package ru.mipt.java2017.sm08.application;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mipt.java2017.sm08.models.Customer;
import ru.mipt.java2017.sm08.models.Good;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;

import java.util.List;

public class DatabaseObserver {

    private final Logger logger = LoggerFactory.getLogger("observer");
    private final Session session;
    private final EntityManager entityManager;


    public DatabaseObserver(DatabaseAccess access) {
        session = access.session;
        entityManager = access.entityManager;
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

        DatabaseObserver program = new DatabaseObserver(new DatabaseAccess());
        program.getCustomerById(1);
        program.getAllCustomers();
        program.getAllGoodsCheaperThanUsingJPA(1000);


        System.exit(0);
    }


}
