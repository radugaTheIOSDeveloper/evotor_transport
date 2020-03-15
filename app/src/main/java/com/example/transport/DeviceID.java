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

public class DeviceID {

    public static final String LOG_TAG = "START_API";

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
            //    .addHeader("Authorization", "Bearer " + "49936d14fd59419310dfa27028e242dfdf80e5f0")
                .addHeader("Authorization", "Token " + "api_token")
                .build();

        Response response = client.newCall(request).execute();
        String result = response.body().string();

        return result;

    }


    public List<ItemStart> itemsStart(){

        List<ItemStart> itemmList = new ArrayList<>();


        String _url = null;


        try {
            String url = Uri.parse("https://avto.infosaver.ru/api/v0/evotor-device-id/")
                    .buildUpon()
                    .appendQueryParameter("format", "json")
                    .build().toString();



            String jsonString = getJSONString(url);
            JSONObject jsonBody = new JSONObject(jsonString);

            parseItems(itemmList, jsonBody);




        } catch (IOException ioe){
            Log.e(LOG_TAG, "Ошибка загрузки данных", ioe);

        }catch (JSONException joe){
            Log.e(LOG_TAG, "Ошибка парсинга JSON", joe);
        }
        return itemmList;


    }

    //TODO api and adapter

    private void parseItems (List<ItemStart> items , JSONObject jsonBody) throws IOException, JSONException {

        Log.d(LOG_TAG, "jsonString rout api = " + jsonBody);


//        ItemStart item = new ItemStart();
//            item.setDeviceID(jsonstring);
//
//        items.add(item);

//        JSONArray jsonArray = new JSONArray(jsonstring);
//
//        for (int i = 0; i < jsonArray.length(); i++) {
//            JSONObject nameObject = jsonArray.getJSONObject(i);
//            ItemStart item = new ItemStart();
//            item.setDeviceID(nameObject.getString("device_id"));
//
//
//            items.add(item);
//
//        }



        ItemStart item = new ItemStart();

        if (jsonBody.has("device_id")){
            item.setDeviceID(jsonBody.getString("device_id"));
            items.add(item);


        }




    }


}
