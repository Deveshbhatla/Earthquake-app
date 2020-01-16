package com.example.earthquake.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.earthquake.Model.EarthQuake;
import com.example.earthquake.R;
import com.example.earthquake.Util.Constants;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuakesListActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private ListView listView;
    private RequestQueue queue;
    private ArrayAdapter arrayAdapter;
    private List<EarthQuake> quakeList;
    private ArrayList<String> arrayList;
    private BitmapDescriptor[] iconColors;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quakes_list);
        quakeList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listview);

        queue = Volley.newRequestQueue(this);
        arrayList = new ArrayList<>();
        getAllQuakes(Constants.URL);


    }

    void getAllQuakes(String url)// fetch data from json
    {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                final EarthQuake earthQuake = new EarthQuake();
                try {
                    JSONArray jsonArray = response.getJSONArray("features");
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        final JSONObject properties = jsonArray.getJSONObject(i).getJSONObject("properties");
                        Log.d("properties", properties.getString("place"));
                        //get geometry properties
                        JSONObject geometry = jsonArray.getJSONObject(i).getJSONObject("geometry");
                        // get coordinates
                        final JSONArray coordinates = geometry.getJSONArray("coordinates");
                           final double lon = coordinates.getDouble(0);
                           final double lat = coordinates.getDouble(1);


                        double altitude = coordinates.getDouble(2);
                        Log.d("Quake", lon + " " + lat);
                        earthQuake.setPlace(properties.getString("place"));
                        earthQuake.setType(properties.getString("type"));
                        earthQuake.setTime(properties.getLong("time"));
                        earthQuake.setLat(lat);
                        earthQuake.setLon(lon);
                        earthQuake.setMagnitude(properties.getDouble("mag"));
                        earthQuake.setDetailLink(properties.getString("detail"));
                        arrayList.add(earthQuake.getPlace());



                    }

                    //pre baked code
                    arrayAdapter = new ArrayAdapter<>(QuakesListActivity.this, android.R.layout.simple_list_item_1,
                            android.R.id.text1, arrayList);
                    listView.setAdapter(arrayAdapter);

                    arrayAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;


    }
}
