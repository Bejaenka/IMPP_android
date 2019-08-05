package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static native String hello(final String to);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.loadLibrary("rust");


        Log.d("rust", this.hello("IMPP"));

        String r = this.hello("Martin and Bianca");
        ((TextView)findViewById(R.id.hello)).setText(r);
    }


}
