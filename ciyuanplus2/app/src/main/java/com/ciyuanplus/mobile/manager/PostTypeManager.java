package com.ciyuanplus.mobile.manager;

import com.ciyuanplus.mobile.net.bean.PostTypeItem;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.GsonUtils;
import com.ciyuanplus.mobile.utils.Utils;

/**
 * Created by Alen on 2017/2/11.
 * <p>
 * 当前系统的帖子字典
 * 保存在一个常亮 和本地数据中
 * <p>
 * 这里可以优化把系统配置都放在这个实例中。暂时先这样处理
 */
public class PostTypeManager {
    private static PostTypeManager manager;
    private PostTypeItem[] mTypeDictData;


    private PostTypeManager() {
        mTypeDictData = queryLocalInfo();
    }

    public static synchronized PostTypeManager getInstance() {
        if (manager == null) {
            manager = new PostTypeManager();
        }
        return manager;
    }

    public PostTypeItem[] getTypeInfo() {
        PostTypeItem[] temp = new PostTypeItem[mTypeDictData.length + 1];
        temp[0] = new PostTypeItem("0", "全部");
        System.arraycopy(mTypeDictData, 0, temp, 1, mTypeDictData.length);
        return temp;
    }


    public void clearTypeInfo() {
        mTypeDictData = null;
    }

    public long insertOrReplace(String json) {
        SharedPreferencesManager.putString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_POST_TYPE_DICT, json);
        manager = new PostTypeManager();
        return 0;
    }

    public PostTypeItem getPostType(String typeid) {
        if (mTypeDictData == null) {
            return null;
        }
        for (PostTypeItem aMTypeDictData : mTypeDictData) {
            if (Utils.isStringEquals(typeid, aMTypeDictData.typeId)) return aMTypeDictData;
        }
        return null;
    }

    private PostTypeItem[] queryLocalInfo() {
//        String uid = "";
//        if(LoginStateManager.isLogin()) {
//            uid = SharedPreferencesManager.getString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_ID, "");
//        } else {
//            uid = SharedPreferencesManager.getString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_TEMP_USER_ID, "");
//        }
        String infoJson = SharedPreferencesManager.getString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_POST_TYPE_DICT, "");
        PostTypeItem[] myInfoItem = GsonUtils.getGsson().fromJson(infoJson, PostTypeItem[].class);
        return myInfoItem;
    }
}
