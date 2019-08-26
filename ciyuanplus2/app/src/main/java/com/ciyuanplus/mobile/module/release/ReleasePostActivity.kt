package com.ciyuanplus.mobile.module.release

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.ciyuanplus.mobile.MyBaseActivity
import com.ciyuanplus.mobile.R
import com.ciyuanplus.mobile.adapter.GridImageAdapter
import com.ciyuanplus.mobile.image_select.common.Constant
import com.ciyuanplus.mobile.manager.CacheManager
import com.ciyuanplus.mobile.manager.EventCenterManager
import com.ciyuanplus.mobile.manager.FullyGridLayoutManager
import com.ciyuanplus.mobile.manager.SharedPreferencesManager
import com.ciyuanplus.mobile.net.ApiContant
import com.ciyuanplus.mobile.net.ApiContant.REQUEST_ADD_EVENT_INFO
import com.ciyuanplus.mobile.net.ApiContant.REQUEST_ADD_MY_POST_URL
import com.ciyuanplus.mobile.net.LiteHttpManager
import com.ciyuanplus.mobile.net.MyHttpListener
import com.ciyuanplus.mobile.net.ResponseData
import com.ciyuanplus.mobile.net.bean.FreshNewItem
import com.ciyuanplus.mobile.net.parameter.ReleasePostNewParameter
import com.ciyuanplus.mobile.net.parameter.ReleasePostParameter
import com.ciyuanplus.mobile.net.parameter.UpLoadFileApiParameter
import com.ciyuanplus.mobile.net.parameter.UpdateNewPostApiParameter
import com.ciyuanplus.mobile.net.response.UpLoadFilesResponse
import com.ciyuanplus.mobile.utils.Constants
import com.ciyuanplus.mobile.utils.PictureUtils
import com.ciyuanplus.mobile.utils.Utils
import com.ciyuanplus.mobile.widget.CommonToast
import com.ciyuanplus.mobile.widget.GridSpacesItemDecoration
import com.litesuits.http.exception.HttpException
import com.litesuits.http.request.AbstractRequest
import com.litesuits.http.request.StringRequest
import com.litesuits.http.request.content.multi.FilePart
import com.litesuits.http.request.param.HttpMethods
import com.litesuits.http.response.Response
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.tools.DateUtils
import com.luck.picture.lib.tools.StringUtils
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_release_post.*
import java.io.File
import java.util.HashMap
import kotlin.collections.ArrayList

/**
 * author : Kris
 * e-mail : chengkai_1215@126.com
 * date   : 2019/2/21
 * class  : ReleasePostActivity.kt
 * desc   : 发布帖子
 * version: 1.0
 */


class ReleasePostActivity : MyBaseActivity(), View.OnClickListener {

    private var selectList: MutableList<LocalMedia> = ArrayList()
    private lateinit var adapter: GridImageAdapter
    private var maxSelectNum = 9
    private var themeId = R.style.picture_default_style
    private var isWorldChecked = true
    private val mImagePathList = ArrayList<String>()
    private val mUploadUrls = ArrayList<String>()
    private val mUploadMap = HashMap<String, String>()
    private var mContent: String? = null
    private var isEdit = false
    private var isVideo = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_release_post)


        initView()
    }
      private var activityUuid=""
    private var mActivityItem=0
    private fun initView() {
        mActivityItem= intent.getIntExtra(Constants.INTENT_OPEN_TYPE,0)
          if(mActivityItem==1){
              activityUuid=intent.getStringExtra("activityUuid")

        }
        title_bar.setTitle("发布")
        title_bar.setOnBackListener(View.OnClickListener { finish() })

        tv_release.setOnClickListener(this)
        iv_play.setOnClickListener(this)
        iv_del_video.setOnClickListener(this)

        val space = resources.getDimension(R.dimen.spacing_tiny)
        recycler.addItemDecoration(GridSpacesItemDecoration(3, space.toInt(), false))
        val manager = FullyGridLayoutManager(this@ReleasePostActivity, 3, GridLayoutManager.VERTICAL, false)
        recycler.layoutManager = manager
        adapter = GridImageAdapter(this@ReleasePostActivity, onAddPicClickListener)
        adapter.setMediaList(selectList)
        adapter.setSelectMax(maxSelectNum)
        recycler.adapter = adapter
        adapter.setOnItemClickListener { position, v ->
            if (selectList.size > 0) {
                val media = selectList.get(position)
                val pictureType = media.pictureType
                val mediaType = PictureMimeType.pictureToVideo(pictureType)
                when (mediaType) {
                    1 ->
                        // 预览图片 可自定长按保存路径
                        //PictureSelector.create(ReleasePostActivity.this).themeStyle(themeId).externalPicturePreview(position, "/custom_file", selectList);
                        PictureSelector.create(this@ReleasePostActivity).themeStyle(R.style.picture_default_style).openExternalPreview(position, selectList)
                    2 ->
                        // 预览视频
                        PictureSelector.create(this@ReleasePostActivity).externalPictureVideo(media.path)
                    3 ->
                        // 预览音频
                        PictureSelector.create(this@ReleasePostActivity).externalPictureAudio(media.path)
                }
            }
        }
    }


    private val onAddPicClickListener = GridImageAdapter.onAddPicClickListener {

        //        val mode = cb_mode.isChecked()
        val mode = true
//        val isCamera = cb_isCamera.isChecked()
        val isCamera = true

        val isShowCrop = true

        if (mode) {
            // 进入相册 以下是例子：不需要的api可以不写
            PictureSelector.create(this@ReleasePostActivity)
                    .openGallery(PictureMimeType.ofAll())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                    .maxSelectNum(maxSelectNum)// 最大图片选择数量
                    .minSelectNum(1)// 最小选择数量
                    .imageSpanCount(4)// 每行显示个数
                    .selectionMode(if (isCamera)
                        PictureConfig.MULTIPLE
                    else
                        PictureConfig.SINGLE)// 多选 or 单选
                    .videoSelectionMode(PictureConfig.SINGLE)
                    .previewImage(true)// 是否可预览图片
                    .previewVideo(true)// 是否可预览视频
                    .enablePreviewAudio(true) // 是否可播放音频
                    .isCamera(isCamera)// 是否显示拍照按钮
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                    //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                    .enableCrop(false)// 是否裁剪
                    .compress(true)// 是否压缩
                    .synOrAsy(true)//同步true或异步false 压缩 默认同步
                    //.compressSavePath(getPath())//压缩图片保存地址
                    //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
//                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .hideBottomControls(!isShowCrop)// 是否显示uCrop工具栏，默认不显示
                    .isGif(false)// 是否显示gif图片
                    .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                    .circleDimmedLayer(false)// 是否圆形裁剪
                    .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                    .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                    .openClickSound(true)// 是否开启点击声音
                    .selectionMedia(selectList)// 是否传入已选图片
                    //.isDragFrame(false)// 是否可拖动裁剪框(固定)
                    //                        .videoMaxSecond(15)
                    //                        .videoMinSecond(10)
                    //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
                    //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                    //.rotateEnabled(true) // 裁剪是否可旋转图片
                    //.scaleEnabled(true)// 裁剪是否可放大缩小图片
                    //.videoQuality()// 视频录制质量 0 or 1
                    //.videoSecond()//显示多少秒以内的视频or音频也可适用
                    .recordVideoSecond(10)//录制视频秒数 默认60s
                    .forResult(PictureConfig.CHOOSE_REQUEST)//结果回调onActivityResult code
        } else {
            // 单独拍照
            PictureSelector.create(this@ReleasePostActivity)
                    .openCamera(PictureMimeType.ofImage())// 单独拍照，也可录像或也可音频 看你传入的类型是图片or视频
                    .theme(themeId)// 主题样式设置 具体参考 values/styles
                    .maxSelectNum(maxSelectNum)// 最大图片选择数量
                    .minSelectNum(1)// 最小选择数量
                    .selectionMode(if (isCamera)
                        PictureConfig.MULTIPLE
                    else
                        PictureConfig.SINGLE)// 多选 or 单选
                    .videoSelectionMode(PictureConfig.SINGLE)
                    .previewImage(true)// 是否可预览图片
                    .previewVideo(true)// 是否可预览视频
                    .enablePreviewAudio(true) // 是否可播放音频
                    .isCamera(true)// 是否显示拍照按钮
                    .enableCrop(false)// 是否裁剪0
                    .compress(true)// 是否压缩
//                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                    .isGif(false)// 是否显示gif图片
                    .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                    .circleDimmedLayer(false)// 是否圆形裁剪
                    .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                    .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                    .openClickSound(true)// 是否开启点击声音
                    .selectionMedia(selectList)// 是否传入已选图片
                    .previewEggs(false)//预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    //.cropCompressQuality(90)// 裁剪压缩质量 默认为100
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
                    //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                    //.rotateEnabled() // 裁剪是否可旋转图片
                    //.scaleEnabled()// 裁剪是否可放大缩小图片
                    //.videoQuality()// 视频录制质量 0 or 1
                    //.videoSecond()////显示多少秒以内的视频or音频也可适用
                    .forResult(PictureConfig.CHOOSE_REQUEST)//结果回调onActivityResult code
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val path = getPath()
        Logger.d(path)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data)
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的

                    val firstMedia = selectList[0]
                    isVideo = PictureMimeType.isVideo(firstMedia.pictureType)
                    if (isVideo) {

                        isVideo = true
                        Log.e("media路径-----》", firstMedia.path)
                        Log.e("media类型-----》", firstMedia.mimeType.toString())
                        Log.e("media类型-----》", firstMedia.pictureType.toString())
                        Log.e("media类型-----》", PictureMimeType.isVideo(firstMedia.pictureType).toString())

                        val options = RequestOptions()
                                .centerCrop()
                                //                        .placeholder(R.drawable.ic_default)
                                //                        .error(R.drawable.ic_default)
                                //                        .transform(new GlideRoundTransform(getApplicationContext(), 8))
                                .diskCacheStrategy(DiskCacheStrategy.NONE)

                        tv_duration.visibility = View.VISIBLE
                        tv_duration.text = DateUtils.timeParse(firstMedia.duration)
                        val drawable = ContextCompat.getDrawable(this, R.drawable.video_icon)
                        if (null != drawable) {
                            StringUtils.modifyTextViewDrawable(tv_duration, drawable, 0)
                        }
                        Glide.with(this)
                                .load(firstMedia.path)
                                .apply(options)
                                .into(iv_video)
                        rl_video.visibility = View.VISIBLE
                        recycler.visibility = View.GONE
                        tv_duration.visibility = View.GONE
                        tv_duration.text = ""

                    } else {

                        rl_video.visibility = View.GONE
                        recycler.visibility = View.VISIBLE
                        for (media in selectList) {
                            Log.e("media路径-----》", media.path)
                            Log.e("media类型-----》", media.mimeType.toString())
                            Log.e("media类型-----》", media.pictureType.toString())
                            Log.e("media类型-----》", PictureMimeType.isVideo(media.pictureType).toString())

                        }
                        adapter.setMediaList(selectList)
                        adapter.notifyDataSetChanged()


                    }
                }
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.left_back -> finish()


            R.id.iv_play -> PictureSelector.create(this@ReleasePostActivity).externalPictureVideo(selectList[0].path)

            R.id.iv_del_video -> {
                selectList.clear()
                rl_video.visibility = View.GONE
                recycler.visibility = View.VISIBLE
                adapter.setMediaList(selectList)
                adapter.notifyDataSetChanged()
            }
            R.id.tv_release -> {

                mImagePathList.clear()

                if (et_content.text.toString().isEmpty()) {
                    CommonToast.getInstance("请描述这一刻的想法").show()
                    return
                }

                for (it in selectList) {
                    mImagePathList.add(it.path)
                }
                if (isVideo) {
                    if(Utils.isStringEmpty(activityUuid)&&mActivityItem==0){
                        uploadVideo(REQUEST_ADD_MY_POST_URL, 0)
                    }else{
                        uploadVideo(REQUEST_ADD_EVENT_INFO, 0,activityUuid,1)
                    }

                } else {
                    if (Utils.isStringEmpty(activityUuid)&&mActivityItem==0){
                        uploadImageFile(REQUEST_ADD_MY_POST_URL, 0)
                    }else {
                        uploadImageFile(REQUEST_ADD_EVENT_INFO, 0, activityUuid, 0)
                    }
                }
            }
        }
    }

    private fun uploadVideo(url: String, bizType: Int) {

        showLoadingDialog()
        Thread(Runnable // 这里最好新开线程进行处理
        {
            val body = UpLoadFileApiParameter().requestBody

            val needUploadFile = ArrayList<String>()

            for (i in mImagePathList.indices) {
                if (!mImagePathList[i].startsWith("/storage")) continue

                Logger.d("视频帖子类型。。。。。。${PictureMimeType.createVideoType(mImagePathList[i])}")
                body.addPart(FilePart("files", File(mImagePathList[i]), PictureMimeType.createVideoType(mImagePathList[i])))
                needUploadFile.add(mImagePathList[i])

            }

            val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_UPLOAD_FILES_URL)
            postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
            postRequest.setHttpBody<AbstractRequest<String>>(body)
            postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(this@ReleasePostActivity) {
                override fun onSuccess(s: String?, response: Response<String>?) {
                    super.onSuccess(s, response)

                    dismissLoadingDialog()

                    val response1 = UpLoadFilesResponse(s)
                    if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {

                        mUploadMap.clear()
                        for (i in response1.fileList.url.indices) {
                            mUploadMap[needUploadFile[i]] = response1.fileList.url[i]
                            mUploadUrls.add(response1.fileList.url[i])
                        }
                        startNewPost(url, bizType)

                    } else {

                        CommonToast.getInstance(response1.mMsg).show()
                    }

                }

                override fun onFailure(e: HttpException?, response: Response<String>?) {
                    super.onFailure(e, response)
                    CommonToast.getInstance(e.toString()).show()

                    dismissLoadingDialog()
                }
            })
            LiteHttpManager.getInstance().executeAsync(postRequest)
            Constant.imageList.clear()
        }).start()

    }

    private fun uploadVideo(url: String, bizType: Int,activityUuid:String,type:Int) {

        showLoadingDialog()
        Thread(Runnable // 这里最好新开线程进行处理
        {
            val body = UpLoadFileApiParameter().requestBody

            val needUploadFile = ArrayList<String>()

            for (i in mImagePathList.indices) {
                if (!mImagePathList[i].startsWith("/storage")) continue

                Logger.d("视频帖子类型。。。。。。${PictureMimeType.createVideoType(mImagePathList[i])}")
                body.addPart(FilePart("files", File(mImagePathList[i]), PictureMimeType.createVideoType(mImagePathList[i])))
                needUploadFile.add(mImagePathList[i])

            }

            val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_UPLOAD_FILES_URL)
            postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
            postRequest.setHttpBody<AbstractRequest<String>>(body)
            postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(this@ReleasePostActivity) {
                override fun onSuccess(s: String?, response: Response<String>?) {
                    super.onSuccess(s, response)

                    dismissLoadingDialog()

                    val response1 = UpLoadFilesResponse(s)
                    if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {

                        mUploadMap.clear()
                        for (i in response1.fileList.url.indices) {
                            mUploadMap[needUploadFile[i]] = response1.fileList.url[i]
                            mUploadUrls.add(response1.fileList.url[i])
                        }
                        startNewPost(url, bizType,activityUuid,type)

                    } else {

                        CommonToast.getInstance(response1.mMsg).show()
                    }

                }

                override fun onFailure(e: HttpException?, response: Response<String>?) {
                    super.onFailure(e, response)
                    CommonToast.getInstance(e.toString()).show()

                    dismissLoadingDialog()
                }
            })
            LiteHttpManager.getInstance().executeAsync(postRequest)
            Constant.imageList.clear()
        }).start()

    }
    // 上传多个文件
    private fun uploadImageFile(url: String, bizType: Int) {

        showLoadingDialog()
        Thread(Runnable // 这里最好新开线程进行处理
        {
            val body = UpLoadFileApiParameter().requestBody
            var fileNotEmpty = false
            val needUploadFile = ArrayList<String>()
            for (i in mImagePathList.indices) {
                if (!mImagePathList[i].startsWith("/storage")) continue

                val origin = File(mImagePathList[i])
                val fileName = origin.name

                val compressPath = PictureUtils.compressImage(mImagePathList.get(i),
                        CacheManager.getInstance().cacheDirectory + fileName.substring(0, fileName.lastIndexOf(".")) + "compressImage.jpeg")
                val compressImage = File(compressPath)
                if (compressImage.exists())
                    body.addPart(FilePart("files", compressImage, "image/jpeg"))
                else
                    body.addPart(FilePart("files", File(mImagePathList[i]), "image/jpeg"))
                needUploadFile.add(mImagePathList[i])
                fileNotEmpty = true
            }
            if (!fileNotEmpty) {// 只有编辑会到这个逻辑里面， 否则在前面就截住了
                val uploadUrls = arrayOfNulls<String>(mImagePathList.size)
                for (i in mImagePathList.indices) {
                    if (!mImagePathList[i].startsWith("/storage"))
                        uploadUrls[i] = mImagePathList[i]
                }
                updateNewPost()
            }
            val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_UPLOAD_FILES_URL)
            postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
            postRequest.setHttpBody<AbstractRequest<String>>(body)
            postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(this@ReleasePostActivity) {
                override fun onSuccess(s: String?, response: Response<String>?) {
                    super.onSuccess(s, response)

                    dismissLoadingDialog()
                    val response1 = UpLoadFilesResponse(s)
                    if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                        if (!isEdit) {
                            mUploadMap.clear()
                            for (i in response1.fileList.url.indices) {
                                mUploadMap[needUploadFile[i]] = response1.fileList.url[i]
                                mUploadUrls.add(response1.fileList.url[i])
                            }
                            startNewPost(url, bizType)
                        } else {

                            for (i in response1.fileList.url.indices) {
                                mUploadMap[needUploadFile[i]] = response1.fileList.url[i]
                                mUploadUrls.add(response1.fileList.url[i])
                            }
                            updateNewPost()
                        }
                    } else {

                        CommonToast.getInstance(response1.mMsg).show()
                    }

                }

                override fun onFailure(e: HttpException?, response: Response<String>?) {
                    super.onFailure(e, response)
                    dismissLoadingDialog()
                    CommonToast.getInstance(e.toString()).show()
                }
            })
            LiteHttpManager.getInstance().executeAsync(postRequest)
            Constant.imageList.clear()
        }).start()

    }
    private fun uploadImageFile(url: String, bizType: Int,activityUuid: String,type: Int) {

        showLoadingDialog()
        Thread(Runnable // 这里最好新开线程进行处理
        {
            val body = UpLoadFileApiParameter().requestBody
            var fileNotEmpty = false
            val needUploadFile = ArrayList<String>()
            for (i in mImagePathList.indices) {
                if (!mImagePathList[i].startsWith("/storage")) continue

                val origin = File(mImagePathList[i])
                val fileName = origin.name

                val compressPath = PictureUtils.compressImage(mImagePathList.get(i),
                        CacheManager.getInstance().cacheDirectory + fileName.substring(0, fileName.lastIndexOf(".")) + "compressImage.jpeg")
                val compressImage = File(compressPath)
                if (compressImage.exists())
                    body.addPart(FilePart("files", compressImage, "image/jpeg"))
                else
                    body.addPart(FilePart("files", File(mImagePathList[i]), "image/jpeg"))
                needUploadFile.add(mImagePathList[i])
                fileNotEmpty = true
            }
            if (!fileNotEmpty) {// 只有编辑会到这个逻辑里面， 否则在前面就截住了
                val uploadUrls = arrayOfNulls<String>(mImagePathList.size)
                for (i in mImagePathList.indices) {
                    if (!mImagePathList[i].startsWith("/storage"))
                        uploadUrls[i] = mImagePathList[i]
                }
                updateNewPost()
            }
            val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_UPLOAD_FILES_URL)
            postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
            postRequest.setHttpBody<AbstractRequest<String>>(body)
            postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(this@ReleasePostActivity) {
                override fun onSuccess(s: String?, response: Response<String>?) {
                    super.onSuccess(s, response)

                    dismissLoadingDialog()
                    val response1 = UpLoadFilesResponse(s)
                    if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                        if (!isEdit) {
                            mUploadMap.clear()
                            for (i in response1.fileList.url.indices) {
                                mUploadMap[needUploadFile[i]] = response1.fileList.url[i]
                                mUploadUrls.add(response1.fileList.url[i])
                            }
                            startNewPost(url, bizType,activityUuid,type)
                        } else {

                            for (i in response1.fileList.url.indices) {
                                mUploadMap[needUploadFile[i]] = response1.fileList.url[i]
                                mUploadUrls.add(response1.fileList.url[i])
                            }
                            updateNewPost()
                        }
                    } else {

                        CommonToast.getInstance(response1.mMsg).show()
                    }

                }

                override fun onFailure(e: HttpException?, response: Response<String>?) {
                    super.onFailure(e, response)
                    dismissLoadingDialog()
                    CommonToast.getInstance(e.toString()).show()
                }
            })
            LiteHttpManager.getInstance().executeAsync(postRequest)
            Constant.imageList.clear()
        }).start()

    }

    // 编辑旧帖子
    private fun updateNewPost() {

        showLoadingDialog()
        mContent = et_content.text.toString()// 需要先上传完成之后才能搞

        val postRequest = StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_UPDATE_MY_POST_URL)
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
        postRequest.setHttpBody<AbstractRequest<String>>(UpdateNewPostApiParameter("", mContent
                , mUploadUrls, "0", "0", "", "", ""
                , FreshNewItem.FRESH_ITEM_NEWS_COLLECTION.toString() + "", "").requestBody)
        val sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "")
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader<AbstractRequest<String>>("authToken", sessionKey)
        postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(this@ReleasePostActivity) {
            override fun onSuccess(s: String?, response: Response<String>?) {
                super.onSuccess(s, response)

                dismissLoadingDialog()

                val response1 = ResponseData(s)
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show()
                    EventCenterManager.synSendEvent(EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_ZONE_NEWS_LIST))
                    if (isWorldChecked)
                        EventCenterManager.synSendEvent(EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_ALL_NEWS_LIST))
                    EventCenterManager.synSendEvent(EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_REFRESH_NEWS_ITEM))// 需要更新帖子，否则不及时
                    finish()// 关闭页面
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(e: HttpException?, response: Response<String>?) {
                super.onFailure(e, response)
                dismissLoadingDialog()
            }

        })
        LiteHttpManager.getInstance().executeAsync(postRequest)
    }

    //新建帖子
    private fun startNewPost(url: String, bizType: Int) {

        showLoadingDialog()

        mContent = et_content.text.toString()// 需要先上传完成之后才能搞

        val type = if (isVideo) {
            "1"
        } else {
            "0"
        }

        val postRequest = StringRequest(ApiContant.URL_HEAD + url)
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
        postRequest.setHttpBody<AbstractRequest<String>>(ReleasePostParameter(mContent,
                mUploadUrls, type).requestBody)

        val sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "")
        if (!Utils.isStringEmpty(sessionKey)) {
            postRequest.addHeader<AbstractRequest<String>>("authToken", sessionKey)
        }
        postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(this) {
            override fun onSuccess(s: String?, response: Response<String>?) {
                super.onSuccess(s, response)

                dismissLoadingDialog()

                val response1 = ResponseData(s)
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show()
                    EventCenterManager.synSendEvent(EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_ZONE_NEWS_LIST))
                    if (isWorldChecked)
                        EventCenterManager.synSendEvent(EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_ALL_NEWS_LIST))
                    finish()// 关闭页面
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(e: HttpException?, response: Response<String>?) {
                super.onFailure(e, response)

                dismissLoadingDialog()
            }

        })

        LiteHttpManager.getInstance().executeAsync(postRequest)
    }

    //新建帖子
    private fun startNewPost(url: String, bizType: Int,activityUuid: String,type: Int) {

        showLoadingDialog()

        mContent = et_content.text.toString()// 需要先上传完成之后才能搞

        val type1 = if (isVideo) {
            "1"
        } else {
            "0"
        }

        val postRequest = StringRequest(ApiContant.URL_HEAD + url)
        postRequest.setMethod<AbstractRequest<String>>(HttpMethods.Post)
        postRequest.setHttpBody<AbstractRequest<String>>(ReleasePostNewParameter(mContent,
                mUploadUrls, type1,activityUuid).requestBody)

        val sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "")
        if (!Utils.isStringEmpty(sessionKey)) {
            postRequest.addHeader<AbstractRequest<String>>("authToken", sessionKey)
        }
        postRequest.setHttpListener<AbstractRequest<String>>(object : MyHttpListener<String>(this) {
            override fun onSuccess(s: String?, response: Response<String>?) {
                super.onSuccess(s, response)

                dismissLoadingDialog()

                val response1 = ResponseData(s)
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show()
                    EventCenterManager.synSendEvent(EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_ZONE_NEWS_LIST))
                    if (isWorldChecked)
                        EventCenterManager.synSendEvent(EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_ALL_NEWS_LIST))
                    finish()// 关闭页面
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(e: HttpException?, response: Response<String>?) {
                super.onFailure(e, response)

                dismissLoadingDialog()
            }

        })

        LiteHttpManager.getInstance().executeAsync(postRequest)
    }

    /**
     * 自定义压缩存储地址
     *
     * @return
     */
    private fun getPath(): String {
        val path = Environment.getExternalStorageDirectory().toString() + "/Luban/image/"
        val file = File(path)
        return if (file.mkdirs()) {
            path
        } else path
    }

}
