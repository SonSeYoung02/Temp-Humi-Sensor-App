package com.example.temp_humi_sensor;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TempHumiDataControl{
    private TempHumiDataSensor api;
    public TempHumiDataControl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://112.172.235.137:42949/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(TempHumiDataSensor.class);
    }
    public void loadSensorData(SensorCallback callback){
        Call<TempHumiDataSensor.SensorData> call = api.getSensorData();

        call.enqueue(new Callback<TempHumiDataSensor.SensorData>() {
            @Override
            public void onResponse(Call<TempHumiDataSensor.SensorData> call, Response<TempHumiDataSensor.SensorData> response) {
                if(response.isSuccessful()){
                    TempHumiDataSensor.SensorData data = response.body();
                    String today = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault())
                            .format(new Date());

                    data.today = today;

                    callback.onSuccess(data);
                }else{
                    callback.onError("[Error] HTTP " + response.code());
                }
            }
            @Override
            public void onFailure(Call<TempHumiDataSensor.SensorData> call,Throwable t){
                Log.d("Error","[Error] "+t.getMessage());
            }
        });
    }
}
