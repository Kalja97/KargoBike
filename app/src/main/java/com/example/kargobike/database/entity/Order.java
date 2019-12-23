package com.example.kargobike.database.entity;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class Order implements Comparable, Serializable {

    private String orderNr;
    private String customer;
    private String fromAddress;
    private String toAddress;
    private String product;
    private int howMany;
    private String rider;
    private String dateDelivery;
    private String timeDelivery;
    private String state;
    private ArrayList<String> checkpointsID;

    //Empty Constructor
    public Order(){

    }

    //Constructor with attributes
    public Order(@NonNull String orderNr, String customer, String fromAddress, String toAddress, String product, int howMany, String rider, String dateDelivery,String timeDelivery, String state) {
        this.orderNr = orderNr;
        this.customer = customer;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.product = product;
        this.howMany = howMany;
        this.rider = rider;
        this.dateDelivery =  dateDelivery;
        this.timeDelivery = timeDelivery;
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

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
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

    public String getDateDelivery() {
        return dateDelivery;
    }

    public void setDateDelivery(String dateDelivery) {
        this.dateDelivery = dateDelivery;
    }

    public String getTimeDelivery() {
        return timeDelivery;
    }

    public void setTimeDelivery(String timeDelivery) {
        this.timeDelivery = timeDelivery;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ArrayList<String> getCheckpointsID() {
        return checkpointsID;
    }

    public void setCheckpointsID(ArrayList<String> checkpointsID) {
        this.checkpointsID = checkpointsID;
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
                ", customer='" + customer + '\'' +
                ", fromAddress='" + fromAddress + '\'' +
                ", toAddress='" + toAddress + '\'' +
                ", product='" + product + '\'' +
                ", howMany=" + howMany +
                ", rider='" + rider + '\'' +
                ", dateDelivery='" + dateDelivery + '\'' +
                ", timeDelivery='" + timeDelivery + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("OrderNr", orderNr);
        result.put("customer", customer);
        result.put("fromAddress", fromAddress);
        result.put("toAddress", toAddress);
        result.put("product", product);
        result.put("howMany", howMany);
        result.put("rider", rider);
        result.put("dateDelivery", dateDelivery);
        result.put("timeDelivery", timeDelivery);
        result.put("state", state);
        result.put("checkpoints", checkpointsID);
        return result;
    }
}
