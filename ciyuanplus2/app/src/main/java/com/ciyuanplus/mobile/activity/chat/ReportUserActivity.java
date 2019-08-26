package com.ciyuanplus.mobile.activity.chat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.adapter.ReportAdapter;
import com.ciyuanplus.mobile.adapter.ReportImageAdapter;
import com.ciyuanplus.mobile.image_select.ImageLoader;
import com.ciyuanplus.mobile.image_select.ImgSelActivity;
import com.ciyuanplus.mobile.image_select.ImgSelConfig;
import com.ciyuanplus.mobile.image_select.common.Constant;
import com.ciyuanplus.mobile.image_select.utils.FileUtils;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.manager.CacheManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.module.popup.select_image_pop.SelectImagePopActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.ReportItem;
import com.ciyuanplus.mobile.net.parameter.ReportPostApiParameter;
import com.ciyuanplus.mobile.net.parameter.UpLoadFileApiParameter;
import com.ciyuanplus.mobile.net.response.UpLoadFilesResponse;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.PictureUtils;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonTitleBar;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.ciyuanplus.mobile.widget.LengthFilter;
import com.ciyuanplus.mobile.widget.LoadingDialog;
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

public class ReportUserActivity extends MyBaseActivity {
    @BindView(R.id.m_report_user_grid_view)
    RecyclerView mReportUserGridView;
    @BindView(R.id.m_report_user_input)
    EditText mReportUserInput;
    @BindView(R.id.m_report_user_img_grid_view)
    GridView mReportUserImgGridView;
    @BindView(R.id.m_report_user_confirm)
    TextView mReportUserConfirm;
    @BindView(R.id.m_report_user_common_title)
    CommonTitleBar m_js_common_title;


    private final ArrayList<ReportItem> mList = new ArrayList<>();
    private final ArrayList<String> mImagePaths = new ArrayList<>();
    private final ArrayList<String> mUploadUrls = new ArrayList<>();
    private ReportAdapter mAdapter;
    private ReportImageAdapter mImageAdapter;
    private String mUserUuid;
    private File tempFile;
    private Dialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_report_user);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        mUserUuid = getIntent().getStringExtra(Constants.INTENT_USER_ID);
        this.initView();
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
        mReportUserGridView.setLayoutManager(gridLayoutManager);
        mReportUserGridView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = Utils.dip2px(10);
            }
        });
        mAdapter = new ReportAdapter(this, mList, null);
        mReportUserGridView.setAdapter(mAdapter);

        mImagePaths.add(ReportImageAdapter.ADD_OTHER_IMAGE_FLAG);
        mImageAdapter = new ReportImageAdapter(this, mImagePaths);
        mReportUserImgGridView.setAdapter(mImageAdapter);

        mReportUserInput.setFilters(new InputFilter[]{new LengthFilter(200)});

        LoadingDialog.Builder builder = new LoadingDialog.Builder(this);
        builder.setMessage("加载中....");
        mLoadingDialog = builder.create();
        mLoadingDialog.setCanceledOnTouchOutside(false);
    }

    private void initData() {
        mList.add(new ReportItem("0", "垃圾营销", false));
        mList.add(new ReportItem("1", "不实信息", false));
        mList.add(new ReportItem("2", "有害信息", false));
        mList.add(new ReportItem("3", "违法信息", false));
        mList.add(new ReportItem("4", "淫秽信息", false));
        mList.add(new ReportItem("5", "人身攻击", false));
        mList.add(new ReportItem("9", "欺诈骗钱", false));
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
        postRequest.setHttpBody(new ReportPostApiParameter("1", mUserUuid, selectReason, reason, mUploadUrls).getRequestBody());
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

    public void deleteImage(String tag) {
        mImagePaths.remove(ReportImageAdapter.ADD_OTHER_IMAGE_FLAG);
        mImagePaths.remove(tag);
        if (mImagePaths.size() < 8) mImagePaths.add(ReportImageAdapter.ADD_OTHER_IMAGE_FLAG);
        mImageAdapter.notifyDataSetChanged();
    }

    public void reqeustAddNewImage() {
        Intent intent = new Intent(this, SelectImagePopActivity.class);
        this.startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_PITCURE_OPTION);
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
                if (mImagePaths.size() < (mImagePaths.contains(ReportImageAdapter.ADD_OTHER_IMAGE_FLAG) ? 9 : 8)) {
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
                mImagePaths.remove(ReportImageAdapter.ADD_OTHER_IMAGE_FLAG);
                mImagePaths.add(tempFile.getAbsolutePath());
                if (mImagePaths.size() < 8)
                    mImagePaths.add(ReportImageAdapter.ADD_OTHER_IMAGE_FLAG);
                Constant.imageList.add(tempFile.getAbsolutePath());

                mImageAdapter.notifyDataSetChanged();
            }
        } else if (requestCode == Constants.REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT);
            mImagePaths.remove(ReportImageAdapter.ADD_OTHER_IMAGE_FLAG);
            for (int i = 0; i < pathList.size(); i++)
                if (!mImagePaths.contains(pathList.get(i))) {
                    mImagePaths.add(pathList.get(i));
                }
            if (mImagePaths.size() < 8) mImagePaths.add(ReportImageAdapter.ADD_OTHER_IMAGE_FLAG);
            mImageAdapter.notifyDataSetChanged();
        }
    }

    // 打开图片选择activity
    private void startSelectImage() {
        ImageLoader loader = (context, path, imageView) -> Glide.with(context.getApplicationContext()).load(path).apply(new RequestOptions().error(R.mipmap.imgfail)).into(imageView);
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
                .maxNum(8)
                .build();
        ImgSelActivity.startActivity(this, config, Constants.REQUEST_CODE_SELECT_IMAGE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constant.imageList.clear();// 清掉当前的图片，防止下次进来还有
    }

    // 上传多个文件
    private void uploadImageFile(final ArrayList<String> selectReason, final String reason) {
        mImagePaths.remove(ReportImageAdapter.ADD_OTHER_IMAGE_FLAG);
        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) mLoadingDialog.show();
        MultipartBody body = new UpLoadFileApiParameter().getRequestBody();
        for (int i = 0; i < mImagePaths.size(); i++) {
            if (!mImagePaths.get(i).startsWith("/storage")) continue;
            File origin = new File(mImagePaths.get(i));
            String fileName = origin.getName();
            String compressPath = PictureUtils.compressImage(mImagePaths.get(i),
                    CacheManager.getInstance().getCacheDirectory() + fileName.substring(0, fileName.lastIndexOf(".")) + "compressImage.jpeg");
            File compressImage = new File(compressPath);
            if (compressImage.exists())
                body.addPart(new FilePart("files", compressImage, "image/jpeg"));
            else body.addPart(new FilePart("files", new File(mImagePaths.get(i)), "image/jpeg"));
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
                    Collections.addAll(mUploadUrls, response1.fileList.url);
                    reportPost(selectReason, reason);
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

    @OnClick(R.id.m_report_user_confirm)
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        int id = view.getId();
        if (id == R.id.m_report_user_confirm) {
            if (mLoadingDialog != null && !mLoadingDialog.isShowing()) mLoadingDialog.show();

            ArrayList<String> selectReason = new ArrayList<>();
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).isChecked) selectReason.add(mList.get(i).id);
            }
            if (selectReason.size() == 0) {
                CommonToast.getInstance("请选择至少一个理由").show();
                if (mLoadingDialog != null && mLoadingDialog.isShowing())
                    mLoadingDialog.dismiss();
                return;
            }
            String reason = mReportUserInput.getText().toString();
            if (mImagePaths.size() > 0) {
                uploadImageFile(selectReason, reason);
            } else {
                reportPost(selectReason, reason);
            }
        }
    }
}
