package com.ciyuanplus.mobile.module.news.select_show_type;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.utils.Constants;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Alen on 2018/1/4.
 */

public class SelectShowTypeActivity extends MyBaseActivity {
    public static final String SELECTED = "selected";
    @BindView(R.id.m_select_show_type_1)
    TextView mSelectShowType1;
    @BindView(R.id.m_select_show_type_2)
    TextView mSelectShowType2;
    private final int[] Ids = {R.id.m_select_show_type_1, R.id.m_select_show_type_2, R.id.m_select_show_type_3,
            R.id.m_select_show_type_4};
    private int mSelectedShowType;
    private String name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_select_show_type);
        name = getIntent().getStringExtra(Constants.INTENT_COMMUNITY_NAME);
        mSelectedShowType = getIntent().getIntExtra(SELECTED, 0);
        Unbinder unbinder = ButterKnife.bind(this);

        this.initView();
    }

    private void initView() {
        mSelectShowType1.setText(name + "附近");
        mSelectShowType2.setText(name);

        TextView selectedView = (findViewById(Ids[mSelectedShowType]));
        selectedView.setTextColor(0xff0091ea);
    }

    @OnClick({R.id.m_select_show_type_1, R.id.m_select_show_type_2, R.id.m_select_show_type_3,
            R.id.m_select_show_type_4, R.id.m_select_show_type_lp})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        Intent intent = new Intent();
        for (int i = 0; i < 4; i++) {
            if (view.getId() == Ids[i]) {
                intent.putExtra(SELECTED, i);
                setResult(RESULT_OK, intent);
                finish();
                break;
            }
        }
        if (view.getId() == R.id.m_select_show_type_lp) {
            setResult(RESULT_CANCELED, intent);
            this.finish();
        }
    }
}
