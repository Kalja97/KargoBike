package com.example.kargobike.database.entity;



import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class Order implements Comparable{

    private String orderNr;

    private String sender;
    private String receiver;

    private String product;
    private int howMany;

    private String rider;
    private String datePickup;
    private String dateDelivery;

    private String state;

    //Empty Constructor
    public Order(){

    }

    //Constructor with attributes
    public Order(@NonNull String orderNr, String sender, String receiver, String product, int howMany, String rider,
                 String datePickup, String dateDelivery, String state){

        this.sender = sender;
        this.receiver = receiver;
        this.product = product;
        this.howMany = howMany;
        this.rider = rider;
        this.datePickup = datePickup;
        this.dateDelivery = dateDelivery;
        this.state = state;
    }

    //Getter and Setter

    @Exclude
    public String getOrderNr() {
        return orderNr;
    }

    public void setOrderNr(@NonNull String orderNr) {
        this.orderNr = orderNr;
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

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getHowMany() {
        return howMany;
    }

    public void setHowMany(int howMany) {
        this.howMany = howMany;
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
    public int compareTo(@NonNull Object o) {
        return toString().compareTo(o.toString());
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderNr='" + orderNr + '\'' +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", product='" + product + '\'' +
                ", howMany=" + howMany +
                ", rider='" + rider + '\'' +
                ", datePickup='" + datePickup + '\'' +
                ", dateDelivery='" + dateDelivery + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("OrderNr", orderNr);
        result.put("sender", sender);
        result.put("receiver", receiver);
        result.put("product", product);
        result.put("howMany", howMany);
        result.put("rider", rider);
        result.put("datePickup", datePickup);
        result.put("dateDelivery", dateDelivery);
        result.put("state", state);
        return result;

    }
}
