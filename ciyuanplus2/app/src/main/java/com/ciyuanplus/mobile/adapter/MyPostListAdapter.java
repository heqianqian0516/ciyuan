package com.ciyuanplus.mobile.adapter;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.net.bean.FreshNewItem;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.video.SampleCoverVideo;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * author : Kris
 * e-mail : chengkai_1215@126.com
 * date   : 2019/3/25 8:44 PM
 * class   : MyPostListAdapter
 * desc   :
 * version: 1.0
 */
public class MyPostListAdapter extends BaseQuickAdapter<FreshNewItem, BaseViewHolder> {


    public MyPostListAdapter(@Nullable List<FreshNewItem> data) {
        super(R.layout.item_home_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FreshNewItem item) {

        helper.setText(R.id.tv_comment_count, Utils.formateNumber(item.commentCount));
        helper.setText(R.id.tv_like_count, Utils.formateNumber(item.likeCount));

        Drawable drawable = mContext.getResources().getDrawable(item.isLike == 1 ? R.drawable.icon_like : R.drawable.icon_unlike);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        ((TextView) helper.getView(R.id.tv_like_count)).setCompoundDrawables(drawable, null, null, null);

        helper.setText(R.id.tv_browse_count, String.valueOf(item.browseCount));
//                helper.mCommentCount.setText(String.valueOf(item.commentCount));


        if (!Utils.isStringEmpty(item.description)) {
            helper.setVisible(R.id.tv_content, true);
            helper.setText(R.id.tv_content, item.description.replace("\r", "\n"));
        } else {
            helper.setGone(R.id.tv_content, false);
        }

        helper.setText(R.id.tv_name, item.nickname);


        if (!TextUtils.isEmpty(item.imgs)) {

            if (!StringUtils.isEmpty(item.postType) && StringUtils.equals(item.postType, "1")) {
                //视频帖子
                helper.setGone(R.id.nineGrid, false);
                SampleCoverVideo coverVideo = helper.getView(R.id.video_item_player);

                helper.getView(R.id.video_item_player).setVisibility(View.VISIBLE);
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
//                Glide.with(mContext).load(item.someOne).into(((SampleCoverVideo) helper.getView(R.id.video_item_player)).);

            } else {


                helper.setGone(R.id.video_item_player, false);
                helper.setVisible(R.id.nineGrid, true);


                ArrayList<ImageInfo> imageInfo = new ArrayList<>();

                String[] paths = item.imgs.split(",");
                for (String path : paths) {

                    ImageInfo info = new ImageInfo();
                    info.setThumbnailUrl(Constants.IMAGE_LOAD_HEADER + path);
                    info.setBigImageUrl(Constants.IMAGE_LOAD_HEADER + path);
                    Logger.d("图片路径 " + Constants.IMAGE_LOAD_HEADER + path);
                    imageInfo.add(info);

                }
                ((NineGridView) helper.getView(R.id.nineGrid)).setAdapter(new NineGridViewClickAdapterImp(mContext, imageInfo, item));
            }
        } else {
            helper.setGone(R.id.video_item_player, false);
            helper.setGone(R.id.nineGrid, false);

        }
        RequestOptions ops = new RequestOptions().placeholder(R.drawable.ic_default_image_007).error(R.mipmap.default_head_).dontAnimate().centerCrop();
        Glide.with(mContext).load(Constants.IMAGE_LOAD_HEADER + item.photo).apply(ops).into((ImageView) helper.getView(R.id.riv_head_image));

        helper.addOnClickListener(R.id.riv_head_image)
                .addOnClickListener(R.id.tv_add)
                .addOnClickListener(R.id.ll_share)
                .addOnClickListener(R.id.ll_like);
    }
}
