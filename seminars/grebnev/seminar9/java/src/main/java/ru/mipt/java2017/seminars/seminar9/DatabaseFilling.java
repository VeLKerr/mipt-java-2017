package ru.mipt.java2017.seminars.seminar9;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.mipt.java2017.seminars.seminar9.model.Customer;
import ru.mipt.java2017.seminars.seminar9.model.CustomerOrder;
import ru.mipt.java2017.seminars.seminar9.model.Good;
import ru.mipt.java2017.seminars.seminar9.model.OrderItem;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class DatabaseFilling {

    public static void main(String[] args) {

        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();

        try (SessionFactory sessionFactory = new MetadataSources(registry)
                .buildMetadata()
                .buildSessionFactory()) {

            try (Session session = sessionFactory.openSession()) {

                session.getTransaction().begin();

                session.delete(session.get(Customer.class, 0));
                session.delete(session.get(Good.class, 0));
                session.delete(session.get(CustomerOrder.class, 0));

                Customer sidorov = new Customer("Сидр", "Сидоров", "Ленинградский проспект");
                Good cellPhone = new Good("Мобильный телефон", new BigDecimal("2953.65"));
                Good table = session.get(Good.class, 1);

                CustomerOrder order = new CustomerOrder(sidorov);
                Set<OrderItem> orderItems = new HashSet<>();
                orderItems.add(new OrderItem(order, cellPhone, 2));
                orderItems.add(new OrderItem(order, table, 1));
                order.setItems(orderItems);

                session.save(sidorov);
                session.save(cellPhone);
                session.save(order);
                orderItems.forEach(session::save);

                session.getTransaction().commit();
            }
        }
    }

    private static void saveCustomer(Connection connection, Customer customer) throws Exception{
//        try (Statement statement = connection.createStatement()) {
//            statement.execute("INSERT INTO customers (first_name, last_name, address) " +
//                    "VALUES (" +
//                    customer.getFirstName() + ", " +
//                    customer.getLastName() + ", " +
//                    customer.getAddress() + ")");
//        }

        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO customers (first_name, last_name, address) " +
                        "VALUES (?, ? ?)")) {
            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getAddress());
        }
    }
}
