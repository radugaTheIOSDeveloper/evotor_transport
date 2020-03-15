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

import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class APICost {

    String cacheJscn;
    String rout_id;

    public static final String LOG_TAG = "com.example.transportapp";

    public String getJSONString (String UrlSpec) throws IOException {

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
                    //   .cacheControl(CacheControl.FORCE_CACHE)
                  //  .cacheControl(new CacheControl.Builder().onlyIfCached().build())
                 //   .addHeader("Authorization", "Bearer " + "49936d14fd59419310dfa27028e242dfdf80e5f0")
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



    public List<ItemCost> itemCosts(String strJson ,String _idRout){

        cacheJscn = strJson;
        rout_id = _idRout;
        List<ItemCost> itemCosts = new ArrayList<>();

        try {
            String url = Uri.parse("https://avto.infosaver.ru/api/v0/coast/?route_id="+ rout_id)
                    .buildUpon()
                    .appendQueryParameter("format", "json")
                    .build().toString();


            String jsonString = getJSONString(url);
            //JSONObject jsonBody = new JSONObject(jsonString);

            parseItems(itemCosts, jsonString);


        } catch (IOException ioe){
            Log.e(LOG_TAG, "Ошибка загрузки данных", ioe);

        }catch (JSONException joe){
            Log.e(LOG_TAG, "Ошибка парсинга JSON", joe);
        }
        return itemCosts;


    }

    private void parseItems (List<ItemCost> itemCosts , String jsonString) throws IOException, JSONException {

        Log.d(LOG_TAG, "json cost = " + jsonString);
        JSONArray array = null;

        if (jsonString == null || jsonString.equals("")){


        }else {

            ItemCost item = new ItemCost();
            item.setCost(jsonString);
            itemCosts.add(item);
        }



    }



}
