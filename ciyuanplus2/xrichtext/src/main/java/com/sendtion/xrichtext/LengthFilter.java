package com.sendtion.xrichtext;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Toast;

/**
 * Created by Alen on 2017/9/6.
 * 控制最大输入值的提示功能  800字未上限
 */

class LengthFilter implements InputFilter {

    private final int mMax;
    private final Context mContext;
    private final int mRand; // 提示的上限文字

    public LengthFilter(Context context, int max, int rand) {
        mContext = context;
        mMax = max;
        mRand = rand;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        int keep = mMax - (dest.length() - (dend - dstart));

        if (keep <= 0) {
            Toast.makeText(mContext, "不能输入超过 " + mRand + "字符", Toast.LENGTH_SHORT).show();
            return "";
        } else if (keep >= end - start) {
            return null; // keep original
        } else {
            keep += start;
            Toast.makeText(mContext, "不能输入超过 " + mRand + "字符", Toast.LENGTH_SHORT).show();

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
