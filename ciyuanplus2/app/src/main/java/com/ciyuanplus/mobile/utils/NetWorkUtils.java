package com.ciyuanplus.mobile.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ciyuanplus.mobile.App;

/**
 * Created by Alen on 2017/7/5.
 */

public class NetWorkUtils {
    /**
     * Check the network availability
     *
     * @param context
     * @return True if network available, false otherwise.
     */
    private static boolean isNetworkAvailable(Context context) {
        if (null == context) {
            return false;
        }

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check the network availability
     *
     * @return True if network available, false otherwise.
     */
    public static boolean isNetworkAvailable() {
        return isNetworkAvailable(App.mContext);
    }
}
