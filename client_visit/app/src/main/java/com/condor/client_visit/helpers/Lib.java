package com.condor.client_visit.helpers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.DisplayMetrics;

import com.condor.client_visit.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class Lib {
    public static OkHttpClient setTokenHeader(String token){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + token)
                        .build();
                return chain.proceed(newRequest);
            }
        }).addInterceptor(logging).build();

        return client;
    }

    public static HashMap<String,String> getCreds(Context context){
        HashMap<String,String> creds = new HashMap<String,String>();
        creds.put(context.getString(R.string.sh_codusur),(String)SharedPrefCaller.getSharedPref(context,"string",context.getString(R.string.sh_codusur)));
        creds.put(context.getString(R.string.sh_token),(String)SharedPrefCaller.getSharedPref(context,"string",context.getString(R.string.sh_token)));
        creds.put(context.getString(R.string.sh_idvisita),(String)SharedPrefCaller.getSharedPref(context,"string",context.getString(R.string.sh_idvisita)));
        return  creds;
    }

    public static int[] getScreenSize(Activity activity){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return new int[]{displayMetrics.widthPixels,displayMetrics.heightPixels};
    }

    public static String getBase64FromImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return  encoded;
    }
}
