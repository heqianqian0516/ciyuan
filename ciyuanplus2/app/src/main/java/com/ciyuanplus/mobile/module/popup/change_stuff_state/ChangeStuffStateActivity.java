package com.ciyuanplus.mobile.module.popup.change_stuff_state;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/6/21.
 */

public class ChangeStuffStateActivity extends MyBaseActivity {
    public static final String SELECTED = "selected";
    public static final String SELLING = "selling";
    public static final String ORDERED = "ordered";
    public static final String SELLED = "selled";

    @BindView(R.id.m_change_stuff_state_selling)
    LinearLayout mChangeStuffStateSelling;
    @BindView(R.id.m_change_stuff_state_ordered)
    LinearLayout mChangeStuffStateOrdered;
    @BindView(R.id.m_change_stuff_state_sold)
    LinearLayout mChangeStuffStateSold;
    @BindView(R.id.m_change_stuff_state_cancel)
    TextView mChangeStuffStateCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_change_stuff_state);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        this.initView();
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);
    }


    @OnClick({R.id.m_change_stuff_state_selling, R.id.m_change_stuff_state_ordered, R.id.m_change_stuff_state_sold, R.id.m_change_stuff_state_cancel})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        Intent data = new Intent();

        switch (view.getId()) {
            case R.id.m_change_stuff_state_selling:
                data.putExtra(SELECTED, SELLING);
                ChangeStuffStateActivity.this.setResult(RESULT_OK, data);
                finish();
                break;
            case R.id.m_change_stuff_state_ordered:
                data.putExtra(SELECTED, ORDERED);
                ChangeStuffStateActivity.this.setResult(RESULT_OK, data);
                finish();
                break;
            case R.id.m_change_stuff_state_sold:
                data.putExtra(SELECTED, SELLED);
                ChangeStuffStateActivity.this.setResult(RESULT_OK, data);
                finish();
                break;
            case R.id.m_change_stuff_state_cancel:
                finish();
                break;
        }
    }
}
