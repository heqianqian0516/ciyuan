package com.ciyuanplus.mobile.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ciyuanplus.mobile.R
import com.ciyuanplus.mobile.net.bean.RankedFirstItem
import com.ciyuanplus.mobile.net.response.CommodityListItemRes
import com.ciyuanplus.mobile.net.response.ShopProdListItemRes
import com.ciyuanplus.mobile.utils.Constants
import com.ciyuanplus.mobile.widget.SquareImageView
import com.kris.baselibrary.util.NumberUtil
import com.orhanobut.logger.Logger

import java.util.ArrayList


class ExchangeMallAdapter(var context: Context?, data: List<ShopProdListItemRes.CommodityItem>) :
        BaseQuickAdapter<ShopProdListItemRes.CommodityItem, BaseViewHolder>(R.layout.exchange_mall_item, data) {
    override fun convert(helper: BaseViewHolder?, item: ShopProdListItemRes.CommodityItem?) {

        if (item != null) {


            helper!!.setText(R.id.tv_title, item.name)
                    .setText(R.id.tv_price_now, "￥ ${NumberUtil.getAmountValue(item.price.toDouble() / 100)}")
                    .setText(R.id.tv_price_original, "￥ ${NumberUtil.getAmountValue(item.orgPrice.toDouble() / 100)}")

            helper.getView<TextView>(R.id.tv_price_original).paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            helper.getView<TextView>(R.id.tv_price_original).paint.isAntiAlias = true

            val list = item.img.split(",")
            Logger.d("图片地址 ${Constants.IMAGE_LOAD_HEADER + list[0]}")

            val options = RequestOptions().placeholder(R.drawable.ic_default_image_007).error(R.mipmap.imgfail).dontAnimate().centerCrop()
            Glide.with(mContext).load(Constants.IMAGE_LOAD_HEADER + list[0]).apply(options).into(helper.getView(R.id.iv_image))
            /*if(item.source==null){
                helper.setVisible(R.id.hot_taobao,false);
                helper.setVisible(R.id.tv_price_original,true)
            } else if(item.source=="taobao"){
                helper.setVisible(R.id.hot_taobao,true);
                helper.setVisible(R.id.tv_price_original,false)
            }*/

        }
    }
}
