package com.ciyuanplus.mobile.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.inter.MyOnClickListener;


public class CustomDialog extends Dialog {

    public CustomDialog(Context context) {
        super(context);
    }

    private CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private final Context context; // 上下文对象
        private String title; // 对话框标题
        private String message; // 对话框内容
        private String confirm_btnText; // 按钮名称“确定”
        private String cancel_btnText; // 按钮名称“取消”
        private String neutral_btnText; // 按钮名称“隐藏”
        private View contentView; // 对话框中间加载的其他布局界面
        /* 按钮坚挺事件 */
        private OnClickListener confirm_btnClickListener;
        private OnClickListener cancel_btnClickListener;
        private OnClickListener neutral_btnClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        /* 设置对话框信息 */
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
         * @param
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * 设置对话框界面
         *
         * @param v View
         * @return
         */
        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param confirm_btnText
         * @return
         */
        public Builder setPositiveButton(int confirm_btnText,
                                         OnClickListener listener) {
            this.confirm_btnText = (String) context.getText(confirm_btnText);
            this.confirm_btnClickListener = listener;
            return this;
        }

        /**
         * Set the positive button and it's listener
         *
         * @param confirm_btnText
         * @return
         */
        public Builder setPositiveButton(String confirm_btnText,
                                         OnClickListener listener) {
            this.confirm_btnText = confirm_btnText;
            this.confirm_btnClickListener = listener;
            return this;
        }

        /**
         * Set the negative button resource and it's listener
         *
         * @param
         * @return
         */
        public Builder setNegativeButton(int cancel_btnText,
                                         OnClickListener listener) {
            this.cancel_btnText = (String) context.getText(cancel_btnText);
            this.cancel_btnClickListener = listener;
            return this;
        }

        /**
         * Set the negative button and it's listener
         *
         * @param
         * @return
         */
        public Builder setNegativeButton(String cancel_btnText,
                                         OnClickListener listener) {
            this.cancel_btnText = cancel_btnText;
            this.cancel_btnClickListener = listener;
            return this;
        }

        /**
         * Set the netural button resource and it's listener
         *
         * @param
         * @return
         */
        public Builder setNeutralButton(int neutral_btnText,
                                        OnClickListener listener) {
            this.neutral_btnText = (String) context.getText(neutral_btnText);
            this.neutral_btnClickListener = listener;
            return this;
        }

        /**
         * Set the netural button and it's listener
         *
         * @param
         * @return
         */
        public Builder setNeutralButton(String neutral_btnText,
                                        OnClickListener listener) {
            this.neutral_btnText = neutral_btnText;
            this.neutral_btnClickListener = listener;
            return this;
        }

        public CustomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomDialog dialog = new CustomDialog(context,
                    R.style.my_dialog_style);
            View layout = inflater != null ? inflater.inflate(R.layout.layout_custom_dialog, null) : null;
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            // set the dialog title
            ((TextView) layout.findViewById(R.id.m_title)).setText(title);
            ((TextView) layout.findViewById(R.id.m_title)).getPaint()
                    .setFakeBoldText(true);

            if (title == null || title.trim().length() == 0) {
                ((TextView) layout.findViewById(R.id.m_message))
                        .setGravity(Gravity.CENTER);
            }
            int buttonNumber = 0;

            if (neutral_btnText != null) buttonNumber++;
            if (confirm_btnText != null) buttonNumber++;
            if (cancel_btnText != null) buttonNumber++;
            if (buttonNumber == 1) {
                layout.findViewById(R.id.m_opera_lp_2).setVisibility(View.GONE);
                layout.findViewById(R.id.m_opera_lp_1).setVisibility(View.VISIBLE);
                layout.findViewById(R.id.m_second_line).setVisibility(View.GONE);
                layout.findViewById(R.id.m_single_line).setVisibility(View.GONE);
                layout.findViewById(R.id.m_confirm_btn).setBackgroundResource(
                        R.drawable.dialog_single_btn_selector);
            } else if (buttonNumber == 2) {
                layout.findViewById(R.id.m_opera_lp_2).setVisibility(View.GONE);
                layout.findViewById(R.id.m_opera_lp_1).setVisibility(View.VISIBLE);
                layout.findViewById(R.id.m_second_line).setVisibility(View.GONE);
                layout.findViewById(R.id.m_single_line).setVisibility(View.VISIBLE);
            } else {
                // if no confirm button or cancle button or neutral just set the
                // visibility to GONE
                layout.findViewById(R.id.m_opera_lp_2).setVisibility(View.VISIBLE);
                layout.findViewById(R.id.m_opera_lp_1).setVisibility(View.GONE);
                layout.findViewById(R.id.m_second_line).setVisibility(View.VISIBLE);
                layout.findViewById(R.id.m_single_line).setVisibility(View.VISIBLE);
            }
            // set the confirm button
            if (confirm_btnText != null) {
                Button confirm;
                if (buttonNumber > 2) confirm = layout.findViewById(R.id.m_confirm_btn_2);
                else confirm = layout.findViewById(R.id.m_confirm_btn);
                confirm.setText(confirm_btnText);
                if (confirm_btnClickListener != null) {
                    confirm.setOnClickListener(new MyOnClickListener() {
                        public void performRealClick(View v) {
                            confirm_btnClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                } else {
                    confirm.setOnClickListener(new MyOnClickListener() {

                        @Override
                        public void performRealClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            } else {
                layout.findViewById(R.id.m_confirm_btn).setVisibility(View.GONE);
            }
            // set the cancel button
            if (cancel_btnText != null) {
                Button cancel;
                if (buttonNumber > 2) cancel = layout.findViewById(R.id.m_cancel_btn_2);
                else cancel = layout.findViewById(R.id.m_cancel_btn);
                cancel.setText(cancel_btnText);
                if (cancel_btnClickListener != null) {
                    cancel.setOnClickListener(new MyOnClickListener() {
                        public void performRealClick(View v) {
                            cancel_btnClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                } else {
                    cancel.setOnClickListener(new MyOnClickListener() {

                        @Override
                        public void performRealClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            } else {
                layout.findViewById(R.id.m_cancel_btn).setVisibility(View.GONE);
            }
            if (neutral_btnText != null) {
                Button neutral;
                if (buttonNumber > 2) neutral = layout.findViewById(R.id.m_neutral_btn_2);
                else neutral = layout.findViewById(R.id.m_neutral_btn);
                neutral.setText(neutral_btnText);
                if (neutral_btnClickListener != null) {
                    neutral.setOnClickListener(new MyOnClickListener() {
                        public void performRealClick(View v) {
                            neutral_btnClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_NEUTRAL);
                        }
                    });
                } else {
                    neutral.setOnClickListener(new MyOnClickListener() {

                        @Override
                        public void performRealClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            } else {
                layout.findViewById(R.id.m_neutral_btn).setVisibility(View.GONE);
            }

            // set the content message
            if (message != null) {
                ((TextView) layout.findViewById(R.id.m_message)).setText(message);
            } else if (contentView != null) {
                // if no message set
                // add the contentView to the dialog body
                ((RelativeLayout) layout.findViewById(R.id.m_content)).removeAllViews();
                ((RelativeLayout) layout.findViewById(R.id.m_content)).addView(contentView, new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT));
            }
            dialog.setContentView(layout);
            return dialog;
        }

    }
}
