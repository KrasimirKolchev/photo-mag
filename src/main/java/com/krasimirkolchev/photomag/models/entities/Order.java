package com.krasimirkolchev.photomag.models.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    private List<OrderItems> orderItems;
    private Double totalAmount;
    private User user;
    private LocalDateTime purchaseDateTime;
    private String chargeId;
    private Address address;

    public Order() {
    }

    @ManyToMany(targetEntity = OrderItems.class)
    public List<OrderItems> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItems> orderItems) {
        this.orderItems = orderItems;
    }

    @Column(name = "total_amounut", nullable = false)
    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @ManyToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "purchase_date_time", nullable = false)
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    public LocalDateTime getPurchaseDateTime() {
        return purchaseDateTime;
    }

    public void setPurchaseDateTime(LocalDateTime purchaseDateTime) {
        this.purchaseDateTime = purchaseDateTime;
    }

    @Column(name = "charge_id", nullable = false)
    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    @ManyToOne
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
