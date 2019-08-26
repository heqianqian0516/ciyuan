package com.ciyuanplus.mobile.module.mine.welfare_and_activity;

import com.ciyuanplus.mobile.adapter.WelfareAndActivityAdapter;
import com.ciyuanplus.mobile.net.bean.RecycleViewItemData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by kk on 2018/5/30.
 */

public class MyActivityAndWelfarePresenter implements MyActivityAndWelfareContract.Presenter {


    private final MyActivityAndWelfareContract.View mView;
    private final List<RecycleViewItemData> mItemList = new ArrayList<>();
    private final WelfareAndActivityAdapter mAdapter;

    @Inject
    public MyActivityAndWelfarePresenter(MyActivityAndWelfareContract.View mView) {
        this.mView = mView;

        doRequest();

        mAdapter = new WelfareAndActivityAdapter(mView.getDefaultContext(), mItemList);
        mView.getMainList().setAdapter(mAdapter);

    }

    private void doRequest() {

//        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_myaction);
//        postRequest.setMethod(HttpMethods.Post);
//        postRequest.setHttpBody(new MyActionParameter(UserInfoData.getInstance().getUserInfoItem().currentCommunityUuid, UserInfoData.getInstance().getUserInfoItem().uuid).getRequestBody());
//        String sessionKey = SharedPreferencesManager.getString(
//                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
//        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
//        postRequest.setHttpListener(new MyHttpListener<String>(App.mContext) {
//                                        @Override
//                                        public void onSuccess(String s, Response<String> response) {
//                                            super.onSuccess(s, response);
//
//                                            MyActionResponse myActionResponse = new MyActionResponse(s);
//                                            BeanMyAction beanMyAction = myActionResponse.beanMyAction;
////                                            CommonToast.getInstance("活动" + beanMyAction.activityOrderListResultList.size() + "福利" + beanMyAction.giftInfoListResultList.size()).show();
//
//                                            if (beanMyAction.giftInfoListResultList.size() + beanMyAction.activityOrderListResultList.size() > 0) {
//
//
//                                                for (int i = 0; i < beanMyAction.activityOrderListResultList.size(); i++) {
//
//                                                    mItemList.add(new RecycleViewItemData(beanMyAction.activityOrderListResultList.get(i), WelfareAndActivityAdapter.TYPE_ACTIVITY));
//                                                }
//
//                                                for (int i = 0; i < beanMyAction.giftInfoListResultList.size(); i++) {
//
//                                                    mItemList.add(new RecycleViewItemData(beanMyAction.giftInfoListResultList.get(i), WelfareAndActivityAdapter.TYPE_WELFARE));
//                                                }
//
//
//                                                mAdapter.notifyDataSetChanged();
//
//                                                mView.updatePage(true);
//                                            } else {
//                                                mView.updatePage(false);
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onFailure(HttpException e, Response<String> response) {
//                                            super.onFailure(e, response);
//                                            mView.updatePage(false);
//                                        }
//                                    }
//
//        );
//
//        LiteHttpManager.getInstance().executeAsync(postRequest);

    }

    @Override
    public void detachView() {

    }
}
