package ru.mipt.java2017.seminars.seminar10;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.mipt.java2017.seminars.seminar10.model.CustomerOrder;

import java.util.List;

public class DatabaseReports {

    public static void main(String[] args) {

        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure() // configures settings from hibernate.cfg.xml
            .build();

        try (SessionFactory sessionFactory = new MetadataSources(registry)
            .buildMetadata()
            .buildSessionFactory()) {

            try (Session session = sessionFactory.openSession()) {

                for (CustomerOrder order : session.createQuery("from CustomerOrder o order by o.customer.lastName", CustomerOrder.class).list()) {
                    System.out.println(String.format("Заказ от %s по адресу %s на сумму %s",
                        order.getCustomer().getLastName(),
                        order.getCustomer().getAddress(),
                        order.getAmount()));
                }

                // SELECT c.last_name, c.address, SUM(g.price * oi.quantity)
                // FROM orders o LEFT JOIN order_items oi ON o.id = o.order_id
                //               INNER JOIN goods g ON g.id = oi.good_id
                //               INNER JOIN customers c ON c.id = o.customer_id
                // GROUP BY c.id, c.last_name, c.address
                // ORDER BY c.last_name

                System.out.println();

                List result = session.createQuery(
                      "select o.customer.lastName, o.customer.address," +
                            " sum(oi.good.price * oi.quantity) " +
                      "from CustomerOrder o join o.items oi " +
                      "group by o, o.customer.lastName, o.customer.address " +
                      "order by o.customer.lastName").list();
                for (Object o : result)
                {
                    Object[] row = (Object[]) o;
                    System.out.println(String.format("Заказ от %s по адресу %s на сумму %s",
                        row[0],
                        row[1],
                        row[2]));
                }
            }
        }
    }
}
