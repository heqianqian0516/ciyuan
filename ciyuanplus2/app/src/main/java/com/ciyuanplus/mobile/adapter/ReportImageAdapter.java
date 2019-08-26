package com.ciyuanplus.mobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.activity.chat.ReportUserActivity;
import com.ciyuanplus.mobile.image_select.common.Constant;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2016/12/26.
 * <p>
 * 举报页面的图片 适配器
 * 没有修改的必要  暂时不改成RecyclerView
 */

public class ReportImageAdapter extends BaseAdapter {
    public static final String ADD_OTHER_IMAGE_FLAG = "addOther";
    private final ReportUserActivity mContext;
    private List<String> mImageList;
    private final MyOnClickListener myOnClickListener = new MyOnClickListener() {
        @Override
        public void performRealClick(View view) {
            if (view.getId() == R.id.m_list_item_report_del) {
                mContext.deleteImage((String) view.getTag());
                mImageList.remove(view.getTag());
                Constant.imageList.remove(view.getTag());
                notifyDataSetChanged();
            } else if (view.getId() == R.id.m_list_item_report_add) {// 添加新图片
                mContext.reqeustAddNewImage();
            }
        }
    };


    public ReportImageAdapter(ReportUserActivity mContext, List<String> mImageLists) {
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
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.list_item_report_image, null);
            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (Utils.isStringEquals(ADD_OTHER_IMAGE_FLAG, item)) {
            holder.add.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
            holder.add.setOnClickListener(myOnClickListener);
        } else {
            holder.add.setVisibility(View.GONE);
            holder.image.setVisibility(View.VISIBLE);
            holder.delete.setVisibility(View.VISIBLE);
            RequestOptions options = new RequestOptions().placeholder(R.drawable.ic_default_image_007)
                    .error(R.mipmap.imgfail).dontAnimate().centerCrop();
            if (!item.startsWith("/storage")) {


                Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + item).apply(options).into(holder.image);
//                ImageLoader.getInstance().displayImage(Constants.IMAGE_LOAD_HEADER + item, holder.image);
            } else {
                Glide.with(App.mContext).load(item).apply(options).into(holder.image);
                //ImageLoader.getInstance().displayImage("file:///" + item, holder.image);
            }
            holder.delete.setTag(item);
            holder.delete.setOnClickListener(myOnClickListener);
        }
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.m_list_item_report_iv)
        ImageView image;
        @BindView(R.id.m_list_item_report_del)
        ImageView add;
        @BindView(R.id.m_list_item_report_add)
        ImageView delete;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
