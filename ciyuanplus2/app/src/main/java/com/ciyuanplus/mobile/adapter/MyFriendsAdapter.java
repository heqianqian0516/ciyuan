package com.ciyuanplus.mobile.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.base.irecyclerview.IViewHolder;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.module.mine.friends.MyFriendsActivity;
import com.ciyuanplus.mobile.module.mine.search_friends.SearchFriendsActivity;
import com.ciyuanplus.mobile.net.bean.FriendsItem;
import com.ciyuanplus.mobile.net.bean.UserInfoItem;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.widget.CustomDialog;
import com.ciyuanplus.mobile.widget.RoundImageView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2017/5/20.
 */

public class MyFriendsAdapter extends RecyclerView.Adapter<MyFriendsAdapter.ViewHolder> {
    private final Activity mContext;
    private final MyOnClickListener myOnClickListener = new MyOnClickListener() {
        @Override
        public void performRealClick(View view) {
            final FriendsItem item = (FriendsItem) view.getTag();
            int id = view.getId();
            if (id == R.id.m_list_item_my_friends_follow) {
                if (mContext instanceof MyFriendsActivity)
                    ((MyFriendsActivity) mContext).mPresenter.requestFollowUser(item);
                else if (mContext instanceof SearchFriendsActivity)
                    ((SearchFriendsActivity) mContext).mPresenter.requestFollowUser(item);
            } else if (id == R.id.m_list_item_my_friends_fan_state) {
                CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
                builder.setMessage("确定要取消关注吗？");
                builder.setPositiveButton("确定", (dialog, which) -> {
                    if (mContext instanceof MyFriendsActivity)
                        ((MyFriendsActivity) mContext).mPresenter.requestUnFollowUser(item);
                    else if (mContext instanceof SearchFriendsActivity)
                        ((SearchFriendsActivity) mContext).mPresenter.requestUnFollowUser(item);
                    dialog.dismiss();
                });
                builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
                CustomDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        }
    };
    private final ArrayList<FriendsItem> mList;
    private final View.OnClickListener mItemClickListener;

    public MyFriendsAdapter(Activity mContext, ArrayList<FriendsItem> mList, View.OnClickListener mItemClickListener) {
        this.mContext = mContext;
        this.mList = mList;
        this.mItemClickListener = mItemClickListener;
    }

    public FriendsItem getItem(int i) {
        return mList.get(i);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_my_friends, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FriendsItem item = mList.get(position);

        Glide.with(mContext).load(Constants.IMAGE_LOAD_HEADER + item.photo).apply(new RequestOptions().placeholder(R.drawable.ic_default_image_007).error(R.mipmap.imgfail)
                .dontAnimate().centerCrop()).into(holder.icon);
//        ImageLoaderManger.getInstance().displayImage(Constants.IMAGE_LOAD_HEADER + item.photo, holder.icon, new ImageSize(75, 75));
        holder.name.setText(item.nickname);
        if (item.followType == 0) {//被关注
            holder.fanLayout.setVisibility(View.GONE);
            holder.follow.setVisibility(View.VISIBLE);
        } else {
            holder.fanLayout.setVisibility(View.VISIBLE);
            holder.follow.setVisibility(View.GONE);
            if (item.followType == 1) { // 我关注了别人
                holder.fanIcon.setImageResource(R.mipmap.chat_follow_yiguanzhu);
                holder.fanText.setText(mContext.getResources().getString(R.string.string_main_friends_fan_alert));
            } else {// 互相关注了
                holder.fanIcon.setImageResource(R.mipmap.chat_follow_xianghu);
                holder.fanText.setText(mContext.getResources().getString(R.string.string_main_friends_fan_huxiang_alert));
            }
        }
        holder.sex.setImageResource(UserInfoItem.getSexImageResource(item.sex));
//        if (item.neibType == 1)
//            holder.name.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.mipmap.icon_neighbor_label), null);
//        else holder.name.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

        holder.follow.setTag(item);
        holder.follow.setOnClickListener(myOnClickListener);
        holder.fanLayout.setTag(item);
        holder.fanLayout.setOnClickListener(myOnClickListener);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class ViewHolder extends IViewHolder {
        @BindView(R.id.m_list_item_my_friends_icon)
        RoundImageView icon;
        @BindView(R.id.m_list_item_my_friends_name)
        TextView name;
        @BindView(R.id.m_list_item_my_friends_follow)
        ImageView follow;
        @BindView(R.id.m_list_item_my_friends_fan_state)
        LinearLayout fanLayout;
        @BindView(R.id.m_list_item_my_friends_fan_state_img)
        ImageView fanIcon;
        @BindView(R.id.m_list_item_my_friends_sex)
        ImageView sex;
        @BindView(R.id.m_list_item_my_friends_fan_state_text)
        TextView fanText;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}