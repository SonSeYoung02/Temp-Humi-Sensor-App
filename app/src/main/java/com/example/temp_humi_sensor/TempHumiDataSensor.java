package com.example.temp_humi_sensor;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TempHumiDataSensor {
    public class SensorData{
        public float Temp;
        public float Humi;
        public float Fahrenheit;
        public String date;
        public String today;
    }
    @GET("sensor")
    public Call<SensorData> getSensorData();
}
