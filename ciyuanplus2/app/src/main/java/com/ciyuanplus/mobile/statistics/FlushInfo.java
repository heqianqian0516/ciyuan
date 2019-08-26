package com.ciyuanplus.mobile.statistics;


/**
 * @author Alen
 */
class FlushInfo {
    private final FlushType mFlushType;
    private final String mFlushInfo;

    public FlushInfo(FlushType type, String info) {
        this.mFlushType = type;
        this.mFlushInfo = info;
    }

    public String getFlushInfo() {
        return this.mFlushInfo;
    }

    public FlushType getFlushType() {
        return this.mFlushType;
    }
}
