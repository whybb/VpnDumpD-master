package com.vpndumpdemo;

import android.content.Context;

import org.litepal.LitePalApplication;


public class VpnDumpApp extends LitePalApplication {


    private static Context mContext;



    @Override
    public void onCreate() {
        initialize(this);
        super.onCreate();
        mContext = this;


    }

    public static Context getContext(){
        return mContext;
    }


}
