package com.ciyuanplus.mobile.net.response;

import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.FriendsItem;
import com.ciyuanplus.mobile.net.bean.ParticipationItembean;
import com.ciyuanplus.mobile.net.bean.TaskNumberBean;
import com.ciyuanplus.mobile.net.bean.TaskNumberListBean;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.GsonUtils;
import com.ciyuanplus.mobile.utils.Utils;
import com.google.gson.Gson;


/**
 * Created by Alen on 2017/2/11.
 */
public class TaskNumberResponse extends ResponseData {

   /* public ArrayList<TaskNumberBean> taskNumberBeans;
    public  String data1;*/
   public TaskNumberListBean taskNumberListBean;
    public TaskNumberResponse(String data) {
        super(data);
        if (!Utils.isStringEquals(mCode, CODE_OK)) return;
        Gson gson = GsonUtils.getGsson();
        taskNumberListBean = gson.fromJson(data, TaskNumberListBean.class);

    }

}
