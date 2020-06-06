package com.impp.grow;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Downloader extends AsyncTask <String, Void, String>{

    private static native int importfromGoogleSheet(final String sheet);
    private ProgressDialog dialog;
    private MainActivity mainActivity;

    public Downloader(MainActivity activity) {

        this.mainActivity = activity;
    }

    @Override
    protected void onPreExecute() {
        //Toast.makeText(mainActivity.getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onPostExecute(String result) {
        // do UI work here

    }

    @Override
    protected String doInBackground(String... urls) {
        String result;
        URL url;
        HttpURLConnection urlConnection;

        try {
            url = new URL(urls[0]);
            urlConnection = (HttpURLConnection) url.openConnection();

            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                result = response.toString();

                //int i = importfromGoogleSheet(result);


                return result;
            }

            Log.d("URL-FAIL", "Connection not working");


        } catch (MalformedURLException e) {

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}


