package com.example.kargobike.database.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Checkpoint {

    private String id;

    //    private Long fk;
    private String checkPointID;
    private String checkpointName;
    private String type;
    private String gps;
    private String datetime;
    private String rider;

    //Constructor
    public Checkpoint(){

    }

    public Checkpoint(String checkPointID, String checkpointName, String type, String gps, String datetime, String rider){
        this.checkPointID = checkPointID;
        this.checkpointName = checkpointName;
        this. type = type;
        this.gps = gps;
        this.datetime = datetime;
        this.rider = rider;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getcheckPointID() {
        return checkPointID;
    }

    public void setcheckPointID(String checkPointID) {
        this.checkPointID = checkPointID;
    }

    public String getCheckpointName() {
        return checkpointName;
    }

    public void setCheckpointName(String checkpointName) {
        this.checkpointName = checkpointName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getRider() {
        return rider;
    }

    public void setRider(String rider) {
        this.rider = rider;
    }

    @Override

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Checkpoint)) return false;
        Checkpoint o = (Checkpoint) obj;
        return o.getId().equals(this.getId());
    }

    @Override
    public String toString() {
        return id + "  (" + type + ")";
    }

    //Put informations in a map
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("checkPointID", checkPointID);
        result.put("checkpointName", checkpointName);
        result.put("type", type);
        result.put("gps", gps);
        result.put("datetime", datetime);
        result.put("rider", rider);
        return result;
    }
}
