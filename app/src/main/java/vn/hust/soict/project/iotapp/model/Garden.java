package vn.hust.soict.project.iotapp.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Garden implements Serializable {
    @SerializedName("_id")
    private String id;
    private String idUser;
    private String name;
    private String address;
    private int area; //số khu vực
    private double acreage; //diện tích

    public Garden(String id, String idUser, String name, String address, int area, double acreage) {
        this.id = id;
        this.idUser = idUser;
        this.name = name;
        this.address = address;
        this.area = area;
        this.acreage = acreage;
    }

    public Garden(String idUser, String name, String address, int area, double acreage) {
        this.idUser = idUser;
        this.name = name;
        this.address = address;
        this.area = area;
        this.acreage = acreage;
    }


    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
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

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public double getAcreage() {
        return acreage;
    }

    public void setAcreage(double acreage) {
        this.acreage = acreage;
    }

}
