package com.ciyuanplus.mobile.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.module.others.new_others.OthersActivity;
import com.ciyuanplus.mobile.net.bean.ActivityListItem;
import com.ciyuanplus.mobile.net.bean.FriendsItem;
import com.ciyuanplus.mobile.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2016/12/26.
 * <p>
 * 举报页面的图片 适配器
 * 没有修改的必要  暂时不改成RecyclerView
 */
//热门推荐数据的适配器
public class HomeActivityAdapter extends BaseAdapter {

    private Activity mContext;
    private List<ActivityListItem> mActivityListItem;

    private final MyOnClickListener myOnClickListener = new MyOnClickListener() {
        @Override
        public void performRealClick(View view) {

            ActivityListItem item = (ActivityListItem) view.getTag();
            Intent intent = new Intent(mContext, OthersActivity.class);
            //intent.putExtra(Constants.INTENT_USER_ID, item.uuid);
            mContext.startActivity(intent);
        }
    };


    public HomeActivityAdapter(Activity mContext, List<ActivityListItem> mActivityListItem) {

        this.mContext = mContext;
        this.mActivityListItem = mActivityListItem;
    }

    @Override
    public int getCount() {
        return mActivityListItem.size();
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
        ActivityListItem item =mActivityListItem.get(position);
        if (null == convertView) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.home_activity_image_layout, null);
            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
       // holder.mNickname.setText(item.nickname);

       /* RequestOptions options = new RequestOptions().placeholder(R.mipmap.default_head_)
                .error(R.mipmap.default_head_).dontAnimate().centerCrop();*/
       // String url=mActivityListItem.
     //Glide.with(this.mContext).load(Constants.IMAGE_LOAD_HEADER + item.photo).apply(options).into(holder.mHeadIcon);
        // Glide.with(this.mContext).load().into(holder.mActivityImage);
       /* holder.mInfoLayout.setTag(item);
        holder.mInfoLayout.setOnClickListener(myOnClickListener);*/


        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.home_activity_image)
        ImageView mActivityImage;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
