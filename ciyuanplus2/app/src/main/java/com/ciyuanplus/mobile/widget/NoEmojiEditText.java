package com.ciyuanplus.mobile.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.utils.Utils;

import androidx.appcompat.widget.AppCompatEditText;

public class NoEmojiEditText extends AppCompatEditText implements TextWatcher {
    /**
     * 控件是否有焦点
     */
    private boolean hasFoucs;

    //禁止输入Emoji
    private int cursorPos;
    private String inputAfterText;
    private boolean resetText;

    public NoEmojiEditText(Context context) {
        this(context, null);
    }

    public NoEmojiEditText(Context context, AttributeSet attrs) {
        // 这里构造方法也很重要，不加这个很多属性不能再XML里面定义
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public NoEmojiEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomCommonClearEditText);
        int deleDrawable = a.getResourceId(R.styleable.CustomCommonClearEditText_delete_icon, R.mipmap.nav_icon_search_delete);
        // 设置输入框里面内容发生改变的监听
        addTextChangedListener(this);
        int paddingLeft = getPaddingLeft();

        a.recycle();
    }

    /**
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 当我们按下的位置 在 EditText的宽度 -
     * 图标到控件右边的间距 - 图标的宽度 和 EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {

                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));

                if (touchable) {
                    this.setText("");
                }
            }
        }

        return super.onTouchEvent(event);
    }


    /**
     * 当输入框里面内容发生变化的时候回调的方法
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        if (!resetText && getSelectionEnd() > cursorPos) {
            CharSequence input = s.subSequence(cursorPos, getSelectionEnd());
            if (Utils.containsEmoji(input.toString())) {
                resetText = true;
                CommonToast.getInstance("不支持输入Emoji表情字符").show();
                setText(inputAfterText);
                Spannable text = getText();
                if (text != null) {
                    Spannable spanText = text;
                    Selection.setSelection(spanText, text.length());
                }
            }
        } else {
            resetText = false;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (!resetText) {
            cursorPos = getSelectionEnd();
            inputAfterText = s.toString();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}