package vn.hust.soict.project.iotapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Area implements Serializable {
    @SerializedName("_id")
    private String id;
    private String garden;
    private double acreage;
    private String name;
    private String position;

    public Area(String garden, String name, String position, double acreage) {
        this.garden = garden;
        this.name = name;
        this.position = position;
        this.acreage = acreage;
    }

    public Area(String id, String garden, double acreage, String name, String position) {
        this.id = id;
        this.garden = garden;
        this.acreage = acreage;
        this.name = name;
        this.position = position;
    }

    public double getAcreage() {
        return acreage;
    }

    public void setAcreage(double acreage) {
        this.acreage = acreage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGarden() {
        return garden;
    }

    public void setGarden(String garden) {
        this.garden = garden;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
