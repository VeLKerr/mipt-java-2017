package ru.mipt.java2017.seminars.seminar9.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "orders")
public class CustomerOrder {

    public CustomerOrder()
    {
    }

    @Id
    private int id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order")
    private Set<OrderItem> items;

    public CustomerOrder(Customer customer) {
        this.customer = customer;
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

    public void setItems(Set<OrderItem> items) {
        this.items = items;
    }
}
