//package tech.milin.social.Adapter;
//
//import androidx.recyclerview.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//
//import butterknife.ButterKnife;
//import tech.milin.social.Module.Wiki.AroundWiki.AroundWikiActivity;
//import tech.milin.social.Net.Bean.WikiTypeItem;
//import tech.milin.social.R;
//import tech.milin.social.Utils.Utils;
//
///**
// * Created by Alen on 2017/5/11.
// * 周边页面  民生  列表适配器
// *
// * 貌似不用了
// */
//
//public class AroundLiveHoodAdapter extends RecyclerView.Adapter<AroundLiveHoodAdapter.ViewHolder> {
//    private AroundWikiActivity mContext;
//    private WikiTypeItem[] wikiTypeItems;
//    private View.OnClickListener mItemClickListener;
//
//    public AroundLiveHoodAdapter(AroundWikiActivity mContext, WikiTypeItem[] wikiTypeItems, View.OnClickListener itemClickListener) {
//        this.mContext = mContext;
//        this.wikiTypeItems = wikiTypeItems;
//        this.mItemClickListener = itemClickListener;
//
//    }
//    public void setItemClickListener(View.OnClickListener itemClickListener) {
//        this.mItemClickListener = itemClickListener;
//    }
//
//    public WikiTypeItem getItem(int i) {
//        return wikiTypeItems[i];
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getDefaultContext()).inflate(R.layout.list_item_wiki_type, parent, false);
//        AroundLiveHoodAdapter.ViewHolder holder = new AroundLiveHoodAdapter.ViewHolder(view);
//        view.setOnClickListener(mItemClickListener);
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        WikiTypeItem item = wikiTypeItems[position];
//        if (mContext.getWikiType() != null && Utils.isStringEquals(mContext.getWikiType().id, item.id)) {
//            holder.selected.setSelected(true);
//        } else {
//            holder.selected.setSelected(false);
//        }
//        holder.name.setText(item.name);
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return i;
//    }
//
//    @Override
//    public int getItemCount() {
//        return wikiTypeItems == null ? 0 : wikiTypeItems.length;
//    }
//
//    class ViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.m_list_item_wiki_type_name) TextView name;
//        @BindView(R.id.m_list_item_wiki_type_selected) LinearLayout selected;
//
//        public ViewHolder(View convertView) {
//            super(convertView);
//            ButterKnife.bind(this, convertView);
//
//        }
//    }
//}
