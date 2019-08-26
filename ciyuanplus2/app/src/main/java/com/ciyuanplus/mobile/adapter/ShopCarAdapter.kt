package com.ciyuanplus.mobile.adapter

import com.blankj.utilcode.util.StringUtils
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseItemDraggableAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ciyuanplus.mobile.R
import com.ciyuanplus.mobile.net.bean.ShopCarItemListResponse
import com.ciyuanplus.mobile.utils.Constants
import com.kris.baselibrary.util.NumberUtil

public class ShopCarAdapter(mCommodityList: ArrayList<ShopCarItemListResponse.SpecItem>)
    : BaseItemDraggableAdapter<ShopCarItemListResponse.SpecItem, BaseViewHolder>(R.layout.list_item_shop_car, mCommodityList) {

    override fun convert(helper: BaseViewHolder?, item: ShopCarItemListResponse.SpecItem?) {

        helper?.addOnClickListener(R.id.minusButton, R.id.addButton)
        item?.let {

            Glide.with(mContext).load(Constants.IMAGE_LOAD_HEADER + item.prodImg).into(helper?.getView(R.id.siv_order_image)!!)

            helper?.setText(R.id.tv_title, item.prodName)
                    ?.setText(R.id.tv_price, "￥ : ${NumberUtil.getAmountValue(item.prodPrice.toDouble() / 100)}")
                    ?.setText(R.id.tv_specification, "规格 : ${item.specName}")
                    ?.setText(R.id.prdCount, item.prodCount.toString())
                    ?.addOnClickListener(R.id.addButton)?.addOnClickListener(R.id.minusButton)
                    ?.addOnClickListener(R.id.btnDelete)
        }
    }
}
