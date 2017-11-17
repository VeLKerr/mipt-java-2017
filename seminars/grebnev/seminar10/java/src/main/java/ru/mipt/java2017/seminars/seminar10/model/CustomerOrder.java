package ru.mipt.java2017.seminars.seminar10.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.criterion.Order;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

@Entity
@Table(name = "orders")
public class CustomerOrder {

    public CustomerOrder()
    {
    }

    @Id
    @GeneratedValue(generator = "order_gen")
    @GenericGenerator(name = "order_gen", strategy = "increment")
    private int id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = ALL)
    private Set<OrderItem> items;

    public CustomerOrder(Customer customer) {
        this.customer = customer;
        this.items = new HashSet<>();
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Set<OrderItem> getItems() {
        return items;
    }

    public CustomerOrder addGood(Good good, int quantity) {
        items.add(new OrderItem(this, good, quantity));
        return this;
    }

    public BigDecimal getAmount() {
       BigDecimal result = BigDecimal.ZERO;
       for (OrderItem item : items) {
           result = result.add(
               item.getGood().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
       }
       return result;
    }
}
