package com.example.temp_humi_sensor;

public interface SensorCallback {
    void onSuccess(TempHumiDataSensor.SensorData data);
    void onError(String error);
}
