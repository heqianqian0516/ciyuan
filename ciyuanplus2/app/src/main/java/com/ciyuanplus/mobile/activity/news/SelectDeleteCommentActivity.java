package com.ciyuanplus.mobile.activity.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;

import androidx.annotation.Nullable;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/6/21.
 */

public class SelectDeleteCommentActivity extends MyBaseActivity {
    public static final String DELETE_COMENT = "delete_comment";
    public static final String DELETE_REPLY = "delete_reply";
    public static final String SELECTED = "selected";
    private String mType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_select_delete_comment);
        mType = getIntent().getStringExtra(Constants.INTENT_ACTIVITY_TYPE);
        this.initView();
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);
    }

    @OnClick({R.id.m_select_delete_comment_delete, R.id.m_select_delete_comment_cancel})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        switch (view.getId()) {
            case R.id.m_select_delete_comment_delete:
                Intent data = new Intent();
                data.putExtra(SELECTED, Utils.isStringEquals(mType, DELETE_COMENT) ? DELETE_COMENT : DELETE_REPLY);
                SelectDeleteCommentActivity.this.setResult(RESULT_OK, data);
                finish();
                break;
            case R.id.m_select_delete_comment_cancel:
                finish();
                break;
        }
    }
}
