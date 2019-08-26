package com.ciyuanplus.mobile.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ciyuanplus.mobile.R
import com.ciyuanplus.mobile.net.bean.BindType
import com.ciyuanplus.mobile.net.bean.BindType.Companion.TYPE_QQ
import com.ciyuanplus.mobile.net.bean.BindType.Companion.TYPE_TITLE
import com.ciyuanplus.mobile.net.bean.BindType.Companion.TYPE_WEIBO
import com.ciyuanplus.mobile.net.bean.BindType.Companion.TYPE_WEIXIN


/**
 *    author : Kris
 *    e-mail : chengkai_1215@126.com
 *    date   : 2019/3/1 11:20 PM
 *    class   : AccountManageAdapter
 *    desc   :
 *    version: 1.0
 */


class AccountManageAdapter(data: List<BindType>) : BaseQuickAdapter<BindType, BaseViewHolder>(R.layout.item_account_manage_login_type_bind, data) {

    override fun convert(helper: BaseViewHolder, item: BindType) {

        when (item.type) {
            TYPE_TITLE -> {
                helper.setVisible(R.id.tv_title, true)
                        .setText(R.id.tv_title, item.title)
                        .setGone(R.id.rl_layout, false)

            }
            TYPE_WEIBO -> {
                helper.setGone(R.id.tv_title, false)
                        .setVisible(R.id.rl_layout, true)
                        .setImageResource(R.id.iv_account_icon, R.drawable.icon_account_manage_weibo)
                        .setVisible(R.id.tv_unbind_type, !item.isBind)
                        .setVisible(R.id.bindButton, !item.isBind)
                        .setVisible(R.id.tv_bind_type, item.isBind)
                        .setText(R.id.tv_bind_type, "微博")
                        .setText(R.id.tv_unbind_type, "微博")
                        .setVisible(R.id.tv_nickname, item.isBind)
                        .setText(R.id.tv_nickname, item.nickName)
                        .addOnClickListener(R.id.bindButton)
            }
            TYPE_QQ -> {
                helper.setGone(R.id.tv_title, false)
                        .setVisible(R.id.rl_layout, true)
                        .setImageResource(R.id.iv_account_icon, R.drawable.icon_account_manage_qq)
                        .setVisible(R.id.tv_unbind_type, !item.isBind)
                        .setVisible(R.id.bindButton, !item.isBind)
                        .setVisible(R.id.tv_bind_type, item.isBind)
                        .setText(R.id.tv_bind_type, "QQ")
                        .setText(R.id.tv_unbind_type, "QQ")
                        .setVisible(R.id.tv_nickname, item.isBind)
                        .setText(R.id.tv_nickname, item.nickName)
                        .addOnClickListener(R.id.bindButton)

            }

            TYPE_WEIXIN -> {
                helper.setGone(R.id.tv_title, false)
                        .setVisible(R.id.rl_layout, true)
                        .setImageResource(R.id.iv_account_icon, R.drawable.icon_account_manage_weixin)
                        .setVisible(R.id.tv_unbind_type, !item.isBind)
                        .setVisible(R.id.bindButton, !item.isBind)
                        .setVisible(R.id.tv_bind_type, item.isBind)
                        .setText(R.id.tv_bind_type, "微信")
                        .setText(R.id.tv_unbind_type, "微信")
                        .setVisible(R.id.tv_nickname, item.isBind)
                        .setText(R.id.tv_nickname, item.nickName)
                        .addOnClickListener(R.id.bindButton)
            }
        }
    }
}