package com.example.android;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private static native String hello(final String to);
    private static native String getTitle(final String webcontent);
    private static native boolean buildDatabase(final String webcontent);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();
        String path = context.getFilesDir().getAbsolutePath();
        Log.d("Test", path);

        //Load rust library
        System.loadLibrary("rust");
        Log.d("Rust", this.hello("IMPP"));

        String r = this.hello("Android_4");

        // Download google sheet
        Downloader task = new Downloader();
        String result = null;

        try {
            result = task.execute("https://docs.google.com/spreadsheets/d/e/2PACX-1vTZMOCrZdhsWPB4O-YiLrfE_sR2DcU3hgHQyg1y-_R648YOP3uX9eb0-gAqJN4Re70swEOONzS5t-Yc/pubhtml").get();

            //Log.i("Content", result);

            largeLog("Content", result);


            //r = this.getTitle(result);

            boolean databaseResult =  buildDatabase(result);

            if (databaseResult == true){

                r = "TRUE";
            }

            else if (databaseResult == false){

                r = "FALSE";
            }

            else {

                r = "SHIT";
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        ((TextView)findViewById(R.id.hello)).setText(r);


    }

    // print a string by dividing it into parts of 1000 characters
    public static void largeLog(String tag, String content) {
       if (content.length() > 1000) {
           Log.d(tag, content.substring(0, 1000));
           largeLog(tag, content.substring(1000));
       } else {
           Log.d(tag, content);
       }
    }
}
