package com.ciyuanplus.mobile.statistics;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alen on 2017/7/5.
 */

public class YrLogger {
    private static final Map<Class<?>, LoggerBase> mLoggerList = new HashMap<>();

    private static final String TAG = "logger";
    private final String tag;

    public YrLogger() {
        this.tag = TAG;
    }

    private YrLogger(String tag) {
        this.tag = tag;
    }

    public YrLogger(Class<?> cls) {
        this(cls.getName());
    }

    public static void registerLogger(LoggerBase loggerBase) {
        if (loggerBase != null) {
            mLoggerList.put(loggerBase.getClass(), loggerBase);
        }
    }

    public static void unRegisterLogger(Class<?> cls) {
        if (cls != null) {
            mLoggerList.remove(cls);
        }
    }

    public static void unRegisterLoggerAll() {
        mLoggerList.clear();
    }

    public static void isEnable(Class<?> cls, boolean isEnable) {
        for (Map.Entry<Class<?>, LoggerBase> entry : mLoggerList.entrySet()) {
            if (cls.equals(entry.getValue().getClass())) {
                entry.getValue().setEnable(isEnable);
            }
        }
    }

    public static void flush(FlushInfo info) {
        for (Map.Entry<Class<?>, LoggerBase> entry : mLoggerList.entrySet()) {
            entry.getValue().flush(info);
        }
    }

    //  : LogEvent strong type bind
    public static void b(String tag, String msg) {
        if (tag != null) {
            YrLogger.write(LogLevel.behaviour, tag, msg == null ? "" : msg);
        }
    }

    public static void e(String tag, String msg) {
        if (tag != null) {
            YrLogger.write(LogLevel.error, tag, msg == null ? "" : msg);
        }
    }

    public static void b(String tag) {
        if (tag != null) {
            YrLogger.write(LogLevel.behaviour, tag, "");
        }
    }


    private static void write(LogLevel level, String tag, String msg) {
        if (level == null) {
            level = LogLevel.error;
        }
        if (tag == null) {
            tag = TAG;
        }
        if (msg == null) {
            msg = "";
        }

        synchronized (mLoggerList) {
            for (Map.Entry<Class<?>, LoggerBase> entry : mLoggerList.entrySet()) {
                if (entry.getValue().isEnabled()) {
                    entry.getValue().write(level, tag, msg);
                }
            }
        }
    }

    private static void write(LogLevel level, String tag, String format, Object... args) {
        YrLogger.write(level, tag, String.format(format, args));
    }

}
