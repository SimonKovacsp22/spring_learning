package sk.posam.learning_online.controller.dto;

import sk.posam.learning_online.domain.OrderItem;

import java.util.Set;

public class Purchase {
    private String email;
    private Set<OrderItem> orderItems;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }


}

