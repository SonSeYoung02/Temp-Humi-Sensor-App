package com.example.temp_humi_sensor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity{
    TextView Temptext, FTemptext, HumiText, DateText, ToDayText;
    ImageView Home, Graph;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private TempHumiDataControl control;

    private final Runnable refreshTask = new Runnable() {
        @Override
        public void run() {
            control.loadSensorData(new SensorCallback() {
                @Override
                public void onSuccess(TempHumiDataSensor.SensorData data) {

                    Temptext.setText(data.tempC+" °C");
                    HumiText.setText(data.humidPer+" %");
                    FTemptext.setText(data.tempF+" °F");
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
        Home = (ImageView)findViewById(R.id.Home);
        Graph = (ImageView)findViewById(R.id.Graph);
        
        control = new TempHumiDataControl();

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeintent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(homeintent);
                finish();
            }
        });

        Graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent grahpintent = new Intent(MainActivity.this, GrahpActivity.class);
                startActivity(grahpintent);
                finish();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        handler.post(refreshTask);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(refreshTask);
    }
}
