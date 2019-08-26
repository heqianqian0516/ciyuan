package com.ciyuanplus.mobile.utils;

import com.google.gson.Gson;

/**
 * Created by Alen on 2016/4/15.
 */
public class GsonUtils {
    private static final Gson mGson = null;

    public synchronized static Gson getGsson() {
        if (mGson == null)
            return new Gson();
        return mGson;

    }
}