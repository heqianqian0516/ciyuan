package com.ciyuanplus.mobile.module.popup.select_edit_or_delete_stuff;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.utils.Constants;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/6/21.
 */

public class SelectEditOrDeleteStuffActivity extends MyBaseActivity {
    public static final String SELECTED = "selected";
    public static final String DELETE = "delete";
    public static final String EDIT = "edit";
    public static final String COLLECT = "collect";
    public static final String REPORT = "report";
    public static final String CHANGE = "change";
    @BindView(R.id.m_select_edit_delete_stuff_edit)
    LinearLayout mSelectEditDeleteStuffEdit;
    @BindView(R.id.m_select_edit_delete_stuff_change)
    LinearLayout mSelectEditDeleteStuffChange;
    @BindView(R.id.m_select_edit_delete_stuff_delete)
    LinearLayout mSelectEditDeleteStuffDelete;
    @BindView(R.id.m_select_edit_delete_stuff_mine_lp)
    LinearLayout mSelectEditDeleteStuffMineLp;
    @BindView(R.id.m_select_edit_delete_stuff_collect_image)
    ImageView mSelectEditDeleteStuffCollectImage;
    @BindView(R.id.m_select_edit_delete_stuff_collect)
    LinearLayout mSelectEditDeleteStuffCollect;
    @BindView(R.id.m_select_edit_delete_stuff_report)
    LinearLayout mSelectEditDeleteStuffReport;
    @BindView(R.id.m_select_edit_delete_stuff_others_lp)
    LinearLayout mSelectEditDeleteStuffOthersLp;
    @BindView(R.id.m_select_edit_delete_stuff_cancel)
    TextView mSelectEditDeleteStuffCancel;

    private int mType;
    private int mHasCollected;
    private int status;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_select_edit_delete_stuff);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        mType = getIntent().getIntExtra(Constants.INTENT_ACTIVITY_TYPE, 0);
        mHasCollected = getIntent().getIntExtra(Constants.INTENT_POST_HAS_COLLECTED, 0);
        status = getIntent().getIntExtra(Constants.INTENT_POST_STATUS, 0);
        this.initView();
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);

        mSelectEditDeleteStuffCollectImage.setImageResource(mHasCollected == 1 ? R.mipmap.btn_collectnews : R.mipmap.btn_un_collectnews);
        if (mType == 0) { // 根据不同的分类  区分显示不同的layout
            mSelectEditDeleteStuffMineLp.setVisibility(View.VISIBLE);
            if (status == 8) { // 如果已经被卖出， 这里只能展示删除
                mSelectEditDeleteStuffEdit.setVisibility(View.GONE);
                mSelectEditDeleteStuffChange.setVisibility(View.GONE);
            }
            mSelectEditDeleteStuffOthersLp.setVisibility(View.GONE);
        } else {
            mSelectEditDeleteStuffMineLp.setVisibility(View.GONE);
            mSelectEditDeleteStuffOthersLp.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.m_select_edit_delete_stuff_edit, R.id.m_select_edit_delete_stuff_change,
            R.id.m_select_edit_delete_stuff_delete, R.id.m_select_edit_delete_stuff_collect,
            R.id.m_select_edit_delete_stuff_report, R.id.m_select_edit_delete_stuff_cancel})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        Intent data = new Intent();
        switch (view.getId()) {
            case R.id.m_select_edit_delete_stuff_edit:
                data.putExtra(SELECTED, EDIT);
                SelectEditOrDeleteStuffActivity.this.setResult(RESULT_OK, data);
                finish();
                break;
            case R.id.m_select_edit_delete_stuff_change:
                data.putExtra(SELECTED, CHANGE);
                SelectEditOrDeleteStuffActivity.this.setResult(RESULT_OK, data);
                finish();
                break;
            case R.id.m_select_edit_delete_stuff_delete:
                data.putExtra(SELECTED, DELETE);
                SelectEditOrDeleteStuffActivity.this.setResult(RESULT_OK, data);
                finish();
                break;
            case R.id.m_select_edit_delete_stuff_collect:
                data.putExtra(SELECTED, COLLECT);
                SelectEditOrDeleteStuffActivity.this.setResult(RESULT_OK, data);
                finish();
                break;
            case R.id.m_select_edit_delete_stuff_report:
                data.putExtra(SELECTED, REPORT);
                SelectEditOrDeleteStuffActivity.this.setResult(RESULT_OK, data);
                finish();
                break;
            case R.id.m_select_edit_delete_stuff_cancel:
                finish();
                break;
        }
    }
}
