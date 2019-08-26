package com.ciyuanplus.mobile.module.start_forum.start_option;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.manager.CommunityManager;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.login.LoginActivity;
import com.ciyuanplus.mobile.module.settings.address.AddressManagerActivity;
import com.ciyuanplus.mobile.module.start_forum.start_food.StartFoodActivity;
import com.ciyuanplus.mobile.module.start_forum.start_note.StartNoteActivity;
import com.ciyuanplus.mobile.module.start_forum.start_stuff.StartStuffActivity;
import com.ciyuanplus.mobile.widget.CustomDialog;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/8/11.
 * <p>
 * 发表   帖子和 物品  选择页面
 */

public class StartOptionActivity extends MyBaseActivity implements StartOptionContract.View {
    @Inject
    public StartOptionPresenter mPresenter;// 没有处理数据，这个暂时没用

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_start_options);
        StatusBarCompat.compat(this,getResources().getColor(R.color.title));
        this.initView();
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);
        DaggerStartOptionPresenterComponent.builder().
                startOptionPresenterModule(new StartOptionPresenterModule(this)).build().inject(this);
    }

    @Override
    public Context getDefaultContext() {
        return this;
    }

    @OnClick({R.id.m_start_option_start_stuff,
            R.id.m_start_option_start_life,
            R.id.m_start_option_start_food, R.id.m_start_option_cancel})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        switch (view.getId()) {
            case R.id.m_start_option_start_stuff:
                if (!LoginStateManager.isLogin()) {
                    Intent intent = new Intent(StartOptionActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }

                if (!CommunityManager.getInstance().checkHasDetailAddress(UserInfoData.getInstance().getUserInfoItem().currentCommunityUuid)) {// meiyou
                    CustomDialog.Builder builder = new CustomDialog.Builder(StartOptionActivity.this);
                    builder.setMessage("主人，发布宝贝前请提交您的有效地址哟~");
                    builder.setPositiveButton("去添加", (dialog, which) -> {
                        dialog.dismiss();
                        Intent intent = new Intent(StartOptionActivity.this, AddressManagerActivity.class);
                        startActivity(intent);
                        finish();
                    });
                    builder.setNegativeButton("取消", (dialog, which) -> {
                        dialog.dismiss();
                        finish();
                    });
                    CustomDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                } else {
                    Intent intent = new Intent(StartOptionActivity.this, StartStuffActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.m_start_option_start_life://生活随手记
                if (!LoginStateManager.isLogin()) {
                    Intent intent2 = new Intent(StartOptionActivity.this, LoginActivity.class);
                    startActivity(intent2);
                    return;
                }
                Intent intent2 = new Intent(StartOptionActivity.this, StartNoteActivity.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.m_start_option_start_food://美食品鉴家
                if (!LoginStateManager.isLogin()) {
                    Intent intent3 = new Intent(StartOptionActivity.this, LoginActivity.class);
                    startActivity(intent3);
                    return;
                }
                Intent intent3 = new Intent(StartOptionActivity.this, StartFoodActivity.class);
                startActivity(intent3);
                finish();
                break;
            case R.id.m_start_option_cancel:
                finish();
                break;
        }
    }
}
