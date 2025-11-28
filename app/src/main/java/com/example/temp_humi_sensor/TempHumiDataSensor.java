package com.example.temp_humi_sensor;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TempHumiDataSensor {
    public class SensorData{
        @SerializedName("temp_C")
        public String tempC;

        @SerializedName("temp_F")
        public String tempF;

        @SerializedName("humid_per")
        public String humidPer;

        @SerializedName("datetime")
        public String datetime;
        public String today;
    }
    @GET("sensor")
    public Call<SensorData> getSensorData();
}
