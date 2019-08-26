package com.ciyuanplus.mobile.module.popup.select_image_pop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Alen on 2017/5/18.
 */

public class SelectImagePopActivity extends MyBaseActivity {
    private static final String TAKE_PHOTO = "take";
    public static final String LOACL_PHOTO = "local";
    public static final String SELECTED = "selected";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_select_image_popup);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        this.initView();
    }

    private void initView() {
         ButterKnife.bind(this);

    }

    @OnClick({R.id.m_select_image_popup_camera, R.id.m_select_image_popup_picture, R.id.m_select_image_popup_cancel})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        Intent data = new Intent();

        switch (view.getId()) {
            case R.id.m_select_image_popup_camera:
                data.putExtra(SELECTED, TAKE_PHOTO);
                SelectImagePopActivity.this.setResult(RESULT_OK, data);
                finish();
                break;
            case R.id.m_select_image_popup_picture:
                data.putExtra(SELECTED, LOACL_PHOTO);
                SelectImagePopActivity.this.setResult(RESULT_OK, data);
                finish();
                break;
            case R.id.m_select_image_popup_cancel:
                finish();
                break;
        }
    }
}
