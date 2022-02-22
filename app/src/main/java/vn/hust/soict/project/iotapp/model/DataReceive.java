package vn.hust.soict.project.iotapp.model;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DataReceive {
private double Temperature;
private double Humidity;
private double Soil;

    public DataReceive(double temperature, double humidity, double soil) {
        Temperature = temperature;
        Humidity = humidity;
        Soil = soil;
    }


    public double getTemperature() {
        return Temperature;
    }

    public void setTemperature(double temperature) {
        Temperature = temperature;
    }

    public double getHumidity() {
        return Humidity;
    }

    public void setHumidity(double humidity) {
        Humidity = humidity;
    }

    public double getSoil() {
        return Soil;
    }

    public void setSoil(double soil) {
        Soil = soil;
    }
}
