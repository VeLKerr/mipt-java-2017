package ru.mipt.java2017.seminars.seminar11;

import ru.mipt.java2017.seminars.seminar11.model.CustomerOrder;
import ru.mipt.java2017.seminars.seminar11.model.OrderItem;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.*;
import java.util.List;

public class DatabaseReports {

    public static void main(String[] args) {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("mydatabase");
        try {

            EntityManager entityManager = entityManagerFactory.createEntityManager();
            try {
//                List result = entityManager.createQuery(
//                      "select o.customer.lastName, o.customer.address," +
//                            " sum(oi.good.price * oi.quantity) " +
//                      "from CustomerOrder o join o.items oi " +
//                      "group by o.customer, o.customer.lastName, o.customer.address " +
//                      "order by o.customer.lastName").getResultList();

                CriteriaBuilder cb = entityManager.getCriteriaBuilder();
                CriteriaQuery query = cb.createQuery();
                Root<CustomerOrder> o = query.from(CustomerOrder.class);
                Join<CustomerOrder, OrderItem> oi = o.join("items", JoinType.LEFT);
                query.groupBy(o.get("customer"), o.get("customer").get("lastName"),
                        o.get("customer").get("address"));
                query.orderBy(cb.asc(o.get("customer").get("lastName")));

                query.multiselect(o.get("customer").get("lastName"),
                        o.get("customer").get("address"),
                        cb.sum(cb.prod(oi.get("good").get("price"),
                                oi.get("quantity"))));


                for (Object obj : entityManager.createQuery(query).getResultList())
                {
                    Object[] row = (Object[]) obj;
                    System.out.println(String.format("Заказ от %s по адресу %s на сумму %s",
                        row[0],
                        row[1],
                        row[2]));
                }

            } finally {
                entityManager.close();
            }
        } finally {
            entityManagerFactory.close();
        }
    }
}
