package com.danilov.ivan.commonlibrary.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Calendar;

/**
 * Created by Ivan Danilov on 17.06.2016.
 * Email: i.danilov@globus-ltd.com
 */
public class Utils {

    public final static String PLAY_PATH = "/play_list_path";
    public final static String PHOTO_PATH = "/photo_path";
    private static final String TAG = Utils.class.getSimpleName();

    public static Drawable getDrawable(Context context, int res) {
        Drawable icon;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            icon = VectorDrawableCompat.create(context.getResources(), res, context.getTheme());
        } else {
            icon = context.getResources().getDrawable(res, context.getTheme());
        }
        return icon;
    }

    private static ObjectMapper objectMapper;

    private static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.setTimeZone(Calendar.getInstance().getTimeZone());
        }
        return objectMapper;
    }

    public static <T> String getJsonString(T model) {
        String result = null;
        try {
            StringWriter writer = new StringWriter();
            getObjectMapper().writeValue(writer, model);
            result = writer.toString();
        } catch (IOException e) {
            Log.e(TAG, "Can't write value from mapped data", e);
        }
        return result;
    }

    public static <T> T getModel(String value, Class<T> tClass) {
        try {
            return getObjectMapper().readValue(value, tClass);
        } catch (IOException e) {
            Log.e(TAG, "Can't read value from mapped data", e);
            return null;
        }
    }
}
