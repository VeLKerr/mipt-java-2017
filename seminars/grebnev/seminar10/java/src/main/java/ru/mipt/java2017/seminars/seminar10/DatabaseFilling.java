package ru.mipt.java2017.seminars.seminar10;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.mipt.java2017.seminars.seminar10.model.Customer;
import ru.mipt.java2017.seminars.seminar10.model.CustomerOrder;
import ru.mipt.java2017.seminars.seminar10.model.Good;
import ru.mipt.java2017.seminars.seminar10.model.OrderItem;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.List;
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

                // SELECT o.id FROM orders o
                session.createQuery("from CustomerOrder", CustomerOrder.class)
                    .list().forEach(session::delete);
                session.createQuery("from Customer", Customer.class)
                    .list().forEach(session::delete);
                session.createQuery("from Good", Good.class)
                    .list().forEach(session::delete);

                session.getTransaction().commit();
            }

            try (Session session = sessionFactory.openSession()) {

                session.getTransaction().begin();

//                session.delete(session.get(CustomerOrder.class, 4));
//                session.delete(session.get(Customer.class, 3));
//                session.delete(session.get(Good.class, 4));

                // Покупателей
                Customer ivanov = new Customer("Иван", "Иванов", "ул. Ленина, д. 1");
                Customer petrov = new Customer("Петр", "Петров", "пр. Мира, д. 10");
                Customer sidorov = new Customer("Сидр", "Сидоров", "Ленинградский проспект, д. 67");
                session.save(ivanov);
                session.save(petrov);
                session.save(sidorov);

                // Товары
                Good table = new Good("Стол", new BigDecimal("100.05"));
                Good chair = new Good("Стул", new BigDecimal("25.37"));
                Good dresser = new Good("Комод", new BigDecimal("25.37"));
                Good cellPhone = new Good("Мобильный телефон", new BigDecimal("2953.65"));
                session.save(table);
                session.save(chair);
                session.save(dresser);
                session.save(cellPhone);

                // Заказы
                session.save(new CustomerOrder(ivanov)
                    .addGood(chair, 5));
                session.save(new CustomerOrder(ivanov)
                    .addGood(table, 1)
                    .addGood(chair, 2));
                session.save(new CustomerOrder(petrov)
                    .addGood(table, 1)
                    .addGood(chair, 4)
                    .addGood(dresser, 1));
                session.save(new CustomerOrder(sidorov)
                    .addGood(cellPhone, 2)
                    .addGood(table, 1));

//                CustomerOrder order = new CustomerOrder(sidorov);
//                order.addGood(cellPhone, 2);
//                order.addGood(table, 1);
//                Set<OrderItem> orderItems = new HashSet<>();
//                orderItems.add(new OrderItem(order, cellPhone, 2));
//                orderItems.add(new OrderItem(order, table, 1));
//                order.setItems(orderItems);

//                session.save(sidorov);
//                session.save(cellPhone);
//                session.save(order);
//                orderItems.forEach(session::save);

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
