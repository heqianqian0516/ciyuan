package com.ciyuanplus.mobile

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.blankj.utilcode.util.StringUtils
import com.ciyuanplus.mobile.manager.SharedPreferencesManager
import com.ciyuanplus.mobile.net.ApiContant
import com.ciyuanplus.mobile.net.LiteHttpManager
import com.ciyuanplus.mobile.net.MyHttpListener
import com.ciyuanplus.mobile.net.ResponseData
import com.ciyuanplus.mobile.net.parameter.BindOtherPlatformParameter
import com.ciyuanplus.mobile.net.response.GetAppConfigResponse
import com.ciyuanplus.mobile.utils.Constants
import com.ciyuanplus.mobile.utils.Utils
import com.litesuits.http.request.AbstractRequest
import com.litesuits.http.request.StringRequest
import com.litesuits.http.request.param.HttpMethods
import com.litesuits.http.response.Response
import com.orhanobut.logger.Logger
import java.io.File

/**
 * @author kk
 *
 *  * Creates an IntentService.  Invoked by your subclass's constructor.
 *
 * @param name Used to name the worker thread, important only for debugging.
 */

class RemoveSplashImageService : JobIntentService() {
    override fun onHandleWork(intent: Intent) {

        requestSplashImage()
    }

    private fun requestSplashImage() {
        val postRequest = StringRequest(ApiContant.URL_HEAD + "/api/main/getActivityFlash")
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)

        val map = HashMap<String, String>()
        map["showSection"] = "0"
        postRequest.setHttpBody<AbstractRequest<String>>(BindOtherPlatformParameter(map).requestBody)
        postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(App.mContext) {
            override fun onSuccess(s: String?, response: Response<String>?) {
                super.onSuccess(s, response)
                val response1 = GetAppConfigResponse(s)
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {

                    response1.appConfigItem?.let {

                        if (StringUtils.isEmpty(it.flashPicImage)) {
                            val path = SharedPreferencesManager.getString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_FLASH_IMAGE_PATH, "")
                            val file = File(path)
                            if (file.exists()) {
                                val delete = file.delete()
                                Logger.d(if (delete) "删除成功" else "删除失败")
                            }
                        }
                    }
                }
            }
        })
        LiteHttpManager.getInstance().executeAsync(postRequest)
    }

    companion object {
        //一个 Service 对应一个 id
        private const val JOB_SERVICE_WIDGET_ID = 10111

        fun enqueueWork(context: Context, work: Intent) {
            enqueueWork(context, RemoveSplashImageService::class.java, JOB_SERVICE_WIDGET_ID, work)
        }
    }
}
