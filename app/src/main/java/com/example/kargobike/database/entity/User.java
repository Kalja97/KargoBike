package com.example.kargobike.database.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class User implements Comparable{

    private String idNumber;
    private String firstname;
    private String lastname;
    private String email;

    private Boolean isDispatcher;
    private Boolean isActive;

    //Empty Constructor
    public User(){
        this.isActive = true;
    }

    //Constructor with attributes
    public User(@NonNull String idNumber, String firstname, String lastname, String email, Boolean isDispatcher){

        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.isDispatcher = isDispatcher;
    }

    //Getter and Setter

    @Exclude
    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(@NonNull String idNumber) {
        this.idNumber = idNumber;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getDispatcher() {
        return isDispatcher;
    }

    public void setDispatcher(Boolean dispatcher) {
        isDispatcher = dispatcher;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof User)) return false;
        User u = (User) obj;
        return u.getIdNumber().equals(this.getIdNumber());
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return toString().compareTo(o.toString());
    }

    @Override
    public String toString() {
        return firstname + " " + lastname;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        //result.put("productNumber", number);
        result.put("firstname", firstname);
        result.put("lastname", lastname);
        result.put("email", email);
        result.put("active", isActive);
        result.put("dispatcher", isDispatcher);
        return result;
    }
}
