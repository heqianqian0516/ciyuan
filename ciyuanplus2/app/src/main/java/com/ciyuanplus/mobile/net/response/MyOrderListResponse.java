package com.ciyuanplus.mobile.net.response;

import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.MyOrderItem;
import com.ciyuanplus.mobile.utils.GsonUtils;
import com.ciyuanplus.mobile.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by kk on 2018/5/24.
 */

public class MyOrderListResponse extends ResponseData {


    public ArrayList<MyOrderItem> myOrderItemList;

    public MyOrderListResponse(String data) {
        super(data);

        if (!Utils.isStringEquals(mCode, CODE_OK)) return;
        Gson gson = GsonUtils.getGsson();

        try {
            JSONObject mObject = new JSONObject(data);
            String data1 = mObject.getString("data");
            JSONObject object = new JSONObject(data1);
            String data2 = object.getString("list");
            Logger.d("data2 = " + data2);
            Type type  = new TypeToken<ArrayList<MyOrderItem>>(){}.getType();
            myOrderItemList = gson.fromJson(data2, type);
//            MyOrderList resultBean = new Gson().fromJson(data2, MyOrderList.class);
//            //对象中拿到集合
//            myOrderItemList = resultBean.data;
//            Log.d("真有意思", "MyOrderListResponse: " + myOrderItemList.size());
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
