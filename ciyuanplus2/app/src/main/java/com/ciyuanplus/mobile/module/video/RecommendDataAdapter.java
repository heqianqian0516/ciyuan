package com.ciyuanplus.mobile.module.video;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ciyuanplus.mobile.R;

import java.util.List;

/**
 * 视频页面 推荐  列表适配器
 */

public class RecommendDataAdapter extends BaseQuickAdapter<RecommendData.DataBean.ListBean, BaseViewHolder> {
    private Context mContext;

    /**
     * 构造器，用来初始化RecommendDataAdapter
     *
     * @param data 我们的列表数据
     */
    public RecommendDataAdapter(@Nullable List<RecommendData.DataBean.ListBean> data, Context mContext) {
        super(R.layout.list_item_recommend_layout, data);
        this.mContext = mContext;
    }

    /**
     * 增加一个构造方法，便于没有数据时候初始化适配器
     */
    public RecommendDataAdapter() {
        super(R.layout.list_item_recommend_layout);
    }


    @Override
    protected void convert(BaseViewHolder helper, RecommendData.DataBean.ListBean data) {
        //标题
        helper.setText(R.id.tv_title, data.getContentText());
        //头像
        Glide.with(mContext)
                .load(data.getPhoto())
                .into((ImageView) helper.itemView.findViewById(R.id.iv_head_portrait));
        //昵称
        helper.setText(R.id.tv_author_name, data.getNickname());
        //封面
        Glide.with(mContext)
                .load(data.getSomeOne())
                .into((ImageView) helper.itemView.findViewById(R.id.iv_cover_recommend));
        //点赞 isLike当前用户是否赞过(0:否，1:是)
        TextView tvGiveALike = helper.itemView.findViewById(R.id.tv_give_a_like);
        Drawable whiteHeart = mContext.getResources().getDrawable(R.drawable.icon_white_hearts);
        Drawable redHeart = mContext.getResources().getDrawable(R.drawable.icon_red_hearts);
        int isLike = data.getIsLike();
        if (isLike == 0) {
            whiteHeart.setBounds(0, 0, whiteHeart.getMinimumWidth(), whiteHeart.getMinimumHeight());
            tvGiveALike.setCompoundDrawables(whiteHeart, null, null, null);
        }
        if (isLike == 1) {
            redHeart.setBounds(0, 0, redHeart.getMinimumWidth(), redHeart.getMinimumHeight());
            tvGiveALike.setCompoundDrawables(redHeart, null, null, null);
        }
    }
}
