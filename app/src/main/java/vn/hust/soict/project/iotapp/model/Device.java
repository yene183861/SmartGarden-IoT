package vn.hust.soict.project.iotapp.model;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public class Device {
    @SerializedName("_id")
    private String id;
    private String area;
    private String name;
    private String position;
    private int type;
    /*  1 cảm biến nhiệt độ
        2 cảm biến độ ẩm đất
        3 Cảm biến không khí
    */
    private double value;
    private boolean status;
    private String topic;
    private LocalDateTime time;


    public Device(String idArea, String name, String position, int type, boolean status) {
        this.id = id;
        this.area = idArea;
        this.name = name;
        this.position = position;
        this.type = type;
        this.status = status;
    }


    public Device(String id, String area, String name, String position, int type, double value, boolean status, String topic, LocalDateTime time) {
        this.id = id;
        this.area = area;
        this.name = name;
        this.position = position;
        this.type = type;
        this.value = value;
        this.status = status;
        this.topic = topic;
        this.time = time;
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

    public String getArea() {
        return area;
    }

    public void setArea(String idArea) {
        this.area = idArea;
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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
