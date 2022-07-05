package com.example.immersioncamp1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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

        // 클릭 이벤트

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Weather item = (((MainActivity) MainActivity.mContext).data).get(position);

                if(item.is_rain.equals("1")){
                    Toast.makeText(getApplicationContext(),
                            "온도: " +item.getTemp() + " " + "습도: "+item.getHumidity() + '\n' + "강수량: " +item.rain_amount,Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),
                            "온도: " +item.getTemp() + " " + "습도: "+item.getHumidity() + '\n' + "강수확률: " +item.rain_prob +"%" ,Toast.LENGTH_SHORT).show();
                }
            }
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