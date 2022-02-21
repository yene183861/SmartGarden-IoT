package vn.hust.soict.project.iotapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Device {
    @SerializedName("_id")
    private String id;
    private String idArea;
    private String name;
    private String position;
    private int type;
    /*  1 cảm biến nhiệt độ
        2 cảm biến độ ẩm đất
        3 Cảm biến không khí
    */
    private double value;
    private boolean status;
private Date time;

    public Device(String idArea, String name, String position, int type,double value, boolean status) {
        this.idArea = idArea;
        this.name = name;
        this.position = position;
        this.type = type;
        this.status = status;
    }

    public Device(String id, String idArea, String name, String position, int type, double value, boolean status) {
        this.id = id;
        this.idArea = idArea;
        this.name = name;
        this.position = position;
        this.type = type;
        this.value = value;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdArea() {
        return idArea;
    }

    public void setIdArea(String idArea) {
        this.idArea = idArea;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
