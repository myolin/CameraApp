package com.myolin.cameraapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mzlmy on 4/20/2017.
 */

public class MyPrefs implements Parcelable{

    private static final String FILENAME = "MyPreferences";

    private SharedPreferences preferences;

    public MyPrefs(Context context){
        preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
    }

    public void storeString(String name, String value){
        preferences.edit().putString(name, value).commit();
    }

    public String retrieveString(String name){
        return preferences.getString(name, "");
    }

    public void storeBoolean(String name, boolean value){
        preferences.edit().putBoolean(name, value).commit();
    }

    public void clearAll(){
        preferences.edit().clear().commit();
    }

    public boolean retrieveBoolean(String name){
        return preferences.getBoolean(name, false);
    }

    protected MyPrefs(Parcel in) {
    }

    public static final Creator<MyPrefs> CREATOR = new Creator<MyPrefs>() {
        @Override
        public MyPrefs createFromParcel(Parcel in) {
            return new MyPrefs(in);
        }

        @Override
        public MyPrefs[] newArray(int size) {
            return new MyPrefs[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
