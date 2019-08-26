package com.ciyuanplus.mobile.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.bean.FreshNewItem;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.video.SampleCoverVideo;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * author : Kris
 * e-mail : chengkai_1215@126.com
 * date   : 2019/3/25 8:44 PM
 * class   : HomeQualityPostListAdapter
 * desc   :
 * version: 1.0
 * 首页精品
 */
public class HomeQualityPostListAdapter extends BaseQuickAdapter<FreshNewItem, BaseViewHolder> {


    public HomeQualityPostListAdapter(@Nullable List<FreshNewItem> data) {
        super(R.layout.item_home_list, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, FreshNewItem item) {

        helper.setText(R.id.tv_name, item.nickname);

        if (mContext != null) {
            RequestOptions options = new RequestOptions().error(R.mipmap.default_head_);
            Glide.with(mContext).load(Constants.IMAGE_LOAD_HEADER + item.photo).apply(options).into((ImageView) helper.getView(R.id.riv_head_image));

        }
        Logger.d("图片地址 = " + Constants.IMAGE_LOAD_HEADER + item.photo);
        // 喜欢  浏览
        helper.setText(R.id.tv_like_count, Utils.formateNumber(item.likeCount));
        helper.setText(R.id.tv_comment_count, Utils.formateNumber(item.commentCount));
        Drawable img = mContext.getResources().getDrawable(item.isLike == 1 ? R.drawable.icon_like : R.drawable.icon_unlike);
        ((TextView) helper.getView(R.id.tv_like_count)).setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);

        // 内容
        if (!Utils.isStringEmpty(item.description)) {
            helper.setText(R.id.tv_content, item.description).setVisible(R.id.tv_content, true);
        } else {
            helper.setGone(R.id.tv_content, false);
        }

        helper.setVisible(R.id.tv_add, !Utils.isStringEquals(item.userUuid, UserInfoData.getInstance().getUserInfoItem().uuid) && item.isFollow != 1);
        helper.setText(R.id.tv_add, item.isFollow == 0 ? "+关注" : "已关注");
        helper.setText(R.id.tv_browse_count, Utils.formateNumber(item.browseCount));

        helper.setGone(R.id.video_item_player, false).setGone(R.id.nineGrid, false);


        if (!TextUtils.isEmpty(item.imgs)) {

            if (!StringUtils.isEmpty(item.postType)) {
                //视频帖子

                if (StringUtils.equals(item.postType, "1")) {

                    helper.setGone(R.id.nineGrid, false);
                    SampleCoverVideo coverVideo = helper.getView(R.id.video_item_player);

                    helper.getView(R.id.video_item_player).setVisibility(View.VISIBLE);
//                    coverVideo.loadCoverImage(item.someOne, R.mipmap.imgfail);
                    coverVideo.loadCoverImage(Constants.IMAGE_LOAD_HEADER + item.imgs, R.mipmap.imgfail);
                    coverVideo.setUpLazy(Constants.IMAGE_LOAD_HEADER + item.imgs, true, null, null, "这是title");
                    //增加title
                    coverVideo.getTitleTextView().setVisibility(View.GONE);
                    //设置返回键
                    coverVideo.getBackButton().setVisibility(View.GONE);
                    //设置全屏按键功能
                    coverVideo.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            coverVideo.startWindowFullscreen(mContext, false, true);
                        }
                    });
                    //防止错位设置
                    coverVideo.setPlayTag(TAG);
                    coverVideo.setPlayPosition(helper.getAdapterPosition());
                    //是否根据视频尺寸，自动选择竖屏全屏或者横屏全屏
                    coverVideo.setAutoFullWithSize(true);
                    //音频焦点冲突时是否释放
                    coverVideo.setReleaseWhenLossAudio(false);
                    //全屏动画
                    coverVideo.setShowFullAnimation(true);
                    //小屏时不触摸滑动
                    coverVideo.setIsTouchWiget(false);
                    //全屏是否需要lock功能

                } else if (StringUtils.equals(item.postType, "0")) {
                    helper.setVisible(R.id.nineGrid, true);
                    helper.getView(R.id.video_item_player).setVisibility(View.GONE);

                    ArrayList<ImageInfo> imageInfo = new ArrayList<>();

                    String[] paths = item.imgs.split(",");
                    for (String path : paths) {

                        ImageInfo info = new ImageInfo();
                        info.setThumbnailUrl(Constants.IMAGE_LOAD_HEADER + path);
                        info.setBigImageUrl(Constants.IMAGE_LOAD_HEADER + path);
                        imageInfo.add(info);

                    }
                    ((NineGridView) helper.getView(R.id.nineGrid)).setAdapter(new NineGridViewClickAdapterImp(mContext, imageInfo, item));
                }
            } else {
                helper.setVisible(R.id.nineGrid, true);
                helper.getView(R.id.video_item_player).setVisibility(View.GONE);

                ArrayList<ImageInfo> imageInfo = new ArrayList<>();

                String[] paths = item.imgs.split(",");
                for (String path : paths) {

                    ImageInfo info = new ImageInfo();
                    info.setThumbnailUrl(Constants.IMAGE_LOAD_HEADER + path);
                    info.setBigImageUrl(Constants.IMAGE_LOAD_HEADER + path);
                    imageInfo.add(info);

                }
                ((NineGridView) helper.getView(R.id.nineGrid)).setAdapter(new NineGridViewClickAdapterImp(mContext, imageInfo, item));
            }
        } else {
            helper.setGone(R.id.video_item_player, false).setGone(R.id.nineGrid, false);
        }


        helper.addOnClickListener(R.id.riv_head_image)
                .addOnClickListener(R.id.tv_add)
                .addOnClickListener(R.id.ll_share)
                .addOnClickListener(R.id.ll_like);
    }


    public void loadVideoScreenshot(final Context context, String uri, ImageView imageView, long frameTimeMicros) {

        RequestOptions requestOptions = RequestOptions.frameOf(frameTimeMicros);

        Glide.with(context).load(uri).apply(requestOptions).into(imageView);
    }
}
