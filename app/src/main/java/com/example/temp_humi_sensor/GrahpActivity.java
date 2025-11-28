package com.example.temp_humi_sensor;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GrahpActivity extends AppCompatActivity {

    ImageView Home,Graph;
    LineChart ChartTemp,ChartHumi;

    private FirebaseFirestore db;
    private CollectionReference sensorRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_grahp);
        
        // 하단 네비게이션 바 선언
        Home = (ImageView)findViewById(R.id.Home);
        Graph = (ImageView)findViewById(R.id.Graph);
        // 그래프 id 선언
        ChartTemp = findViewById(R.id.chart_temp);
        ChartHumi = findViewById(R.id.chart_humi);

        // 포트포워딩한 주소로 접속
        Retrofit retrofit_forwarding = new Retrofit.Builder()
                .baseUrl("http://112.172.235.137:42949/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // 로컬 주소로 접속
//        Retrofit retrofit_local = new Retrofit.Builder()
//                .baseUrl("http://10.0.2.2:8000")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();

        // firebase 초기화
        db = FirebaseFirestore.getInstance();
        sensorRef = db.collection("sensor_logs");

        // 데이터 로드해서 차트 그리기
        loadDataAndDrawChart();

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeintent = new Intent(GrahpActivity.this, MainActivity.class);
                startActivity(homeintent);
                finish();
            }
        });

        Graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent grahpintent = new Intent(GrahpActivity.this, GrahpActivity.class);
                startActivity(grahpintent);
                finish();
            }
        });
    }

    private void loadDataAndDrawChart() {
        long now = System.currentTimeMillis();
        long oneHourAge = now - 60 * 60 * 1000L;

        sensorRef
                .whereGreaterThanOrEqualTo("timestamp",oneHourAge)
                .orderBy("timestamp")
                .get()
                .addOnSuccessListener(querySnapshot -> {

                    List<Entry> tempEntries = new ArrayList<>();
                    List<String> labels = new ArrayList<>(); // x축 라벨용 (시간 문자열)

                    int index = 0;
                    for (DocumentSnapshot doc : querySnapshot) {
                        DBSensorData data = doc.toObject(DBSensorData.class);
                        String id = doc.getId();   // json 형식 "2025-11-28_18:08:29"

                        if (data != null) {
                            tempEntries.add(new Entry(index, data.temp_C));

                            String timeLabel = id.split("_")[1]; // "18:08:29"
                            labels.add(timeLabel);

                            index++;
                        }
                    }

                    LineDataSet tempSet = new LineDataSet(tempEntries, "온도 (℃)");
                    tempSet.setColor(Color.RED); // 온도 선 색상 빨강
                    tempSet.setLineWidth(3f);
                    tempSet.setDrawCircles(false);

                    LineData lineData = new LineData(tempSet);
                    ChartTemp.setData(lineData);
                    ChartTemp.getDescription().setText("최근 1시간 온도");

                    // x축 라벨
                    XAxis xAxis = ChartTemp.getXAxis();
                    xAxis.setGranularity(1f);
                    xAxis.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getAxisLabel(float value, AxisBase axis) {
                            int i = (int) value;
                            if (i >= 0 && i < labels.size()) {
                                return labels.get(i);
                            }
                            return "";
                        }
                    });

                    ChartTemp.animateX(800);
                    ChartTemp.invalidate();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "데이터 로드 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        sensorRef
                .whereGreaterThanOrEqualTo("timestamp",oneHourAge)
                .orderBy("timestamp")
                .get()
                .addOnSuccessListener(querySnapshot -> {

                    List<Entry> tempEntries = new ArrayList<>();
                    List<Entry> humiEntries = new ArrayList<>();
                    List<String> labels = new ArrayList<>(); // x축 라벨용 (시간 문자열)

                    int index = 0;
                    for (DocumentSnapshot doc : querySnapshot) {
                        DBSensorData data = doc.toObject(DBSensorData.class);
                        String id = doc.getId();   // json 형식 "2025-11-28_18:08:29"

                        if (data != null) {
                            humiEntries.add(new Entry(index, data.humid_per));

                            String timeLabel = id.split("_")[1]; // "18:08:29"
                            labels.add(timeLabel);

                            index++;
                        }
                    }

                    LineDataSet humiSet = new LineDataSet(humiEntries, "습도 (%)");
                    humiSet.setColor(Color.BLUE);   // 습도 선 색상 파랑
                    humiSet.setLineWidth(3f);
                    humiSet.setDrawCircles(false);

                    LineData lineData = new LineData(humiSet);
                    ChartHumi.setData(lineData);
                    ChartHumi.getDescription().setText("최근 1시간 습도");

                    // x축 라벨
                    XAxis xAxis = ChartHumi.getXAxis();
                    xAxis.setGranularity(1f);
                    xAxis.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getAxisLabel(float value, AxisBase axis) {
                            int i = (int) value;
                            if (i >= 0 && i < labels.size()) {
                                return labels.get(i);
                            }
                            return "";
                        }
                    });

                    ChartHumi.animateX(800);
                    ChartHumi.invalidate();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "데이터 로드 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
        
    }
}