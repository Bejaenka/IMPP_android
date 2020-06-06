package com.impp.grow;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DatabaseCreator extends AsyncTask <String, Void, String>{

    private Activity activity;

    public DatabaseCreator(Activity activity) {
        this.activity = activity;
    }


    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(String result) {
        // do UI work here
    }

    @Override
    protected String doInBackground(String... googleSheetContent) {
        String result;
        int questionNumber = BackendInterface.importfromGoogleSheet(googleSheetContent[0]);

        if(questionNumber > 0){
            return "ok";
        }
        else {
            return "error";
        }

    }

    private boolean create(Context context, String fileName, String jsonString){
        String FILENAME = "database.json";
        try {
            FileOutputStream fos = context.openFileOutput(fileName,Context.MODE_PRIVATE);
            if (jsonString != null) {
                fos.write(jsonString.getBytes());
            }
            fos.close();
            return true;
        } catch (FileNotFoundException fileNotFound) {
            return false;
        } catch (IOException ioException) {
            return false;
        }

    }

    public boolean isFilePresent(Context context, String fileName) {
        String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File(path);
        return file.exists();
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


