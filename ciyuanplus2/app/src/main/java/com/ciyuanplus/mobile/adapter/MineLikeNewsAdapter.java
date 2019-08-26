//package tech.milin.social.Adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//
//
//import butterknife.ButterKnife;
//import tech.milin.social.Net.Bean.FreshNewItem;
//import tech.milin.social.R;
//import tech.milin.social.Widget.RoundImageView;
//import tech.milin.social.Widget.ThreeGrideView;
//
///**
// * Created by Alen on 2017/5/11.
// * 个人中心  我的喜欢 里面的list adapter  不用了
// */
//
//public class MineLikeNewsAdapter extends BaseAdapter {
//    private Context mContext;
//    private ArrayList<FreshNewItem> mNewsItems;
//
//    public MineLikeNewsAdapter(Context mContext, ArrayList<FreshNewItem> mNewsItems) {
//        this.mContext = mContext;
//        this.mNewsItems = mNewsItems;
//    }
//
//    @Override
//    public int getCount() {
//        return mNewsItems == null ? 0 : mNewsItems.size();
//    }
//
//    @Override
//    public FreshNewItem getItem(int i) {
//        return mNewsItems.get(i);
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return i;
//    }
//
//    @Override
//    public View getView(int i, View convertView, ViewGroup viewGroup) {
//        ViewHolder holder = null;
//        if (null == convertView) {
//            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.list_item_mine_like_news, null);
//            holder = new ViewHolder(convertView);
//
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//        return convertView;
//    }
//
//    class ViewHolder {
//        @BindView(R.id.m_list_item_mine_like_news_time_text) TextView time;
//        @BindView(R.id.m_list_item_mine_like_news_name_text) TextView name;
//        @BindView(R.id.m_list_item_mine_like_news_head_image) RoundImageView head;
//        @BindView(R.id.m_list_item_mine_like_news_title_text) TextView content;
//        @BindView(R.id.m_list_item_mine_like_news_images) ThreeGrideView images;
//        @BindView(R.id.m_list_item_mine_like_news_location) TextView location;
//        public ViewHolder(View view) {
//            ButterKnife.bind(this, view);
//        }
//
//    }
//}
