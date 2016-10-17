package com.menemi.social_network.instagram;

/**
 * Created by tester03 on 07.09.2016.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.menemi.dbfactory.DBHandler;
import com.menemi.social_network.SocialNetworkHandler;
import com.menemi.social_network.SocialProfile;
import com.menemi.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class InstagramApp {

    private static InstagramSession mSession;
    private InstagramDialog mDialog;
    private OAuthAuthenticationListener mListener;
    private ProgressDialog mProgress;
    private String mAuthUrl;
    private String mTokenUrl;
    private String mAccessToken;
    private Context mCtx;

    private String mClientId;
    private String mClientSecret;
    private String photo_url;
    private Bitmap image;
    private static int WHAT_FINALIZE = 0;
    private static int WHAT_ERROR = 1;
    private static int WHAT_FETCH_INFO = 2;
    public static String mCallbackUrl = "";
    private static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/";
    private static final String TOKEN_URL = "https://api.instagram.com/oauth/access_token";
    private static final String API_URL = "https://api.instagram.com/v1";
    private static final String TAG = "InstagramAPI";

    public InstagramApp(Context context, String clientId, String clientSecret,
                        String callbackUrl) {

        mClientId = clientId;
        mClientSecret = clientSecret;
        mCtx = context;
        mSession = new InstagramSession(context);
        mAccessToken = mSession.getAccessToken();
        mCallbackUrl = callbackUrl;
        mTokenUrl = TOKEN_URL + "?client_id=" + clientId + "&client_secret="
                + clientSecret + "&redirect_uri=" + mCallbackUrl
                + "&grant_type=authorization_code";
        mAuthUrl = AUTH_URL
                + "?client_id="
                + clientId
                + "&redirect_uri="
                + mCallbackUrl
                + "&response_type=code&display=touch&scope=public_content";
        photo_url = API_URL +"/users/"+ getId() +
                "/media/recent?count=33&access_token=" + getAccessToken();

        InstagramDialog.OAuthDialogListener listener = new InstagramDialog.OAuthDialogListener() {
            @Override
            public void onComplete(String code) {
                getAccessToken(code);
            }

            @Override
            public void onError(String error) {
                mListener.onFail("Authorization failed");
            }
        };

        mDialog = new InstagramDialog(context, mAuthUrl, listener);
        mProgress = new ProgressDialog(context);
        mProgress.setCancelable(false);
    }

    private void getAccessToken(final String code) {
        mProgress.setMessage("Getting access token ...");
        mProgress.show();

        new Thread() {
            @Override
            public void run() {
                Log.i(TAG, "Getting access token");
                int what = WHAT_FETCH_INFO;
                try {
                    URL url = new URL(TOKEN_URL);
                    Log.i(TAG, "Opening Token URL " + url.toString());
                    HttpURLConnection urlConnection = (HttpURLConnection) url
                            .openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    OutputStreamWriter writer = new OutputStreamWriter(
                            urlConnection.getOutputStream());
                    writer.write("client_id=" + mClientId + "&client_secret="
                            + mClientSecret + "&grant_type=authorization_code"
                            + "&redirect_uri=" + mCallbackUrl + "&code=" + code);
                    writer.flush();
                    String response = streamToString(urlConnection
                            .getInputStream());
                    Log.i(TAG, "response " + response);
                    JSONObject jsonObj = (JSONObject) new JSONTokener(response)
                            .nextValue();

                    mAccessToken = jsonObj.getString("access_token");
                    String id = jsonObj.getJSONObject("user").getString("id");
                    String user = jsonObj.getJSONObject("user").getString(
                            "username");
                    String name = jsonObj.getJSONObject("user").getString(
                            "full_name");
                    String userImage = jsonObj.getJSONObject("user").getString(
                            "profile_picture");

                    image =  Utils.getImageFromURL(userImage);
                    mSession.storeAccessToken(mAccessToken, id, user, name,
                            userImage);
                    SocialNetworkHandler.getInstance().getPhotoUrlInsta().clear();
                    getPhoto(mAccessToken,id);
                    Log.d("method_getPhoto"," до сих пор все методы вызваны");
                    setCurrentInstaUserToSQLite();


                } catch (Exception ex) {
                    what = WHAT_ERROR;
                    ex.printStackTrace();
                }

                mHandler.sendMessage(mHandler.obtainMessage(what, 1, 0));
            }
        }.start();
    }

    private void fetchUserName() {
        mProgress.setMessage("Finalizing ...");

        new Thread() {
            @Override
            public void run() {
                Log.i(TAG, "Fetching user info");
                int what = WHAT_FINALIZE;
                try {
                    URL url = new URL(API_URL + "/users/" + mSession.getId()
                            + "/?access_token=" + mAccessToken);
                    Log.d(TAG, "Opening URL " + url.toString());
                    HttpURLConnection urlConnection = (HttpURLConnection) url
                            .openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    urlConnection.connect();
                    String response = streamToString(urlConnection
                            .getInputStream());
                    System.out.println(response);
                    JSONObject jsonObj = (JSONObject) new JSONTokener(response)
                            .nextValue();
                    String name = jsonObj.getJSONObject("data").getString(
                            "full_name");
                    // String bio =
                    // jsonObj.getJSONObject("data").getString("bio");
                    Log.i(TAG, "Got name: " + name);
                } catch (Exception ex) {
                    what = WHAT_ERROR;
                    ex.printStackTrace();
                }

                mHandler.sendMessage(mHandler.obtainMessage(what, 2, 0));
            }
        }.start();

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WHAT_ERROR) {
                mProgress.dismiss();
                if (msg.arg1 == 1) {
                    mListener.onFail("Failed to get access token");
                } else if (msg.arg1 == 2) {
                    mListener.onFail("Failed to get user information");
                }
            } else if (msg.what == WHAT_FETCH_INFO) {
                mProgress.dismiss();
                mListener.onSuccess();
                // fetchUserName();
            } else {
                // mProgress.dismiss();
                // mListener.onSuccess();
            }
        }
    };

    public boolean hasAccessToken() {
        return (mAccessToken == null) ? false : true;
    }

    public void setCurrentInstaUserToSQLite (){
        final SocialProfile profile = new SocialProfile();
        profile.setFirstName(getName());
        profile.setImage(image);
        DBHandler.getInstance().setSocialINSTA(profile,"INSTA");
    }

    public void setListener(OAuthAuthenticationListener listener) {
        mListener = listener;
    }

    // getting username
    public String getUserName() {
        return mSession.getUsername();
    }

    // getting user id
    public String getId() {
        return mSession.getId();
    }

    public Bitmap getImage()
    {
        return image;
    }

    public void setImage(Bitmap image)
    {
        this.image = image;
    }

    // getting username
    public String getName() {
        return mSession.getName();
    }

    // getting user image
    public String getUserPicture() {
        return mSession.getUserImage();
    }

    // getting accesstoken
    public String getAccessToken() {
        return mSession.getAccessToken();
    }

    public void authorize() {
        if (mSession.getAccessToken() == null){
            mDialog.show();

        } else {
            Toast.makeText(mCtx, "Вы уже авторизовались в Instagram", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean isLogged(){
        if (mSession.getAccessToken() == null){
            return false;
        }
        return true;
    }

    private String streamToString(InputStream is) throws IOException {
        String str = "";

        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is));

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                reader.close();
            } finally {
                is.close();
            }

            str = sb.toString();
        }

        return str;
    }

    public void getPhoto()
    {
        new Thread()
        {
            @Override
            public void run()
            {

                InputStream inputStream = null;
                String response = null;
                try
                {
                    inputStream = new URL(photo_url).openConnection().getInputStream();

                } catch (IOException e1)
                {
                    e1.printStackTrace();
                }
                try
                {
                    response = streamToString(inputStream);
                } catch (IOException e1)
                {
                    e1.printStackTrace();
                }
                JSONObject jsonObject;
                try
                {
                    jsonObject = (JSONObject)new JSONTokener(response).nextValue();
                    Log.d("insta_response",response + " - instagramm response");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i).getJSONObject("images").
                                getJSONObject("standard_resolution");
                        SocialNetworkHandler.getInstance().getPhotoUrlInsta().add(object.getString("url"));
                    }
                    Log.d("urlArray",SocialNetworkHandler.getInstance().getPhotoUrlInsta().size() + "");
                } catch (JSONException e1)
                {
                    e1.printStackTrace();
                }
            }
        }.start();
    }

    public void getPhoto(String mAccessToken, String userId)
    {

                InputStream inputStream = null;
                String response = null;
                try
                {
                    inputStream = new URL(API_URL +"/users/"+ userId +
                            "/media/recent?count=33&access_token=" + mAccessToken).openConnection().getInputStream();

                } catch (IOException e1)
                {
                    e1.printStackTrace();
                }
                try
                {
                    response = streamToString(inputStream);
                } catch (IOException e1)
                {
                    e1.printStackTrace();
                }
                JSONObject jsonObject;
                try
                {
                    jsonObject = (JSONObject)new JSONTokener(response).nextValue();
                    Log.d("insta_response",response + " - instagram response");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i).getJSONObject("images").
                                getJSONObject("standard_resolution");
                        SocialNetworkHandler.getInstance().getPhotoUrlInsta().add(object.getString("url"));
                    }

                    Log.d("urlArray",SocialNetworkHandler.getInstance().getPhotoUrlInsta().size() + "");
                } catch (JSONException e1)
                {
                    e1.printStackTrace();
                }
            }




    public void resetAccessToken() {
        if (mAccessToken != null) {
            mSession.resetAccessToken();
            mAccessToken = null;
        }
    }

    public interface OAuthAuthenticationListener {
        public abstract void onSuccess();

        public abstract void onFail(String error);
    }
}