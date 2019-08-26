package com.ciyuanplus.mobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.module.start_forum.start_twitter.StartTwitterActivity;
import com.ciyuanplus.mobile.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2016/12/26.
 */

public class StartTwitterImageAdapter extends BaseAdapter {
    private final StartTwitterActivity mContext;
    private List<String> mImageList;

    public StartTwitterImageAdapter(StartTwitterActivity mContext, List<String> mImageLists) {
        this.mContext = mContext;
        this.mImageList = mImageLists;
    }


    @Override
    public int getCount() {
        return mImageList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        String item = mImageList.get(position);
        if (null == convertView) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.list_item_start_news_image, null);
            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (!item.startsWith("/storage")) {
            Glide.with(mContext).load(Constants.IMAGE_LOAD_HEADER + item).apply(new RequestOptions().placeholder(R.drawable.ic_default_image_007).error(R.mipmap.imgfail)
                    .dontAnimate().centerCrop()).into(holder.image);
            //ImageLoader.getInstance().displayImage(Constants.IMAGE_LOAD_HEADER + item, holder.image);
        } else {
            Glide.with(mContext).load(item).apply(new RequestOptions().placeholder(R.drawable.ic_default_image_007).error(R.mipmap.imgfail)
                    .dontAnimate().centerCrop()).into(holder.image);
            //ImageLoader.getInstance().displayImage("file:///" + item, holder.image);
        }
//        holder.delete.setTag(item);
//        holder.delete.setOnClickListener(myOnClickListener);
        return convertView;
    }

//    MyOnClickListener myOnClickListener = new MyOnClickListener() {
//        @Override
//        public void performRealClick(View view) {
//            if (view.getId() == R.id.m_list_item_start_news_del) {
//                mContext.deleteImage((String) view.getTag());
//                mImageList.remove((String) view.getTag());
//                Constant.imageList.remove((String) view.getTag());
//                notifyDataSetChanged();
//            }
//        }
//    };

    class ViewHolder {
        @BindView(R.id.m_list_item_start_news_iv)
        ImageView image;
        @BindView(R.id.m_list_item_start_news_del)
        ImageView delete;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
