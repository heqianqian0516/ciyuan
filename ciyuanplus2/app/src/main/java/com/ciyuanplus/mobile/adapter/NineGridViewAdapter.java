package com.ciyuanplus.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.SquareImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2017/1/8.
 */
public class NineGridViewAdapter extends BaseAdapter {

    private final String[] images;
    private final int mImageWidth = Utils.px2dip(Utils.getScreenWidth());
    Context context;

    public NineGridViewAdapter(Context contexts, String[] images) {
         context = contexts;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images == null ? 0 : images.length;
    }

    @Override
    public Object getItem(int position) {
        return images == null ? null : images[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            convertView = LayoutInflater.from(App.mContext).inflate(R.layout.grid_item_nine_gride, null);
            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.imageView.setImageBitmap(null);
        Glide.with(context).load(Constants.IMAGE_LOAD_HEADER + images[position] + Constants.IMAGE_LOAD_THUMB_END + ",w_" + mImageWidth + ",h_" + mImageWidth).apply(new RequestOptions().placeholder(R.drawable.ic_default_image_007).error(R.mipmap.imgfail)
                .dontAnimate().centerCrop()).into(holder.imageView);
        //ImageLoader.getInstance().displayImage(Constants.IMAGE_LOAD_HEADER + images[position], holder.imageView, new ImageSize(500,500));
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.m_nine_grid_item_view_image)
        SquareImageView imageView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
