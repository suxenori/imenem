package com.menemi.social_network;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.framework.media.ImagePicker;
import com.google.android.gms.common.api.GoogleApiClient;
import com.menemi.dbfactory.DBHandler;
import com.menemi.dbfactory.SQLiteEngine;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import ru.ok.android.sdk.Odnoklassniki;
import ru.ok.android.sdk.util.OkAuthType;
import ru.ok.android.sdk.util.OkScope;

/**
 * Created by tester03
 */
public class SocialNetworkHandler extends FragmentActivity
{
    public String VK_SOCIAL = "VK";
    public String OK_SOCIAL = "OK";
    public String INSTA_SOCIAL = "INSTA";
    public String G_SOCIAL = "G_PLUS";
    public String FB_SOCIAL = "FB";
    public String TARGET_SOCIAL_CONST = "target_social";
    private CallbackManager callbackManager;
    public static SocialNetworkHandler instance = null;
    private Context context;
    private String fbProfileId;
    private String photoFbCount;
    final ArrayList<Bitmap> photoArray = new ArrayList<Bitmap>();
    private String fbPhotoAlbumId;
    private ArrayList<String> photoUrlOk = new ArrayList<>();
    private ArrayList<String> photoUrlVk = new ArrayList<>();
    private ArrayList<String> photoUrlFb = new ArrayList<>();
    private ArrayList<String> photoUrlInsta = new ArrayList<>();
    private ArrayList<String> photoUrlG_plus = new ArrayList<>();
    private GoogleApiClient g_Client;
    private SocialProfile gSocialProfile;
   // private String url = "https://picasaweb.google.com/data/feed/api/user/103986297589201622409/albumid/6337941618769622081?alt=json&start-index=1&max-results=1000";
    private String getImageUrlG_plus;

    public GoogleApiClient getG_Client()
    {
        return g_Client;


    }

    public void setG_Client(GoogleApiClient client)
    {
        this.g_Client = client;
    }

    public void setPhotoFbCount(String photoFbCount)
    {
        this.photoFbCount = photoFbCount;
    }

    public ArrayList<String> getPhotoUrlOk()
    {
        return photoUrlOk;
    }

    public ArrayList<String> getPhotoUrlG_plus()
    {
        return photoUrlG_plus;
    }

    public ArrayList<String> getPhotoUrlInsta()
    {
        return photoUrlInsta;
    }

    public ArrayList<String> getPhotoUrlFb()
    {
        return photoUrlFb;
    }

    public ArrayList<String> getPhotoUrlVk()
    {
        return photoUrlVk;
    }


    public String getFbAlbumId()
    {
        return fbPhotoAlbumId;
    }

    public SocialProfile getGSocialProfile()
    {
        return gSocialProfile;
    }

    public void setgSocialProfile(SocialProfile gSocialProfile)
    {
        this.gSocialProfile = gSocialProfile;
    }

    public void setFbAlbumId(String fbAlbumId)
    {
        this.fbPhotoAlbumId = fbAlbumId;
    }

    public static final String CLIENT_ID = "139c9a2739ae458696cb03defadbf186";
    public static final String CLIENT_SECRET = "a74568ce2bad488b85709d9522680a1f";
    public static final String CALLBACK_URL = "http://localhost";

    public String APPLICATION_OK_ID = "1248077568";
    public String PUBLICK_APPLICATION_OK_KEY = "CBAICFGLEBABABABA";
    public String SECRET_APPLICATION_OK_KEY = "276DF63234E9C148431C2A58";
    public String REDIRECT_URI = "okauth://ok1248077568";
    public SocialProfile vkUserProfile = new SocialProfile();
    public SocialProfile okUserProfile = new SocialProfile();
    public String[] userScope = new String[]{
            VKScope.FRIENDS,
            VKScope.WALL,
            VKScope.PHOTOS,
            VKScope.NOHTTPS,
            VKScope.MESSAGES,
            VKScope.DOCS
    };

    protected SocialNetworkHandler()
    {

    }

    public static SocialNetworkHandler getInstance()
    {
        if (instance == null)
        {
            instance = new SocialNetworkHandler();
        }
        return instance;
    }


    public boolean isAuthFb()
    {
        if (AccessToken.getCurrentAccessToken() != null)
        {
            return true;
        }
        return false;
    }

    public void logOut()
    {

        LoginManager.getInstance().logOut();
        fbProfileId = null;

    }

    public void okAuth(Activity activity)
    {
        Odnoklassniki.createInstance(activity, APPLICATION_OK_ID, PUBLICK_APPLICATION_OK_KEY);
        Odnoklassniki.getInstance().requestAuthorization(activity, REDIRECT_URI, OkAuthType.ANY, OkScope.VALUABLE_ACCESS, OkScope.PHOTO_CONTENT, OkScope.LONG_ACCESS_TOKEN);
    }

    public void getCurrentOkUser()
    {

        new GetCurrentUserTask().execute("users.getCurrentUser");
    }


    public void getCurrentOkUserPhoto(String anchor)
    {

        new GetCurrentUserTask().execute("photos.getPhotos");
    }

    public void getUserAvatar(String request)
    {

        new downloadImage().execute(request);
    }
    public void getImageG_plus()
    {
        final InputStream[] inputStream = new InputStream[1];
        new Thread(() -> {
                URL url = null;
            try
            {
                url = new URL("https://picasaweb.google.com/data/feed/api/user/103986297589201622409?alt=json");
                inputStream[0] = url.openConnection().getInputStream();
            } catch (MalformedURLException e)
            {
                e.printStackTrace();

            } catch (IOException e)
            {
                e.printStackTrace();
            }
            try
            {
                String line = streamToString(inputStream[0]);
                try
                {
                    JSONObject jsonObj = (JSONObject) new JSONTokener(line).nextValue();
                    String albumUrl = jsonObj.getJSONObject("feed").getJSONArray("link").getJSONObject(3).getString("href");
                    jsonObj =  getAlbumUrls(albumUrl);
                    albumUrl = jsonObj.getJSONObject("feed").getJSONArray("entry").getJSONObject(0).getJSONArray("link").getJSONObject(0).getString("href");
                    jsonObj = getAlbumUrls(albumUrl + "&start-index=1&max-results=1000");
                    JSONArray arrayContent = jsonObj.getJSONObject("feed").getJSONArray("entry");
                    for (int i = 0; i < arrayContent.length(); i++)
                    {
                        photoUrlG_plus.add(arrayContent.getJSONObject(i).getJSONObject("content").getString("src"));
                    }
                    Log.d("albumUrl",photoUrlG_plus.size() + "");
                    Log.d("albumUrl",albumUrl);



                } catch (JSONException e)
                {

                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }).start();




    }
    public JSONObject getAlbumUrls(String url){

        final JSONObject[] jsonObject = {null};
        final InputStream[] inputStream = new InputStream[1];

        URL source = null;
        try
        {
            source = new URL(url);
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        try
        {
            inputStream[0] = source.openConnection().getInputStream();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        String line = null;
        try
        {
            line = streamToString(inputStream[0]);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        try
                {
                    jsonObject[0] = (JSONObject) new JSONTokener(line).nextValue();
                } catch (JSONException e)
                {

                }

        return jsonObject[0];
    }
/*
    public void getImageG_plus(){
        Log.d("getImage","called!!!!!!!!!!!!");
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    getImageUrlG_plus = "https://picasaweb.google.com/data/feed/api/user/103986297589201622409?alt=json";
                    URL url = new URL(getImageUrlG_plus);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    urlConnection.connect();
                    Stream stream = (Stream) urlConnection.getInputStream();
                    String response = streamToString((InputStream) stream);
                    try
                    {
                        JSONObject jsonObj = (JSONObject) new JSONTokener(response)
                                .nextValue();
                        Log.d("G_plus", jsonObj + "");
                        Log.d("G_plus","called");
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                } catch (java.io.IOException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }*/

    public void authFb(Activity activity)
    {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("email", "user_photos", "public_profile", "user_about_me", "user_birthday","user_friends"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(final LoginResult loginResult)
            {
                Log.d("test_fb", "Success");
                Log.d("fbProfileId", loginResult + "");
                setCurrentFbUserToSQLite();

            }

            @Override
            public void onCancel()
            {

            }

            @Override
            public void onError(FacebookException error)
            {

            }
        });
    }

    public void getPhotoFromFb(Context context, final AccessToken accessToken)
    {
        this.context = context;

        FacebookSdk.sdkInitialize(context);
        AppEventsLogger.activateApp(context);
        if (Integer.parseInt(photoFbCount) != photoUrlFb.size() && Integer.parseInt(photoFbCount) > photoUrlFb.size())
        {

                final GraphRequest request = GraphRequest.newGraphPathRequest(accessToken, "/" + getFbAlbumId() + "/photos",
                        new GraphRequest.Callback()
                        {
                            @Override
                            public void onCompleted(GraphResponse response)
                            {
                                String next_value;
                                Log.d("next_value", response + " - response");
                                try
                                {
                                    JSONArray result = response.getJSONObject().getJSONArray("data");
                                    for (int i = 0; i < result.length(); i++)
                                    {
                                        photoUrlFb.add(result.getJSONObject(i).getJSONArray("images").getJSONObject(0).getString("source"));
                                        Log.d("http_image1", result.getJSONObject(i).getJSONArray("images").getJSONObject(0).getString("source"));
                                    }
                                    Log.d("next_value", photoUrlFb.size() + " - size of array");
                                    JSONObject pagingObj = response.getJSONObject().getJSONObject("paging");
                                    next_value = pagingObj.getString("next");
                                    Log.d("next_value", next_value + " - next path");


                                } catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        });

                Bundle param = new Bundle();
                param.putString("fields", "images");
                param.putString("limit", "500");
                request.setParameters(param);
                request.executeAsync();

        }
    }

    //https://www.googleapis.com/plus/v1/people/103986297589201622409?fields=image&key=AIzaSyA6TYMX3_c33BUyqwyeqpBRZtbu86-ASI0

    public void getPhotoFromG(String apiKey, String userId){
        String request = "" + userId + " " + apiKey;
        new downloadImage().execute(request);
    }


    public void setCurrentFbUserToSQLite (){
        final SocialProfile profile = new SocialProfile();
        profile.setFirstName(Profile.getCurrentProfile().getFirstName());
        profile.setMiddleName(Profile.getCurrentProfile().getMiddleName());
        profile.setLastName(Profile.getCurrentProfile().getLastName());
        if (!photoArray.isEmpty()){
            photoArray.clear();
        }
        new downloadImage().execute(String.valueOf(Profile.getCurrentProfile().getProfilePictureUri(100,100)));
        SocialNetworkHandler.setChangeArray(new SocialNetworkHandler.ChangePhotoArray()
        {
            @Override
            public void changeArray(ArrayList<Bitmap> photoArray)
            {
                profile.setImage(photoArray.get(0));
                if (DBHandler.getInstance().isEmtyTable(SQLiteEngine.TABLE_SOCIAL_FB)){
                    DBHandler.getInstance().setSocialFB(profile,"FB");
                }

                Log.d("method","method is called");
            }
        });

    }

    public void getProfileAlbumId(final Context context, AccessToken accessToken)
    {
        final GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback()
                {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response)
                    {
                        JSONObject jsonObject;
                        try
                        {
                            Log.d("response", response + "");
                            jsonObject = new JSONObject(String.valueOf(object)).getJSONObject("albums");
                            JSONArray array = jsonObject.getJSONArray("data");
                            JSONObject result = array.getJSONObject(0);
                            String id = result.getString("id");
                            setFbAlbumId(id);
                            String photoCount = result.getString("photo_count");
                            setPhotoFbCount(photoCount);
                            Log.d("photoCount", photoCount + " - from Fb");
                            Log.d("AlbumIdFb", id + " - album id from Fb");
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                            setCurrentFbUserToSQLite();
                            getPhotoFromFb(context,AccessToken.getCurrentAccessToken());

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "albums{id,photo_count}");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void updateUI(ImageView view, TextView textView, Drawable icon, String status){
        view.setImageDrawable(icon);
        textView.setText(status);
    }

    public void getCurrentVkUser()
    {
        VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "photo_100"));
        request.executeWithListener(new VKRequest.VKRequestListener()
        {
            @Override
            public void onComplete(VKResponse response) {
                try
                {
                    super.onComplete(response);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

                try
                {
                    JSONArray array = response.json.getJSONArray("response");
                    for (int i = 0; i < array.length(); i++)
                    {
                        JSONObject object = array.getJSONObject(i);
                        Log.d("response", response.json + " - value");
                        vkUserProfile.setFirstName((object.getString("first_name")));
                        vkUserProfile.setLastName((object.getString("last_name")));
                        vkUserProfile.setId((object.getString("id")));
                        Log.d("idvk", vkUserProfile.getId());
                        Log.d("idvk", vkUserProfile.getFirstName());
                        Log.d("idvk", vkUserProfile.getLastName());
                        Log.d("idvk", object.getString("photo_100"));
                        vkUserProfile.setPhotoUrl((object.getString("photo_100")));
                        if (!photoArray.isEmpty()){
                            photoArray.clear();
                        }
                        new downloadImage().execute((object.getString("photo_100")));
                        setChangeArray(new ChangePhotoArray()
                        {
                            @Override
                            public void changeArray(ArrayList<Bitmap> photoArray)
                            {

                                vkUserProfile.setImage(photoArray.get(0));
                                DBHandler.getInstance().setSocialVK(vkUserProfile,"VK");
                                Log.d("photo_array",photoArray.size() + "");
                            }
                        });

                        Log.d("idvk", vkUserProfile.getPhotoUrl());

                    }


                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
                Log.d("VkPerson", response + " - success");
            }

            @Override
            public void onError(VKError error)
            {
                super.onError(error);
                Log.d("VkPerson", error + " - error");
            }
        });

    }

    public void authVk(Activity activity)
    {
        VKSdk.login(activity, userScope);
    }

    public void getUserPhotoFromVk(String offset,String count)
    {
        ImagePicker picker = new ImagePicker();
        picker.onPickImage(new MediaMetadata(),5);
        final String firstCount = count;

        VKRequest request = new VKRequest("photos.getAll", VKParameters.from(VKApiConst.PHOTO_SIZES, "1",VKApiConst.OFFSET,offset, VKApiConst.COUNT, count));
        request.executeWithListener(new VKRequest.VKRequestListener()
        {
            @Override
            public void onComplete(VKResponse response) throws JSONException
            {
               
                    super.onComplete(response);

                Log.d("vk_response", String.valueOf(response.json));
                try
                {
                    JSONObject object = response.json.getJSONObject("response");
                    JSONArray items = object.getJSONArray("items");
                    String count = object.getString("count");
                    Log.d("vk_count", count);
                    Log.d("src", response + "");
                    Log.d("src", items.length() + " - items.length");
                    for (int i = 0; i < items.length(); i++)
                    {
                        JSONObject item = items.getJSONObject(i);
                        Log.d("src", item.length() + " one item");
                        JSONArray sizes = item.getJSONArray("sizes");
                        Log.d("src", sizes.length() + " quality count");
                        JSONObject bestQuality = sizes.getJSONObject(sizes.length() - 1);
                        photoUrlVk.add(bestQuality.getString("src"));
                    }
                    if (photoUrlVk.size() == Integer.parseInt(count)){
                        do{
                            getUserPhotoFromVk(firstCount,String.valueOf(200));
                        } while (photoUrlVk.size() < Integer.parseInt(count));

                    }
                    Log.d("items", items.length() + " ");
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        });
    }

    public void setFbProfileId(String fbProfileId)
    {
        this.fbProfileId = fbProfileId;
    }

    public String getFbProfileId()
    {
        return fbProfileId;
    }


    public class downloadImage extends AsyncTask<String, Void, Bitmap>
    {

        private Exception exception;

        protected Bitmap doInBackground(String... urls)
        {
            try
            {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e)
            {
                this.exception = e;

                return null;
            }
        }

        protected void onPostExecute(Bitmap b)
        {
            if (!photoArray.isEmpty())
            {
                photoArray.clear();
            }
            photoArray.add(b);

            changeArrayListener.changeArray(photoArray);
        }
    }

    protected final class GetCurrentUserTask extends AsyncTask<String, Void, String>
    {
        String param;
        @Override
        protected String doInBackground(final String... params)
        {

           // String anchor = params[0];
            Map<String, String> args = new HashMap<>();
           // Log.d("anchor111",anchor + " 111111");
           /* if (anchor != null){

                args.put("anchor",anchor);
            }*/
            args.put("fields", "photo.pic640x480");
            args.put("detectTotalCount","TRUE");
            try
            {
                param = params[0];

                if (param.equals("photos.getPhotos")){

                    return Odnoklassniki.getInstance().request(params[0],args,"get");

                } else if (param.equals("users.getCurrentUser")){

                    return Odnoklassniki.getInstance().request(params[0],null,"get");
                }

            } catch (Exception exc)
            {
                Log.e("Odnoklassniki", "Failed to get current user info", exc);
            }
            return null;
        }

        @Override
        protected void onPostExecute(final String result)
        {
            if (result != null)
            {
                if (param.equals("users.getCurrentUser")){
                    try
                    {
                        JSONObject object = new JSONObject(result);
                        okUserProfile.setId(object.getString("uid"));
                        okUserProfile.setFirstName(object.getString("first_name"));
                        Log.d("ok_user",(object.getString("first_name") + " " + object.getString("last_name")));
                        okUserProfile.setLastName(object.getString("last_name"));
                        okUserProfile.setPhotoUrl(object.getString("pic_2"));
                        new downloadImage().execute(object.getString("pic_2"));
                        setChangeArray(new ChangePhotoArray()
                        {
                            @Override
                            public void changeArray(ArrayList<Bitmap> photoArray)
                            {
                                okUserProfile.setImage(photoArray.get(0));
                                DBHandler.getInstance().setSocialOK(okUserProfile,"OK");
                            }
                        });
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                } else if (param.equals("photos.getPhotos")){
                    try
                    {
                           JSONObject object = new JSONObject(result);
                           Log.d("ok_result",result);
                           JSONArray array = object.getJSONArray("photos");
                           String hasMore = object.getString("hasMore");
                           String anchor = object.getString("anchor");
                           Log.d("anchor",anchor + "");
                           for (int i = 0; i < array.length(); i++)
                           {
                               photoUrlOk.add(array.getJSONObject(i).getString("pic640x480"));
                           }
                           Log.d("photoUrlOk", photoUrlOk.size() + "");

                        if (anchor != null && hasMore.equals("true")){

                            getCurrentOkUserPhoto(anchor);
                        }

                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }

                Log.d("resultOk", result);
            }
        }
    }

    private static ChangePhotoArray changeArrayListener = new ChangePhotoArray()
    {
        @Override
        public void changeArray(ArrayList<Bitmap> photoArray)
        {

        }
    };
    public interface ChangePhotoArray
    {
        void changeArray(ArrayList<Bitmap> photoArray);
    }

    public static void setChangeArray(ChangePhotoArray choiseListener)
    {
        SocialNetworkHandler.changeArrayListener = choiseListener;
    }

    private String streamToString(InputStream is) throws IOException
    {
        String str = "";

        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line = null;

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


}