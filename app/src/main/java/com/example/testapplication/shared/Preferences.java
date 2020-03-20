package com.example.testapplication.shared;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.testapplication.OrderUp;
import com.example.testapplication.shared.enums.PrefParamEnum;

public enum Preferences {
    INSTANCE;
    private static final String TAG = "Preferences";

    public static void setPref(final String param, final Object value){
        if(value.getClass().equals(String.class)) {
            String stringValue = String.valueOf(value);
            getPref().edit().putString(param, stringValue).commit();
        } else if(value.getClass().equals(Integer.class)) {
            int intValue = Integer.valueOf(value.toString());
            getPref().edit().putInt(param, intValue).commit();
        } else if(value.getClass().equals(Boolean.class)) {
            boolean boolValue = Boolean.valueOf(value.toString());
            getPref().edit().putBoolean(param, boolValue).commit();
        } else if(value.getClass().equals(Long.class)) {
            long longValue = Long.valueOf(value.toString());
            getPref().edit().putLong(param, longValue).commit();
        } else if(value.getClass().equals(Float.class)) {
            float floatValue = Float.valueOf(value.toString());
            getPref().edit().putFloat(param, floatValue).commit();
        } else {
            Log.d(TAG, "setPref: ");
        }
    }

    public static SharedPreferences getPref() {
        return OrderUp.getContext().getSharedPreferences("OrderUp", Context.MODE_PRIVATE);
    }

    public static boolean getMode() {
        return getPref().getBoolean(PrefParamEnum.MODE.name(), false);
    }
}
