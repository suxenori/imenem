package com.menemi.dbfactory;

import android.support.annotation.UiThread;

/**
 * Created by Ui-Developer on 15.09.2016.
 */
public interface InternetConnectionListener {
    @UiThread
    void internetON();
    @UiThread
    void internetOFF();

}
