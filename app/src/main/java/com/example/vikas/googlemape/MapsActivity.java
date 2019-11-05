package com.example.vikas.googlemape;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMyLocationChangeListener  {

    GoogleMap gm;
    EditText ed;
    Button b1;
    LocationManager locationManager ;

    boolean GpsStatus ;
    SupportMapFragment map;

    Marker marker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        CheckGpsStatus() ;
        map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        b1 = (Button) findViewById(R.id.googlemape_activity);
        ed = (EditText) findViewById(R.id.serbar);


        map.getMapAsync(this);
        b1.setOnClickListener(this);
    }
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        gm = googleMap;
   //     gm.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        CheckGpsStatus() ;
        if(GpsStatus == true)
        {

        }else {
            Toast.makeText(this, "Please turn on GPS", Toast.LENGTH_SHORT).show();
        }
        gm.setMyLocationEnabled(true);
        gm.setOnMyLocationChangeListener(this);
    }

    @Override
    public void onClick(View v) {

        String placename=ed.getText().toString();
        if(ed.getText().toString().equals(""))
        {
            Toast.makeText(this, "Enter data ", Toast.LENGTH_SHORT).show();
            return;
        }
        Geocoder geocoder=new Geocoder(MapsActivity.this);
        List<Address> ads= null;
        try {
            ads = geocoder.getFromLocationName(placename,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(ads.size()==0)
        {
            Toast.makeText(this, "No place found", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Place Found", Toast.LENGTH_SHORT).show();
            Address address=ads.get(0);
            double lat=address.getLatitude();
            double lon=address.getLongitude();
            LatLng ll=new LatLng(lat,lon);


            if(marker!=null) marker.remove();
            marker=gm.addMarker(new MarkerOptions().position(ll));
            gm.animateCamera(CameraUpdateFactory.newLatLngZoom(ll,15));

        }
    }

    @Override
    public void onMyLocationChange(Location location) {

        double lat=location.getLatitude();
        double log=location.getLongitude();
        LatLng ll=new LatLng(lat,log);

        Geocoder geocoder=new Geocoder(MapsActivity.this);
        try
        {
            List<Address> list=geocoder.getFromLocation(lat,log,1);
            if(list.size()==0)
            {
                Toast.makeText(this, "Location Name Not Found", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Address address=list.get(0);
                int maxindex = address.getMaxAddressLineIndex();

                String adder="";

                for(int y=0;y<=maxindex;y++)
                {
                    adder=adder+address.getAddressLine(y)+" ";
                }
                if(marker!=null) marker.remove();
                marker=gm.addMarker(new MarkerOptions().position(ll).title(address.getAddressLine(0)).snippet(adder));
                gm.animateCamera(CameraUpdateFactory.newLatLngZoom(ll,gm.getCameraPosition().zoom));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void CheckGpsStatus(){


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

}
