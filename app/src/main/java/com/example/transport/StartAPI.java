package com.example.transport;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StartAPI {

    public static final String LOG_TAG = "START_API";
    String idDriver;
    String idCar;
    String idRouts;


    public String getJSONString (String UrlSpec) throws IOException {

        OkHttpClient client = new OkHttpClient.Builder()
                .followSslRedirects(false)
                .followRedirects(false)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .build();

        Request request = new Request.Builder()
                .url(UrlSpec)
          //      .addHeader("Authorization", "Bearer " + "49936d14fd59419310dfa27028e242dfdf80e5f0")
        .addHeader("Authorization", "Token " + "api_token")
            .build();

         Response response = client.newCall(request).execute();
         String result = response.body().string();

        return result;

    }


    public List<ItemStart> itemsStart(String type, String _id){

        List<ItemStart> itemmList = new ArrayList<>();


        String _url = null;


        if (type.equals("routs")){
            _url = "https://avto.infosaver.ru/api/v0/routes/";

        } else if (type.equals("car")){

            _url = "https://avto.infosaver.ru/api/v0/cars/?route_id=" +_id;


        }else  if (type.equals("driver")){
            _url = "https://avto.infosaver.ru/api/v0/drivers/?car_id=" +_id;

        }

        try {
            String url = Uri.parse(_url)
                    .buildUpon()
                    .appendQueryParameter("format", "json")
                    .build().toString();


            if (type.equals("routs")){

                String jsonString = getJSONString(url);
                parseItems(itemmList, jsonString);

            } else if (type.equals("car")){

                String jsonString = getJSONString(url);
                parseCars(itemmList, jsonString);

            }else  if (type.equals("driver")){
                String jsonString = getJSONString(url);
                parseDriver(itemmList, jsonString);
            }



        } catch (IOException ioe){
            Log.e(LOG_TAG, "Ошибка загрузки данных", ioe);

        }catch (JSONException joe){
            Log.e(LOG_TAG, "Ошибка парсинга JSON", joe);
        }
        return itemmList;


    }

    //TODO api and adapter

    private void parseItems (List<ItemStart> items , String jsonstring) throws IOException, JSONException {

        Log.d(LOG_TAG, "jsonString rout api = " + jsonstring);

        JSONArray array = null;

        array= new JSONArray(jsonstring);

        for (int i = 0; i < array.length(); i++) {
            JSONObject nameObject = array.getJSONObject(i);
            ItemStart item = new ItemStart();
            item.setRout(nameObject.getString("name"));
            item.setIdRout(nameObject.getString("id"));
            item.setIdNds(nameObject.getInt("nds"));

            items.add(item);

        }

    }

    private void parseCars (List<ItemStart> items , String jsonstring) throws IOException, JSONException {

        Log.d(LOG_TAG, "jsonString Start Api = " + jsonstring);

        JSONArray array = null;

        array= new JSONArray(jsonstring);

        for (int i = 0; i < array.length(); i++) {
            JSONObject nameObject = array.getJSONObject(i);
            ItemStart item = new ItemStart();
            item.setCar(nameObject.getString("name"));
            item.setIdCar(nameObject.getString("id"));


            items.add(item);

        }

    }



    private void parseDriver (List<ItemStart> items , String jsonstring) throws IOException, JSONException {

        Log.d(LOG_TAG, "jsonString Start Api  driver = " + jsonstring);
        JSONArray array = null;

        array= new JSONArray(jsonstring);

        for (int i = 0; i < array.length(); i++) {
            JSONObject nameObject = array.getJSONObject(i);
            ItemStart item = new ItemStart();
            item.setDriver(nameObject.getString("name"));
            item.setIdDriver(nameObject.getString("id"));

            items.add(item);

        }

    }

}
