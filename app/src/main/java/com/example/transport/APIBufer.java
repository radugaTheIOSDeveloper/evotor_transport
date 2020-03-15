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

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class APIBufer {

    public static final String LOG_TAG = "apic";

    String  jsonsss;
    public String getJSONString (String UrlSpec) throws IOException {
//
//        FormBody body = new FormBody.Builder()
//                .add("",jsonsss)
//                .build();


        MediaType JSON = MediaType.parse("application/json");

        RequestBody bodys = RequestBody.create(JSON, jsonsss);


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
                .post(bodys)
                .build();




        Response response = client.newCall(request).execute();

        int responseCode = response.code();
        Log.d(LOG_TAG, "responce code = " + responseCode);


        String result = response.body().string();
        return result;

    }


    public List<Item> itemsStart(String jsonStr){

        List<Item> itemmList = new ArrayList<>();

        jsonsss = jsonStr;


        try {
            String url = Uri.parse("https://avto.infosaver.ru/api/v0/stat/")
                    .buildUpon()
                    .appendQueryParameter("format", "json")
                    .build().toString();



            String jsonString = getJSONString(url);
            parseItems(itemmList, jsonString);



        } catch (IOException ioe){
            Log.e(LOG_TAG, "Ошибка загрузки данных", ioe);

        }catch (JSONException joe){
            Log.e(LOG_TAG, "Ошибка парсинга JSON", joe);
        }
        return itemmList;


    }

    //TODO api and adapter

    private void parseItems (List<Item> items , String jsonstring) throws IOException, JSONException {

        Log.d("LOG_TAG", "jsonString Start Api = " + jsonstring);


    }
}
