package com.ciyuanplus.mobile.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Alen on 2017/3/5.
 */
public class SleftAdapterGrideView extends GridView {

    public SleftAdapterGrideView(Context context) {
        super(context);
    }

    public SleftAdapterGrideView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SleftAdapterGrideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
