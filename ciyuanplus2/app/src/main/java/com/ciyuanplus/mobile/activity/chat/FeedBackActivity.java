package com.ciyuanplus.mobile.activity.chat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.base.irecyclerview.OnRefreshListener;
import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.adapter.FeedBackAdapter;
import com.ciyuanplus.mobile.image_select.ImageLoader;
import com.ciyuanplus.mobile.image_select.ImgSelActivity;
import com.ciyuanplus.mobile.image_select.ImgSelConfig;
import com.ciyuanplus.mobile.image_select.common.Constant;
import com.ciyuanplus.mobile.image_select.utils.FileUtils;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.manager.CacheManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.FeedBackItem;
import com.ciyuanplus.mobile.net.parameter.GetFeedBackListApiParameter;
import com.ciyuanplus.mobile.net.parameter.SendFeedBacktApiParameter;
import com.ciyuanplus.mobile.net.parameter.UpLoadFileApiParameter;
import com.ciyuanplus.mobile.net.response.GetFeedBackListResponse;
import com.ciyuanplus.mobile.net.response.UpLoadFileResponse;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.PictureUtils;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.ciyuanplus.mobile.widget.LengthFilter;
import com.ciyuanplus.mobile.widget.LoadingDialog;
import com.ciyuanplus.mobile.widget.NoEmojiEditText;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.content.multi.FilePart;
import com.litesuits.http.request.content.multi.MultipartBody;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/6/23.
 * <p>
 * //V2.2.0 版本 添加逻辑  每3秒刷新一次  requested by 金虎哲
 */

public class FeedBackActivity extends MyBaseActivity implements OnRefreshListener, OnLoadMoreListener,
        com.scwang.smartrefresh.layout.listener.OnRefreshListener, com.ciyuanplus.base.irecyclerview.OnLoadMoreListener {
    private static final int MESSAGE_UPLOAD = 1002;
    @BindView(R.id.m_feed_back_back_image)
    ImageView mFeedBackBackImage;
    @BindView(R.id.m_feed_back_right_image)
    ImageView mFeedBackRightImage;
    @BindView(R.id.m_feed_back_common_title)
    RelativeLayout mFeedBackCommonTitle;
    @BindView(R.id.m_feed_back_input_edit)
    NoEmojiEditText mFeedBackInputEdit;
    @BindView(R.id.m_feed_back_add_image)
    ImageView mFeedBackAddImage;
    @BindView(R.id.m_feed_back_send_btn)
    TextView mFeedBackSendBtn;
    @BindView(R.id.m_feed_back_camera_image)
    ImageView mFeedBackCameraImage;
    @BindView(R.id.m_feed_back_photo_image)
    ImageView mFeedBackPhotoImage;
    @BindView(R.id.m_feed_back_image_lp)
    LinearLayout mFeedBackImageLp;
    @BindView(R.id.m_feed_back_bottom_lp)
    RelativeLayout mFeedBackBottomLp;
    @BindView(R.id.m_feed_back_opera_lp)
    LinearLayout mFeedBackOperaLp;
    @BindView(R.id.m_feed_back_chat_list)
    RecyclerView mFeedBackChatList;
    @BindView(R.id.m_feed_back_root)
    RelativeLayout mFeedBackRoot;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout mRefreshLayout;
    private int mPage;
    private Dialog mLoadingDialog;

    private String mInputString;
    private final ArrayList<FeedBackItem> mList = new ArrayList<>();
    private FeedBackAdapter mAdapter;
    private File tempFile;//选择头像相关的文件
    private String mHeadIconPath;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int MESSAGE_UPDATE = 1003;
            if (msg.what == MESSAGE_UPLOAD) {// 上传头像文件
                uploadImageFile();
            } else if (msg.what == MESSAGE_UPDATE) {// 倒计时
                mPage = 0;
                requestFeedBackList();
                mHandler.removeMessages(MESSAGE_UPDATE);
                // 每3秒一次
                int COUNT_DOWN_TIME = 3000;
                mHandler.sendEmptyMessageDelayed(MESSAGE_UPDATE, COUNT_DOWN_TIME);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_feed_back);

        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        this.initView();
        requestFeedBackList();
    }

    @Override
    protected void onPause() {
        //mHandler.removeMessages(MESSAGE_UPDATE);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mHandler.removeMessages(MESSAGE_UPDATE);
        //mHandler.sendEmptyMessageDelayed(MESSAGE_UPDATE, COUNT_DOWN_TIME);
    }

    @Override
    protected void onDestroy() {
        //mHandler.removeMessages(MESSAGE_UPDATE);
        super.onDestroy();
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);

        mFeedBackRoot.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            //现在认为只要控件将Activity向上推的高度超过了50屏幕高，就认为软键盘弹起  
            if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > 50)) {
                hidePhotoSelectView();
                resetListViewPostion();
                mFeedBackBottomLp.requestLayout();
            } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > 50)) {// 关闭
                resetListViewPostion();
            }
        });
        LoadingDialog.Builder builder = new LoadingDialog.Builder(this);
        builder.setMessage("加载中....");
        mLoadingDialog = builder.create();
        mLoadingDialog.setCanceledOnTouchOutside(false);

        mFeedBackInputEdit.setFilters(new InputFilter[]{new LengthFilter(255)});
        this.mFeedBackInputEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    mFeedBackAddImage.setVisibility(View.GONE);
                    mFeedBackSendBtn.setVisibility(View.VISIBLE);
                } else {
                    mFeedBackAddImage.setVisibility(View.VISIBLE);
                    mFeedBackSendBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

//        mFeedBackChatList.setPullRefreshEnable(true);
//        mFeedBackChatList.setPullLoadEnable(false);
//        mFeedBackChatList.setXListViewListener(this);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);//   LinearLayoutManager不能共用 真是坑爹
        mFeedBackChatList.setLayoutManager(linearLayoutManager);


        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
        mRefreshLayout.setEnableRefresh(true);
        mRefreshLayout.setEnableLoadMore(false);


        mAdapter = new FeedBackAdapter(this, mList, null);
        mFeedBackChatList.setAdapter(mAdapter);

        mFeedBackChatList.setOnTouchListener((view, motionEvent) -> {
            hidePhotoSelectView();
            return false;
        });
    }

    // 获取消息列表
    private void requestFeedBackList() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_FEED_BACK_LIST_URL);
        postRequest.setMethod(HttpMethods.Post);
        int PAGE_SIZE = 20;
        postRequest.setHttpBody(new GetFeedBackListApiParameter(mPage + "", PAGE_SIZE + "").getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(this) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
//                mFeedBackChatList.setRefreshing(false);
//                mFeedBackChatList.stopRefresh();
//                mFeedBackChatList.stopLoadMore();
                finishRefreshAndLoadMore();
                GetFeedBackListResponse response1 = new GetFeedBackListResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK) && response1.feedBackListInfo.list != null) {
                    if (mPage == 0) mList.clear();
                    for (int i = 0; i < response1.feedBackListInfo.list.length; i++) {
                        mList.add(0, response1.feedBackListInfo.list[i]);
                    }
                    mAdapter.notifyDataSetChanged();
                    if (mPage == 0) {
                        mFeedBackChatList.scrollToPosition(mList.size() + 1);
                    } else {
                        mFeedBackChatList.scrollToPosition(response1.feedBackListInfo.list.length + 1);
                    }
                    mPage++;

                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
//                mFeedBackChatList.setRefreshing(false);
//                mFeedBackChatList.stopRefresh();
//                mFeedBackChatList.stopLoadMore();
                finishRefreshAndLoadMore();
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    private void sendFeedBack(String type, String content) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_FEED_BACK_SEND_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new SendFeedBacktApiParameter(content, type).getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(this) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                if (mLoadingDialog != null && mLoadingDialog.isShowing())
                    mLoadingDialog.dismiss();
                resetView();
                ResponseData response1 = new ResponseData(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    mPage = 0;
                    requestFeedBackList();
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
                if (mLoadingDialog != null && mLoadingDialog.isShowing())
                    mLoadingDialog.dismiss();
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    @Override
    public void onRefresh() {
        requestFeedBackList();
    }

    private void resetView() {
        mFeedBackImageLp.setVisibility(View.GONE);
        mInputString = "";
        mFeedBackInputEdit.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_REQUEST_CAMERA_OPTION && resultCode == RESULT_OK) {
            if (tempFile != null) {
                mHeadIconPath = tempFile.getAbsolutePath();
                Constant.imageList.clear();
                mHandler.sendEmptyMessage(MESSAGE_UPLOAD);
            }
        } else if (requestCode == Constants.REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT);
            mHeadIconPath = pathList.get(0);
            Constant.imageList.clear();
            mHandler.sendEmptyMessage(MESSAGE_UPLOAD);
        }
    }

    // 上传头像文件， 如果成功就提交修改头像
    private void uploadImageFile() {
        if (Utils.isStringEmpty(mHeadIconPath)) return;
        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) mLoadingDialog.show();
        MultipartBody body = new MultipartBody();
        File origin = new File(mHeadIconPath);
        String fileName = origin.getName();
        String compressPath = PictureUtils.compressImage(mHeadIconPath,
                CacheManager.getInstance().getCacheDirectory() + fileName.substring(0, fileName.lastIndexOf(".")) + "compressImage.jpeg");
        File compressImage = new File(compressPath);
        if (compressImage.exists()) body.addPart(new FilePart("file", compressImage, "image/jpeg"));
        else body.addPart(new FilePart("file", new File(mHeadIconPath), "image/jpeg"));

        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_UPLOAD_FILE_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new UpLoadFileApiParameter().getRequestBody());
        postRequest.setHttpBody(body);
        postRequest.setHttpListener(new MyHttpListener<String>(this) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                UpLoadFileResponse response1 = new UpLoadFileResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
//                    changeAvatar(response1.url);
                    sendFeedBack("2", response1.url);
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
                if (mLoadingDialog != null && mLoadingDialog.isShowing())
                    mLoadingDialog.dismiss();
//                CommonToast.getInstance(getResources().getString(R.string.string_my_profile_upload_head_fail_alert), Toast.LENGTH_SHORT).show();

            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    // 打开图片选择activity
    private void startSelectImage() {

        RequestOptions options = new RequestOptions().placeholder(R.drawable.ic_default_image_007)
                .error(R.mipmap.imgfail).dontAnimate().centerCrop();

        ImageLoader loader = (context, path, imageView) -> Glide.with(App.mContext).load(path).apply(options).into(imageView);
        ImgSelConfig config = new ImgSelConfig.Builder(this, loader)
                .multiSelect(true)
                .btnBgColor(Color.GRAY)
                .btnTextColor(Color.BLUE)
                .statusBarColor(Color.parseColor("#ffffff"))
                .title("图片")
                .titleColor(Color.BLACK)
                .titleBgColor(Color.parseColor("#ffffff"))
                .cropSize(1, 1, 200, 200)
                .needCrop(true)
                .needCamera(false)
                .maxNum(1)
                .build();
        ImgSelActivity.startActivity(this, config, Constants.REQUEST_CODE_SELECT_IMAGE);
    }

    //隐藏虚拟键盘
    private void HideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

        }
    }

    private void hidePhotoSelectView() {
        mFeedBackImageLp.setVisibility(View.GONE);
//        resetListViewPostion();
    }

    private void resetListViewPostion() {
        mFeedBackChatList.post(() -> {
            mFeedBackChatList.scrollToPosition(mAdapter.getItemCount() + 1);// 搞不懂为啥会这样
        });
    }

    @OnClick({R.id.m_feed_back_add_image, R.id.m_feed_back_send_btn, R.id.m_feed_back_back_image,
            R.id.m_feed_back_camera_image, R.id.m_feed_back_photo_image, R.id.m_feed_back_root})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        switch (view.getId()) {
            case R.id.m_feed_back_add_image:
                HideKeyboard(mFeedBackInputEdit);
                mFeedBackImageLp.setVisibility(View.VISIBLE);
                resetListViewPostion();
                break;
            case R.id.m_feed_back_send_btn:
                mInputString = mFeedBackInputEdit.getText().toString();
//            HideKeyboard(mFeedBackInputEdit);
                if (Utils.isStringEmpty(mInputString)) return;
                sendFeedBack("1", mInputString);
                break;
            case R.id.m_feed_back_camera_image:
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(FeedBackActivity.this.getPackageManager()) != null) {
                    tempFile = new File(FileUtils.createRootPath(FeedBackActivity.this) + "/" + System.currentTimeMillis() + ".jpg");
                    FileUtils.createFile(tempFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                    startActivityForResult(cameraIntent, Constants.REQUEST_CODE_REQUEST_CAMERA_OPTION);
                } else {
                    Toast.makeText(FeedBackActivity.this, getString(R.string.image_select_open_camera_failure), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.m_feed_back_photo_image:
                startSelectImage();
                break;
            case R.id.m_feed_back_root:
                hidePhotoSelectView();
                break;
            case R.id.m_feed_back_back_image:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onLoadMore() {
        // do nothing
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        requestFeedBackList();
    }

    @Override
    public void onLoadMore(RefreshLayout refreshlayout) {

    }

    private void finishRefreshAndLoadMore() {

        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
    }
}
