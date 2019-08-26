package com.ciyuanplus.mobile.statistics;

/**
 * Created by Alen on 2017/7/5.
 */

public enum LogLevel {
    undefine(0, "U"),
    verbose(1, "V"),
    debug(2, "D"),
    info(3, "I"),
    warn(4, "W"),
    error(5, "E"),
    special(6, "S"),
    behaviour(7, "B"),
    remote(8, "R");

    private final int mLevelType;
    private final String mLevelTag;

    LogLevel(int levelType, String levelTag) {
        this.mLevelType = levelType;
        this.mLevelTag = levelTag;
    }

    public static LogLevel getLogLevelByType(int value) {
        for (LogLevel type : LogLevel.values()) {
            if (type.getType() == value) {
                return type;
            }
        }
        return null;
    }

    public static LogLevel getLogLevelByTag(String value) {
        for (LogLevel type : LogLevel.values()) {
            if (type.getTag().equals(value)) {
                return type;
            }
        }
        return null;
    }

    private int getType() {
        return this.mLevelType;
    }

    private String getTag() {
        return this.mLevelTag;
    }

    public boolean isLessThan(LogLevel level) {
        return (level != null && this.mLevelType < level.getType());
    }

    public boolean isBigThan(LogLevel level) {
        return (level != null && this.mLevelType > level.getType());
    }
}
