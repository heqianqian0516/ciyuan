package com.ciyuanplus.mobile.module.start_forum.start_post;

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
import com.ciyuanplus.mobile.adapter.PopupTagAdapter;
import com.ciyuanplus.mobile.image_select.ImageLoader;
import com.ciyuanplus.mobile.image_select.ImgSelActivity;
import com.ciyuanplus.mobile.image_select.ImgSelConfig;
import com.ciyuanplus.mobile.image_select.common.Constant;
import com.ciyuanplus.mobile.image_select.utils.FileUtils;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.manager.CacheManager;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.PostTypeManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.module.popup.select_image_pop.SelectImagePopActivity;
import com.ciyuanplus.mobile.module.popup.start_post_guide.StartPostGuideActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.FreshNewItem;
import com.ciyuanplus.mobile.net.bean.PostTypeItem;
import com.ciyuanplus.mobile.net.parameter.UpdateOldPostApiParameter;
import com.ciyuanplus.mobile.net.response.UpLoadFilesResponse;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.PictureUtils;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonTitleBar;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.ciyuanplus.mobile.widget.CustomDialog;
import com.ciyuanplus.mobile.widget.LengthFilter;
import com.ciyuanplus.mobile.widget.LoadingDialog;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.content.multi.FilePart;
import com.litesuits.http.request.content.multi.MultipartBody;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;
import com.sendtion.xrichtext.RichTextEditor;
import com.sendtion.xrichtext.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 长文编辑页面
 * Created by Alen on 2018/1/15.
 */

public class StartPostActivity extends MyBaseActivity {
    private static final int MAX_IMAGE_SIZE = 20;
    @BindView(R.id.m_start_post_add_image)
    ImageView mStartPostAddImage;
    @BindView(R.id.m_start_post_anonymous_check)
    CheckBox mStartPostAnonymousCheck;
    @BindView(R.id.m_start_post_target_zone_check)
    ImageView mStartPostTargetZoneCheck;
    @BindView(R.id.m_start_post_bottom_lp)
    LinearLayout mStartPostBottomLp;
    @BindView(R.id.m_start_post_title)
    EditText mStartPostTitle;
    @BindView(R.id.m_start_post_content)
    RichTextEditor mStartPostContent;
    @BindView(R.id.m_start_post_common_title)
    CommonTitleBar m_js_common_title;

    private Dialog mLoadingDialog;
    private FreshNewItem mNewsItem;
    private boolean isEdit = false;
    private boolean isWorldChecked = true;

    private final ArrayList<String> mImagepathList = new ArrayList<>();
    private final ArrayList<String> mUploadUrls = new ArrayList<>();
    private final HashMap<String, String> mUploadMap = new HashMap<>();
    private File tempFile;
    private String mTitle;
    private String mContent;
    private PopupWindow mWorldPopupWindow;

    private PostTypeItem mSelectedType;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_start_post);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        mNewsItem = (FreshNewItem) getIntent().getSerializableExtra(Constants.INTENT_UDPATE_NENWS_ITEM);
        if (mNewsItem == null && savedInstanceState != null)
            mNewsItem = (FreshNewItem) savedInstanceState.getSerializable(Constants.INTENT_UDPATE_NENWS_ITEM);

        isEdit = !(mNewsItem == null);

        this.initView();

        if (!SharedPreferencesManager.getBoolean(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_START_POST_HAS_GUIDE, false)) {
            Intent intent = new Intent(this, StartPostGuideActivity.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mNewsItem != null)
            outState.putSerializable(Constants.INTENT_UDPATE_NENWS_ITEM, mNewsItem);
        super.onSaveInstanceState(outState);
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);
        m_js_common_title.setLeftImage(R.mipmap.nav_icon_back);
        m_js_common_title.setRightImage(R.mipmap.nav_icon_save);
        m_js_common_title.setLeftClickListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View view) {
                onBackPressed();
            }
        });
        m_js_common_title.setCenterText("发布长文");
        m_js_common_title.setRightClickListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View view) {
                if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                    CommonToast.getInstance("正在发布，请勿重复点击").show();
                    return;
                }

                if (mLoadingDialog != null && !mLoadingDialog.isShowing()) mLoadingDialog.show();

                mTitle = mStartPostTitle.getText().toString();
                if (Utils.isStringEmpty(mTitle)) {
                    CommonToast.getInstance(getResources().getString(R.string.start_news_empty_alert)).show();
                    if (mLoadingDialog != null && mLoadingDialog.isShowing())
                        mLoadingDialog.dismiss();
                    return;
                }
                mContent = getEditData();
                if (Utils.isStringEmpty(mContent)) {
                    CommonToast.getInstance(getResources().getString(R.string.start_post_content_empty_alert)).show();
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
        mStartPostTitle.setFilters(new InputFilter[]{new LengthFilter(50)});

        mStartPostContent.setmImagePreViewListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View view) {
                int pos = mStartPostContent.getImageViewIndex(view);

                Intent intent = new Intent(App.mContext, FullScreenImageActivity.class);
                Bundle b = new Bundle();
                b.putStringArray(Constants.INTENT_FULL_SCREEN_IMAGE_VIEWER_IMAGES, getEditImages());
                intent.putExtras(b);
                intent.putExtra(Constants.INTENT_FULL_SCREEN_IMAGE_VIEWER_INDEX, pos);
                StartPostActivity.this.startActivity(intent);
            }
        });
        mStartPostContent.setmOnDeleteImageListener((image) -> mImagepathList.remove(image));
        mStartPostContent.setMaxLength(1800);

        LoadingDialog.Builder builder = new LoadingDialog.Builder(this);
        builder.setMessage("加载中....");
        mLoadingDialog = builder.create();
        mLoadingDialog.setCanceledOnTouchOutside(false);

        PopupTagAdapter popupTagAdapter = new PopupTagAdapter(this, PostTypeManager.getInstance().getTypeInfo());

        if (isEdit) { // 如果是编辑， 需要先把之前的信息放进来
            if (!Utils.isStringEmpty(mNewsItem.imgs)) {
                mImagepathList.clear();
                mUploadMap.clear();
                String[] paths = mNewsItem.imgs.split(",");
                for (String path : paths) {
                    mImagepathList.add(Constants.IMAGE_LOAD_HEADER + path);
                    mUploadMap.put(Constants.IMAGE_LOAD_HEADER + path, path);
                }
            }
            //mSelectedType = PostTypeManager.getInstance().getType();//
            //updateTagView();

            mStartPostAnonymousCheck.setChecked(mNewsItem.isAnonymous == 1);
            //mStartPostAnonymousCheck.setEnabled(false);
            mStartPostAnonymousCheck.setOnTouchListener((v, event) -> {
                CommonToast.getInstance("编辑时不能更改匿名状态").show();
                return true;
            });
            isWorldChecked = (mNewsItem.isPublic == 1);
            mStartPostTargetZoneCheck.setImageResource(isWorldChecked ? R.mipmap.launch_icon_gongkai : R.mipmap.launch_icon_xiaoqu);
            mSelectedType = PostTypeManager.getInstance().getPostType(mNewsItem.postType);
            popupTagAdapter.setmDefalutType(mSelectedType);

            mStartPostTitle.setText(mNewsItem.title);
            if (!Utils.isStringEmpty(mNewsItem.contentText)) showEditData(mNewsItem.contentText);
        }

        mStartPostAnonymousCheck.setOnCheckedChangeListener((compoundButton, b) -> CommonToast.getInstance(b ? "您已选择匿名发布" : "您已取消匿名发布").show());
        mStartPostTargetZoneCheck.setOnClickListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View v) {
                showWorldPopUpWindow();

            }
        });
        mStartPostTitle.requestFocus();

    }

    // 选择发布在小区还是世界的弹框
    private void showWorldPopUpWindow() {
        if (mWorldPopupWindow == null) {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view1 = inflater != null ? inflater.inflate(R.layout.layout_world_select_popup, null) : null;
            mWorldPopupWindow = new PopupWindow(view1, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mWorldPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            view1.findViewById( R.id.m_world_select_popup_world).setOnClickListener(new MyOnClickListener() {
                @Override
                public void performRealClick(View v) {
                    mWorldPopupWindow.dismiss();
                    isWorldChecked = true;
                    mStartPostTargetZoneCheck.setImageResource(R.mipmap.launch_icon_gongkai);
                }
            });
            view1.findViewById( R.id.m_world_select_popup_zone).setOnClickListener(new MyOnClickListener() {
                @Override
                public void performRealClick(View v) {
                    mWorldPopupWindow.dismiss();
                    isWorldChecked = false;
                    mStartPostTargetZoneCheck.setImageResource(R.mipmap.launch_icon_xiaoqu);
                }
            });
        }
        mWorldPopupWindow.showAsDropDown(mStartPostTargetZoneCheck, -Utils.dip2px(65), -Utils.dip2px(160));
        mWorldPopupWindow.setFocusable(true);
        mWorldPopupWindow.setOutsideTouchable(true);
        mWorldPopupWindow.update();
    }

    @Override
    public void onBackPressed() {
        mTitle = mStartPostTitle.getText().toString();
        mContent = getEditData();// 需要先上传完成之后才能搞
        if (Utils.isStringEmpty(mTitle) && Utils.isStringEmpty(mContent)) {
            finish();
        } else {
            CustomDialog.Builder builder = new CustomDialog.Builder(StartPostActivity.this);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_SELECT_PITCURE_OPTION && resultCode == RESULT_OK && data != null) {
            // 选择头像的方式
            String option = data.getStringExtra(SelectImagePopActivity.SELECTED);
            if (Utils.isStringEquals(option, SelectImagePopActivity.LOACL_PHOTO)) {
                startSelectImage();
            } else {
                if (mImagepathList.size() < MAX_IMAGE_SIZE) {
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
                Constant.imageList.clear();
                //Constant.imageList.add(tempFile.getAbsolutePath());
                mStartPostContent.insertImage(tempFile.getAbsolutePath(), Utils.getScreenWidth());
            }
        } else if (requestCode == Constants.REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT);
            for (int i = 0; i < pathList.size(); i++) {
                File file = new File(pathList.get(i));
                if (file.exists()) {
                    mImagepathList.add(pathList.get(i));
                    mStartPostContent.insertImage(pathList.get(i), Utils.getScreenWidth());
                } else { // 这里有可能选到没有的zhaopian
                    CommonToast.getInstance("图片路径无效").show();
                }
            }
            Constant.imageList.clear();
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
                .maxNum(MAX_IMAGE_SIZE - mImagepathList.size())
                .build();
        ImgSelActivity.startActivity(this, config, Constants.REQUEST_CODE_SELECT_IMAGE);
    }

    //新建帖子  已经不允许发布新的post类型帖子了
    private void startNewPost() {
//        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) mLoadingDialog.show();
//        mContent = getEditData();// 需要先上传完成之后才能搞
//
//        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_ADD_MY_POST_URL);
//        postRequest.setMethod(HttpMethods.Post);
//        postRequest.setHttpBody(new AddNewPostApiParameter(mTitle, mContent, mUploadUrls, mStartPostAnonymousCheck.isChecked() ? "1" : "0", isWorldChecked ? "1" : "0", mSelectedType == null ? "" : mSelectedType.typeId).getRequestBody());
//
//        String sessionKey = SharedPreferencesManager.getString(
//                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
//        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
//        postRequest.setHttpListener(new MyHttpListener<String>() {
//            @Override
//            public void onSuccess(String s, Response<String> response) {
//                super.onSuccess(s, response);
//                if (mLoadingDialog != null && mLoadingDialog.isShowing())
//                    mLoadingDialog.dismiss();
//                ResponseData response1 = new ResponseData(s);
//                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
//                    CommonToast.getInstance(response1.mMsg).show();
//                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_ZONE_NEWS_LIST));
//                    if (isWorldChecked)
//                        EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_ALL_NEWS_LIST));
//
//                    finish();// 关闭页面
//                } else {
//                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                super.onFailure(e, response);
//                if (mLoadingDialog != null && mLoadingDialog.isShowing())
//                    mLoadingDialog.dismiss();
////                CommonToast.getInstance(App.mContext.getResources().getString(R.string.start_news_fail_alert), Toast.LENGTH_SHORT).show();
//            }
//        });
//        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    // 编辑旧帖子
    private void updateNewPost() {
        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) mLoadingDialog.show();
        mContent = getEditData();// 需要先上传完成之后才能搞

        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_UPDATE_OLD_POST_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new UpdateOldPostApiParameter(mTitle, mNewsItem.postUuid, mContent,
                mUploadUrls, mStartPostAnonymousCheck.isChecked() ? "1" : "0", isWorldChecked ? "1" : "0",
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


    // 上传多个文件
    private void uploadImageFile() {

        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) mLoadingDialog.show();
        MultipartBody body = new MultipartBody();
        boolean fileNotEmpty = false;
        final List<String> needUploadFile = new ArrayList<>();
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
            needUploadFile.add(mImagepathList.get(i));
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
                        mUploadMap.clear();
                        for (int i = 0; i < response1.fileList.url.length; i++) {
                            mUploadMap.put(needUploadFile.get(i), response1.fileList.url[i]);
                        }
                        startNewPost();
                    } else {
//                        String[] paths = mNewsItem.imgs.split(",");
//                        for(int i = 0; i < paths.length; i++) {
//                            mUploadUrls.add(paths[i]);
//                        }
                        for (int i = 0; i < response1.fileList.url.length; i++) {
                            mUploadMap.put(needUploadFile.get(i), response1.fileList.url[i]);
                        }
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
        mStartPostContent.clearAllLayout();
        Constant.imageList.clear();// 清掉当前的图片，防止下次进来还有
    }

    public void deleteImage(String tag) {

        mUploadUrls.remove(tag);
    }

    private String[] getEditImages() {
        String[] images = new String[mImagepathList.size()];
        int index = 0;
        List<RichTextEditor.EditData> editList = mStartPostContent.buildEditData();
        for (RichTextEditor.EditData itemData : editList) {
            if (itemData.inputStr != null) {

            } else if (itemData.imagePath != null) {
                images[index] = itemData.imagePath;
                index++;
            }
        }
        return images;
    }

    private String getEditData() {
        List<RichTextEditor.EditData> editList = mStartPostContent.buildEditData();
        StringBuilder content = new StringBuilder();
        mUploadUrls.clear();
        for (RichTextEditor.EditData itemData : editList) {
            if (itemData.inputStr != null) {
                content.append(itemData.inputStr);
            } else if (itemData.imagePath != null) {
                content.append("<img src=\"").append(mUploadMap.get(itemData.imagePath)).append("\"/>");
                mUploadUrls.add(mUploadMap.get(itemData.imagePath));
            }
        }
        return content.toString();
    }

    private void showEditData(final String content) {
        mStartPostContent.post(() -> {
            mStartPostContent.clearAllLayout();
            List<String> textList = StringUtils.cutStringByImgTag(content);
            for (int i = 0; i < textList.size(); i++) {
                String text = textList.get(i);
                if (text.contains("<img")) {
                    String imagePath = StringUtils.getImgSrc(text);
//                        int width = Utils.getScreenWidth();
//                        int height = Utils.getScreenHeight();
                    mStartPostContent.measure(0, 0);
//                        Bitmap bitmap = ImageUtils.getSmallBitmap(imagePath, width, height);
//                        if (bitmap != null){
//                            mStartPostContent.addImageViewAtIndex(mStartPostContent.getLastIndex(), imagePath);
//                        } else {
//                            mStartPostContent.addEditTextAtIndex(mStartPostContent.getLastIndex(), text);
//                        }
                    mStartPostContent.addImageViewAtIndex(mStartPostContent.getLastIndex(), Constants.IMAGE_LOAD_HEADER + imagePath, "");

                } else {
                    mStartPostContent.addEditTextAtIndex(mStartPostContent.getLastIndex(), text);
                }
            }
        });

    }

    @OnClick({R.id.m_start_post_add_image})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        switch (view.getId()) {
            case R.id.m_start_post_add_image:
                if (mImagepathList.size() >= MAX_IMAGE_SIZE) {
                    CommonToast.getInstance("最多上传" + MAX_IMAGE_SIZE + "张图片").show();
                    return;
                }
                Intent intent = new Intent(StartPostActivity.this, SelectImagePopActivity.class);
                StartPostActivity.this.startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_PITCURE_OPTION);
                break;
        }
    }
}