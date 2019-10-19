package com.ciyuanplus.mobile.net.response;

import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.NewTaskListBean;
import com.ciyuanplus.mobile.net.bean.TaskNumberListBean;
import com.ciyuanplus.mobile.utils.GsonUtils;
import com.ciyuanplus.mobile.utils.Utils;
import com.google.gson.Gson;


/**
 * Created by Alen on 2017/2/11.
 */
public class NewTaskResponse extends ResponseData {

   /* public ArrayList<TaskNumberBean> taskNumberBeans;
    public  String data1;*/
   public NewTaskListBean newTaskListBean;
    public NewTaskResponse(String data) {
        super(data);
        if (!Utils.isStringEquals(mCode, CODE_OK)) return;
        Gson gson = GsonUtils.getGsson();
        newTaskListBean = gson.fromJson(data, NewTaskListBean.class);

    }

}
