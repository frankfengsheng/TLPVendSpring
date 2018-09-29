package com.tcn.vendspring;

import android.app.Application;
import android.content.Context;

public class MyAplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        MyAplication.context=getAplicationContext();
    }

    public static Context getAplicationContext(){
        return MyAplication.context;
    }
}
