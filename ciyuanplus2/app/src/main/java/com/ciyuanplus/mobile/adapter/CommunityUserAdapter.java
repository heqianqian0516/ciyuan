package com.ciyuanplus.mobile.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.activity.chat.CommunityContactsActivity;
import com.ciyuanplus.mobile.activity.chat.SearchCommunityContactsActivity;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.module.login.LoginActivity;
import com.ciyuanplus.mobile.net.bean.CommunityUserItem;
import com.ciyuanplus.mobile.net.bean.UserInfoItem;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CustomDialog;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2017/5/20.
 */

public class CommunityUserAdapter extends RecyclerView.Adapter<CommunityUserAdapter.ViewHolder> implements SectionIndexer {
    private final RequestOptions mOptions;
    private final Activity mContext;
    private final MyOnClickListener myOnClickListener = new MyOnClickListener() {

        @Override
        public void performRealClick(View view) {
            final CommunityUserItem item = (CommunityUserItem) view.getTag();
            if (view.getId() == R.id.m_list_item_community_user_opera_lp) {
                if (!LoginStateManager.isLogin()) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
                    return;
                }
                if (item.isFollow == 0 || item.isFollow == 3) {// 关注某人
                    if (mContext instanceof CommunityContactsActivity)
                        ((CommunityContactsActivity) mContext).requestFollowUser(item);
                    else ((SearchCommunityContactsActivity) mContext).requestFollowUser(item);
                } else { // 取消关注
                    CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
                    builder.setMessage("确定要取消关注吗？");
                    builder.setPositiveButton("确定", (dialog, which) -> {
                        if (mContext instanceof CommunityContactsActivity)
                            ((CommunityContactsActivity) mContext).requestUnFollowUser(item);
                        else
                            ((SearchCommunityContactsActivity) mContext).requestUnFollowUser(item);
                        dialog.dismiss();
                    });
                    builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
                    CustomDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                }
            }
        }
    };
    private final ArrayList<CommunityUserItem> CommunityUserItems;
    private View.OnClickListener mItemClickListener;

    public CommunityUserAdapter(Activity mContext, ArrayList<CommunityUserItem> CommunityUserItems, View.OnClickListener itemClickListener) {
        this.mContext = mContext;
        this.CommunityUserItems = CommunityUserItems;
        this.mItemClickListener = itemClickListener;
        mOptions = new RequestOptions().placeholder(R.drawable.ic_default_image_007)
                .error(R.mipmap.imgfail).dontAnimate().centerCrop();

    }

    public void setItemClickListener(View.OnClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public CommunityUserItem getItem(int i) {
        return CommunityUserItems.get(i);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_community_user, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        CommunityUserItem item = CommunityUserItems.get(i);
        int section = getSectionForPosition(i);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (mContext instanceof CommunityContactsActivity && (i == getPositionForSection(section))) {
            holder.tag.setVisibility(View.VISIBLE);
            holder.divider.setVisibility(View.GONE);
            holder.tag.setText(CommunityUserItems.get(i).py.toUpperCase().charAt(0) + "");
        } else {
            holder.tag.setVisibility(View.GONE);//一定要用gone
            holder.divider.setVisibility(View.VISIBLE);
        }
        holder.name.setText(item.nickname);
        if (!Utils.isStringEmpty(item.photo))


            Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + item.photo).apply(mOptions).into(holder.icon);
//            ImageLoaderManger.getInstance().displayImage(Constants.IMAGE_LOAD_HEADER + item.photo,
//                holder.icon, new ImageSize(75, 75));
        holder.sex.setImageResource(UserInfoItem.getSexImageResource(item.sex));
        switch (item.isFollow) {
            case 0: // wei guanzhu
                holder.operaImage.setImageResource(R.mipmap.icon_follow);
                holder.operaName.setVisibility(View.GONE);
                break;
            case 1: // 已关注
                holder.operaImage.setImageResource(R.mipmap.chat_follow_yiguanzhu);
                holder.operaName.setVisibility(View.VISIBLE);
                holder.operaName.setText(mContext.getResources().getString(R.string.string_main_friends_fan_alert));
                break;
            case 2:
                holder.operaImage.setImageResource(R.mipmap.chat_follow_xianghu);
                holder.operaName.setVisibility(View.VISIBLE);
                holder.operaName.setText(mContext.getResources().getString(R.string.string_main_friends_fan_huxiang_alert));
                break;
            case 3: // 别人关注我  我没关注别人
                holder.operaImage.setImageResource(R.mipmap.icon_follow);
                holder.operaName.setVisibility(View.GONE);
                break;
        }
        holder.operaLayout.setTag(item);
        holder.operaLayout.setOnClickListener(myOnClickListener);

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return CommunityUserItems == null ? 0 : CommunityUserItems.size();
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = CommunityUserItems.get(i).py;
            if (sortStr == null || sortStr.length() == 0) return -1;
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == sectionIndex) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        if (Utils.isStringEmpty(CommunityUserItems.get(position).py)) return -1;
        return CommunityUserItems.get(position).py.toUpperCase().charAt(0);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.m_list_item_community_user_tag)
        TextView tag;
        @BindView(R.id.m_list_item_community_user_name)
        TextView name;
        @BindView(R.id.m_list_item_community_user_sex)
        ImageView sex;
        @BindView(R.id.m_list_item_community_user_head)
        ImageView icon;
        @BindView(R.id.m_list_item_community_user_opera_image)
        ImageView operaImage;
        @BindView(R.id.m_list_item_community_user_opera_name)
        TextView operaName;
        @BindView(R.id.m_list_item_community_user_divider)
        View divider;
        @BindView(R.id.m_list_item_community_user_opera_lp)
        LinearLayout operaLayout;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}