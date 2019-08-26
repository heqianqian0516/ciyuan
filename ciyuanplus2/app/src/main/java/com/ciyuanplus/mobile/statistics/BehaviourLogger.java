package com.ciyuanplus.mobile.statistics;

import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alen on 2017/7/5.
 */

public class BehaviourLogger extends LoggerBase {
    public static final String BEHAVIOUR_SUFFIX = ".behaviour";
    private static final int LOG_COUNT = 100;
    private final String LOG_PATH;

    private String mFileName = "";
    private JSONArray mLogList = new JSONArray();
    private final Object mLock = new Object();
    private final Map<String, BehaviourLoggerElement> mBehaviourLoggerSendMap = new HashMap<>();
    private Map<String, String> mBehaviourLoggerMatchMap = new HashMap<>();

    public BehaviourLogger(String filePath, Map<String, String> behaviourLoggerMatchMap) {
        this.LOG_PATH = Environment.getExternalStorageDirectory() + filePath;
        this.setEnableLevel(LogLevel.behaviour);

        // NOTE(junfengli) : mBehaviourLoggerSendMap is use to write log, "in" and "out" must pairing
        // exit, so when "in" we save in mBehaviourLoggerSendMap's value
        // when "out" we get out "in" and "out" to log pair
        // mBehaviourLoggerMatchMap is use to match "in" and "out"
        if (null != behaviourLoggerMatchMap) {
            this.mBehaviourLoggerMatchMap = behaviourLoggerMatchMap;
            for (String ble : this.mBehaviourLoggerMatchMap.values()) {
                if (ble != null) {
                    this.mBehaviourLoggerSendMap.put(ble, null);
                }
            }
        }
    }

    private boolean initFileName() {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmssS");
        String timestamp = dateformat.format(new Date());
        this.mFileName = this.LOG_PATH + timestamp + "_" + this.mLogList.length() + BEHAVIOUR_SUFFIX;

        File dir = new File(this.LOG_PATH);
        if (!dir.exists()) {
            return dir.mkdirs();
        }
        return true;
    }

    private void handleBehaviourLogger(FlushInfo info) throws JSONException {
        synchronized (this.mLogList) {
            for (BehaviourLoggerElement ble : this.mBehaviourLoggerSendMap.values()) {
                if (ble != null) {
                    String logEventOut = this.mBehaviourLoggerMatchMap.get(ble.mKey);
                    // JSONArray jArrayIn = new JSONArray();
                    JSONObject jsonObjectIn = new JSONObject();
                    JSONObject jsonObjectOut = new JSONObject();
                    String detail = "";

                    jsonObjectIn.put("key", ble.mKey);
                    jsonObjectIn.put("timestamp", ble.mTimestamp);
                    jsonObjectIn.put("details", ble.mDetails);

                    jsonObjectOut.put("out", logEventOut);
                    jsonObjectOut.put("timestamp", new Date().getTime() + "");
                    if (info != null) {
                        detail = info.getFlushInfo();
                    }
                    jsonObjectOut.put("deatails", detail);

                    // NOTE(junfengli) : add in
                    this.mLogList.put(jsonObjectIn);
                    // NOTE(junfengli) : add out
                    this.mLogList.put(jsonObjectOut);
                }
            }
        }
    }

    @Override
    public void write(LogLevel level, String tag, String msg) {
        if (!this.getEnableLevel().equals(level)) {
            return;
        }
        this.write(level, new Date().getTime() + "", tag, msg);
    }

    @Override
    protected void write(LogLevel level, String time, String tag, String msg) {
        if (!this.getEnableLevel().equals(level)) {
            return;
        }

        try {
            // JSONArray jArray = new JSONArray();
            // jArray.put(tag);
            // jArray.put(time);
            // jArray.put(msg);
            JSONObject jObject = new JSONObject(msg);
            jObject.put("timestamp", time);
            // && this.mBehaviourLoggerMap.get(tag) == null
            synchronized (this.mLogList) {
                if (!this.mBehaviourLoggerSendMap.containsKey(tag)
                        && !this.mBehaviourLoggerMatchMap.containsKey(tag)) {
                    // NOTE(junfengli) : if not "in" and "out" , we log it directly
                    this.mLogList.put(jObject);
                } else if (this.mBehaviourLoggerMatchMap.containsKey(tag)) {
                    // NOTE(junfengli) : if is "in" , we save "in", not log
                    BehaviourLoggerElement ble = new BehaviourLoggerElement(tag);
                    ble.mTimestamp = time;
                    ble.mDetails = msg;
                    this.mBehaviourLoggerSendMap.put(this.mBehaviourLoggerMatchMap.get(tag), ble);
                } else if (this.mBehaviourLoggerSendMap.containsKey(tag)) {
                    // NOTE(junfengli) : if "out", we get out "in" and "out" to log pair
                    BehaviourLoggerElement ble = this.mBehaviourLoggerSendMap.get(tag);
                    if (ble != null) {
                        this.mBehaviourLoggerSendMap.put(tag, null);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("key", ble.mKey);
                        jsonObject.put("timestamp", ble.mTimestamp);
                        jsonObject.put("details", ble.mDetails);
                        // JSONArray jsonArray = new JSONArray();
                        // jsonArray.put(ble.mKey);
                        // jsonArray.put(ble.mTimestamp);
                        // jsonArray.put(ble.mDetails);
                        // NOTE(junfengli) : add in
                        this.mLogList.put(jsonObject);
                        // NOTE(junfengli) : add out
                        this.mLogList.put(jObject);
                    }
                }
            }
            if (this.mLogList.length() >= LOG_COUNT) {
                this.flush(null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return : -1 写日志时发生错误 0 当前队列没有日志可写 1 写日志成功
     * @author : junfengli
     */
    @Override
    public int flush(FlushInfo info) {
        if (info != null) {
            try {
                this.handleBehaviourLogger(info);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (!this.initFileName()) {
            return -1;
        }
        JSONArray queue = new JSONArray();
        synchronized (this.mLogList) {
            queue = this.mLogList;
            this.mLogList = new JSONArray();
        }
        if (queue.length() == 0) {
            return 0;
        }
        synchronized (this.mLock) {
            FileLock lock = null;
            FileWriter writer = null;
            try {
                writer = new FileWriter(this.mFileName, true);
                writer.write(queue.toString());
                writer.flush();
            } catch (FileNotFoundException e) {
                return -1;
            } catch (IOException e) {
                return -1;
            } finally {
                try {
                    if (writer != null) {
                        writer.close();

                    }
                } catch (IOException e) {
                    e.printStackTrace();

                }

            }
        }
        return 1;
    }

    private class BehaviourLoggerElement {
        final String mKey;
        String mTimestamp = "";
        String mDetails = "";

        BehaviourLoggerElement(String key) {
            this.mKey = key;
        }
    }

}
