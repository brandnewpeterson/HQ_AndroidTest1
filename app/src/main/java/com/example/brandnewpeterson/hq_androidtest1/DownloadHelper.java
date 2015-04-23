package com.example.brandnewpeterson.hq_androidtest1;

import android.content.Context;
import android.content.res.AssetManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by brandnewpeterson on 4/21/15.
 */
public class DownloadHelper {

    public String downloadUrl(String myurl) throws IOException {
            InputStream is = null;

            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                //Log.d("Testing", "The response is: " + response);
                is = conn.getInputStream();

                // Convert the InputStream into a string
                String contentAsString = convertStreamToString(is);

                //Repath for local format files
                contentAsString = contentAsString.replace("href=\"/css", "href=\"./css");
                contentAsString = contentAsString.replace("src: url('/font", "src: url('./font");


                //Log.d("Testing", "The result is: " + contentAsString);

                return contentAsString;

                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }



    public void saveOutFile(File f, String content) throws IOException {
        Writer writer = new BufferedWriter(new FileWriter(f));
        writer.write(content);
        writer.close();
    }



    public String convertStreamToString(InputStream is) {
    /*
     * To convert the InputStream to String we use the BufferedReader.readLine()
     * method. We iterate until the BufferedReader return null which means
     * there's no more data to read. Each line will appended to a StringBuilder
     * and returned as String.
     */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public JSONObject parseJSONAsset(Context c) throws JSONException {
        ArrayList<String> urlStrings = new ArrayList<String>();

        AssetManager assetManager = c.getAssets();
        InputStream ims = null;
        try {
            ims = assetManager.open("index.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String jsonString = convertStreamToString(ims);

        return new JSONObject(jsonString);
    }

    public String cleanUpUrl(String url){

        if (url.contains("{userId}")) url = url.replace("{userId}", "276");
        if (url.contains("{appSecretKey}")) url = url.replace("{appSecretKey}", "gvx32RFZLNGhmzYrfDCkb9jypTPa8Q");
        if (url.contains("{currencyCode}")) url = url.replace("{currencyCode}", "USD");
        if (url.contains("{offerId}")) url = url.replace("{offerId}", "10736598");
        if (url.contains("{selectedVouchers}")) url = url.replace("{selectedVouchers}", "[]");

        return url;
    }

}

