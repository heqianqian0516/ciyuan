package com.ciyuanplus.mobile.widget;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by Alen on 2017/9/6.
 * 控制最大输入值的提示功能
 */

public class LengthFilter implements InputFilter {

    private final int mMax;

    public LengthFilter(int max) {
        mMax = max;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        int keep = mMax - (dest.length() - (dend - dstart));

        if (keep <= 0) {
            CommonToast.getInstance("不能输入超过" + mMax + "字符").show();
            return "";
        } else if (keep >= end - start) {
            return null; // keep original
        } else {
            keep += start;
            CommonToast.getInstance("不能输入超过" + mMax + "字符").show();

            if (Character.isHighSurrogate(source.charAt(keep - 1))) {
                --keep;
                if (keep == start) {
                    return "";
                }
            }
            return source.subSequence(start, keep);
        }
    }

}
