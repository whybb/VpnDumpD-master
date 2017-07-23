package com.vpndumpdemo.utils;

import android.util.Log;


public class MyLog {


    private static final boolean DEBUG=true;

    public static void logd(Object o , String msg){

        String clazz = o.getClass().getName();

        if(DEBUG){
            Log.d(clazz.substring(clazz.lastIndexOf('.')+1),msg);

        }

    }

    public static void loge(Object o , String msg){

        if(DEBUG){

            String clazz = o.getClass().getName();

            Log.e(clazz.substring(clazz.lastIndexOf('.')+1),msg);


        }
    }

}
