package vn.hust.soict.project.iotapp.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Garden implements Serializable {
    @SerializedName("_id")
    private String id;
    private String name;
    private String address;
    private double acreage; //diện tích

    public Garden(String id, String idUser, String name, String address, double acreage) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.acreage = acreage;
    }

    public Garden(String name, String address, double acreage) {
        this.name = name;
        this.address = address;
        this.acreage = acreage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getAcreage() {
        return acreage;
    }

    public void setAcreage(double acreage) {
        this.acreage = acreage;
    }

}
