package com.ciyuanplus.mobile.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.activity.news.FullScreenImageActivity;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.module.forum_detail.daily_detail.DailyDetailActivity;
import com.ciyuanplus.mobile.module.forum_detail.food_detail.FoodDetailActivity;
import com.ciyuanplus.mobile.module.forum_detail.note_detail.NoteDetailActivity;
import com.ciyuanplus.mobile.module.forum_detail.post_detail.PostDetailActivity;
import com.ciyuanplus.mobile.module.forum_detail.stuff_detail.StuffDetailActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.FreshNewItem;
import com.ciyuanplus.mobile.net.parameter.AddPostBrowseCountApiParameter;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.listener.HttpListener;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Alen on 2017/1/7.
 * <p>
 * 列表中展示图片的控件
 */
public class ThreeGrideView extends LinearLayout {
    private RecyclerView mMulityGride;
    private RelativeLayout mSingleLayout;
    private LinearLayout mTwoLayout;
    private LinearLayout mThreeLayout;
    private ImageView mSingleImageView;
    private ImageView mThreeImageView1;
    private ImageView mThreeImageView2;
    private ImageView mThreeImageView3;
    private LinearLayout mImageNumLayout;
    private TextView mImageNum;

    private FreshNewItem mItem;
    private String userUuid;
    private final Context mContext;
    private final int mImageWidth = Utils.getScreenWidth();
    private String[] mImages;
    private final MyOnClickListener myOnClickListener = new MyOnClickListener() {
        @Override
        public void performRealClick(View view) {
            if (view.getId() == R.id.m_three_grid_view_single_image) {
                goFullScreenActivity(0);
            } else if (view.getId() == R.id.m_three_grid_view_two_image_1) {
                goFullScreenActivity(0);
            } else if (view.getId() == R.id.m_three_grid_view_two_image_2) {
                goFullScreenActivity(1);
            } else if (view.getId() == R.id.m_three_grid_view_three_image_1) {
                goFullScreenActivity(0);
            } else if (view.getId() == R.id.m_three_grid_view_three_image_2) {
                goFullScreenActivity(1);
            } else if (view.getId() == R.id.m_three_grid_view_three_image_3 || view.getId() == R.id.ll_image_num) {
                goFullScreenActivity(2);
            }
        }
    };
    private final RequestOptions mOptions;

    public ThreeGrideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_three_gride, this);
        mOptions = new RequestOptions().dontAnimate()
                .centerCrop()
                .skipMemoryCache(true);
        initView();
    }

    private void initView() {
        mMulityGride = findViewById(R.id.m_three_grid_view_multi_grid);
        mSingleLayout = findViewById(R.id.m_three_grid_view_single_image_lp);
        mTwoLayout = findViewById(R.id.m_three_grid_view_two_image_lp);
        mThreeLayout = findViewById(R.id.m_three_grid_view_three_image_lp);

        mSingleImageView = findViewById(R.id.m_three_grid_view_single_image);

        ImageView twoImageView1 = findViewById(R.id.m_three_grid_view_two_image_1);
        ImageView twoImageView2 = findViewById(R.id.m_three_grid_view_two_image_2);

        mThreeImageView1 = findViewById(R.id.m_three_grid_view_three_image_1);
        mThreeImageView2 = findViewById(R.id.m_three_grid_view_three_image_2);
        mThreeImageView3 = findViewById(R.id.m_three_grid_view_three_image_3);

        mImageNumLayout = findViewById(R.id.ll_image_num);
        mImageNum = findViewById(R.id.tv_image_num);


        mSingleImageView.setOnClickListener(myOnClickListener);
        twoImageView1.setOnClickListener(myOnClickListener);
        twoImageView2.setOnClickListener(myOnClickListener);
        mThreeImageView1.setOnClickListener(myOnClickListener);
        mThreeImageView2.setOnClickListener(myOnClickListener);
        mThreeImageView3.setOnClickListener(myOnClickListener);

        mImageNumLayout.setOnClickListener(myOnClickListener);


        int screenWidth = Utils.getScreenWidth();// 获取屏幕宽度  
        ViewGroup.LayoutParams lp = mSingleImageView.getLayoutParams();
        lp.width = (mImageWidth - Utils.dip2px(28));

        lp.height = screenWidth / 75 * 42;
        mSingleImageView.setLayoutParams(lp);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mMulityGride.setLayoutManager(linearLayoutManager);
//        int screenWidth = Utils.getScreenWidth() - Utils.dip2px(40);// 获取屏幕宽度  
//        ViewGroup.LayoutParams lp = mSingleImage.getLayoutParams();
//        lp.width = screenWidth;
//        lp.height = screenWidth / 16 * 9;
//        mSingleImage.setLayoutParams(lp);
    }

    public void setDataSource(String[] images, FreshNewItem mItem) {
        this.mItem = mItem;
        mImages = images;
        if (mImages == null || mImages.length == 0) return;

        if (mImages.length == 1) {
            mImageNumLayout.setVisibility(GONE);
            mSingleLayout.setVisibility(View.VISIBLE);
            Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + mImages[0])
                    .into(mSingleImageView);
            mTwoLayout.setVisibility(View.GONE);
            mThreeLayout.setVisibility(View.GONE);
            mMulityGride.setVisibility(View.GONE);
        }
//        else if (mImages.length == 2) {
//            mImageNumLayout.setVisibility(GONE);
//            mSingleLayout.setVisibility(View.GONE);
//            mTwoLayout.setVisibility(View.VISIBLE);
//            mThreeLayout.setVisibility(View.GONE);
//            mMulityGride.setVisibility(View.GONE);
//            Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + mImages[0])
//                    .dontAnimate().centerCrop().into(mTwoImageView1);
//            Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + mImages[1])
//                    .dontAnimate().centerCrop().into(mTwoImageView2);
//        }
        else {

            mSingleLayout.setVisibility(View.GONE);
            mTwoLayout.setVisibility(View.GONE);
            mThreeLayout.setVisibility(View.VISIBLE);
            mMulityGride.setVisibility(View.GONE);
            mImageNumLayout.setVisibility(GONE);

            int width = (mImageWidth - Utils.dip2px(37)) / 3;
            int height = (mImageWidth - Utils.dip2px(37)) / 3;

            Glide.with(mContext).load(Constants.IMAGE_LOAD_HEADER + mImages[0] + Constants.IMAGE_LOAD_THUMB_END + ",w_" + width + ",h_" + height)
                    .apply(mOptions)
                    .thumbnail(0.1f)

                    .into(mThreeImageView1);
            Glide.with(mContext).load(Constants.IMAGE_LOAD_HEADER + mImages[1] + Constants.IMAGE_LOAD_THUMB_END + ",w_" + width + ",h_" + height)
                    .apply(mOptions)
                    .thumbnail(0.1f)
                    .into(mThreeImageView2);
            if (images.length > 2) {
                mThreeImageView3.setVisibility(VISIBLE);
                mThreeImageView3.setClickable(true);
                Glide.with(mContext).load(Constants.IMAGE_LOAD_HEADER + mImages[2] + Constants.IMAGE_LOAD_THUMB_END + ",w_" + width + ",h_" + height)
                        .apply(mOptions)
                        .thumbnail(0.1f)
                        .into(mThreeImageView3);
            } else {
                mThreeImageView3.setClickable(false);
                mThreeImageView3.setVisibility(INVISIBLE);
            }

            if (images.length > 3) {
                mImageNumLayout.setVisibility(VISIBLE);
                mImageNum.setText(String.format("%d", images.length));
            }
        }


//        else if (mImages.length == 3) {
//            mSingleLayout.setVisibility(View.GONE);
//            mTwoLayout.setVisibility(View.GONE);
//            mThreeLayout.setVisibility(View.VISIBLE);
//            mMulityGride.setVisibility(View.GONE);
//            Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + mImages[0])
//                    .dontAnimate().centerCrop().into(mThreeImageView1);
//            Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + mImages[1])
//                    .dontAnimate().centerCrop().into(mThreeImageView2);
//            Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + mImages[2])
//                    .dontAnimate().centerCrop().into(mThreeImageView3);
//        }
//        else {
//            mSingleLayout.setVisibility(View.GONE);
//            mTwoLayout.setVisibility(View.GONE);
//            mThreeLayout.setVisibility(View.GONE);
//            mMulityGride.setVisibility(View.VISIBLE);
//
//            mNineGridAdapter = new ThreeImageViewAdapter(mContext, mImages, (v) -> {
//                int position = mMulityGride.getChildAdapterPosition(v);
//                if (position == 8 && images.length > 9) {// 点击more
//                    goDetailActivity();
//                } else {
//                    goFullScreenActivity(position);
//                }
//            });
//            mMulityGride.setAdapter(mNineGridAdapter);
//        }
    }

    private void goDetailActivity() {
        if (mItem.bizType == FreshNewItem.FRESH_ITEM_STUFF) {//宝贝
            Intent intent = new Intent(mContext, StuffDetailActivity.class);
            intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, mItem.postUuid);
            mContext.startActivity(intent);
        } else if (mItem.bizType == FreshNewItem.FRESH_ITEM_DAILY) {
            Intent intent = new Intent(mContext, DailyDetailActivity.class);
            intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, mItem.postUuid);
            mContext.startActivity(intent);
        } else if (mItem.bizType == FreshNewItem.FRESH_ITEM_POST) { // 长文和说说

            Intent intent = new Intent(mContext, PostDetailActivity.class);
            intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, mItem.postUuid);
            mContext.startActivity(intent);

        } else if (mItem.bizType == FreshNewItem.FRESH_ITEM_NEWS
                || mItem.bizType == FreshNewItem.FRESH_ITEM_NOTE
                || mItem.bizType == FreshNewItem.FRESH_ITEM_NEWS_COLLECTION) {
            Intent intent = new Intent(mContext, NoteDetailActivity.class);
            intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, mItem.postUuid);
            mContext.startActivity(intent);
        } else if (mItem.bizType == FreshNewItem.FRESH_ITEM_FOOD
                || mItem.bizType == FreshNewItem.FRESH_ITEM_LIVE
                || mItem.bizType == FreshNewItem.FRESH_ITEM_COMMENT) {
            Intent intent = new Intent(mContext, FoodDetailActivity.class);
            intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, mItem.postUuid);
            mContext.startActivity(intent);
        }
    }

    private void goFullScreenActivity(int position) {
        addBrowseCount();
        Intent intent = new Intent(App.mContext, FullScreenImageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle b = new Bundle();
        b.putStringArray(Constants.INTENT_FULL_SCREEN_IMAGE_VIEWER_IMAGES, mImages);
        intent.putExtras(b);
        intent.putExtra(Constants.INTENT_FULL_SCREEN_IMAGE_VIEWER_INDEX, position);
        App.mContext.startActivity(intent);
    }

    // 更新新鲜事浏览量
    private void addBrowseCount() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_ADD_BROWSE_COUNT_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new AddPostBrowseCountApiParameter(mItem.postUuid).getRequestBody());
        postRequest.setHttpListener(new HttpListener<String>() {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                ResponseData response1 = new ResponseData(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(
                            Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT, mItem.postUuid));
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }
}
