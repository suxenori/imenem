package com.menemi.utils;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.menemi.R;
import com.menemi.customviews.OCDialog;
import com.menemi.dbfactory.DBHandler;
import com.menemi.dbfactory.Fields;
import com.menemi.personobject.Interests;
import com.menemi.personobject.PayPlan;
import com.menemi.personobject.PersonObject;

import org.jetbrains.annotations.Contract;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * Created by irondev on 14.06.16.
 */
public class Utils {
    public static final String PICTURE_QUALITY_THUMBNAIL = "thumb";
    public static final String PICTURE_QUALITY_MEDIUM = "medium";
    public static final String PICTURE_QUALITY_LARGE = "large";
    public enum UNITS{METRIC, IMPERIAL}
    private static final Pattern emailPattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");

    public static String strSeparator = "_,_";

    public static int boolToInt(boolean value) {
        return value ? 1 : 0;
    }

    public static boolean intToBool(int value) {
        return value != 0;
    }

    public static java.sql.Date getDateFromString(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;

        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        return sqlDate;
    }

    public static java.sql.Date getDateFromString1(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        Date date = null;

        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        return sqlDate;
    }


    public static java.sql.Date getDateFromServer(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));////2016-07-07T00:00:00.000Z
        Date date = null;

        try {
            if(dateString != null) {
                date = format.parse(dateString);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        java.sql.Date sqlDate = null;
        if(date != null) {
        sqlDate = new java.sql.Date(date.getTime());
        }
        return sqlDate;
    }

    public static String getStringFromDate(Date date) {
        if(date == null){
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }




    public static String getStringTimeFromDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }

    public static String getStringDateForDB(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ");
        return format.format(date);
    }
    public static java.sql.Date getDateFromServer2(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));  // 2016-08-12T16:44:24.640+00:00"
        Date date = null;

        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        return sqlDate;
    }


    public static String getStringForUrl(double value) {
        String result = "" + value;
        result = result.replace('.', ',');
        Log.v("Utils", "was " + value + " become " + result);
        return result;
    }


    public static ArrayList<Integer> extractIds(ArrayList<PersonObject> personObjects) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (int i = 0; i < personObjects.size(); i++) {
            ids.add(personObjects.get(i).getPersonId());

        }
        return ids;

    }

    public static Bitmap getBitmapFromStringBase64(String bitmapString) {
        if(bitmapString == null){
            return null;
        }
        byte[] decodedString = Base64.decode(bitmapString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

    }

    public static Bitmap getCroppedBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        int min;
        if (bitmap.getWidth() < bitmap.getHeight()) {
            min = bitmap.getWidth();
        } else {
            min = bitmap.getHeight();
        }

        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                min / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static boolean stringToBool(String value) {
        if (value.toLowerCase().equals("true")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean nullToBool(String value) {
        if (value.toLowerCase().equals("null")) {
            return false;
        } else {
            return true;
        }
    }

    public static void sortPlans(ArrayList<PayPlan> plans) {

        boolean isSorted = false;
        while (!isSorted) {
            isSorted = true;
            boolean isPopularFinished = false; // can be no populars or first some populars
            int firstUnPopular = 0;
            for (int i = 0; i < plans.size(); i++) {
                if (!plans.get(i).isPopular()) {
                    isPopularFinished = true;
                    firstUnPopular = i;

                } else { // find popular
                    if (isPopularFinished) { //and it is unsorted
                        isSorted = false;
                        PayPlan plan = plans.get(i);
                        plans.remove(plan);
                        plans.add(firstUnPopular, plan);
                        break;
                    }
                }

            }
        }

    }
public static ProgressDialog startLodingProgress(Context ctx, String title, DialogInterface.OnDismissListener cancelListener){
    ProgressDialog progress = new ProgressDialog(ctx);
    progress.setMessage(title);
    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    progress.setIndeterminate(true);
    progress.show();
    progress.setOnDismissListener(cancelListener);
    return progress;
}
    public static int dpToPx(Context context, int dp) {
        int px = Math.round(dp * getPixelScaleFactor(context));
        return px;
    }


    public static String getBitmapToBase64String(Bitmap photo) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String photoBase64 = null;

        if (photo != null) {

            photo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            photoBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        }
        return photoBase64;
    }

    private static float getPixelScaleFactor(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static boolean isOnline(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        return ni != null && ni.isConnected();
    }

    public static Bitmap megaBlur(Context ctx, Bitmap image) {
        if (ctx == null) {
            return image;
        }
        Bitmap result =blur(ctx, blur(ctx, blur(ctx, blur(ctx, image, 25), 25), 25), 25) ;

        return result;
    }


    @Contract("null, _ -> null")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap blur(Context ctx, Bitmap image, float blurRadius) {
        if (image == null) {
            return null;
        }
        Bitmap outputBitmap = null;
        try {
            System.gc();
            outputBitmap = image.copy(image.getConfig(),true);//Bitmap.createScaledBitmap(src, dstWidth, dstHeight, filter);Bitmap.createBitmap(image);
            RenderScript renderScript = RenderScript.create(ctx);
            Allocation tmpIn = Allocation.createFromBitmap(renderScript, image);
            Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);

            //Intrinsic Gausian blur filter
            ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
            theIntrinsic.setRadius(blurRadius);
            theIntrinsic.setInput(tmpIn);
            theIntrinsic.forEach(tmpOut);
            tmpOut.copyTo(outputBitmap);
        } catch (OutOfMemoryError error) {
            System.gc();
            return blur(ctx, image, blurRadius);
        }
        return outputBitmap;
    }

    public static Bitmap scaleBitmap(Bitmap bitmapToScale) {
        if (bitmapToScale == null)
            return null;
//get the original width and height
        int width = bitmapToScale.getWidth();
        int height = bitmapToScale.getHeight();
// create a matrix for the manipulation
        Matrix matrix = new Matrix();

// resize the bit map
        matrix.postScale(5 * width, 5 * height);

// recreate the new Bitmap and set it back
        return Bitmap.createBitmap(bitmapToScale, 0, 0, bitmapToScale.getWidth(), bitmapToScale.getHeight(), matrix, false);
    }


    public static Bitmap overlay(Bitmap top, Bitmap bottom) {
        if (top.getWidth() == 0 || top.getHeight() == 0) {
            return top;
        }
        int border = 5;
        bottom = Bitmap.createScaledBitmap(bottom, bottom.getWidth() / 2, bottom.getHeight() / 2, false);
        Bitmap bmOverlay = Bitmap.createBitmap(bottom.getWidth(), bottom.getHeight(), bottom.getConfig());
        int offset = Math.abs(top.getWidth() - top.getHeight()) / 2;
        if (top.getWidth() > top.getHeight()) {
            top = Bitmap.createBitmap(top, offset, 0, top.getHeight(), top.getHeight());
        } else {
            top = Bitmap.createBitmap(top, 0, offset, top.getWidth(), top.getWidth());
        }

        top = Bitmap.createScaledBitmap(top, bottom.getWidth() - (border * 2), bottom.getHeight() - (border * 2), false);
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bottom, new Matrix(), null);
        canvas.drawBitmap(top, border, border, null);
        return bmOverlay;
    }

    public static Bitmap getBitmapFromResource(Context ctx, int recourceId) {
        return BitmapFactory.decodeResource(ctx.getResources(),
                recourceId);
    }

    public static Drawable getDrawableRes(Context ctx, int resource) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return ctx.getResources().getDrawable(resource, ctx.getTheme());
        } else {
            return ctx.getResources().getDrawable(resource);
        }
    }

    public static int[] JSONArrayToIntArray(JSONArray jsonArray) {
        int[] result = new int[jsonArray.length()];
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                result[i] = jsonArray.getInt(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Bitmap scaleBitmapToMin(Bitmap image) {
        int maxSize = 1280;
        if (image.getHeight() < 1280 && image.getWidth() < 1280) {
            return image;
        }
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static Bitmap scaleBitmapToMinMarker(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Log.d("scaleBitmapToMin", "width = " + bitmap.getWidth());
        Log.d("scaleBitmapToMin", "height = " + bitmap.getHeight());
      /*  if(width < 200 && height < 200){
            return Bitmap.createBitmap(bitmap,0,0,200,200);
        }*/
        if (width > height) {
            float aspect = width / 100;
            width = 100;
            height /= aspect;
        } else {
            float aspect = height / 100;
            height = 100;
            width /= aspect;
        }
        Bitmap.createBitmap(bitmap, 0, 0, width, height);

        return Bitmap.createBitmap(bitmap, 0, 0, 100, 100);
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 12;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static String getMessageTimeForChat(java.sql.Date date) {

        Log.v("time util call", "" + date.toString());
        java.sql.Date now = new java.sql.Date(System.currentTimeMillis());
        if (date.getYear() == now.getYear() && date.getMonth() == now.getMonth() && date.getDay() == now.getDay()) {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            return format.format(date);
        } else if (date.getYear() == now.getYear() && date.getMonth() == now.getMonth() && date.getDay() == now.getDay() - 1) {
            return "Yesterday";
        } else {
            SimpleDateFormat format = new SimpleDateFormat("MM-dd");
            return format.format(date);
        }
    }


    public static void concatenate(ArrayList<Interests> array1, ArrayList<Interests> array2) {
        Interests[] array1and2 = new Interests[array1.size() + array2.size()];
        System.arraycopy(array1, 0, array1and2, 0, array1.size());
        System.arraycopy(array2, 0, array1and2, array1.size(), array2.size());

    }


    public static void getPosition(Context ctx, final OnFinishListener onFinishListener, LocationListener locationListener) {

        LocationManager locationManager = (LocationManager)
                ctx.getSystemService(Context.LOCATION_SERVICE);

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location location1 = getLastKnownLocation(locationManager);
            if (location1 != null) {
                Log.v("ShowMapFragment", "getPosition " + location1.toString());

                final LatLng location = new LatLng(location1.getLatitude(), location1.getLongitude());
                DBHandler.getInstance().setPersonPosition(new LatLng(location1.getLatitude(), location1.getLongitude()), new DBHandler.ResultListener() {
                    @Override
                    public void onFinish(Object personPositionOK) {
                        onFinishListener.onFinish(location);
                    }
                });

            }
        } catch (SecurityException se) {
            se.printStackTrace();
        }


    }

    public static void buildLocationSettingsRequest( final Activity activity)
        {


            final LocationManager manager = (LocationManager) activity.getSystemService( Context.LOCATION_SERVICE );

            if ( manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                return;
            }
            final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
            final String title = "GPS is disabled";
            final String message = "Enable either GPS or any other location"
                    + " service to find current location.  Click OK to go to"
                    + " location services settings to let you do so.";
           OCDialog dialog = new OCDialog(activity, title, message, R.drawable.map, ()->{

                activity.startActivity(new Intent(action));
            }, ()->{

                activity.getFragmentManager().popBackStack();
            });






        }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix,
                true);
    }

    public interface OnFinishListener {
        void onFinish(Object object);
    }

    static int rangeMiles = 5;

    private static Location getLastKnownLocation(LocationManager locationManager) throws SecurityException {

        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }



    public static Bitmap stringToBitmap(String source){
        byte[] decodedString = Base64.decode(source, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public static Bitmap getImageFromURL(String targetUrl) throws IOException
    {

        URL url = new URL(targetUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.connect();
        InputStream input = connection.getInputStream();
        Bitmap myBitmap = BitmapFactory.decodeStream(input);
        return myBitmap;
    }

    public static void clearCashe(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }
    public static String constructStartURL() {
        return Fields.URL_FOR_SERVER + "/" + Fields.PREFIX;
    }

    public static String convertArrayToString(String[] array){
        String str = "";
        for (int i = 0;i<array.length; i++) {
            str = str+array[i];
            // Do not append comma at the end of last element
            if(i<array.length-1){
                str = str+strSeparator;
            }
        }
        return str;
    }
    public static String convertArrayToString(int[] array){
        String str = "";
        for (int i = 0;i<array.length; i++) {
            str = str+array[i];
            // Do not append comma at the end of last element
            if(i<array.length-1){
                str = str+strSeparator;
            }
        }
        return str;
    }
    public static int[] convertStringToIntArray(String str){
        String[] arr = str.split(strSeparator);
        int[] result = new int[arr.length];
         for (int i = 0; i < arr.length; i++) {
            result[i] = Integer.parseInt(arr[i]);
        }
        return result;
    }
    public static boolean isEmailValid(String email){
        return emailPattern.matcher(email).matches();
    }
    public static boolean isNameValid(String name){
        if(name == null || name.length() == 0) {
            return false;
        }
        return true;
    }

    public static String[] convertStringToArray(String str){
        String[] arr = str.split(strSeparator);
        return arr;
    }
    public static UNITS getUnits() {
        String countryCode = Locale.getDefault().getCountry();
        if ("US".equals(countryCode) || "LR".equals(countryCode) || "MM".equals(countryCode))
        {return UNITS.IMPERIAL;} // burma
        return UNITS.METRIC;
    }
    public static String prepareShortUnts(double distance) {
        DecimalFormat df2 = new DecimalFormat("#.#");
        return df2.format(distance);

    }
}