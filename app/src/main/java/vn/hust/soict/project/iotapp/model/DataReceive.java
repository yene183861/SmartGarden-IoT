package vn.hust.soict.project.iotapp.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DataReceive {
private double temperature;
private double humidity_air;
private double humidity_soil;
private LocalDateTime time;

    public DataReceive(double temperature, double humidity_air, double humidity_soil, LocalDateTime time) {
        this.temperature = temperature;
        this.humidity_air = humidity_air;
        this.humidity_soil = humidity_soil;
        this.time = time;
    }

    public DataReceive(double temperature, double humidity_air, double humidity_soil) {
        this.temperature = temperature;
        this.humidity_air = humidity_air;
        this.humidity_soil = humidity_soil;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity_air() {
        return humidity_air;
    }

    public void setHumidity_air(double humidity_air) {
        this.humidity_air = humidity_air;
    }

    public double getHumidity_soil() {
        return humidity_soil;
    }

    public void setHumidity_soil(double humidity_soil) {
        this.humidity_soil = humidity_soil;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
