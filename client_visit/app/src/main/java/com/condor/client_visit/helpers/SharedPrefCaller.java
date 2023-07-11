package com.condor.client_visit.helpers;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class SharedPrefCaller {
    //Static method that will set the shared prefrences in this app
    public static void setSharedPref(@NonNull Context context, @NonNull String type, @NonNull String name, @NonNull Object value){

        SharedPreferences sharedPref = context.getSharedPreferences(context.getApplicationContext().getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        switch (type)
        {
            case "string":
                editor.putString(name, (String)value);
                break;
            case "int":
                editor.putInt(name,(int)value);
                break;
            case "float":
                editor.putFloat(name,(float)value);
                break;
            case "long":
                editor.putLong(name,(long)value);
                break;
            case "boolean":
                editor.putBoolean(name,(boolean)value);
                break;
        }
        editor.commit();
    }

    //Static method that gets the shared prefrences in this app
    public  static Object getSharedPref(@NonNull Context context,@NonNull String type,@NonNull String name){
        String t = context.getApplicationContext().getPackageName();
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getApplicationContext().getPackageName(), MODE_PRIVATE);
        Object val = null;
        switch (type)
        {
            case "string":
                val =  (Object) sharedPref.getString(name,"");
                break;
            case "int":
                val =  (Object) sharedPref.getInt(name,0);
                break;
            case "float":
                val =  (Object) sharedPref.getFloat(name,0);
                break;
            case "long":
                val =  (Object) sharedPref.getLong(name,0);
                break;
            case "boolean":
                val =  (Object) sharedPref.getBoolean(name,false);
                break;
        }
        return  val;
    }



    public static  void clearAll(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(context.getApplicationContext().getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();
    }
}
