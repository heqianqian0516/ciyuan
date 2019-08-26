package com.ciyuanplus.mobile.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ciyuanplus.mobile.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

/**
 * Created by Alen on 2017/3/5.
 */
public class LoadingDialog extends Dialog {
    public TextView mMessageText;

    public LoadingDialog(@NonNull Context context) {
        super(context);
    }

    private LoadingDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public LoadingDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    public static class Builder {
        private final Context context; // 上下文对象
        private String message;

        public Builder(Context context) {
            this.context = context;
        }

        /* 设置对话框信息 */
        public LoadingDialog.Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public LoadingDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LoadingDialog loadingDialog = new LoadingDialog(context, R.style.loading_dialog);
            View layout = inflater != null ? inflater.inflate(R.layout.loading_dialog, null) : null;
            loadingDialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            // main.xml中的ImageView
//            ProgressBar spaceshipImage = (ProgressBar) layout.findViewById(R.id.loading_progress);
            TextView tipTextView = layout.findViewById(R.id.m_tipTextView);// 提示文字

            tipTextView.setText(message);// 设置加载信息

            loadingDialog.mMessageText = tipTextView;
            loadingDialog.setCancelable(false);// 不可以用“返回键”取消
            loadingDialog.setContentView(layout);// 设置布局
            return loadingDialog;
        }

    }
}
