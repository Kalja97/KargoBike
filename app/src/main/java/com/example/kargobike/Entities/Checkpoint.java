package com.example.kargobike.Entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "checkpoints",
        foreignKeys =
        @ForeignKey(
                entity = Order.class,
                parentColumns = "orderNr",
                childColumns = "orderNr",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {
                @Index(
                        value = {"orderNr"}
                )}
)

public class Checkpoint {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    //    private Long fk;
    private String orderNr;
    private String checkpointName;
    private String type;
    private String gps;
    private String datetime;
    private String rider;

    public Checkpoint(String orderNr, String checkpointName, String type, String gps, String datetime, String rider){
        this.orderNr = orderNr;
        this.checkpointName = checkpointName;
        this. type = type;
        this.gps = gps;
        this.datetime = datetime;
        this.rider = rider;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNr() {
        return orderNr;
    }

    public void setOrderNr(String orderNr) {
        this.orderNr = orderNr;
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
}
