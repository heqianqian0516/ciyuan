package com.ciyuanplus.mobile.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ciyuanplus.mobile.R
import com.ciyuanplus.mobile.net.bean.AddressItem

class MyAddressAdapterNew(mCommunityItems: ArrayList<AddressItem>)
    : BaseQuickAdapter<AddressItem, BaseViewHolder>(R.layout.item_address, mCommunityItems) {

    override fun convert(helper: BaseViewHolder?, item: AddressItem?) {

        if (item != null) {
            helper?.let {

                it.setText(R.id.tv_buyer, item.name)
                        .setText(R.id.tv_buyer_address, item.address)
                        .setText(R.id.tv_buyer_phone_num, item.mobile)
                        .addOnClickListener(R.id.btnDelete)
                        .addOnClickListener(R.id.deleteLayout)
                        .addOnClickListener(R.id.contentLayout)
            }
        }
    }
}
