package com.menemi.dbfactory.rest;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by irondev on 10.06.16.
 */
abstract class JSONSender extends AsyncTask<Void, Void, String> {
    String urlString = null;
    private OnUploadFinishListener onUploadFinishListener = new OnUploadFinishListener() {

        @Override
        public void onUploadFinish(String s) {
            Log.e("Listener", "I is called, but appear to be empty, data is " + s);
        }
    };

    public JSONSender(String urlString, OnUploadFinishListener onUploadFinishListener) {
        this.urlString = urlString;
        this.onUploadFinishListener = onUploadFinishListener;


    }

    @Override
    protected String doInBackground(Void... params) {
        Log.v("Rest POST", " in background");
        String resultString = "";
        try {
            URL url = new URL(urlString);


            HttpURLConnection urlConnection = null;

                urlConnection = (HttpURLConnection) url.openConnection();
                //Log.v("Rest POST", "response: " + urlConnection.getResponseMessage());

                urlConnection.setDoOutput(true);
                urlConnection.setChunkedStreamingMode(0);
                //Log.v("Rest POST", "response: " + urlConnection.getResponseCode());
                urlConnection.setRequestProperty("Content-Type", "application/json");
                    OutputStream out = null;

                        out = new BufferedOutputStream(urlConnection.getOutputStream());
                        out.write(parcing().getBytes(Charset.forName("UTF-8")));


                            out.flush();

                            Log.v("Rest POST", "response: " + urlConnection.getResponseCode() + " msg:" + urlConnection.getResponseMessage());
                            resultString = Loader.readString(urlConnection);




            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        return resultString;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.v("response body", "" + s);
        onUploadFinishListener.onUploadFinish(s);
    }

    /**
     * Owerride this method in ancestor and parce String obtained from server
     */
    abstract protected String parcing() throws JSONException;

    public interface OnUploadFinishListener {
        void onUploadFinish(String s);
    }
}
