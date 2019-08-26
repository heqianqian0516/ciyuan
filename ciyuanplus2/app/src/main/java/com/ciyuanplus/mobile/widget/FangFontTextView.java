package com.ciyuanplus.mobile.widget;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by Alen on 2017/12/15.
 */

public class FangFontTextView extends AppCompatTextView {
    public FangFontTextView(Context context) {
        super(context);
        this.init(context);
    }


    public FangFontTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init(context);

    }

    public FangFontTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context);

    }


    private void init(Context context) {
        AssetManager assertMgr = context.getAssets();
        Typeface font = Typeface.createFromAsset(assertMgr, "fangzheng_lanting_xihei.ttf");
        setTypeface(font);
    }
}
