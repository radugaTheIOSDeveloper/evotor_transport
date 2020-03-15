package com.example.transport;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class API {

    String cacheJscn;

    public static final String LOG_TAG = "com.example.transportapp";

    public String getJSONString (String UrlSpec) throws IOException {

        int cacheSize = 1 * 1024 * 1024; // 10MB


        //Cache cache = new Cache(c.getCacheDir(), cacheSize);
      //  Context c = MyGalery.getContext();
    //    Cache cache = new Cache(new File(c.getExternalCacheDir(),"cacheFileName"), cacheSize);
        OkHttpClient client = new OkHttpClient.Builder()
                .followSslRedirects(false)
                .followRedirects(false)
             //   .cache(cache)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .build();

        String result = null;

        try {

            Request request = new Request.Builder()
                    .url(UrlSpec)
                //       .cacheControl(CacheControl.FORCE_CACHE)
               //     .cacheControl(new CacheControl.Builder().onlyIfCached().build())
           //         .addHeader("Authorization", "Bearer " + "49936d14fd59419310dfa27028e242dfdf80e5f0")
            .addHeader("Authorization", "Token " + "api_token")
            .build();

            Response response = client.newCall(request).execute();
            result = response.body().string();



        } catch (IOException ioe){
            Log.e(LOG_TAG, "Ошибка загрузки данных  туточки" + cacheJscn);
            result = cacheJscn;

        }


        return result;

    }



    public List<Item> items(String jsonCache, String _idRout){

        List<Item> itemmList = new ArrayList<>();

        cacheJscn = jsonCache;
        Log.d(LOG_TAG, jsonCache +  " json cahse + id rout" + _idRout);
        String _url = null;



        if (_idRout.equals("false")){
        _url = "https://avto.infosaver.ru/api/v0/payButtons/";
        }else {

            _url = "https://avto.infosaver.ru/api/v0/payButtons/?route_id=" + _idRout;
        }

        try {
            String url = Uri.parse(_url)
                    .buildUpon()
                    .appendQueryParameter("format", "json")
                    .build().toString();


            String jsonString = getJSONString(url);


            Log.e(LOG_TAG, "fuf");

            parseItems(itemmList, jsonString);


        } catch (IOException ioe){
            Log.e(LOG_TAG, "Ошибка загрузки данных", ioe);

        }catch (JSONException joe){
            Log.e(LOG_TAG, "Ошибка парсинга JSON", joe);
        }
        return itemmList;


    }

    private void parseItems (List<Item> items , String jsonstring) throws IOException, JSONException {

        Log.d(LOG_TAG, "json float  = " + jsonstring);

        JSONArray array = null;

        if (jsonstring == null || jsonstring.equals("")){

        }else {
            array= new JSONArray(jsonstring);

            for (int i = 0; i < array.length(); i++) {
                JSONObject nameObject = array.getJSONObject(i);
                Item item = new Item();
                item.setName(nameObject.getString("name"));
                item.setSum((float) nameObject.getDouble("coast"));
                Log.d(LOG_TAG, "float =" + item.getSum());
                item.setType(nameObject.getInt("pay_type"));
                item.setId(nameObject.getString("id"));
                item.setHexColor(nameObject.getString("color"));
                item.setNds(nameObject.getInt("nds"));
                item.setJsonString(jsonstring);

                items.add(item);

            }
        }





    }



}
