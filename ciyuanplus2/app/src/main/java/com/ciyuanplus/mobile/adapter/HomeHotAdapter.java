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
public class HomeHotAdapter extends BaseAdapter {

    private Activity mContext;
    private List<FriendsItem> mFriendsItems;

    private final MyOnClickListener myOnClickListener = new MyOnClickListener() {
        @Override
        public void performRealClick(View view) {

            FriendsItem item = (FriendsItem) view.getTag();
            Intent intent = new Intent(mContext, OthersActivity.class);
            intent.putExtra(Constants.INTENT_USER_ID, item.uuid);
            mContext.startActivity(intent);
        }
    };


    public HomeHotAdapter(Activity mContext, List<FriendsItem> mFriendsItems) {

        this.mContext = mContext;
        this.mFriendsItems = mFriendsItems;
    }

    @Override
    public int getCount() {
        return mFriendsItems.size();
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
        FriendsItem item = mFriendsItems.get(position);
        if (null == convertView) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.item_home_hot, null);
            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mNickname.setText(item.nickname);

        RequestOptions options = new RequestOptions().placeholder(R.mipmap.default_head_)
                .error(R.mipmap.default_head_).dontAnimate().centerCrop();

        Glide.with(this.mContext).load(Constants.IMAGE_LOAD_HEADER + item.photo).apply(options).into(holder.mHeadIcon);

        holder.mInfoLayout.setTag(item);
        holder.mInfoLayout.setOnClickListener(myOnClickListener);


        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.iv_head_icon)
        ImageView mHeadIcon;
        @BindView(R.id.tv_nickname)
        TextView mNickname;
        @BindView(R.id.ll_info)
        LinearLayout mInfoLayout;
        @BindView(R.id.ll_root)
        View root;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
