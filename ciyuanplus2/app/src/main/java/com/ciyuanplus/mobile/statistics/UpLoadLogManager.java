package com.ciyuanplus.mobile.statistics;

import android.os.Environment;

import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.FileUtils;
import com.ciyuanplus.mobile.utils.NetWorkUtils;
import com.ciyuanplus.mobile.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Alen on 2017/7/5.
 */

public class UpLoadLogManager {
    private static UpLoadLogManager sLoadLogManager;
    private boolean mCanUpLoadCrash = true;

    private List<String> mLogPathList;

    private UpLoadLogManager() {
    }

    public static UpLoadLogManager getInstance() {
        if (sLoadLogManager == null) {
            sLoadLogManager = new UpLoadLogManager();
        }
        return sLoadLogManager;
    }

    public void upload() {
        new Thread(() -> {
            if (!NetWorkUtils.isNetworkAvailable()) return;
            mLogPathList = FileUtils.getFileList(Environment.getExternalStorageDirectory() + Constants.BehaviourLogPath, BehaviourLogger.BEHAVIOUR_SUFFIX);
            if (mLogPathList.size() == 0) return;
            for (int i = 0; i < mLogPathList.size(); i++) {
                String logString = FileUtils.read(mLogPathList.get(i));
                // 这里读出来的是[]
                if (Utils.isStringEmpty(logString) || logString.length() < 2) {
                    return;
                }
                logString = logString.substring(1, logString.length() - 1);
                try {
                    FileUtils.forceDelete(new File(mLogPathList.get(i)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!Utils.isStringEmpty(logString)) {
                    LogHandlerManager.requestLog(logString, 1);
                }
            }
        }).start();
    }

    private void uploadLog() {
        boolean canUpLoad = true;
        if (canUpLoad) {//
            String log = "";
            LogHandlerManager.requestLog(log, 1);
        }
    }


}
