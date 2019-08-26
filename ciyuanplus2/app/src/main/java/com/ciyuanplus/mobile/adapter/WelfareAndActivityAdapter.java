package com.ciyuanplus.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.net.bean.RecycleViewItemData;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kk on 2018/5/30.
 */

public class WelfareAndActivityAdapter extends RecyclerView.Adapter<WelfareAndActivityAdapter.ViewHolder> {

    public static final int TYPE_ACTIVITY = 0;
    public static final int TYPE_WELFARE = 1;
    private final Context mContext;

    private final List<RecycleViewItemData> mList;

    private final int[] mTypes = {
            R.layout.list_item_mine_welfare,
            R.layout.list_item_mine_welfare,
    }; //food 推荐 stuff //买卖
    private final View.OnClickListener myOnClickListener = new MyOnClickListener() {

        @Override
        public void performRealClick(View v) {

//            Object tag = v.getTag();
//            BeanMyAction.GiftBean item = (BeanMyAction.GiftBean) tag;
//            Intent intent = new Intent(mContext, JsWebViewActivity.class);
//            intent.putExtra(Constants.INTENT_OPEN_URL, item.giftUrl);
//            intent.putExtra(Constants.INTENT_JS_WEB_VIEW_PARAM, item.id + "");
//            mContext.startActivity(intent);
        }
    };

    public WelfareAndActivityAdapter(Context mContext, List<RecycleViewItemData> mList) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;

        View view = LayoutInflater.from(this.mContext).inflate(mTypes[viewType], null);

        switch (viewType) {
            case TYPE_WELFARE:
                viewHolder = new WelfareViewHolder(view);
                break;
            case TYPE_ACTIVITY:
                viewHolder = new ActivityHolder(view);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

//        switch (holder.getItemViewType()) {
//            case TYPE_ACTIVITY:
//                ActivityHolder activityHolder = (ActivityHolder) holder;
//
//
//                BeanMyAction.ActivityBean item = (BeanMyAction.ActivityBean) mList.get(position).t;
//
//
//                if (item.distance > 1000) {
//                    double distanceDouble = item.distance / 1000;
//                    java.text.DecimalFormat formatter = new java.text.DecimalFormat("0.0");
//                    String format = formatter.format(distanceDouble);
//                    SpannableString spanString = new SpannableString(item.shopName + " · " + "距离我" + format + "km".replace("\r", "\n"));
//                    activityHolder.time.setText(String.format("%s · 距离我%skm", item.shopName, format));
//
//                } else {
//                    int distanceInt = (int) item.distance;
//                    activityHolder.time.setText(String.format("%s · 距离我%sm", item.shopName, distanceInt));
//                }
//
//                activityHolder.title.setText(item.title);
//                activityHolder.area.setText(item.activityTime);
//                activityHolder.status.setVisibility(View.VISIBLE);
//
//                Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + item.listImg).apply(new RequestOptions()
//                        .placeholder(R.drawable.ic_default_image_007).error(R.mipmap.imgfail)
//                        .dontAnimate().centerCrop()).into(activityHolder.image);
//
//                activityHolder.mView.setTag(item);
//                activityHolder.mView.setOnClickListener(myOnClickListener);
//
//                break;
//
//            case TYPE_WELFARE:
//
//                BeanMyAction.GiftBean itemGift = (BeanMyAction.GiftBean) mList.get(position).t;
//                WelfareViewHolder welfareViewHolder = (WelfareViewHolder) holder;
//                welfareViewHolder.title.setText(itemGift.title);
//                welfareViewHolder.area.setText(itemGift.giftArea);
//                welfareViewHolder.time.setText(itemGift.startTime);
//                welfareViewHolder.status.setVisibility(View.GONE);
//
//                Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + itemGift.indexImage).apply(new RequestOptions()
//                        .placeholder(R.drawable.ic_default_image_007).error(R.mipmap.imgfail)
//                        .dontAnimate().centerCrop()).into(welfareViewHolder.image);
//
//                welfareViewHolder.mView.setTag(itemGift);
//                welfareViewHolder.mView.setOnClickListener(myOnClickListener);
//                break;
//        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (0 == mList.get(position).dataType) {
            return TYPE_ACTIVITY;
        }
        return TYPE_WELFARE;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class ActivityHolder extends ViewHolder {
        @BindView(R.id.m_list_item_mine_welfare_title)
        TextView title;
        @BindView(R.id.m_list_item_mine_welfare_image)
        ImageView image;
        @BindView(R.id.m_list_item_mine_welfare_area)
        TextView area;
        @BindView(R.id.m_list_item_mine_welfare_time)
        TextView time;
        @BindView(R.id.tv_status)
        TextView status;
        @BindView(R.id.ll_container)
        LinearLayout mView;

        ActivityHolder(View view) {
            super(view);
        }


    }

    class WelfareViewHolder extends ViewHolder {
        @BindView(R.id.m_list_item_mine_welfare_title)
        TextView title;
        @BindView(R.id.m_list_item_mine_welfare_image)
        ImageView image;
        @BindView(R.id.m_list_item_mine_welfare_area)
        TextView area;
        @BindView(R.id.m_list_item_mine_welfare_time)
        TextView time;
        @BindView(R.id.tv_status)
        TextView status;
        @BindView(R.id.ll_container)
        LinearLayout mView;

        WelfareViewHolder(View view) {
            super(view);
        }

    }
}
