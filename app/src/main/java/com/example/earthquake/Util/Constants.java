package com.example.earthquake.Util;


import java.util.Random;

public class Constants {
    public static final String URL="https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/1.0_day.geojson";
    public static final int LIMIT=30;
    public static int randomInt(int max,int min)//random color generator
    {
        return new Random().nextInt(max-min)+min;
    }
}
