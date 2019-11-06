package com.ciyuanplus.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.allen.library.CircleImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.module.video.RecommendData;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.List;

public class VideoDetailAdapter extends RecyclerView.Adapter<VideoDetailAdapter.ViewHolder>{
    private List<RecommendData.DataBean.ListBean> data;
    private Context mContext;
    private int index = 0;
    public VideoDetailAdapter(List<RecommendData.DataBean.ListBean> data, Context mContext) {
        this.data = data;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public VideoDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_detail_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoDetailAdapter.ViewHolder holder, int position) {
        holder.tvNickname.setText(data.get(position).getNickname());
        holder.tvTitle.setText(data.get(position).getContentText());
        RequestOptions options = new RequestOptions().placeholder(R.mipmap.default_head_)
                .error(R.mipmap.default_head_).dontAnimate().centerCrop();
        Glide.with(this.mContext).load(Constants.IMAGE_LOAD_HEADER + data.get(position).getPhoto()).apply(options).into(holder.iconHead);
        holder.tvCommentNum.setText(data.get(position).getCommentCount()+"");
        holder.tvFollownum.setText(data.get(position).getLikeCount()+"");
       // holder.tvSharenum.setText(data.get(position).get);
        holder.videoPlayer.setUpLazy(Constants.IMAGE_LOAD_HEADER+data.get(position).getImgs(), true, null, null, "这是title");
        //增加title
        holder.videoPlayer.getTitleTextView().setVisibility(View.GONE);
        //设置返回键
        holder.videoPlayer.getBackButton().setVisibility(View.GONE);
        //设置全屏按键功能
        holder.videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.videoPlayer.startWindowFullscreen(mContext, false, true);
            }
        });
        holder.videoPlayer.setPlayPosition(position);
        //是否根据视频尺寸，自动选择竖屏全屏或者横屏全屏
        holder.videoPlayer.setAutoFullWithSize(true);
        //音频焦点冲突时是否释放
        holder.videoPlayer.setReleaseWhenLossAudio(false);
        //全屏动画
        holder.videoPlayer.setShowFullAnimation(true);
        //小屏时不触摸滑动
        holder.videoPlayer.setIsTouchWiget(false);

        index++;
        if (index >= data.size()-1) {
            index = 0;
        }
        holder.imageComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 if (onCommentClickListener!=null){
                   onCommentClickListener.onCommnetListener(position);
                 }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;
        private final ImageView iconHead;
        private final TextView tvNickname;
        private final StandardGSYVideoPlayer videoPlayer;
        private final ImageView imageComment;
        private final TextView tvCommentNum;
        private final TextView tvFollownum;
        private final ImageView imageShare;
        private final TextView tvSharenum;
        private final ImageView imageFollow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            iconHead = itemView.findViewById(R.id.iv_head_portrait);
            tvNickname = itemView.findViewById(R.id.tv_author_name);
            videoPlayer = itemView.findViewById(R.id.video_player);
            imageComment = itemView.findViewById(R.id.image_comment);
            tvCommentNum = itemView.findViewById(R.id.tv_comment_num);
            tvFollownum = itemView.findViewById(R.id.tv_follow_num);
            imageShare = itemView.findViewById(R.id.image_share);
            tvSharenum = itemView.findViewById(R.id.tv_share_num);
            imageFollow = itemView.findViewById(R.id.image_follow);
        }
    }

    //评论接口回调
    public static interface OnCommentClickListener{
        public void onCommnetListener(int position);
    }
    OnCommentClickListener onCommentClickListener;
    public void setOnCommentClickListener(OnCommentClickListener onCommentClickListener){
         this.onCommentClickListener=onCommentClickListener;
    }
}
