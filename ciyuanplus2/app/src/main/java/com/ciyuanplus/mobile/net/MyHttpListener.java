package com.ciyuanplus.mobile.net;

import android.app.Activity;
import android.content.Context;

import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.litesuits.http.data.HttpStatus;
import com.litesuits.http.exception.ClientException;
import com.litesuits.http.exception.HttpClientException;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.exception.HttpNetException;
import com.litesuits.http.exception.HttpServerException;
import com.litesuits.http.exception.NetException;
import com.litesuits.http.exception.ServerException;
import com.litesuits.http.exception.handler.HttpExceptionHandler;
import com.litesuits.http.listener.HttpListener;
import com.litesuits.http.response.Response;
import com.orhanobut.logger.Logger;

/**
 * Created by Alen on 2017/6/22.
 */

public class MyHttpListener<Data> extends HttpListener<Data> {
    private final Context context;

    public MyHttpListener(Context context) {
        this.context = context;
    }

    @Override
    public boolean disableListener() {// 防止 页面关闭了  回调还去做页面操作导致崩溃
        return context == null || (context instanceof Activity && ((Activity) context).isFinishing());
    }

    @Override
    public void onSuccess(Data data, Response<Data> response) {
        super.onSuccess(data, response);
        Logger.json(data.toString());
        System.out.println(data.toString());
    }

    @Override
    public void onFailure(HttpException e, Response<Data> response) {
        super.onFailure(e, response);

        Logger.d(e.toString());

        new HttpExceptionHandler() {
            @Override
            protected void onClientException(HttpClientException e, ClientException e1) {
                CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_client_error_alert)).show();
            }

            @Override
            protected void onNetException(HttpNetException e, NetException e1) {
                switch (e.getExceptionType()) {
                    case NetworkNotAvilable:
                        CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_network_not_available_alert)).show();
                        break;
                    case NetworkUnstable:
                        CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_network_un_stable_alert)).show();
                        break;
                    case NetworkDisabled:
                        CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_network_disable_alert)).show();
                        break;
                    default:
                        CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_http_error_alert)).show();
                        break;
                }
            }

            @Override
            protected void onServerException(HttpServerException e, ServerException e1, HttpStatus httpStatus) {
                CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_server_error_alert)).show();
            }
        }.handleException(e);
    }
}
