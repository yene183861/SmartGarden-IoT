package vn.hust.soict.project.iotapp.model;

public class DataReceive {
private String temperature;
private String humidity_air;
private String humidity_soil;
private String id;
private String packet_no;

    public DataReceive(String temperature, String humidity_air, String humidity_soil, String id, String packet_no) {
        this.temperature = temperature;
        this.humidity_air = humidity_air;
        this.humidity_soil = humidity_soil;
        this.id = id;
        this.packet_no = packet_no;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity_air() {
        return humidity_air;
    }

    public void setHumidity_air(String humidity_air) {
        this.humidity_air = humidity_air;
    }

    public String getHumidity_soil() {
        return humidity_soil;
    }

    public void setHumidity_soil(String humidity_soil) {
        this.humidity_soil = humidity_soil;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPacket_no() {
        return packet_no;
    }

    public void setPacket_no(String packet_no) {
        this.packet_no = packet_no;
    }

    @Override
    public String toString() {
        return "DataReceive{" +
                "temperature='" + temperature + '\'' +
                ", humidity_air='" + humidity_air + '\'' +
                ", humidity_soil='" + humidity_soil + '\'' +
                ", id='" + id + '\'' +
                ", packet_no='" + packet_no + '\'' +
                '}';
    }
}
