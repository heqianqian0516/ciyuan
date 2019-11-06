package com.ciyuanplus.mobile.adapter

import android.annotation.SuppressLint
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
import com.ciyuanplus.mobile.R2
import com.ciyuanplus.mobile.net.bean.RankedFirstItem
import com.ciyuanplus.mobile.net.response.CommodityListItemRes
import com.ciyuanplus.mobile.net.response.ShopProdDetailItemRes
import com.ciyuanplus.mobile.net.response.ShopProdListItemRes
import com.ciyuanplus.mobile.utils.Constants
import com.ciyuanplus.mobile.widget.SquareImageView
import com.kris.baselibrary.util.NumberUtil
import com.orhanobut.logger.Logger

import java.util.ArrayList


class ExchangeDetailAdapter(var context: Context?, data: List<ShopProdDetailItemRes.CommodityItem>) :
        BaseQuickAdapter<ShopProdDetailItemRes.CommodityItem, BaseViewHolder>(R.layout.exchange_mall_guige_pop, data) {
    override fun convert(helper: BaseViewHolder?, item: ShopProdDetailItemRes.CommodityItem?) {

        if (item != null) {

            helper!!.setText(R.id.tv_guige, item.name)
           // helper.setBackgroundRes(R.id.tv_guige,R.drawable.exchange_guige_bg)
           // helper.setTextColor(R.id.tv_guige,R.drawable.exchange_tv_color)
            //helper.setBackgroundColor(R.id.tv_guige,R.drawable.exchange_guige_bg)

        }

    }
}
