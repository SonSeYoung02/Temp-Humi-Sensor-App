package com.example.temp_humi_sensor;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity{
    TextView Temptext, FTemptext, HumiText, DateText, ToDayText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // 버튼 객체 생성
        Temptext = (TextView)findViewById(R.id.TempText);
        FTemptext = (TextView)findViewById(R.id.FTempText);
        HumiText = (TextView)findViewById(R.id.HumiText);
        DateText = (TextView)findViewById(R.id.DateText);
        ToDayText = (TextView)findViewById(R.id.ToDay);
        
        // 센서 온도 가져오기
        TempHumiDataControl control = new TempHumiDataControl();
        control.loadSensorData(new SensorCallback(){
            @Override
            public void onSuccess(TempHumiDataSensor.SensorData data){
                Temptext.setText(data.Temp + "°C");
                FTemptext.setText(data.Fahrenheit + "°F");
                HumiText.setText(data.Humi+"%");
                DateText.setText(data.date);
                ToDayText.setText(data.date);
            }

            @Override
            public void onError(String error){
                Log.d("Error","[Error] "+error);
            }
        });

    }
}
