package vn.hust.soict.project.iotapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Area implements Serializable {
    @SerializedName("_id")
    private String id;
    private String idGarden;
    private int area;
    private String name;
    private String position;

    public Area(String idGarden, String name, String position) {
        this.idGarden = idGarden;
        this.name = name;
        this.position = position;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdGarden() {
        return idGarden;
    }

    public void setIdGarden(String idGarden) {
        this.idGarden = idGarden;
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
