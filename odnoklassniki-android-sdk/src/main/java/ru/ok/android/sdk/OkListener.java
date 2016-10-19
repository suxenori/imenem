package ru.ok.android.sdk;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Listener methods are guaranteed to be called on the main (UI) thread.
 */
public interface OkListener {
    /**
     * Request was successful
     */
    public void onSuccess(final JSONObject json) throws JSONException, IOException;

    /**
     * Request was unsuccessful due any reason.
     */
    public void onError(String error);
}