package com.example.mybaicizhan.Http;

import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.example.mybaicizhan.XML.XMLReader;

import org.w3c.dom.Text;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyOkHttp {

    static private final String TAG = "MyOkHttp";

    static private final String ICIBA_URL1 = "http://dict-co.iciba.com/api/dictionary.php?w=";
    static private final String ICIBA_URL2 = "&key=148005F281453417C86C6C4E6BC6D65F";
    static private final String ICIBA_URL3 = "http://open.iciba.com/dsapi/";

    private OkHttpClient client;

    private String downloadXML;
    private String downloadJson;
    public String getDownloadXML(){
        return downloadXML;
    }
    public String getDownloadJson(){
        return downloadJson;
    }

    public MyOkHttp(){
        client = new OkHttpClient();
        downloadXML = null;
        downloadJson = null;
    }

    public void getWordByApiForEnglish(String word){
        try{
            String URL = ICIBA_URL1 + word + ICIBA_URL2;
            Request request = new Request.Builder().url(URL).build();
            Response response = client.newCall(request).execute();
            downloadXML = response.body().string();
        }
        catch (Exception e){
            Log.d(TAG,"error");
            e.printStackTrace();
        }
        if(downloadXML != null) {
            Log.d(TAG, downloadXML);
        }
    }

    public void getSententcePerDayByApi(){
        try{
            String URL = ICIBA_URL3;
            Request request = new Request.Builder().url(URL).build();
            Response response = client.newCall(request).execute();
            downloadJson = response.body().string();
        }
        catch (Exception e){
            Log.d(TAG,"error");
            e.printStackTrace();
        }
        if(downloadJson != null) {
            Log.d(TAG, downloadJson);
        }
    }

}
