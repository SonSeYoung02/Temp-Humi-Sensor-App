package com.example.temp_humi_sensor;

import android.util.Log;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.Calendar;

public class TempHumiDataControl{
    private TempHumiDataSensor api;
    TextView DateText;
    public TempHumiDataControl() {
        Retrofit retrofit_forwarding = new Retrofit.Builder()
                .baseUrl("http://112.172.235.137:42949/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit_forwarding.create(TempHumiDataSensor.class);

//        Retrofit retrofit_local = new Retrofit.Builder()
//                .baseUrl("http://10.0.2.2:8000")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        api = retrofit_local.create(TempHumiDataSensor.class);
    }
    public void loadSensorData(SensorCallback callback){
        Call<TempHumiDataSensor.SensorData> call = api.getSensorData();

        call.enqueue(new Callback<TempHumiDataSensor.SensorData>() {
            @Override
            public void onResponse(Call<TempHumiDataSensor.SensorData> call, Response<TempHumiDataSensor.SensorData> response) {
                if(response.isSuccessful()){
                    TempHumiDataSensor.SensorData data = response.body();

                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH) + 1;
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    String formatted = String.format("%04d-%02d-%02d", year, month, day);
                    data.today = formatted;

                    Log.d("Sensor",
                            "[C Temp]:"+data.tempC +
                            ", [Humi]:"+data.humidPer +
                            ", [F Temp]:"+data.tempF +
                            ", [today]:"+data.datetime);

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
