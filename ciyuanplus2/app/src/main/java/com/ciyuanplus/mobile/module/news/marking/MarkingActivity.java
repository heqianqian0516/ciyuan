package com.ciyuanplus.mobile.module.news.marking;

import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.ciyuanplus.mobile.widget.LengthFilter;
import com.ciyuanplus.mobile.widget.MarkView;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Alen on 2018/1/9.
 */

public class MarkingActivity extends MyBaseActivity implements MarkingContract.View {
    @BindView(R.id.m_marking_close_image)
    ImageView mMarkingCloseImage;
    @BindView(R.id.m_marking_mark_view)
    MarkView mMarkingMarkView;
    @BindView(R.id.m_marking_input_view)
    EditText mMarkingInputView;
    @BindView(R.id.m_marking_cancel)
    TextView mMarkingCancel;
    @BindView(R.id.m_marking_ok)
    TextView mMarkingOk;

    @Inject

    MarkingPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_marking);
        this.initView();
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);

        DaggerMarkingPresenterComponent.builder().
                markingPresenterModule(new MarkingPresenterModule(this)).build().inject(this);
        mMarkingInputView.setFilters(new InputFilter[]{new LengthFilter(120)});
        mPresenter.initData(getIntent());

        mMarkingMarkView.setValue(2.0f);
    }

    @Override
    public Context getDefaultContext() {
        return this;
    }

    @OnClick({R.id.m_marking_close_image, R.id.m_marking_cancel, R.id.m_marking_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.m_marking_close_image:
                finish();
                break;
            case R.id.m_marking_cancel:
                finish();
                break;
            case R.id.m_marking_ok:
                int score = (int) mMarkingMarkView.getValue();
                if (score < 2) {
                    CommonToast.getInstance("请评分").show();
                    return;
                }
                mPresenter.submmitMark(mMarkingInputView.getText().toString(), score + "");
                break;
        }
    }
}
