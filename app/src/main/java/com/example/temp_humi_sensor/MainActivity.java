package com.example.temp_humi_sensor;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity{
    TextView Temptext, FTemptext, HumiText, DateText, ToDayText;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private TempHumiDataControl control;

    private final Runnable refreshTask = new Runnable() {
        @Override
        public void run() {
            control.loadSensorData(new SensorCallback() {
                @Override
                public void onSuccess(TempHumiDataSensor.SensorData data) {

                    Temptext.setText(data.tempC);
                    HumiText.setText(data.humidPer);
                    FTemptext.setText(data.tempF);
                    DateText.setText(data.datetime);
                    ToDayText.setText(data.today);
                }

                @Override
                public void onError(String error) {
                    Log.e("Sensor", "error: " + error);
                }
            });

            handler.postDelayed(this, 2000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 버튼 객체 생성
        Temptext = (TextView)findViewById(R.id.TempText);
        FTemptext = (TextView)findViewById(R.id.FTempText);
        HumiText = (TextView)findViewById(R.id.HumiText);
        DateText = (TextView)findViewById(R.id.DateText);
        ToDayText = (TextView)findViewById(R.id.ToDay);
        
        control = new TempHumiDataControl();

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 화면이 보일 때부터 2초 반복 시작
        handler.post(refreshTask);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 다른 화면으로 나가면 반복 중지 (메모리 낭비 방지)
        handler.removeCallbacks(refreshTask);
    }
}
