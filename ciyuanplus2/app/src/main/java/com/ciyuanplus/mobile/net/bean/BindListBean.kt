package com.ciyuanplus.mobile.net.bean

data class BindListBean(val isWxBind: Int = 0,
                        val isQQBind: Int = 0,
                        val sinaName: String = "",
                        val loginType: Int = 0,
                        val isSinaBind: Int = 0,
                        val qqName: String = "",
                        val wxId: String = "",
                        val sinaId: String = "",
                        val wxName: String = "",
                        val qqId: String = "")
