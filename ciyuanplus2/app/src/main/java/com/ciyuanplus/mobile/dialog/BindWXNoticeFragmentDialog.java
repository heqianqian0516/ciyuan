package com.ciyuanplus.mobile.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.ciyuanplus.mobile.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public class BindWXNoticeFragmentDialog extends DialogFragment {

    private OnBindClickedListener onBindClickedListener;

    public void setOnBindClickedListener(OnBindClickedListener onBindClickedListener) {

        this.onBindClickedListener = onBindClickedListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_bind_wx_notice, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ImageView cancel = view.findViewById(R.id.iv_cancel);
        cancel.setOnClickListener(v -> getDialog().dismiss());

        Button bindWX = view.findViewById(R.id.btn_bind);
        bindWX.setOnClickListener(v -> {
            dismiss();
            onBindClickedListener.onBind();
        });

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Window dialogWindow = getDialog().getWindow();
        WindowManager.LayoutParams lp = dialogWindow != null ? dialogWindow.getAttributes() : null;
        lp.gravity = Gravity.CENTER;//改变在屏幕中的位置,如果需要改变上下左右具体的位置，比如100dp，则需要对布局设置margin
        Display defaultDisplay = getActivity().getWindowManager().getDefaultDisplay();
        lp.width = defaultDisplay.getWidth() / 4 * 3;  //改变宽度
//            lp.height=300;//   改变高度
        dialogWindow.setAttributes(lp);

        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);
    }

    @Override
    public void onStart() {

        super.onStart();

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        getDialog().getWindow().getDecorView().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    interface OnBindClickedListener {
        void onBind();
    }
}
