package com.ciyuanplus.mobile.manager;

import com.ciyuanplus.mobile.net.bean.WikiTypeItem;
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
public class WikiTypeManager {
    private static WikiTypeManager manager;
    private WikiTypeItem[] mTypeDictData;


    private WikiTypeManager() {
        mTypeDictData = queryLocalInfo();
    }

    public static synchronized WikiTypeManager getInstance() {
        if (manager == null) {
            manager = new WikiTypeManager();
        }
        return manager;
    }

    public WikiTypeItem[] getTypeInfo() {// 加一个全部在里面

        WikiTypeItem[] temp = new WikiTypeItem[mTypeDictData.length + 1];
        temp[0] = new WikiTypeItem("0", "全部");
        System.arraycopy(mTypeDictData, 0, temp, 1, mTypeDictData.length);
        return temp;
    }


    public void clearTypeInfo() {
        mTypeDictData = null;
    }

    public long insertOrReplace(String json) {
        SharedPreferencesManager.putString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_WIKI_TYPE_DICT, json);
        manager = new WikiTypeManager();
        return 0;
    }

    public WikiTypeItem getWikiType(String typeid) {
        if (mTypeDictData == null) {
            return null;
        }
        for (WikiTypeItem aMTypeDictData : mTypeDictData) {
            if (Utils.isStringEquals(typeid, aMTypeDictData.id)) return aMTypeDictData;
        }
        return null;
    }

    private WikiTypeItem[] queryLocalInfo() {
//        String uid = "";
//        if(LoginStateManager.isLogin()) {
//            uid = SharedPreferencesManager.getString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_ID, "");
//        } else {
//            uid = SharedPreferencesManager.getString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_TEMP_USER_ID, "");
//        }
        String infoJson = SharedPreferencesManager.getString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_WIKI_TYPE_DICT, "");
        WikiTypeItem[] myInfoItem = GsonUtils.getGsson().fromJson(infoJson, WikiTypeItem[].class);
        return myInfoItem;
    }
}
