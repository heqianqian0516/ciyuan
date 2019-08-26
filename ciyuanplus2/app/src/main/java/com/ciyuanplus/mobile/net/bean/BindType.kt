package com.ciyuanplus.mobile.net.bean

/**
 *    author : Kris
 *    e-mail : chengkai_1215@126.com
 *    date   : 2019/3/1 11:32 PM
 *    class   : BindType
 *    desc   :
 *    version: 1.0
 */
class BindType(val type: Int, val isBind: Boolean, val nickName: String?, val title: String?,val loginType:Int) {

    companion object {
        const val TYPE_LOGIN_TYPE = -1
        const val TYPE_TITLE = 0
        const val TYPE_QQ = 1
        const val TYPE_WEIXIN = 2
        const val TYPE_WEIBO = 3
    }

}