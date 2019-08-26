package com.ciyuanplus.mobile.module.settings.select_sex;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.net.bean.UserInfoItem;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/5/18.
 */

public class SelectSexPopActivity extends MyBaseActivity implements SelectSexContract.View {
    @Inject
 SelectSexPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_select_sex_popup);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        this.initView();
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);
        DaggerSelectSexPresenterComponent.builder().
                selectSexPresenterModule(new SelectSexPresenterModule(this)).build().inject(this);
    }


    @OnClick({R.id.m_select_sex_popup_man, R.id.m_select_sex_popup_female, R.id.m_select_sex_popup_cancel})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        switch (view.getId()) {
            case R.id.m_select_sex_popup_man:
                mPresenter.changeSex(UserInfoItem.SEX_MAN);

                break;
            case R.id.m_select_sex_popup_female:
                mPresenter.changeSex(UserInfoItem.SEX_FEMALE);

                break;
            case R.id.m_select_sex_popup_cancel:
                finish();

                break;
        }
    }

    @Override
    public Context getDefaultContext() {
        return this;
    }
}
