package com.example.immersioncamp1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class widgetAdapter extends BaseAdapter {
    private List<Weather> mData;
    private Map<String, Integer> mWeatherImageMap;

    public widgetAdapter(List<Weather> mData) {
        this.mData = mData;
        mWeatherImageMap = new HashMap<>();
        mWeatherImageMap.put("맑음", R.drawable.sunny);
        mWeatherImageMap.put("흐림", R.drawable.fog);
        mWeatherImageMap.put("구름많음", R.drawable.cloudy);
        mWeatherImageMap.put("비", R.drawable.rainy);
        mWeatherImageMap.put("눈", R.drawable.snow);
        mWeatherImageMap.put("맑음_밤", R.drawable.sunny_night);
        mWeatherImageMap.put("흐림_밤", R.drawable.fog_night);
        mWeatherImageMap.put("구름많음_밤", R.drawable.cloudy_night);
        mWeatherImageMap.put("비_밤", R.drawable.rainy_night);
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Weather weather = mData.get(position);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_weather_widget, parent, false);

            //날씨, 도시, 기온 View
            ImageView weatherimage =(ImageView) convertView.findViewById(R.id.weatherimage);
            weatherimage.setClipToOutline(true);
            TextView citytext = convertView.findViewById(R.id.citytext);
            TextView temptext = convertView.findViewById(R.id.temptext);

            holder.weatherimage = weatherimage;
            holder.citytext = citytext;
            holder.temptext = temptext;

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 데이터 설정 -> 홀더에 저장
        holder.citytext.setText(weather.getCity());
        holder.temptext.setText(weather.getTemp());
        holder.weatherimage.setImageResource(mWeatherImageMap.get(weather.getWeather()));

        return convertView;
    }
    private class ViewHolder {
        ImageView weatherimage;
        TextView citytext;
        TextView temptext;
    }
}

