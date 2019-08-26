package com.ciyuanplus.mobile.activity.news;

import android.graphics.Rect;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.adapter.ReportAdapter;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.ReportItem;
import com.ciyuanplus.mobile.net.parameter.ReportPostApiParameter;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonTitleBar;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.ciyuanplus.mobile.widget.LengthFilter;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/8/22.
 */

public class ReportPostActivity extends MyBaseActivity {
    private final ArrayList<ReportItem> mList = new ArrayList<>();
    @BindView(R.id.m_report_post_grid_view)
    RecyclerView mReportPostGridView;
    @BindView(R.id.m_report_post_input)
    EditText mReportPostInput;
    @BindView(R.id.m_report_post_confirm)
    TextView mReportPostConfirm;
    @BindView(R.id.m_report_post_common_title)
    CommonTitleBar m_js_common_title;

    private int reportType;
    private ReportAdapter mAdapter;
    private String mPostUuid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_report_post);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        mPostUuid = getIntent().getStringExtra(Constants.INTENT_POST_ID);
        reportType = getIntent().getIntExtra(Constants.INTENT_ACTIVITY_TYPE, 0);
        this.initView();
        StatisticsManager.onEventInfo("report", "report_submit_page_load");

        this.initData();
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);
        m_js_common_title.setLeftImage(R.mipmap.nav_icon_back);
        m_js_common_title.setCenterText("举报");
        m_js_common_title.setLeftClickListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View view) {
                onBackPressed();
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mReportPostGridView.setLayoutManager(gridLayoutManager);
        mReportPostGridView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = Utils.dip2px(10);
            }
        });
        mAdapter = new ReportAdapter(this, mList, null);
        mReportPostGridView.setAdapter(mAdapter);

        mReportPostInput.setFilters(new InputFilter[]{new LengthFilter(200)});

    }

    private void initData() {
        mList.add(new ReportItem("0", "垃圾营销", false));
        mList.add(new ReportItem("1", "不实信息", false));
        mList.add(new ReportItem("2", "有害信息", false));
        mList.add(new ReportItem("3", "违法信息", false));
        mList.add(new ReportItem("4", "淫秽信息", false));
        mList.add(new ReportItem("5", "人身攻击", false));
        mList.add(new ReportItem("6", "内容抄袭", false));
        mList.add(new ReportItem("7", "违规活动", false));
        mList.add(new ReportItem("8", "页面故障", false));
        mList.add(new ReportItem("11", "其他", false));
        mAdapter.notifyDataSetChanged();
    }

    // 举报帖子
    private void reportPost(ArrayList<String> selectReason, String reason) {
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_REPORT_POST);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new ReportPostApiParameter(reportType + "", mPostUuid, selectReason, reason, null).getRequestBody());
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(this) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                ResponseData response1 = new ResponseData(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    //
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    CommonToast.getInstance(response1.mMsg).show();
                    finish();
                }
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    @OnClick(R.id.m_report_post_confirm)
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        int id = view.getId();

        if (id == R.id.m_report_post_confirm) {
            ArrayList<String> selectReason = new ArrayList<>();
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).isChecked) selectReason.add(mList.get(i).id);
            }
            if (selectReason.size() == 0) {
                CommonToast.getInstance("请选择至少一个理由").show();
                return;
            }
            String reason = mReportPostInput.getText().toString();
            reportPost(selectReason, reason);
        }
    }
}
