package com.ciyuanplus.mobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.base.irecyclerview.IViewHolder;
import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.activity.news.FullScreenImageActivity;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.net.bean.FeedBackItem;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.RoundCornersTransformation;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2017/5/20.
 */

public class FeedBackAdapter extends RecyclerView.Adapter<FeedBackAdapter.ViewHolder> {
    private final MyOnClickListener myOnClickListener = new MyOnClickListener() {
        @Override
        public void performRealClick(View view) {
            String path = (String) view.getTag(R.id.glide_item_tag);
            String[] images = new String[1];
            images[0] = path;
            int id = view.getId();
            if (id == R.id.m_list_item_chat_mine_message_image) {
                Intent intent = new Intent(App.mContext, FullScreenImageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle b = new Bundle();
                b.putStringArray(Constants.INTENT_FULL_SCREEN_IMAGE_VIEWER_IMAGES, images);
                intent.putExtras(b);
                intent.putExtra(Constants.INTENT_FULL_SCREEN_IMAGE_VIEWER_INDEX, 0);
                App.mContext.startActivity(intent);
            } else if (id == R.id.m_list_item_chat_other_message_image) {
                Intent intent = new Intent(App.mContext, FullScreenImageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle b = new Bundle();
                b.putStringArray(Constants.INTENT_FULL_SCREEN_IMAGE_VIEWER_IMAGES, images);
                intent.putExtras(b);
                intent.putExtra(Constants.INTENT_FULL_SCREEN_IMAGE_VIEWER_INDEX, 0);
                App.mContext.startActivity(intent);
            }

        }
    };
    private final Context mContext;
    private final ArrayList<FeedBackItem> FeedBackItems;
    private final View.OnClickListener mItemClickListener;

    public FeedBackAdapter(Context mContext, ArrayList<FeedBackItem> FeedBackItems, View.OnClickListener itemClickListener) {
        this.mContext = mContext;
        this.FeedBackItems = FeedBackItems;
        this.mItemClickListener = itemClickListener;
    }

    public FeedBackItem getItem(int i) {
        return FeedBackItems.get(i);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chat_other, parent, false);
        FeedBackAdapter.ViewHolder holder = new FeedBackAdapter.ViewHolder(view);
        view.setOnClickListener(mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FeedBackItem item = FeedBackItems.get(position);
        if (Utils.isStringEmpty(item.sendUserUuid)) { // 客服
            holder.mineLayout.setVisibility(View.VISIBLE);
            holder.feedLayout.setVisibility(View.GONE);
            holder.mineTime.setText(Utils.getFormattedTimeString(item.createTime));
            if (item.messageType == 1) {
                holder.mineImage.setVisibility(View.GONE);
                holder.mineMessage.setVisibility(View.VISIBLE);
                holder.mineMessage.setText(item.contentText);
            } else {
                holder.mineImage.setVisibility(View.VISIBLE);
                holder.mineImage.setTag(R.id.glide_item_tag, item.contentText);
                holder.mineImage.setOnClickListener(myOnClickListener);
                holder.mineMessage.setVisibility(View.GONE);

                RequestOptions options = RequestOptions
                        .bitmapTransform(new RoundCornersTransformation(mContext, 20,
                                RoundCornersTransformation.CornerType.EXCEPT_BOTTOM_LEFT)).placeholder(R.drawable.ic_default_image_007)
                        .error(R.mipmap.imgfail).dontAnimate().centerCrop();

                Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + item.contentText)
                        .apply(options).into(holder.mineImage);

            }
        } else {
            holder.mineLayout.setVisibility(View.GONE);
            holder.feedLayout.setVisibility(View.VISIBLE);
            holder.feedTime.setText(Utils.getFormattedTimeString(item.createTime));
            if (item.messageType == 1) {
                holder.feedImage.setVisibility(View.GONE);
                holder.feedMessage.setVisibility(View.VISIBLE);
                holder.feedMessage.setText(item.contentText);
            } else {
                holder.feedImage.setVisibility(View.VISIBLE);
                holder.feedImage.setTag(R.id.glide_item_tag, item.contentText);
                holder.feedImage.setOnClickListener(myOnClickListener);
                holder.feedMessage.setVisibility(View.GONE);

                RequestOptions options = RequestOptions
                        .bitmapTransform(new RoundCornersTransformation(mContext, 20,
                                RoundCornersTransformation.CornerType.EXCEPT_BOTTOM_LEFT)).placeholder(R.drawable.ic_default_image_007)
                        .error(R.mipmap.imgfail).dontAnimate();

                Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + item.contentText)
                        .apply(options).into(holder.feedImage);
            }
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return FeedBackItems == null ? 0 : FeedBackItems.size();
    }

    /*
     * 设置图片---自定义图片4个角中的指定角为圆角
     * @param url 图片的url
     * @param cornerRadius 圆角像素大小
     * @param corners 自定义圆角:<br>
     * 以下参数为FlexibleRoundedBitmapDisplayer中静态变量:<br>
     * CORNER_NONE　无圆角<br>
     * CORNER_ALL 全为圆角<br>
     * CORNER_TOP_LEFT | CORNER_TOP_RIGHT | CORNER_BOTTOM_LEFT | CORNER_BOTTOM_RIGHT　指定圆角（选其中若干组合  ） <br>
     * @param image url为空时加载该图片
     * @param imageView 要设置图片的ImageView
     */
//    private void setRoundedImage(String url, int cornerRadius, int corners, ImageView imageView) {
//        ImageLoader imageLoader = ImageLoader.getInstance();
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.ic_default_image_007).showStubImage(R.drawable.ic_default_image_007)
//                .showImageForEmptyUri(R.mipmap.imgfail)//url为空时显示的图片
//                .showImageOnFail(R.mipmap.imgfail)//加载失败显示的图片
//                .cacheInMemory()//内存缓存
//                .cacheOnDisc()//磁盘缓存
//                .displayer(new FlexibleRoundedBitmapDisplayer(cornerRadius,corners)) // 自定义增强型BitmapDisplayer
//                .build();
//        imageLoader.displayImage(url, imageView, options);
//    }

    class ViewHolder extends IViewHolder {
        @BindView(R.id.m_list_item_chat_mine_lp)
        LinearLayout mineLayout;
        @BindView(R.id.m_list_item_chat_other_lp)
        LinearLayout feedLayout;
        @BindView(R.id.m_list_item_chat_mine_time)
        TextView mineTime;
        @BindView(R.id.m_list_item_chat_mine_message_text)
        TextView mineMessage;
        @BindView(R.id.m_list_item_chat_other_time)
        TextView feedTime;
        @BindView(R.id.m_list_item_chat_other_message_text)
        TextView feedMessage;
        @BindView(R.id.m_list_item_chat_mine_message_image)
        ImageView mineImage;
        @BindView(R.id.m_list_item_chat_other_message_image)
        ImageView feedImage;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }
}