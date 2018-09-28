package com.example.testfornyblesoft.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.testfornyblesoft.R;
import com.example.testfornyblesoft.adapter.SavedData;
import com.example.testfornyblesoft.preference.Preferences;

import java.util.List;

public class DescriptionActivity extends AppCompatActivity {
    private TextView tvItemLatitude, tvItemLongitude, tvItemAddress, tvItemTemperature, tvItemHumidity, tvItemPressure, tvDate;
    private Button bBack;
    private List<SavedData> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        initViews();
        loadData();
        showData();
    }

    private int getPosition() {
        Intent intent = getIntent();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return intent.getIntExtra("position", 0);
    }

    private void initViews() {
        tvItemAddress = findViewById(R.id.tvItemAddress);
        tvItemHumidity = findViewById(R.id.tvItemHumidity);
        tvItemLatitude = findViewById(R.id.tvItemLatitude);
        tvItemLongitude = findViewById(R.id.tvItemLongitude);
        tvItemPressure = findViewById(R.id.tvItemPressure);
        tvItemTemperature = findViewById(R.id.tvItemTemperature);
        tvDate = findViewById(R.id.tvDate);
        bBack = findViewById(R.id.bBack);
        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DescriptionActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void loadData() {
        Preferences preferences = Preferences.getPreferences(this);
        data = preferences.loadFile();
    }

    private void showData() {
        SavedData savedData = data.get(getPosition());
        tvDate.setText(savedData.getDate().toString());
        tvItemLatitude.append(String.valueOf(savedData.getLat()));
        tvItemLongitude.append(String.valueOf(savedData.getLon()));
        tvItemAddress.setText(savedData.getAddress());
        tvItemTemperature.append(savedData.getWeather().getMain().getTemp().toString());
        tvItemPressure.append(savedData.getWeather().getMain().getPressure().toString());
        tvItemHumidity.append(savedData.getWeather().getMain().getHumidity().toString());
    }
}
