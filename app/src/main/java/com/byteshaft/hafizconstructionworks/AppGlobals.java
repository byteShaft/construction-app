package com.byteshaft.hafizconstructionworks;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.ConnectivityManager;


public class AppGlobals extends Application {

    private static Context sContext;

    public static final String SERVER_IP = "http://192.168.100.3:5000/";
//    public static final String BASE_URL = String.format("%s/v1/", SERVER_IP);
    public static Typeface typefaceBold;
    public static Typeface typefaceNormal;

    public static boolean isInternetAvailable = false;


    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new NetworkChangeReceiver(), intentFilter);
//        typefaceBold = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/bold.ttf");
//        typefaceNormal = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/regular.ttf");
    }

    public static Context getContext() {
        return sContext;
    }
}
