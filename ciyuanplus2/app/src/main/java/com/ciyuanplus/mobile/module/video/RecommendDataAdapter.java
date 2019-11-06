package com.ciyuanplus.mobile.module.video;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.allen.library.CircleImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.RoundImageView;
import com.luck.picture.lib.tools.ScreenUtils;

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
        //适配item大小
        int screenWidth = ScreenUtils.getScreenWidth(mContext);
        ViewGroup.LayoutParams layoutParams = helper.getView(R.id.item_cl).getLayoutParams();
        layoutParams.width = (screenWidth / 2) - Utils.dip2px(6);
        helper.getView(R.id.item_cl).setLayoutParams(layoutParams);
        //标题
        helper.setText(R.id.tv_title, data.getContentText());
        //头像
        RequestOptions options = new RequestOptions().placeholder(R.mipmap.default_head_)
                .error(R.mipmap.default_head_).dontAnimate().centerCrop();
        Glide.with(this.mContext).load(Constants.IMAGE_LOAD_HEADER + data.getPhoto()).apply(options).into((ImageView) helper.itemView.findViewById(R.id.iv_head_portrait));

        //昵称
        helper.setText(R.id.tv_author_name, data.getNickname());
        //封面

        Glide.with(mContext)
                .load(Constants.IMAGE_LOAD_HEADER+data.getSomeOne())
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

        //给item子控件添加点击事件，至于处理，在RecommendFragment中执行
        helper.addOnClickListener(R.id.tv_give_a_like);
        helper.addOnClickListener(R.id.iv_cover_recommend);
    }
}
