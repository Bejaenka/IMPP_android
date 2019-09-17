package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

//import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private static native String hello(final String to);
    //private static native String loadData(final String to, final String st);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.loadLibrary("rust");

        //Log.d("rust", this.hello("IMPP"));

        String r = this.hello("Martin and Bianca");

        //String r = this.loadData("yes", "https://docs.google.com/spreadsheets/d/14fNP2Elca82rryRJ8-a_XwH3_oZgrJyXqh7r7Q7GuEc/edit#gid=0");
        ((TextView)findViewById(R.id.hello)).setText(r);

    }


}
