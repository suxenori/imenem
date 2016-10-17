package com.menemi.utils;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by irondev on 28.06.16.
 */
public class ViewScaler {
    private static int screenHeight = 0;
    private static int screenWidth = 0;
    public static void setScreen(int screenHeight, int screenWidth){
        ViewScaler.screenHeight = screenHeight;
        ViewScaler.screenWidth = screenWidth;
    }
    public static int getPixelsH(float percents){
        return (int)((screenHeight / 100) * percents);
    }
    public static int getPixelsW(float percents){
        return (int)((screenWidth / 100) * percents);
    }
    public static float getTextSize(float percents){
        return (screenHeight / 100) * percents;
    }
    public static void setSizeSquare(View resorce, float size){
        resorce.getLayoutParams().height = getPixelsH(size);
        resorce.getLayoutParams().width = getPixelsH(size);
    }
    public static void setSize(View resorce, float height, float width){
        resorce.getLayoutParams().height = getPixelsH(height);
        resorce.getLayoutParams().width = getPixelsW(width);
    }
    public static void setMargins(View resorce,float left, float top, float right, float bottom){
        ((ViewGroup.MarginLayoutParams)resorce.getLayoutParams()).setMargins(getPixelsW(left),getPixelsH(top),getPixelsW(right),getPixelsH(bottom));
    }

}