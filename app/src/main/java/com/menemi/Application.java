package com.menemi;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.vk.sdk.VKSdk;

/**
 * Created by Ui-Developer on 07.09.2016.
 */
public class Application extends com.menemi.social_network.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(getApplicationContext());
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
