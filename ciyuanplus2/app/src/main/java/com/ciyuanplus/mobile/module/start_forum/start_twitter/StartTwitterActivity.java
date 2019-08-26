package com.ciyuanplus.mobile.module.start_forum.start_twitter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.activity.news.FullScreenImageActivity;
import com.ciyuanplus.mobile.adapter.StartTwitterImageAdapter;
import com.ciyuanplus.mobile.image_select.ImageLoader;
import com.ciyuanplus.mobile.image_select.ImgSelActivity;
import com.ciyuanplus.mobile.image_select.ImgSelConfig;
import com.ciyuanplus.mobile.image_select.common.Constant;
import com.ciyuanplus.mobile.image_select.utils.FileUtils;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.manager.CacheManager;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.module.popup.select_image_pop.SelectImagePopActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.FreshNewItem;
import com.ciyuanplus.mobile.net.bean.PostTypeItem;
import com.ciyuanplus.mobile.net.parameter.AddNewApiParameter;
import com.ciyuanplus.mobile.net.parameter.UpdateNewApiParameter;
import com.ciyuanplus.mobile.net.response.UpLoadFilesResponse;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.PictureUtils;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonTitleBar;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.ciyuanplus.mobile.widget.CustomDialog;
import com.ciyuanplus.mobile.widget.LengthFilter;
import com.ciyuanplus.mobile.widget.LoadingDialog;
import com.ciyuanplus.mobile.widget.SleftAdapterGrideView;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.content.multi.FilePart;
import com.litesuits.http.request.content.multi.MultipartBody;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by Alen on 2017/5/12.
 * <p>
 * 发布和编辑新鲜事页面
 * V
 *
 * 这个暂时不需要改用MVP了   以后可能会删掉
 */

public class StartTwitterActivity extends MyBaseActivity {
    @BindView(R.id.m_start_twitter_camera_btn)
    ImageView mStartTwitterCameraBtn;
    @BindView(R.id.m_start_twitter_anonymous_check)
    CheckBox mStartTwitterAnonymousCheck;
    @BindView(R.id.m_start_twitter_target_zone_check)
    ImageView mStartTwitterTargetZoneCheck;
    @BindView(R.id.m_start_twitter_bottom_lp)
    LinearLayout mStartTwitterBottomLp;
    @BindView(R.id.m_start_twitter_content)
    EditText mStartTwitterContent;
    @BindView(R.id.m_start_twitter_images_gride)
    SleftAdapterGrideView mStartTwitterImagesGride;
    @BindView(R.id.m_start_twitter_content_lp)
    LinearLayout mStartTwitterContentLp;
    @BindView(R.id.m_start_twitter_common_title)
    CommonTitleBar m_js_common_title;


    private StartTwitterImageAdapter mImageAdatper;
    private FreshNewItem mNewsItem;
    private boolean isEdit = false;

    private boolean isWorldChecked = true;

    private String mTopic;
    private final ArrayList<String> mImagepathList = new ArrayList<>();
    private final ArrayList<String> mUploadUrls = new ArrayList<>();
    private File tempFile;
    private Dialog mLoadingDialog;

    //
    private PopupWindow mWorldPopupWindow;

    private PostTypeItem mSelectedType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_start_twitter);
        StatusBarCompat.compat(this,getResources().getColor(R.color.title));
        mNewsItem = (FreshNewItem) getIntent().getSerializableExtra(Constants.INTENT_UDPATE_NENWS_ITEM);
        if (mNewsItem == null && savedInstanceState != null)
            mNewsItem = (FreshNewItem) savedInstanceState.getSerializable(Constants.INTENT_UDPATE_NENWS_ITEM);

        isEdit = !(mNewsItem == null);

        this.initView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mNewsItem != null)
            outState.putSerializable(Constants.INTENT_UDPATE_NENWS_ITEM, mNewsItem);
        super.onSaveInstanceState(outState);
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);

        m_js_common_title.setRightImage(R.mipmap.nav_icon_save);
        m_js_common_title.setLeftClickListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View v) {
                onBackPressed();
            }
        });
        m_js_common_title.setCenterText("发布说说");
        m_js_common_title.setRightClickListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View view) {
                if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                    CommonToast.getInstance("正在发布，请勿重复点击").show();
                    return;
                }
                if (mLoadingDialog != null && !mLoadingDialog.isShowing()) mLoadingDialog.show();

                mTopic = mStartTwitterContent.getText().toString();
                if (Utils.isStringEmpty(mTopic) && mImagepathList.size() == 0) {
                    CommonToast.getInstance(getResources().getString(R.string.start_news_content_empty_alert)).show();
                    if (mLoadingDialog != null && mLoadingDialog.isShowing())
                        mLoadingDialog.dismiss();
                    return;
                }
                if (isEdit) {
                    if (mImagepathList.size() == 0) updateNewPost();
                    else uploadImageFile();
                } else {
                    if (mImagepathList.size() == 0) startNewPost();
                    else uploadImageFile();
                }
            }
        });
        LoadingDialog.Builder builder = new LoadingDialog.Builder(this);
        builder.setMessage("加载中....");
        mLoadingDialog = builder.create();
        mLoadingDialog.setCanceledOnTouchOutside(false);

        mStartTwitterContent.setFilters(new InputFilter[]{new LengthFilter(800)});


        mImageAdatper = new StartTwitterImageAdapter(this, mImagepathList);
        mStartTwitterImagesGride.setAdapter(mImageAdatper);
        mStartTwitterImagesGride.setOnItemClickListener((adapterView, view, i, l) -> {
            if (l == -1) {
                return;
            }
            int postion = (int) l;
            String[] imges = new String[mImagepathList.size()];
            for (int k = 0; k < mImagepathList.size(); k++) {
                imges[k] = mImagepathList.get(k);
            }
            Intent intent = new Intent(App.mContext, FullScreenImageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle b = new Bundle();
            b.putStringArray(Constants.INTENT_FULL_SCREEN_IMAGE_VIEWER_IMAGES, imges);
            intent.putExtras(b);
            intent.putExtra(Constants.INTENT_FULL_SCREEN_IMAGE_VIEWER_INDEX, postion);
            App.mContext.startActivity(intent);
        });

        if (isEdit) { // 如果是编辑， 需要先把之前的信息放进来
            if (!Utils.isStringEmpty(mNewsItem.contentText)) {
                mStartTwitterContent.setText(mNewsItem.contentText);
                mStartTwitterContent.setSelection(mNewsItem.contentText.length());
            }
            if (!Utils.isStringEmpty(mNewsItem.imgs)) {
                mImagepathList.clear();
                String[] paths = mNewsItem.imgs.split(",");
                for (String path : paths) {
                    mUploadUrls.add(path);
                    mImagepathList.add(path);
                }
                mImageAdatper.notifyDataSetChanged();
            }
            mStartTwitterAnonymousCheck.setChecked(mNewsItem.isAnonymous == 1);
            isWorldChecked = (mNewsItem.isPublic == 1);
            mStartTwitterTargetZoneCheck.setImageResource(isWorldChecked ? R.mipmap.launch_icon_gongkai : R.mipmap.launch_icon_xiaoqu);

            //mStartTwitterAnonymousCheck.setEnabled(false);
            mStartTwitterAnonymousCheck.setOnTouchListener((v, event) -> {
                CommonToast.getInstance("编辑时不能更改匿名状态").show();
                return true;
            });
        }

        mStartTwitterAnonymousCheck.setOnCheckedChangeListener((compoundButton, b) -> CommonToast.getInstance(b ? "您已选择匿名发布" : "您已取消匿名发布").show());
        mStartTwitterTargetZoneCheck.setOnClickListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View v) {
                showWorldPopUpWindow();
            }
        });

    }

    //新建帖子
    private void startNewPost() {
        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) mLoadingDialog.show();
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_ADD_MY_NEWS_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new AddNewApiParameter(mTopic, mUploadUrls, mStartTwitterAnonymousCheck.isChecked() ? "1" : "0", isWorldChecked ? "1" : "0", mSelectedType == null ? "" : mSelectedType.typeId).getRequestBody());

        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(this) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                if (mLoadingDialog != null && mLoadingDialog.isShowing())
                    mLoadingDialog.dismiss();
                ResponseData response1 = new ResponseData(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_ZONE_NEWS_LIST));
                    if (isWorldChecked)
                        EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_ALL_NEWS_LIST));

                    finish();// 关闭页面
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
                if (mLoadingDialog != null && mLoadingDialog.isShowing())
                    mLoadingDialog.dismiss();
//                CommonToast.getInstance(App.mContext.getResources().getString(R.string.start_news_fail_alert), Toast.LENGTH_SHORT).show();
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    // 选择发布在小区还是世界的弹框
    private void showWorldPopUpWindow() {
        if (mWorldPopupWindow == null) {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view1 = inflater != null ? inflater.inflate(R.layout.layout_world_select_popup, null) : null;
            mWorldPopupWindow = new PopupWindow(view1, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mWorldPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            view1.findViewById(R.id.m_world_select_popup_world).setOnClickListener(new MyOnClickListener() {
                @Override
                public void performRealClick(View v) {
                    mWorldPopupWindow.dismiss();
                    isWorldChecked = true;
                    mStartTwitterTargetZoneCheck.setImageResource(R.mipmap.launch_icon_gongkai);

                }
            });
            view1.findViewById(R.id.m_world_select_popup_zone).setOnClickListener(new MyOnClickListener() {
                @Override
                public void performRealClick(View v) {
                    mWorldPopupWindow.dismiss();
                    isWorldChecked = false;
                    mStartTwitterTargetZoneCheck.setImageResource(R.mipmap.launch_icon_xiaoqu);
                }
            });
        }
        mWorldPopupWindow.showAsDropDown(mStartTwitterTargetZoneCheck, -Utils.dip2px(65), -Utils.dip2px(160));
        mWorldPopupWindow.setFocusable(true);
        mWorldPopupWindow.setOutsideTouchable(true);
        mWorldPopupWindow.update();
    }
    // 编辑旧帖子
    private void updateNewPost() {
        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) mLoadingDialog.show();
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_UPDATE_MY_NEWS_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new UpdateNewApiParameter(mNewsItem.postUuid, mTopic, mUploadUrls,
                mStartTwitterAnonymousCheck.isChecked() ? "1" : "0", isWorldChecked ? "1" : "0",
                mSelectedType == null ? "" : mSelectedType.typeId).getRequestBody());

        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(this) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                if (mLoadingDialog != null && mLoadingDialog.isShowing())
                    mLoadingDialog.dismiss();
                ResponseData response1 = new ResponseData(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_ZONE_NEWS_LIST));
                    if (isWorldChecked)
                        EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_ALL_NEWS_LIST));
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_REFRESH_NEWS_ITEM));// 需要更新帖子，否则不及时

                    finish();// 关闭页面
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
                if (mLoadingDialog != null && mLoadingDialog.isShowing())
                    mLoadingDialog.dismiss();
//                CommonToast.getInstance(App.mContext.getResources().getString(R.string.start_news_fail_alert), Toast.LENGTH_SHORT).show();
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_SELECT_PITCURE_OPTION && resultCode == RESULT_OK && data != null) {
            // 选择头像的方式
            String option = data.getStringExtra(SelectImagePopActivity.SELECTED);
            if (Utils.isStringEquals(option, SelectImagePopActivity.LOACL_PHOTO)) {
                startSelectImage();
            } else {
                if (mImagepathList.size() < 9) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (cameraIntent.resolveActivity(this.getPackageManager()) != null) {
                        tempFile = new File(FileUtils.createRootPath(this) + "/" + System.currentTimeMillis() + ".jpg");
                        FileUtils.createFile(tempFile);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                        startActivityForResult(cameraIntent, Constants.REQUEST_CODE_REQUEST_CAMERA_OPTION);
                    } else {
                        Toast.makeText(this, getString(R.string.image_select_open_camera_failure), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else if (requestCode == Constants.REQUEST_CODE_REQUEST_CAMERA_OPTION && resultCode == RESULT_OK) {
            if (tempFile != null) {
                mImagepathList.add(tempFile.getAbsolutePath());
                Constant.imageList.add(tempFile.getAbsolutePath());
                mImageAdatper.notifyDataSetChanged();
            }
        } else if (requestCode == Constants.REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT);
            for (int i = 0; i < pathList.size(); i++)
                if (!mImagepathList.contains(pathList.get(i))) mImagepathList.add(pathList.get(i));
            mImageAdatper.notifyDataSetChanged();
        }
    }

    // 打开图片选择activity
    private void startSelectImage() {
        ImageLoader loader = (context, path, imageView) -> Glide.with(App.mContext).load(path).apply(new RequestOptions().error(R.mipmap.imgfail)).into(imageView);
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
                .maxNum(9)
                .build();
        ImgSelActivity.startActivity(this, config, Constants.REQUEST_CODE_SELECT_IMAGE);
    }

    // 上传多个文件
    private void uploadImageFile() {

        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) mLoadingDialog.show();
        MultipartBody body = new MultipartBody();
        boolean fileNotEmpty = false;
        for (int i = 0; i < mImagepathList.size(); i++) {
            if (!mImagepathList.get(i).startsWith("/storage")) continue;
            File origin = new File(mImagepathList.get(i));
            String fileName = origin.getName();
            String compressPath = PictureUtils.compressImage(mImagepathList.get(i),
                    CacheManager.getInstance().getCacheDirectory() + fileName.substring(0, fileName.lastIndexOf(".")) + "compressImage.jpeg");
            File compressImage = new File(compressPath);
            if (compressImage.exists())
                body.addPart(new FilePart("files", compressImage, "image/jpeg"));
            else body.addPart(new FilePart("files", new File(mImagepathList.get(i)), "image/jpeg"));
            fileNotEmpty = true;
        }
        if (!fileNotEmpty) {// 只有编辑会到这个逻辑里面， 否则在前面就截住了
            String[] uploadUrls = new String[mImagepathList.size()];
            for (int i = 0; i < mImagepathList.size(); i++) {
                if (!mImagepathList.get(i).startsWith("/storage"))
                    uploadUrls[i] = mImagepathList.get(i);
            }
            updateNewPost();
            return;
        }
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_UPLOAD_FILES_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(body);
        postRequest.setHttpListener(new MyHttpListener<String>(this) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                UpLoadFilesResponse response1 = new UpLoadFilesResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    if (!isEdit) {
                        mUploadUrls.clear();
                        Collections.addAll(mUploadUrls, response1.fileList.url);
                        startNewPost();
                    } else {
//                        String[] paths = mNewsItem.imgs.split(",");
//                        for(int i = 0; i < paths.length; i++) {
//                            mUploadUrls.add(paths[i]);
//                        }
                        Collections.addAll(mUploadUrls, response1.fileList.url);
                        updateNewPost();
                    }
                } else {
                    if (mLoadingDialog != null && mLoadingDialog.isShowing())
                        mLoadingDialog.dismiss();
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
        Constant.imageList.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constant.imageList.clear();// 清掉当前的图片，防止下次进来还有
    }

    public void deleteImage(String tag) {
        mUploadUrls.remove(tag);
        mImagepathList.remove(tag);
        Constant.imageList.remove(tag);
    }

    // 正在编辑的帖子，需要先判断一下是否需要保存  再退出

    @Override
    public void onBackPressed() {
        mTopic = mStartTwitterContent.getText().toString();
        if (Utils.isStringEmpty(mTopic) && mImagepathList.size() == 0) {
            finish();
        } else {
            CustomDialog.Builder builder = new CustomDialog.Builder(StartTwitterActivity.this);
            builder.setMessage("是否放弃发布？");
            builder.setPositiveButton("确定", (dialog, which) -> {
                dialog.dismiss();
                finish();
            });
            builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
            CustomDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

    @OnClick({R.id.m_start_twitter_camera_btn})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        switch (view.getId()) {
            case R.id.m_start_twitter_camera_btn:
                if (mImagepathList.size() >= 9) {
                    CommonToast.getInstance("最多上传9张图片").show();
                    return;
                }
                Intent intent = new Intent(StartTwitterActivity.this, SelectImagePopActivity.class);
                StartTwitterActivity.this.startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_PITCURE_OPTION);
                break;

        }
    }
}
