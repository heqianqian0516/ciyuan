package com.ciyuanplus.mobile.net;

import android.content.Context;

import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.utils.Utils;
import com.litesuits.http.HttpConfig;
import com.litesuits.http.LiteHttp;

/**
 * Created by Alen on 2016/11/26.
 */
public class LiteHttpManager {//
    private static LiteHttp liteHttp;

    public static LiteHttp getInstance() {
        if (liteHttp == null) {
            initLiteHttp(App.mContext);
        }
        return liteHttp;
    }

    private static void initLiteHttp(Context context) {
        HttpConfig config = new HttpConfig(context) // configuration quickly
                .setDebugged(true)// log output when debugged
                .setDetectNetwork(true)              // detect network before connect
                .setDoStatistics(true)
                .setDefaultMaxRetryTimes(3)
                .setDefaultCharSet("UTF-8")
                .setDebugged(Utils.isDebug())
                .setForRetry(3, false) // statistics of time and traffic
                //.setDisableNetworkFlags(HttpConfig.FLAG_NET_DISABLE_MOBILE) //不用2/3/4G网络
                .setUserAgent("Mozilla/5.0 (...)")   // set custom User-Agent
                .setTimeOut(8000, 120000);             // connect and socket timeout: 2s
        liteHttp = LiteHttp.newApacheHttpClient(config);
    }


}
