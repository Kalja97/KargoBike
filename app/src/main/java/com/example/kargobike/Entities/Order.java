package com.example.kargobike.Entities;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "orders", primaryKeys = {"orderNr"})
public class Order implements Comparable {

    /*@PrimaryKey(autoGenerate = true)
    private Long orderid;*/
    @NonNull
    private String orderNr;

    private String sender;
    private String receiver;

    private String product;
    private int howMany;

    private String rider;
    private String datePickup;
    private String dateDelivery;

    private String state;


    public Order(@NonNull String orderNr, int howMany, String product, String sender, String receiver,
                 String rider, String datePickup, String dateDelivery, String state){

        this.orderNr = orderNr;
        this.howMany = howMany;
        this.product = product;
        this.sender = sender;
        this.receiver = receiver;
        this.rider = rider;
        this.datePickup = datePickup;
        this.dateDelivery = dateDelivery;
        this.state = state;
    }

    @Ignore
    public Order() {

    }

    /* public Long getOrderid() {
         return orderid;
     }

     public void setOrderid(Long orderid) {
         this.orderid = orderid;
     }
 */
    public String getOrderNr() {
        return orderNr;
    }

    public void setOrderNr(String orderNr) {
        this.orderNr = orderNr;
    }

    public int getHowMany() {
        return howMany;
    }

    public void setHowMany(int howMany) {
        this.howMany = howMany;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getRider() {
        return rider;
    }

    public void setRider(String rider) {
        this.rider = rider;
    }

    public String getDatePickup() {
        return datePickup;
    }

    public void setDatePickup(String datePickup) {
        this.datePickup = datePickup;
    }

    public String getDateDelivery() {
        return dateDelivery;
    }

    public void setDateDelivery(String dateDelivery) {
        this.dateDelivery = dateDelivery;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Order)) return false;
        Order o = (Order) obj;
        return o.getOrderNr().equals(this.getOrderNr());
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNr);
    }

    @Override
    public String toString() {
        return orderNr;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return toString().compareTo(o.toString());
    }
}

