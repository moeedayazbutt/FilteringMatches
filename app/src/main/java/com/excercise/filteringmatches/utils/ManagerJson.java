package com.excercise.filteringmatches.utils;

import android.app.Activity;
import android.content.res.AssetManager;

import com.excercise.filteringmatches.constants.JsonConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import static com.excercise.filteringmatches.constants.JsonConstants.JSON_CHARSET_NAME;

/**
 */

public class ManagerJson {
    public static String loadJSONFromAsset(AssetManager assetManager, String fileName) {
        String json = null;
        try {
            InputStream is = assetManager.open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, JSON_CHARSET_NAME);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static String getString(JSONObject jsonObject, String key) throws JSONException {
        if(jsonObject.has(key)){
            return jsonObject.getString(key);
        }
        else {
            return null;
        }
    }

    public static boolean getBoolean(JSONObject jsonObject, String key) throws JSONException {
        if(jsonObject.has(key)){
            return jsonObject.getBoolean(key);
        }
        else {
            return false;
        }
    }

    public static Double getDouble(JSONObject jsonObject, String key) throws JSONException {
        if(jsonObject.has(key)){
            return jsonObject.getDouble(key);
        }
        else {
            return null;
        }
    }

    public static Integer getInt(JSONObject jsonObject, String key) throws JSONException {
        if(jsonObject.has(key)){
            return jsonObject.getInt(key);
        }
        else {
            return null;
        }
    }
}
