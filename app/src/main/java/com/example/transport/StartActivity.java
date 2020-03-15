package com.example.transport;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.LongFunction;

public class StartActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    Spinner  routesSpuner;
    Spinner driverSpiner;
    Spinner carSpiner;
    String type;
    String idRoute;
    String idCar;
    String idDriver;
    Integer nds;

    Button btn_info;
    Button downloadData;

    TextView textViewDriver;
    TextView textViewnumRout;
    TextView textViewRout;

    SharedPreferences sharedPreferencesId;

    ArrayList<String> rout = new ArrayList<>();
    ArrayList<String> id_rout = new ArrayList<>();

    ArrayList<String> car = new ArrayList<>();
    ArrayList<String> id_car = new ArrayList<>();

    ArrayList<String> driver = new ArrayList<>();
    ArrayList<String> id_driver = new ArrayList<>();
    ArrayList<Integer> idnds = new ArrayList<>();
    public static final String APP_PREFERENCES = "mysettings";

    Button reconect;
    Button btnEnter;
    ProgressBar pb;


    String statusSwitch;

    Switch switchTerminal;

    public static final String LOG_TAG = "START_SCREEN";

    //TODO Location

    private LocationManager locationManager;
    StringBuilder sbGPS = new StringBuilder();
    StringBuilder sbNet = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        routesSpuner = findViewById(R.id.routesSpuner);
        driverSpiner = findViewById(R.id.driverSpiner);
        carSpiner = findViewById(R.id.carSpiner);
        pb = findViewById(R.id.pbSatart);
        btnEnter = findViewById(R.id.buttonEnter);
        reconect = findViewById(R.id.reconect);
        carSpiner.setAlpha(0);

        textViewDriver = findViewById(R.id.textViewDriver);
        textViewRout = findViewById(R.id.textViewRout);
        textViewnumRout = findViewById(R.id.textViewnumRout);
        downloadData = findViewById(R.id.downloadData);
        switchTerminal = findViewById(R.id.switchTerminal);

        btn_info = findViewById(R.id.btn_info);


        //TODO LOcation
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);



        //TODO Shared preference =

        reconect.setAlpha(0.f);
        type = "routs";
        idRoute = "fale";
        routesSpuner.setPrompt("Выберите маршрут");
        rout.add("Выберите маршрут");





        String cars;

        downloadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                startActivity(getIntent());

            }
        });

        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(StartActivity.this, InfoActivity.class);
                intent.putExtra("ativityid", "settings");

                startActivity(intent);

            }
        });

        sharedPreferencesId = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        cars = sharedPreferencesId.getString("routTW","");

        if (cars == null|| cars == ""){

            textViewDriver.setText("Выберите водителя");
            textViewnumRout.setText("Выберите номер машины");
            textViewRout.setText("Выберите маршрут");

        } else {

            textViewDriver.setText(sharedPreferencesId.getString("driverTW",""));
            textViewnumRout.setText(sharedPreferencesId.getString("carTW",""));
            textViewRout.setText(sharedPreferencesId.getString("routTW",""));

        }


        car.add("Выберите машиуну");
        routesSpuner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                driverSpiner.setAlpha(0);

                type = "car";
                if (position == 0){
                    idRoute = null;
                    nds = null;

                }else {



                    textViewRout.setText("Номер маршрута: " + rout.get(position));
                    carSpiner.setAlpha(1);
                    nds = idnds.get(position -1);
                    idRoute = id_rout.get(position - 1);
                    pb.setVisibility(ProgressBar.VISIBLE);

                    Log.d(LOG_TAG, "nds = " + nds);

                    new ItemsCar().execute();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        driverSpiner.setPrompt("Выберите водителя");
        driverSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                if (position == 0){
                    idDriver = null;

                }else {
                    textViewDriver.setText("Водитель: "+ driver.get(position));

                    idDriver = id_driver.get(position - 1);
                    Log.d(LOG_TAG, "id  =" + idCar);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        carSpiner.setPrompt("Выберите машину");
        carSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                type = "driver";

                if (position == 0){
                    idCar = null;

                }else {

                    driverSpiner.setAlpha(1);
                    textViewnumRout.setText("Номер машины: "+ car.get(position));


                    idCar = id_car.get(position - 1);

                    Log.d(LOG_TAG, "id  =" + idCar);
                    pb.setVisibility(ProgressBar.VISIBLE);



                    new ItemsDriver().execute();


                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.d(LOG_TAG, "iicar = " + idCar + "     " +idDriver);


                if (idRoute == null|| idRoute.equals("")){

                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Выберите маршрут",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();


                }else {

                    sharedPreferencesId = getSharedPreferences(APP_PREFERENCES,MODE_PRIVATE);
                    SharedPreferences.Editor ed = sharedPreferencesId.edit();

                    Log.d(LOG_TAG, "nds == " + nds);

                    if (idCar == null || idCar.equals("")){

                        ed.putString("carTW","Не указан номер машины");

                    }else {
                        ed.putString("carTW",textViewnumRout.getText().toString());

                    }


                    if (idDriver == null|| idDriver.equals("")){
                        ed.putString("driverTW","Не указан водитель");

                    }else {
                        ed.putString("driverTW",textViewDriver.getText().toString());

                    }

                    ed.putString("route", idRoute);
                    ed.putString("driver",idDriver);
                    ed.putString("car",idCar);
                    ed.putInt("nds",nds);
                    ed.putString("routTW",textViewRout.getText().toString());
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Сохранено",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    ed.commit();


                    Intent intent = new Intent(StartActivity.this, MyGalery.class);
                    startActivity(intent);
                }




            }
        });






        pb.setVisibility(ProgressBar.VISIBLE);




        //TODO Switch








        reconectFunc();

        reconect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pb.setVisibility(ProgressBar.VISIBLE);

                reconectFunc();

            }
        });



        if (switchTerminal != null) {
            switchTerminal.setOnCheckedChangeListener(StartActivity.this);

            sharedPreferencesId = getSharedPreferences(APP_PREFERENCES,MODE_PRIVATE);
            statusSwitch = sharedPreferencesId.getString("statusSwitch","");

            Log.d(LOG_TAG, "status switc =" + statusSwitch);

            if (statusSwitch == null || statusSwitch.equals("false") || statusSwitch.equals("")){

                switchTerminal.setChecked(false);

            }else {
                switchTerminal.setChecked(true);
            }


        }


    }





    @Override
    public void onCheckedChanged(CompoundButton  buttonView, boolean isChecked) {


        sharedPreferencesId = getSharedPreferences(APP_PREFERENCES,MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferencesId.edit();

        if (isChecked == false){

            ed.putString("statusSwitch","false");

            Log.d(LOG_TAG, "false");

        }else {
            Log.d(LOG_TAG, "true");

            ed.putString("statusSwitch","true");

        }
        ed.commit();


//        Long tsLong = System.currentTimeMillis()/1000;
//        String ts = tsLong.toString();
//        Log.d(LOG_TAG, "текущее время = " + ts);
//
//
//        Toast.makeText(this, "Отслеживание переключения: " + (isChecked ? "on" : "off"),
//                Toast.LENGTH_SHORT).show();
    }



    //TODO Location





    void reconectFunc(){

        sharedPreferencesId = getSharedPreferences(APP_PREFERENCES,MODE_PRIVATE);

        if ((Boolean) NetworkManager.isNetworkAvailable(StartActivity.this)) {
            reconect.setAlpha(0.f);
            routesSpuner.setAlpha(1.f);

            String di =  sharedPreferencesId.getString("device_id","");

            if (di == null || di.equals("")){

                new DeviceIDClass().execute();


            }else {


                Toast toast = Toast.makeText(getApplicationContext(),
                        "Device id = " + di,
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }


            new ItemStartClass().execute();

        } else {

            driverSpiner.setAlpha(0.f);
            routesSpuner.setAlpha(0.f);
            carSpiner.setAlpha(0.f);
            reconect.setAlpha(1.f);
            pb.setVisibility(ProgressBar.INVISIBLE);

            Toast toast = Toast.makeText(getApplicationContext(),
                    "Нет подключения к сети интернет! Проверьте свое подключение!",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }


    }

    public static class NetworkManager {

        public static boolean isNetworkAvailable(Context context) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            } else {
                return false;
            }
        }
    }

    public  class ItemStartClass extends AsyncTask<Void, Void, List<ItemStart>> {

        @Override
        protected List<ItemStart> doInBackground(Void... voids) {

            return new StartAPI().itemsStart(type, "_id");


        }

        @Override
        protected void onPostExecute(List<ItemStart> items) {

            pb.setVisibility(ProgressBar.INVISIBLE);

                rout.clear();
                rout.add("Выберите маршрут");
                id_rout.clear();
                idnds.clear();

            for (int i = 0; i < items.size(); i++){
                rout.add(items.get(i).getRout() + " маршрут");
                id_rout.add(items.get(i).getIdRout());
                idnds.add(items.get(i).getIdNds());

            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(StartActivity.this,android.R.layout.simple_spinner_item,rout );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            routesSpuner.setAdapter(adapter);



        }

    }


    public  class ItemsCar extends AsyncTask<Void, Void, List<ItemStart>> {

        @Override
        protected List<ItemStart> doInBackground(Void... voids) {

            return new StartAPI().itemsStart(type, idRoute);


        }

        @Override
        protected void onPostExecute(List<ItemStart> items) {

            pb.setVisibility(ProgressBar.INVISIBLE);

            car.clear();
            id_car.clear();
            car.add("Выберите машину");

            for (int i = 0; i < items.size(); i++){
                car.add(items.get(i).getCar());
                id_car.add(items.get(i).getIdCar());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(StartActivity.this,android.R.layout.simple_spinner_item,car);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            carSpiner.setAdapter(adapter);



        }

    }



    public  class ItemsDriver extends AsyncTask<Void, Void, List<ItemStart>> {

        @Override
        protected List<ItemStart> doInBackground(Void... voids) {

            return new StartAPI().itemsStart(type, idCar);


        }

        @Override
        protected void onPostExecute(List<ItemStart> items) {

            pb.setVisibility(ProgressBar.INVISIBLE);

            driver.clear();
            id_driver.clear();
            driver.add("Выберите водителя");

            for (int i = 0; i < items.size(); i++){
                driver.add(items.get(i).getDriver());
                id_driver.add(items.get(i).getIdDriver());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(StartActivity.this,android.R.layout.simple_spinner_item,driver);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            driverSpiner.setAdapter(adapter);

        }

    }



    //TODO start class device id



    public  class DeviceIDClass extends AsyncTask<Void, Void, List<ItemStart>> {

        @Override
        protected List<ItemStart> doInBackground(Void... voids) {

            return new DeviceID().itemsStart();


        }

        @Override
        protected void onPostExecute(List<ItemStart> items) {

            pb.setVisibility(ProgressBar.INVISIBLE);


            Log.d(LOG_TAG, items.get(0).deviceID);


            sharedPreferencesId = getSharedPreferences(APP_PREFERENCES,MODE_PRIVATE);
            SharedPreferences.Editor ed = sharedPreferencesId.edit();

            ed.putString("device_id",items.get(0).deviceID);
            ed.commit();


        }

    }


}
