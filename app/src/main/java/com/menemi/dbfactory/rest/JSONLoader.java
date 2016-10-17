package com.menemi.dbfactory.rest;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kostya on 04.05.2016.
 */
public abstract class JSONLoader extends AsyncTask<Void, Void, String> {
    protected static int requestNumber = 0;
    private final String dataSource;

    HttpURLConnection urlConnection = null;
    //Listener to be called after all data is loaded
    private OnLoadFinishListener onLoadFinishListener = new OnLoadFinishListener() {
        @Override
        public void onFinish(Object object) {
            Log.w("OnLoadFinishListener", "JSONLoader: Custom listener is not defined");
        }
    };


    /**
     * @param dataURL              link to the JSON file.
     * @param onLoadFinishListener implement this interface to get results in place
     *                             where you create object of this class
     */
    public JSONLoader(@NonNull String dataURL, @NonNull OnLoadFinishListener onLoadFinishListener) {

        this.onLoadFinishListener = onLoadFinishListener;
        this.dataSource = dataURL;
        requestNumber++;
        Log.v("JSONLoader", requestNumber + "CONSTRUCTOR URL = " + dataURL);
    }

    public JSONLoader(@NonNull HttpURLConnection urlConnection, @NonNull OnLoadFinishListener onLoadFinishListener) {

        this.onLoadFinishListener = onLoadFinishListener;
        this.urlConnection = urlConnection;
        this.dataSource = urlConnection.getURL().toString();

    }

    public static String readString(HttpURLConnection urlConnection) throws IOException {
        //Preparing to read data
        StringBuffer buffer = new StringBuffer();
        try {
            System.gc();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            //reading JSON
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
            System.gc();
            return readString(urlConnection);
        }
        return buffer.toString();
    }

    private HttpURLConnection connect() throws IOException {
        HttpURLConnection urlConnection = null;
        Log.v("Loader", "start loader");
        //Preparing URL
        URL url = new URL(dataSource);

        //connecting to Server
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");

        return urlConnection;
    }

    @Override
    protected String doInBackground(Void... params) {
        String resultJson = "";


        try {
            if (urlConnection == null) {
                urlConnection = connect();
                Log.d("HttpURLConnection", "connected");
            }


            resultJson = readString(urlConnection);
            //Log.v("GET", "" + urlConnection.getResponseCode());
        } catch (IOException ioe) {
            Log.d("JSONloader", "IOException");
            ioe.printStackTrace();
            return "{ \"result\" : \"failed\" }";
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.v("JSONloader", resultJson);
        return resultJson;
    }

    @Override
    protected void onPostExecute(String strJson) {
        super.onPostExecute(strJson);
        //call for Owerided method in ancestor
        parcing(strJson);

    }

    public OnLoadFinishListener getOnLoadFinishListener() {
        return onLoadFinishListener;
    }

    /**
     * Owerride this method in ancestor and parce String obtained from server
     *
     * @param jsonString string with JSON code from server
     */
    abstract protected void parcing(String jsonString);


    public interface OnLoadFinishListener {
        void onFinish(Object object);
    }

}
