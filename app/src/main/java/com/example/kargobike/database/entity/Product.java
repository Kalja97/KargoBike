package com.example.kargobike.database.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class Product implements Comparable{

    private String number;
    private String name;
    private Double price;
    private Boolean active;

    //Empty Constructor
    public Product(){

    }

    //Constructor with attributes
    public Product(@NonNull String productNr, String name, Double price){

        this.name = name;
        this.price = price;
        this.active = true;
    }

    //Getter and Setter

    @Exclude
    public String getProductNumber() {
        return number;
    }

    public void setProductNumber(@NonNull String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Order)) return false;
        Order o = (Order) obj;
        return o.getOrderNr().equals(this.getProductNumber());
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return toString().compareTo(o.toString());
    }

    @Override
    public String toString() {
        return name + " (" + price + ")";
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("productNumber", number);
        result.put("productName", name);
        result.put("productPrice", price);
        result.put("productState", active);
        return result;

    }
}
