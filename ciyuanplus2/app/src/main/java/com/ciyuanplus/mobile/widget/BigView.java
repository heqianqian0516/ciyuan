package com.ciyuanplus.mobile.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView;
//继承图片框架然后用事件分发将触摸pass掉，不允许其放大
public class BigView extends SubsamplingScaleImageView {
    public BigView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public BigView(Context context) {
        super(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return false;
    }
}
