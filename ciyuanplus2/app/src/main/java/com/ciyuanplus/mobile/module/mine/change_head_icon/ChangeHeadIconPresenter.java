package com.ciyuanplus.mobile.module.mine.change_head_icon;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.blankj.utilcode.util.AppUtils;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.common.Constant;
import com.ciyuanplus.mobile.image_select.utils.FileUtils;
import com.ciyuanplus.mobile.manager.CacheManager;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.popup.select_image_pop.SelectImagePopActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.parameter.ChangeAvatarApiParameter;
import com.ciyuanplus.mobile.net.parameter.UpLoadFileApiParameter;
import com.ciyuanplus.mobile.net.response.UpLoadFileResponse;
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
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Alen on 2017/12/11.
 */

public class ChangeHeadIconPresenter implements ChangeHeadIconContract.Presenter {
    private final ChangeHeadIconContract.View mView;

    private File tempFile;//选择头像相关的文件
    private String cropImagePath;
    private String mHeadIconPath;

    @Inject
    public ChangeHeadIconPresenter(ChangeHeadIconContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_SELECT_PITCURE_OPTION && resultCode == Activity.RESULT_OK && data != null) {
            // 选择头像的方式
            String option = data.getStringExtra(SelectImagePopActivity.SELECTED);
            if (Utils.isStringEquals(option, SelectImagePopActivity.LOACL_PHOTO)) {
                startSelectImage();
            } else {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(mView.getDefaultContext().getPackageManager()) != null) {
                    tempFile = new File(FileUtils.createRootPath(mView.getDefaultContext()) + "/" + System.currentTimeMillis() + ".jpg");
                    FileUtils.createFile(tempFile);
                    cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, parUri(tempFile));
                    ((Activity) mView).startActivityForResult(cameraIntent, Constants.REQUEST_CODE_REQUEST_CAMERA_OPTION);
                } else {
                    Toast.makeText(mView.getDefaultContext(), mView.getDefaultContext().getString(R.string.image_select_open_camera_failure), Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == Constants.REQUEST_CODE_REQUEST_CAMERA_OPTION && resultCode == Activity.RESULT_OK) {

            if (tempFile != null) {
                // 先裁剪
                Logger.d("图片地址 " + tempFile);
                crop(tempFile.getAbsolutePath());
            }
        } else if (requestCode == Constants.REQUEST_CODE_IMAGE_CROP_CODE && resultCode == Activity.RESULT_OK) {
            if (!Utils.isStringEmpty(cropImagePath)) {
                Constant.imageList.clear();
                mHeadIconPath = cropImagePath;
                uploadImageFile();
            }
        } else if (requestCode == Constants.REQUEST_CODE_SELECT_IMAGE && resultCode == Activity.RESULT_OK && data != null) {

            // 图片选择结果回调
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            // 例如 LocalMedia 里面返回三种path
            // 1.media.getPath(); 为原图path
            // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
            // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
            // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的

            LocalMedia firstMedia = selectList.get(0);
            boolean isImage = PictureConfig.TYPE_IMAGE == PictureMimeType.isPictureType(firstMedia.getPictureType());
            if (isImage) {
                Logger.e("media路径-----》", firstMedia.getPath());
                mHeadIconPath = firstMedia.getPath();
                Constant.imageList.clear();
                uploadImageFile();
            }
        }

//        uploadImageFile();
    }


    // 打开图片选择activity
    private void startSelectImage() {

        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create((Activity) mView.getDefaultContext())
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
//                .theme(R.style.picture.white.style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(1)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(3)// 每行显示个数
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
                .previewVideo(false)// 是否可预览视频
                .enablePreviewAudio(true) // 是否可播放音频
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                .enableCrop(true)// 是否裁剪
                .compress(true)// 是否压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                //.compressSavePath(getPath())//压缩图片保存地址
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
//                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示
                .isGif(false)// 是否显示gif图片
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                .circleDimmedLayer(true)// 是否圆形裁剪
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .openClickSound(false)// 是否开启点击声音
//                .selectionMedia(true)// 是否传入已选图片
                .isDragFrame(true)// 是否可拖动裁剪框(固定)
                //                        .videoMaxSecond(15)
                //                        .videoMinSecond(10)
                //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                .rotateEnabled(true) // 裁剪是否可旋转图片
                .scaleEnabled(true)// 裁剪是否可放大缩小图片
                //录制视频秒数 默认60s
                .recordVideoSecond(10)
                .forResult(Constants.REQUEST_CODE_SELECT_IMAGE);//结果回调onActivityResult code
    }

    private void crop(String imagePath) {
        File file = new File(FileUtils.createRootPath(mView.getDefaultContext()) + "/" + System.currentTimeMillis() + ".jpg");

        cropImagePath = file.getAbsolutePath();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(parUri(new File(imagePath)), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        ((Activity) mView).startActivityForResult(intent, Constants.REQUEST_CODE_IMAGE_CROP_CODE);
    }

    // 上传头像文件， 如果成功就提交修改头像
    private void uploadImageFile() {
        if (Utils.isStringEmpty(mHeadIconPath)) return;
        mView.showLoadingDialog();
        MultipartBody body = new UpLoadFileApiParameter().getRequestBody();
        File origin = new File(mHeadIconPath);
        String fileName = origin.getName();
        String compressPath = PictureUtils.compressImage(mHeadIconPath,
                CacheManager.getInstance().getCacheDirectory() + fileName.substring(0, fileName.lastIndexOf(".")) + "compressImage.jpeg");
        File compressImage = new File(compressPath);
        if (compressImage.exists()) body.addPart(new FilePart("file", compressImage, "image/jpeg"));
        else body.addPart(new FilePart("file", new File(mHeadIconPath), "image/jpeg"));

        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_UPLOAD_FILE_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(body);
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                UpLoadFileResponse response1 = new UpLoadFileResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    changeAvatar(response1.url);
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
                mView.dismissLoadingDialog();
//                CommonToast.getInstance(getResources().getString(R.string.string_my_profile_upload_head_fail_alert), Toast.LENGTH_SHORT).show();

            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    // 修改头像
    private void changeAvatar(final String imagePath) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_CHANGE_PHOTO_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new ChangeAvatarApiParameter(imagePath).getRequestBody());
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
                    CommonToast.getInstance(mView.getDefaultContext().getResources().getString(R.string.string_my_profile_change_head_success_alert), Toast.LENGTH_SHORT).show();
                    UserInfoData.getInstance().getUserInfoItem().photo = imagePath;
                    EventCenterManager.asynSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_LOGIN_USER_INFO_UPDATE));
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
                mView.dismissLoadingDialog();
//                CommonToast.getInstance(getResources().getString(R.string.string_my_profile_change_head_fail_alert), Toast.LENGTH_SHORT).show();
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    /**
     * 生成uri
     *
     * @param cameraFile
     * @return
     */
    private Uri parUri(File cameraFile) {
        Uri imageUri;
        String authority = AppUtils.getAppPackageName() + ".FileProvider";
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            //通过FileProvider创建一个content类型的Uri
            imageUri = FileProvider.getUriForFile(mView.getDefaultContext(), authority, cameraFile);
        } else {
            imageUri = Uri.fromFile(cameraFile);
        }
        return imageUri;
    }

    @Override
    public void detachView() {
    }
}
