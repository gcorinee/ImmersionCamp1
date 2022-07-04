package com.example.immersioncamp1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_window);


        weatherAdapter adapter = new weatherAdapter((((MainActivity) MainActivity.mContext).data));
        ListView listView2 = findViewById(R.id.weather_listView);
        listView2.setAdapter(adapter);

        Button imageButton2 = (Button) findViewById(R.id.btn2);
        imageButton2.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });
    }

    public void Show(int num, ArrayList<Weather> _data) {
        if (num == 1) {
            widgetAdapter adapter = new widgetAdapter(_data);
            ListView listView = findViewById(R.id.weather_listView);
            listView.setAdapter(adapter);
        } else {
            weatherAdapter adapter = new weatherAdapter(_data);
            ListView listView2 = findViewById(R.id.weather_listView);
            listView2.setAdapter(adapter);
        }
    }
}