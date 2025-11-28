package com.example.temp_humi_sensor;

public class DBSensorData {
    public float temp_C;
    public float temp_F;
    public float humid_per;
    public String datetime;

    public DBSensorData(){

    }

    public DBSensorData(float temp_C, float temp_F, float humid_per, String datetime){
        this.temp_C = temp_C;
        this.temp_F = temp_F;
        this.humid_per = humid_per;
        this.datetime = datetime;
    }
}
