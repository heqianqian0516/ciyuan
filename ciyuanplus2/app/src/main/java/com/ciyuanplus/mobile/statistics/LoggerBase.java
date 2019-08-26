package com.ciyuanplus.mobile.statistics;

/**
 * Created by Alen on 2017/7/5.
 */

abstract class LoggerBase {
    private boolean mIsEnable = true;
    private LogLevel mEnableLevel = LogLevel.verbose;

    public boolean isEnabled() {
        return this.mIsEnable;
    }

    public void setEnable(boolean isEnable) {
        this.mIsEnable = isEnable;
    }

    LogLevel getEnableLevel() {
        return this.mEnableLevel;
    }

    void setEnableLevel(LogLevel enableLevel) {
        if (enableLevel != null) {
            this.mEnableLevel = enableLevel;
        }
    }

    protected abstract void write(LogLevel level, String tag, String msg);

    protected abstract void write(LogLevel level, String time, String tag, String msg);

    public abstract int flush(FlushInfo info);

    public int flush() {
        return this.flush(null);
    }
}
