package com.ciyuanplus.mobile.module.settings.help;

import com.ciyuanplus.mobile.adapter.QuestionAdapter;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.HelpItem;
import com.ciyuanplus.mobile.net.parameter.RequestContractApiParameter;
import com.ciyuanplus.mobile.net.response.RequestHelpDataResponse;
import com.ciyuanplus.mobile.statistics.StatisticsConstant;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;

import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

/**
 * Created by Alen on 2017/12/11.
 */

public class HelpPresenter implements HelpContract.Presenter {
    private final HelpContract.View mView;

    private QuestionAdapter mQuestionAdapter;
    private final ArrayList<HelpItem> mHelpList = new ArrayList<>();

    @Inject
    public HelpPresenter(HelpContract.View mView) {
        this.mView = mView;

    }

    @Override
    public void initData() {
        mQuestionAdapter = new QuestionAdapter(mView.getDefaultContext(), mHelpList, (v) -> {
            int postion = mView.getListView().getChildLayoutPosition(v);
            if (mQuestionAdapter.getItem(postion).isSpeard) {
                StatisticsManager.onEventInfo(StatisticsConstant.MODULE_HELP,
                        StatisticsConstant.OP_HELP_LIST_ITEM_CLOSE);
            } else {
                StatisticsManager.onEventInfo(StatisticsConstant.MODULE_HELP,
                        StatisticsConstant.OP_HELP_LIST_ITEM_OPEN);
            }
            mQuestionAdapter.getItem(postion).isSpeard = !mQuestionAdapter.getItem(postion).isSpeard;
            mQuestionAdapter.notifyDataSetChanged();
        });
        mView.getListView().setAdapter(mQuestionAdapter);

        reqeustHelpData();
    }

    // 获取帮助中心内容
    private void reqeustHelpData() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_FAQ_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new RequestContractApiParameter().getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                RequestHelpDataResponse response1 = new RequestHelpDataResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else if (response1.helpListItem != null && response1.helpListItem.data.length > 0) {
                    Collections.addAll(mHelpList, response1.helpListItem.data);
                    mQuestionAdapter.notifyDataSetChanged();

                }
            }
//
//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                super.onFailure(e, response);
//                CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_my_profile_get_help_fail_alert)).show();
//            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    @Override
    public void detachView() {
    }
}
