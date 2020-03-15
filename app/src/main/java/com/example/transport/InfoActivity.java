package com.example.transport;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

import static android.location.LocationManager.*;

public class InfoActivity extends AppCompatActivity {

    TextView textViewInfo;
    Button btnBack;
    Boolean index;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 777;

    private LocationManager locationManager;
    StringBuilder sbGPS = new StringBuilder();
    StringBuilder sbNet = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        textViewInfo = findViewById(R.id.textViewInfo);
        btnBack = findViewById(R.id.btnBack);

        Intent intent = getIntent();

        String fName = intent.getStringExtra("ativityid");

        if (fName.equals("settings")) {
            textViewInfo.setText(getResources().getString(R.string.info_start));
            index = true;

        } else {
            textViewInfo.setText(getResources().getString(R.string.info_galery));

            index = false;

        }
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                    //запрашиваем пермишен, уже не показывая диалогов с пояснениями
//                    ActivityCompat.requestPermissions(InfoActivity.this,
//                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                            MY_PERMISSIONS_REQUEST_LOCATION);


                if (index == true) {
                    Intent intent = new Intent(InfoActivity.this, StartActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(InfoActivity.this, MyGalery.class);
                    startActivity(intent);
                }
            }
        });


       // locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


    }


//    @SuppressLint("MissingPermission")
//    @Override
//    protected void onResume() {
//        super.onResume();
//
////
////        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
////            new AlertDialog.Builder(InfoActivity.this)
////                    .setTitle("nbnekmybr")
////                    .setMessage("123asd")
////                    .setPositiveButton("ок", new DialogInterface.OnClickListener() {
////                        @Override
////                        public void onClick(DialogInterface dialogInterface, int i) {
////                            //Юзер одобрил
////                            ActivityCompat.requestPermissions(InfoActivity.this,
////                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
////                                    MY_PERMISSIONS_REQUEST_LOCATION);
////                        }
////                    })
////                    .create()
////                    .show();
////            return;
////        }
//        locationManager.requestLocationUpdates(NETWORK_PROVIDER,
//                0, 0, locationListener);
//        checkEnabled();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        locationManager.removeUpdates(locationListener);
//    }
//
//    private LocationListener locationListener = new LocationListener() {
//
//        @Override
//        public void onLocationChanged(Location location) {
//            showLocation(location);
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//            checkEnabled();
//        }
//
//        @SuppressLint("MissingPermission")
//        @Override
//        public void onProviderEnabled(String provider) {
//            checkEnabled();
//            showLocation(locationManager.getLastKnownLocation(provider));
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//            if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
//                Log.d("LOG", "status = " +"Status: " + String.valueOf(status));
//            }
//        }
//    };
//
//    private void showLocation(Location location) {
//        if (location == null)
//            return;
//
//            Log.d("LOG","format = " + formatLocation(location));
//            textViewInfo.setText("format entermet = " + formatLocation(location)+"i = ");
//
//
//    }
//
//    private String formatLocation(Location location) {
//        if (location == null)
//            return "";
//        return String.format(
//                "Coordinates: lat = %1$.4f, lon = %2$.4f, time = %3$tF %3$tT",
//                location.getLatitude(), location.getLongitude(), new Date(
//                        location.getTime()));
//    }
//
//    private void checkEnabled() {
//
//
//       textViewInfo.setText("Enabled: "
//               + locationManager
//               .isProviderEnabled(LocationManager.NETWORK_PROVIDER));
//
//    }
//


}
