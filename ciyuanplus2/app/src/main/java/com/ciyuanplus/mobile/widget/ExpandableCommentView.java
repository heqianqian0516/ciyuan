//package tech.milin.social.Widget;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.text.Spannable;
//import android.text.SpannableString;
//import android.text.style.ForegroundColorSpan;
//import android.util.AttributeSet;
//import android.view.Gravity;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import tech.milin.social.Module.ForumDetail.PostDetail.PostDetailActivity;
//import tech.milin.social.Manager.UserInfoData;
//import tech.milin.social.Net.Bean.ReplyItem;
//import tech.milin.social.R;
//import tech.milin.social.Utils.Utils;
//
//
///**
// * Created by Alen on 2017/3/26.
// *
// * Drop 不用了
// */
//public class ExpandableCommentView extends LinearLayout implements View.OnClickListener {
//    private ReplyItem[] mComments;
//    private ImageView[] mDeleteViews;
//    private TextView[] mDetailViews;
//    private boolean mExpanded;
//    private Activity mParent;
//
//    public ExpandableCommentView(Context context) {
//        super(context);
//        initView();
//    }
//
//
//    public ExpandableCommentView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        initView();
//    }
//
//    public ExpandableCommentView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        initView();
//    }
//
//    private void initView() {
//        setOrientation(VERTICAL);
//
//    }
//
//    public void setDataSource(ReplyItem[] comments, PostDetailActivity parent) {
//        mParent = parent;
//        mComments = comments;
//        mDetailViews = new TextView[comments.length];
//        mDeleteViews = new ImageView[comments.length];
//        removeAllViews();
//        for (int i = 0; i < comments.length && i < 4; i++) {//
//            addView(createView(i));
//            if (Utils.isStringEquals(mComments[i].sendUserUuid, UserInfoData.getInstance().getUserInfoItem().uuid)) {
//                addView(createDeleteView(i));
//            }
//        }
//
//    }
//
//    private View createView(final int pos) {
//        if (mDetailViews[pos] != null) return mDetailViews[pos];
//        ForegroundColorSpan span1 = new ForegroundColorSpan(0xff333333);
//        ForegroundColorSpan span2 = new ForegroundColorSpan(0xffaaaaaa);
//        ReplyItem item = mComments[pos];
//        String content = item.sendNickname + "回复" + item.toNickname + " : " + item.contentText;
//        SpannableString spanString = new SpannableString(content);
//        spanString.setSpan(span1, 0, item.sendNickname.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spanString.setSpan(span2, item.sendNickname.length(), (item.sendNickname + "回复" + item.toNickname).length() + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        TextView text = new TextView(getDefaultContext());
//        text.setTextSize(Utils.px2dip(30));
//        text.setTextColor(0xff333333);
//        text.setText(spanString);
//        text.setGravity(Gravity.LEFT);
//        mDetailViews[pos] = text;
//        mDetailViews[pos].setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mParent instanceof PostDetailActivity) {
//                    ((PostDetailActivity) mParent).replyReply(mComments[pos]);
//                }
//            }
//        });
//        return text;
//    }
//
//
//    private ImageView createDeleteView(int i) {
//        if (mDeleteViews[i] != null) return mDeleteViews[i];
//        mDeleteViews[i] = new ImageView(getDefaultContext());
//        mDeleteViews[i].setImageResource(R.mipmap.icon_details_comment_delete);
//        mDeleteViews[i].setTag(i);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.setMargins(0, 30, 0, 30);
//        mDeleteViews[i].setLayoutParams(params);
//        mDeleteViews[i].setOnClickListener(this);
//        return mDeleteViews[i];
//    }
//
//    @Override
//    public void onClick(View view) {
//        final int pos = (int) view.getTag();
//        CustomDialog.Builder builder = new CustomDialog.Builder(mParent);
//        builder.setMessage("确定删除吗？");
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                ((PostDetailActivity) mParent).deleteReply(mComments[pos]);
//                dialog.dismiss();
//            }
//        });
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        CustomDialog dialog = builder.create();
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();
//    }
//
////    private void changeState(){
////        mExpanded = !mExpanded;
////        if(mExpanded) {// 展开
////            if(mComments.length > 3) {
////                removeView(mMoreView);
////                for(int i = 3; i < mComments.length; i++) {
////                    addView(createView(i));
////                }
////                addView(mMoreView);
////                mMoreView.setText("收起回复");
////            }
////        } else {
////            if(mComments.length > 3) {
////                for(int i = 3; i < mComments.length; i++) {
////                    removeView(createView(i));
////                }
////                mMoreView.setText("更多回复");
////
////            }
////        }
////    }
//
//}
