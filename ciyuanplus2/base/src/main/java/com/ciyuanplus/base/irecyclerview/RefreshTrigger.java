package com.ciyuanplus.base.irecyclerview;

/**
 * Created by aspsine on 16/3/7.
 */
interface RefreshTrigger {

    void onStart(boolean automatic, int headerHeight, int finalHeight);

    void onMove(boolean finished, boolean automatic, int moved);

    void onRefresh();

    void onRelease();

    void onComplete();

    void onReset();
}
