package com.ciyuanplus.mobile.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.utils.Constants;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by kk on 2018/5/17.
 */

public class ShowActivityImageDialog extends DialogFragment {

    private static final String URL_IMAGE = "urlImage";
    @BindView(R.id.iv_cancel)
    ImageView mCancelButton;
    @BindView(R.id.iv_ad)
    ImageView mAdButton;
    private String mUrlImage;
    private OnAdClickListener onAdClickListener;
    private Unbinder mUnbinder;

    public static ShowActivityImageDialog newInstance(String urlImage) {

        ShowActivityImageDialog dialog = new ShowActivityImageDialog();
        Bundle args = new Bundle();
        args.putString(URL_IMAGE, urlImage);
        dialog.setArguments(args);
        return dialog;

    }

    public void setOnAdClickListener(OnAdClickListener onAdClickListener) {

        this.onAdClickListener = onAdClickListener;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mUrlImage = getArguments().getString(URL_IMAGE);

        }

        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_show_activity_dialog, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        RequestOptions options = new RequestOptions().placeholder(R.drawable.ic_default_image_007).error(R.mipmap.imgfail).dontAnimate().centerCrop();


        Glide.with(this).load(Constants.IMAGE_LOAD_HEADER + mUrlImage).apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                        mCancelButton.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(mAdButton);
    }

    @OnClick({R.id.iv_ad, R.id.iv_cancel})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.iv_cancel:
                getDialog().dismiss();
                break;
            case R.id.iv_ad:
                getDialog().dismiss();
                onAdClickListener.onAdClick();
                break;
        }
    }

    @Override
    public void onStart() {

        super.onStart();


        Window dialogWindow = getDialog().getWindow();
        WindowManager.LayoutParams lp = dialogWindow != null ? dialogWindow.getAttributes() : null;
//        lp.gravity = Gravity.CENTER;//改变在屏幕中的位置,如果需要改变上下左右具体的位置，比如100dp，则需要对布局设置margin
        Display defaultDisplay = getActivity().getWindowManager().getDefaultDisplay();
//        lp.width = defaultDisplay.getWidth() / 5 * 4;  //改变宽度
////        lp.height = defaultDisplay.getHeight() / 4 * 3;  //改变宽度
        lp.dimAmount = 0.5f;
        dialogWindow.setAttributes(lp);

        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mUnbinder.unbind();

    }

    public interface OnAdClickListener {
        void onAdClick();
    }
}
