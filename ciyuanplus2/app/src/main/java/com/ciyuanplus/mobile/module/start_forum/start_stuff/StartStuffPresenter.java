package com.ciyuanplus.mobile.module.start_forum.start_stuff;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.ImageLoader;
import com.ciyuanplus.mobile.image_select.ImgSelActivity;
import com.ciyuanplus.mobile.image_select.ImgSelConfig;
import com.ciyuanplus.mobile.image_select.common.Constant;
import com.ciyuanplus.mobile.image_select.utils.FileUtils;
import com.ciyuanplus.mobile.manager.CacheManager;
import com.ciyuanplus.mobile.manager.CommunityManager;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.popup.select_image_pop.SelectImagePopActivity;
import com.ciyuanplus.mobile.module.settings.address.AddressManagerActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.CommunityItem;
import com.ciyuanplus.mobile.net.bean.FreshNewItem;
import com.ciyuanplus.mobile.net.parameter.AddStuffApiParameter;
import com.ciyuanplus.mobile.net.parameter.UpLoadFileApiParameter;
import com.ciyuanplus.mobile.net.parameter.UpdateStuffApiParameter;
import com.ciyuanplus.mobile.net.response.UpLoadFilesResponse;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.PictureUtils;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.ciyuanplus.mobile.widget.CustomDialog;
import com.google.gson.Gson;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.content.multi.FilePart;
import com.litesuits.http.request.content.multi.MultipartBody;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;
import com.sendtion.xrichtext.RichTextEditor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * Created by Alen on 2017/12/11.
 */

public class StartStuffPresenter implements StartStuffContract.Presenter {
    private final StartStuffContract.View mView;

    public static final int MAX_IMAGE_SIZE = 50;
    public FreshNewItem mFreshNewItem;
    public boolean isEdit = false;

    public final ArrayList<String> mImagepathList = new ArrayList<>();
    private final ArrayList<String> mUploadUrls = new ArrayList<>();
    private final HashMap<String, String> mUploadMap = new HashMap<>();
    private File tempFile;
    private String mContent;
    public String mSelectedCommunityId;
    @Inject
    public StartStuffPresenter(StartStuffContract.View mView) {
        this.mView = mView;

    }
    @Override
    public void detachView() {
    }

    @Override
    public void initData(Intent intent, Bundle savedInstanceState) {
        mFreshNewItem = (FreshNewItem) intent.getSerializableExtra(Constants.INTENT_UDPATE_NENWS_ITEM);
        isEdit = !(mFreshNewItem == null);
        if (mFreshNewItem == null && savedInstanceState != null)
            mFreshNewItem = (FreshNewItem) savedInstanceState.getSerializable(Constants.INTENT_UDPATE_NENWS_ITEM);

        if (isEdit) { // 如果是编辑， 需要先把之前的信息放进来
            if (!Utils.isStringEmpty(mFreshNewItem != null ? mFreshNewItem.imgs : null)) {
                mImagepathList.clear();
                mUploadMap.clear();
                String[] paths = mFreshNewItem.imgs.split(",");
                for (String path : paths) {
                    mImagepathList.add(Constants.IMAGE_LOAD_HEADER + path);
                    mUploadMap.put(Constants.IMAGE_LOAD_HEADER + path, path);
                }
            }
            mSelectedCommunityId = mFreshNewItem.currentCommunityUuid;
            if (!Utils.isStringEmpty(mFreshNewItem.contentText)) mView.showEditData(mFreshNewItem.contentText);
        } else {// 默认发布到当前小区
            mSelectedCommunityId = UserInfoData.getInstance().getUserInfoItem().currentCommunityUuid;
        }
        mView.updateView();
    }

    @Override
    public void submmit() {
        String title = mView.getTitleString();
        if (Utils.isStringEmpty(title)) {
            CommonToast.getInstance(mView.getDefaultContext().getResources().getString(R.string.start_news_empty_alert)).show();
            mView.dismissLoadingDialog();
            return;
        }
        mContent = getEditData();
        if (Utils.isStringEmpty(mContent)) {
            CommonToast.getInstance(mView.getDefaultContext().getResources().getString(R.string.start_post_content_empty_alert)).show();
            mView.dismissLoadingDialog();
            return;
        }
        if (mImagepathList.size() == 0) {
            CommonToast.getInstance(mView.getDefaultContext().getResources().getString(R.string.start_stuff_image_empty_alert)).show();
            mView.dismissLoadingDialog();
            return;
        }
        if (mView.getPayCheckState() && mView.getPrice() == 0.0f) {
            CommonToast.getInstance(mView.getDefaultContext().getResources().getString(R.string.start_stuff_price_zero_alert)).show();
            mView.dismissLoadingDialog();
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

    @Override
    public String getEditData() {
        List<RichTextEditor.EditData> editList = mView.getContextView().buildEditData();
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

    // 上传多个文件
    private void uploadImageFile() {
        mView.showLoadingDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
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
                postRequest.setHttpBody(new UpLoadFileApiParameter().getRequestBody());
                postRequest.setHttpBody(body);
                postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
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
//                        String[] paths = mFreshNewItem.imgs.split(",");
//                        for(int i = 0; i < paths.length; i++) {
//                            mUploadUrls.add(paths[i]);
//                        }
                                for (int i = 0; i < response1.fileList.url.length; i++) {
                                    mUploadMap.put(needUploadFile.get(i), response1.fileList.url[i]);
                                }
                                updateNewPost();
                            }
                        } else {
                            mView.dismissLoadingDialog();
                        }

                    }

                    @Override
                    public void onFailure(HttpException e, Response<String> response) {
                        super.onFailure(e, response);
                        mView.dismissLoadingDialog();
                    }
                });
                LiteHttpManager.getInstance().executeAsync(postRequest);
                Constant.imageList.clear();
            }
        }).start();
    }

    //新建帖子
    private void startNewPost() {
        mView.showLoadingDialog();
        mContent = getEditData();// 需要先上传完成之后才能搞

        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_ADD_STUFF);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new AddStuffApiParameter(mSelectedCommunityId,mView.getTitleString(),
                mContent,"", mUploadUrls, mView.getPriceString())
                .getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                mView.dismissLoadingDialog();

                ResponseData response1 = new ResponseData(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                    // 同时需要刷新物品列表和帖子列表
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_ZONE_NEWS_LIST));
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_ALL_NEWS_LIST));
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_STUFF_LIST));

                    Intent intent = new Intent("android.intent.action.muhomerefresh");
                    intent.putExtra("refresh","issuebaby");
                    LocalBroadcastManager.getInstance(App.mContext).sendBroadcast(intent);
                    ((Activity)mView).finish();// 关闭页面
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
                mView.dismissLoadingDialog();

//                CommonToast.getInstance(App.mContext.getResources().getString(R.string.start_news_fail_alert), Toast.LENGTH_SHORT).show();
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    // 编辑旧帖子
    private void updateNewPost() {
        mView.showLoadingDialog();
        mContent = getEditData();// 需要先上传完成之后才能搞

        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_UPDATE_STUFF);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new UpdateStuffApiParameter(mFreshNewItem.postUuid, mSelectedCommunityId,
                mView.getTitleString(), mContent, "", mUploadUrls, mView.getPriceString()).getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                mView.dismissLoadingDialog();

                ResponseData response1 = new ResponseData(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_ZONE_NEWS_LIST));
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_ALL_NEWS_LIST));
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_REFRESH_NEWS_ITEM));// 需要更新帖子，否则不及时
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_STUFF_LIST));
                    ((Activity)mView).finish();// 关闭页面
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
                mView.dismissLoadingDialog();

//                CommonToast.getInstance(App.mContext.getResources().getString(R.string.start_news_fail_alert), Toast.LENGTH_SHORT).show();
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    // 打开图片选择activity
    private void startSelectImage() {
        ImageLoader loader = (context, path, imageView) -> Glide.with(App.mContext).load(path).apply(new RequestOptions().error(R.mipmap.imgfail)).into(imageView);
        ImgSelConfig config = new ImgSelConfig.Builder(mView.getDefaultContext(), loader)
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
        ImgSelActivity.startActivity((Activity) mView, config, Constants.REQUEST_CODE_SELECT_IMAGE);
    }

    @Override
    public String[] getEditImages() {
        String[] images = new String[mImagepathList.size()];

        int index = 0;
        List<RichTextEditor.EditData> editList = mView.getContextView().buildEditData();
        for (RichTextEditor.EditData itemData : editList) {
            if (itemData.inputStr != null) {

            } else if (itemData.imagePath != null) {
                images[index] = itemData.imagePath;
                index++;
            }
        }
        return images;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_SELECT_PITCURE_OPTION &&
                resultCode == Activity.RESULT_OK && data != null) {
            // 选择头像的方式
            String option = data.getStringExtra(SelectImagePopActivity.SELECTED);
            if (Utils.isStringEquals(option, SelectImagePopActivity.LOACL_PHOTO)) {
                startSelectImage();
            } else {
                if (mImagepathList.size() < MAX_IMAGE_SIZE) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (cameraIntent.resolveActivity(mView.getDefaultContext().getPackageManager()) != null) {
                        tempFile = new File(FileUtils.createRootPath(mView.getDefaultContext()) + "/" + System.currentTimeMillis() + ".jpg");
                        FileUtils.createFile(tempFile);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                        ((Activity) mView).startActivityForResult(cameraIntent, Constants.REQUEST_CODE_REQUEST_CAMERA_OPTION);
                    } else {
                        Toast.makeText((Activity) mView, mView.getDefaultContext().getString(R.string.image_select_open_camera_failure), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else if (requestCode == Constants.REQUEST_CODE_REQUEST_CAMERA_OPTION && resultCode == Activity.RESULT_OK) {
            if (tempFile != null) {
                mImagepathList.add(tempFile.getAbsolutePath());
                Constant.imageList.clear();
                //Constant.imageList.add(tempFile.getAbsolutePath());
                mView.getContextView().insertImage(tempFile.getAbsolutePath(), Utils.getScreenWidth());
            }
        } else if (requestCode == Constants.REQUEST_CODE_SELECT_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT);
            for (int i = 0; i < pathList.size(); i++) {
                File file = new File(pathList.get(i));
                if (file.exists()) {
                    mImagepathList.add(pathList.get(i));
                    mView.getContextView().insertImage(pathList.get(i), Utils.getScreenWidth());
                } else { // 这里有可能选到没有的zhaopian
                    CommonToast.getInstance("图片路径无效").show();
                }
            }
            Constant.imageList.clear();
        } else if (requestCode == Constants.REQUEST_CODE_SELECT_STUFF_COMMUNITY && resultCode == Activity.RESULT_OK && data != null) {
            CommunityItem item = new Gson().fromJson(data.getStringExtra(Constants.INTENT_COMMUNITY_ITEM), CommunityItem.class);
            if (item != null) {
                checkDetailAddress(item);
            }
        }
    }

    //检查是否有详细地址
    private void checkDetailAddress(CommunityItem item) {
        if (!CommunityManager.getInstance().checkHasDetailAddress(item.uuid)) {// meiyou
            CustomDialog.Builder builder = new CustomDialog.Builder(mView.getDefaultContext());
            builder.setMessage("主人，当前的小区需要添加有效的详细地址哟~");
            builder.setPositiveButton("去添加", (dialog, which) -> {
                Intent intent = new Intent(mView.getDefaultContext(), AddressManagerActivity.class);
                mView.getDefaultContext().startActivity(intent);
                dialog.dismiss();
            });
            builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
            CustomDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        } else {
            mSelectedCommunityId = item.uuid;
            mView.setZoneName(item.commName);
        }
    }

    @Override
    public void removeImage(String image) {
        mImagepathList.remove(image);
    }

    @Override
    public void saveInstanceState(Bundle outState) {
        if (mFreshNewItem != null)
            outState.putSerializable(Constants.INTENT_UDPATE_NENWS_ITEM, mFreshNewItem);
    }
}
