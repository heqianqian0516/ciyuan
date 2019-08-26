package com.ciyuanplus.mobile.module.start_forum.start_seek_help;

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
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.module.popup.select_image_pop.SelectImagePopActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.FreshNewItem;
import com.ciyuanplus.mobile.net.parameter.AddNewPostApiParameter;
import com.ciyuanplus.mobile.net.parameter.UpLoadFileApiParameter;
import com.ciyuanplus.mobile.net.parameter.UpdateNewPostApiParameter;
import com.ciyuanplus.mobile.net.response.UpLoadFilesResponse;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.PictureUtils;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
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

public class StartSeekHelpPresenter implements StartSeekHelpContract.Presenter {

    public boolean isWorldChecked = true;
    public final ArrayList<String> mImagepathList = new ArrayList<>();
    public final ArrayList<String> mUploadUrls = new ArrayList<>();
    public final HashMap<String, String> mUploadMap = new HashMap<>();
    private File tempFile;
    private String mContent;
    private static final int MAX_IMAGE_SIZE = 50;
    public FreshNewItem mNewsItem;
    private boolean isEdit = false;

    private final StartSeekHelpContract.View mView;

    @Inject
    public StartSeekHelpPresenter(StartSeekHelpContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void initData(Intent intent, Bundle savedInstanceState) {
        mNewsItem = (FreshNewItem) intent.getSerializableExtra(Constants.INTENT_UDPATE_NENWS_ITEM);
        isEdit = !(mNewsItem == null);
        if (mNewsItem == null && savedInstanceState != null)
            mNewsItem = (FreshNewItem) savedInstanceState.getSerializable(Constants.INTENT_UDPATE_NENWS_ITEM);

        if (isEdit) { //需要先把之前的信息放进来
            isWorldChecked = (mNewsItem != null ? mNewsItem.isPublic : 0) == 1;

            if (!Utils.isStringEmpty(mNewsItem.imgs)) {
                mImagepathList.clear();
                mUploadMap.clear();
                String[] paths = mNewsItem.imgs.split(",");
                for (String path : paths) {
                    mImagepathList.add(Constants.IMAGE_LOAD_HEADER + path);
                    mUploadMap.put(Constants.IMAGE_LOAD_HEADER + path, path);
                }
            }
            mView.updateView();
        }
    }

    @Override
    public void saveInstanceState(Bundle outState) {
        if (mNewsItem != null)
            outState.putSerializable(Constants.INTENT_UDPATE_NENWS_ITEM, mNewsItem);
    }

    @Override
    public void detachView() {
    }

    // 上传多个文件
    private void uploadImageFile(String surl, int biztype) {

        mView.showLoadingDialog();
        new Thread(new Runnable() {// 这里最好新开线程进行处理
            @Override
            public void run() {
                MultipartBody body = new UpLoadFileApiParameter().getRequestBody();
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
                    else
                        body.addPart(new FilePart("files", new File(mImagepathList.get(i)), "image/jpeg"));
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
                                startNewPost(surl, biztype);
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

    // 编辑旧帖子
    private void updateNewPost() {
        mView.showLoadingDialog();
        mContent = mView.getEditData();// 需要先上传完成之后才能搞

        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_UPDATE_MY_POST_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new UpdateNewPostApiParameter(mNewsItem.postUuid, mContent,
                mUploadUrls, mView.isAnonyChecked() ? "1" : "0", isWorldChecked ? "1" : "0",
                "", "", "", FreshNewItem.FRESH_ITEM_NEWS_COLLECTION + "", "").getRequestBody());
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
                    if (isWorldChecked)
                        EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_ALL_NEWS_LIST));
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_REFRESH_NEWS_ITEM));// 需要更新帖子，否则不及时
                    ((Activity) mView).finish();// 关闭页面
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

    //新建帖子
    private void startNewPost(String stt, int biztype) {
        mView.showLoadingDialog();
        mContent = mView.getEditData();// 需要先上传完成之后才能搞

        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + stt);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new AddNewPostApiParameter("", mContent, mUploadUrls,
                mView.isAnonyChecked() ? "1" : "0", isWorldChecked ? "1" : "0", "",
                "", "", "", biztype + "", "")
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
                    if (stt.equals(ApiContant.REQUEST_help)) {
                        Intent intent = new Intent("android.intent.action.muhomerefresh");
                        intent.putExtra("refresh", "issuehelp");
                        LocalBroadcastManager.getInstance(App.mContext).sendBroadcast(intent);
                    } else {
                        Intent intent = new Intent("android.intent.action.muhomerefresh");
                        intent.putExtra("refresh", "issuetalk");
                        LocalBroadcastManager.getInstance(App.mContext).sendBroadcast(intent);
                    }
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_ZONE_NEWS_LIST));
                    if (isWorldChecked)
                        EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_ALL_NEWS_LIST));
                    ((Activity) mView).finish();// 关闭页面
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


    @Override
    public String[] getEditImages(RichTextEditor postContent) {
        String[] images = new String[mImagepathList.size()];
        int index = 0;
        List<RichTextEditor.EditData> editList = postContent.buildEditData();
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
    public void dealActivityResult(int requestCode, int resultCode, Intent data, RichTextEditor postContent) {
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
                        Toast.makeText(mView.getDefaultContext(), mView.getDefaultContext().getString(R.string.image_select_open_camera_failure), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else if (requestCode == Constants.REQUEST_CODE_REQUEST_CAMERA_OPTION && resultCode == Activity.RESULT_OK) {
            if (tempFile != null) {
                mImagepathList.add(tempFile.getAbsolutePath());
                Constant.imageList.clear();
                //Constant.imageList.add(tempFile.getAbsolutePath());
                postContent.insertImage(tempFile.getAbsolutePath(), Utils.getScreenWidth());
            }
        } else if (requestCode == Constants.REQUEST_CODE_SELECT_IMAGE &&
                resultCode == Activity.RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT);
            for (int i = 0; i < pathList.size(); i++) {
                File file = new File(pathList.get(i));
                if (file.exists()) {
                    mImagepathList.add(pathList.get(i));
                    postContent.insertImage(pathList.get(i), Utils.getScreenWidth());
                } else { // 这里有可能选到没有的zhaopian
                    CommonToast.getInstance("图片路径无效").show();
                }
            }
            Constant.imageList.clear();
        }
    }


    @Override
    public void requestPost(String surl, int biztype) {
        if (isEdit) {
            if (mImagepathList.size() == 0) updateNewPost();
            else uploadImageFile(surl, biztype);
        } else {
            if (mImagepathList.size() == 0) startNewPost(surl, biztype);
            else uploadImageFile(surl, biztype);
        }
    }

    @Override
    public void removeImage(String image) {
        mImagepathList.remove(image);
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
}
