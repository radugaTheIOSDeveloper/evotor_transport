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
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class APIRout {

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
                 //      .cacheControl(CacheControl.FORCE_CACHE)
                //    .cacheControl(new CacheControl.Builder().onlyIfCached().build())
                    //.addHeader("Authorization", "Bearer " + "49936d14fd59419310dfa27028e242dfdf80e5f0")
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



    public List<ItemRout> itemRouts(String _cacheJscn,String _idRout){

        cacheJscn = _cacheJscn;
        rout_id = _idRout;
        List<ItemRout> itemRouts = new ArrayList<>();

        try {
            String url = Uri.parse("https://avto.infosaver.ru/api/v0/stop-points/?route_id=" + rout_id)
                    .buildUpon()
                    .appendQueryParameter("format", "json")
                    .build().toString();


            String jsonString = getJSONString(url);


            Log.e(LOG_TAG, "fuf");

            parseItems(itemRouts, jsonString);


        } catch (IOException ioe){
            Log.e(LOG_TAG, "Ошибка загрузки данных", ioe);

        }catch (JSONException joe){
            Log.e(LOG_TAG, "Ошибка парсинга JSON", joe);
        }
        return itemRouts;


    }

    private void parseItems (List<ItemRout> itemRouts , String jsonstring) throws IOException, JSONException {

        Log.d(LOG_TAG, "json = " + jsonstring);

        JSONArray array = null;

        if (jsonstring == null || jsonstring.equals("")){

        }else {
            array= new JSONArray(jsonstring);

            for (int i = 0; i < array.length(); i++) {
                JSONObject nameObject = array.getJSONObject(i);
                ItemRout item = new ItemRout();
                item.setName(nameObject.getString("name"));
                item.setId(nameObject.getString("id"));
                item.setJsonString(jsonstring);

                itemRouts.add(item);

            }
        }
//




    }




}
