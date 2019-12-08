package com.example.android;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {


    private static native String hello(final String to);
    private static native void startQuiz();
    public String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new DownloaderSheet().execute("https://docs.google.com/spreadsheets/d/14fNP2Elca82rryRJ8-a_XwH3_oZgrJyXqh7r7Q7GuEc/edit#gid=0");



        //String out = new Scanner(new URL("http://neverssl.com").openStream(), "UTF-8").useDelimiter("\\A").next();

        //java.lang.System.out.print(out);
        //System.loadLibrary("rust");

        //Log.d("rust", this.hello("IMPP"));

        //String r = this.hello("Android_4");

        //String r = this.loadData("yes", "https://docs.google.com/spreadsheets/d/14fNP2Elca82rryRJ8-a_XwH3_oZgrJyXqh7r7Q7GuEc/edit#gid=0");
        //((TextView)findViewById(R.id.hello)).setText(r);

        //this.startQuiz();

    }


    public final class DownloaderSheet extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {

            try (Scanner scanner = new Scanner(new URL(strings[0]).openStream(),
                    StandardCharsets.UTF_8.toString()))
            {
                scanner.useDelimiter("\\A");
                return scanner.hasNext() ? scanner.next() : "";
            }

            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            result = s;
            System.out.println(s);
        }
    }
}
