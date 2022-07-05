package com.example.immersioncamp1;

public class Weather {
    String city;
    String temp;
    String Weather;
    String humidity;
    String is_rain;
    String rain_prob;
    String rain_amount;

    public Weather(String city, String temp, String weather, String humidity, String is_rain, String rain_prob, String rain_amount) {
        this.city = city;
        this.temp = temp;
        this.Weather = weather;
        this.humidity = humidity;
        this.is_rain = is_rain;
        this.rain_prob = rain_prob;
        this.rain_amount = rain_amount;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getWeather() {
        return Weather;
    }
    public String getHumidity(){
        return humidity;
    }
    public String getIs_rain(){return is_rain;}
    public String getRain_prob(){return rain_prob;}
    public String getRain_amount(){return rain_amount;}

    public void setWeather(String weather) {
        Weather = weather;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "city='" + city + '\'' +
                ", temp='" + temp + '\'' +
                ", Weather='" + Weather + '\'' +
                ", Humidity='" + humidity + '\'' +
                ", is_rain='" + is_rain + '\'' +
                ", rain_prob='" + rain_prob + '\'' +
                ", rain_amount='" + rain_amount + '\'' +
                '}';
    }
}