package com.ciyuanplus.mobile.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.ciyuanplus.mobile.R
import kotlinx.android.synthetic.main.title_bar.view.*


/**
 *    author : Kris
 *    e-mail : chengkai_1215@126.com
 *    date   : 2019/1/14 4:58 PM
 *    class  : TitleBarView
 *    desc   : 导航栏
 *    version: 1.0
 */
class TitleBarView : RelativeLayout {

    private lateinit var mContext: Context
    private val mTitle: TextView by lazy { tv_title_bar_title }
    private val mSubTitle: TextView by lazy { tv_title_bar_sub_title }
    private val mRightCheckableTitle: TextView by lazy { ctv_title_bar_right_txt }
    private val mTitleBarBackground: RelativeLayout by lazy { rl_title_bar_bg }
    private val mBackImage: ImageView by lazy { iv_title_bar_back }
    private val mRightImage: ImageView by lazy { iv_title_bar_right_image }
    private val mRightImageTitle: TextView by lazy { tv_title_bar_right_img_title }
    private val mLeftTitle: TextView by lazy { tv_title_bar_left_text }
    private val mLeftImage: ImageView by lazy { iv_title_bar_left_image }


    //消息
    private val mLayoutNotification: RelativeLayout by lazy { rl_notification }
    //通知
    private val mNotificationNumber: TextView by lazy { tv_notification_num }
    private val mNotificationImage: ImageView by lazy { iv_notification }
    private val mCenterLogoImage: ImageView by lazy { iv_title_bar_logo }


    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context)
    }

    private fun init(context: Context) {
        mContext = context
        LayoutInflater.from(context).inflate(R.layout.title_bar, this, true)

       /* StatusBarUtil.setPaddingSmart(mContext,rl_title_bar_bg)*/
    }


    fun setTitleVisible(visibility: Boolean) {

        if (visibility) {
            mTitle.visibility = View.VISIBLE
        } else {
            mTitle.visibility = View.GONE
        }

    }

    /**
     * 设置主标题
     * @param title 主题内容
     * */
    fun setTitle(title: String) {

        if (title.isNotEmpty()) {
            mTitle.visibility = View.VISIBLE
        }
        mTitle.text = title
    }

    /**
     * 设置主标题
     * @param resId 主题内容id
     * */
    fun setTitle(resId: Int) {
        mTitle.visibility = View.VISIBLE
        mTitle.setText(resId)
    }

    /**
     * 设置主标题颜色
     * @param colorId color resource id
     * */
    fun setTitleColor(colorId: Int) {
        mTitle.setTextColor(mContext.resources.getColor(colorId))
    }

    /**
     * 设置文字+logo标题
     * @param title 标题内容
     * @param logoId 左侧logo资源id
     * */
    fun setLogoTitle(title: String, logoId: Int) {
        mTitle.visibility = View.VISIBLE
        mTitle.text = title
        val drawable = mContext.resources.getDrawable(logoId)
        drawable!!.setBounds(0, 0, drawable.minimumWidth,
                drawable.minimumHeight)
        mTitle.setCompoundDrawables(drawable, null, null, null)
    }

    /**
     * 设置子标题
     * @param resId 标题内容id
     *
     * */
    fun setSubTitle(resId: Int) {
        mSubTitle.visibility = View.VISIBLE
        mSubTitle.setText(resId)
    }

    /**
     * 设置子标题
     *  @param subTitle 标题内容
     * */
    fun setSubTitle(subTitle: String) {
        mSubTitle.visibility = View.VISIBLE
        mSubTitle.text = subTitle
    }

    /**
     * 设置左侧按钮图片和点击事件
     * */
    fun setLeftImageBar(drawable: Drawable, text: String, listener: View.OnClickListener) {
        mLeftTitle.visibility = View.VISIBLE
        mLeftTitle.text = text
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        mLeftTitle.setCompoundDrawables(null, null, drawable, null)
        mLeftTitle.setOnClickListener(listener)
    }

    fun setLefButtonImage(resId: Int) {
        mLeftImage.setImageResource(resId)
    }

    fun setLeftImageButton(resId: Int, listener: View.OnClickListener?) {
        mLeftImage.visibility = visibility
        mLeftImage.setImageResource(resId)
        if (listener != null) mLeftImage.setOnClickListener(listener)
    }

    fun setLeftImageListener(listener: View.OnClickListener?) {
        mLeftImage.visibility = visibility
        if (listener != null) mLeftImage.setOnClickListener(listener)
    }


    /**
     * 设置右侧文字和点击事件
     * */
    fun registerRightTitle(title: String, listener: View.OnClickListener?) {
        if (listener == null) {
            mRightCheckableTitle.visibility = View.INVISIBLE
        } else {
            mRightCheckableTitle.text = title
            mRightCheckableTitle.visibility = View.VISIBLE
            mRightCheckableTitle.setOnClickListener(listener)
        }
    }

    /**
     * 设置右侧文字和点击事件
     * */
    fun registerRightTitle(title: String, colorId: Int, listener: View.OnClickListener?) {
        if (listener == null) {
            mRightCheckableTitle.visibility = View.INVISIBLE
        } else {
            mRightCheckableTitle.text = title
            mRightCheckableTitle.setTextColor(mContext.resources.getColor(colorId))
            mRightCheckableTitle.visibility = View.VISIBLE
            mRightCheckableTitle.setOnClickListener(listener)
        }
    }


    fun setRightImage(resId: Int) {
        mRightImage.setImageResource(resId)
    }

    /**
     * 设置右按钮图片和点击事件
     * @param resId 图片id
     * @param listener 监听事件
     * */
    fun registerRightImage(resId: Int, listener: View.OnClickListener?) {
        if (listener == null) {
            mRightImage.visibility = View.INVISIBLE
        } else {
            val drawable = ContextCompat.getDrawable(context, resId)
            drawable!!.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
            mRightImage.setImageResource(resId)
            mRightImage.visibility = View.VISIBLE
            mRightImage.setOnClickListener(listener)
        }
    }

    /**
     * 设置右侧文字和点击事件
     * @param title 文字id
     * @param listener 监听事件
     * */
    fun setRightPoint(title: Int, listener: View.OnClickListener?) {
        if (listener == null) {
            mRightCheckableTitle.visibility = View.INVISIBLE
        } else {
            mRightCheckableTitle.setText(title)
            mRightCheckableTitle.visibility = View.VISIBLE
            mRightCheckableTitle.setOnClickListener(listener)
        }
    }

    /**
     * 设置右侧文字和点击事件
     * @param title 文字id
     * @param listener 监听事件
     * */
    fun registerRightTitle(title: Int, listener: View.OnClickListener?) {
        if (listener == null) {
            mRightCheckableTitle.visibility = View.INVISIBLE
        } else {
            mRightCheckableTitle.setText(title)
            mRightCheckableTitle.visibility = View.VISIBLE
            mRightCheckableTitle.setOnClickListener(listener)
        }
    }

    /**
     * 设置右侧文字图片和点击事件（图片位于文字上方）
     * @param title 文字id
     * @param resId 图片id
     * @param listener 监听事件
     * */

    fun registerRightImageTitle(title: Int, resId: Int, listener: View.OnClickListener?) {
        if (listener == null) {
            mRightImageTitle.visibility = View.INVISIBLE
        } else {
            mRightImageTitle.setText(title)
            val drawable = ContextCompat.getDrawable(context, resId)
            drawable!!.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
            mRightImageTitle.setCompoundDrawables(null, drawable, null, null)
            mRightImageTitle.visibility = View.VISIBLE
            mRightImageTitle.setOnClickListener(listener)
        }
    }

    /**
     * 设置右侧文字图片和点击事件（图片位于文字上方）
     * @param title 文字
     * @param resId 图片id
     * @param listener 监听事件
     * */
    fun registerRightImageTitle(title: String, resId: Int, listener: View.OnClickListener?) {
        if (listener == null) {
            mRightImageTitle.visibility = View.INVISIBLE
        } else {
            mRightImageTitle.text = title
            val drawable = ContextCompat.getDrawable(context, resId)
            drawable!!.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
            mRightImageTitle.setCompoundDrawables(null, drawable, null, null)
            mRightImageTitle.visibility = View.VISIBLE
            mRightImageTitle.setOnClickListener(listener)
        }
    }

    /**
     * 设置后退箭头点击事件
     * */
    fun setOnBackListener(listener: View.OnClickListener) {
        mBackImage.visibility = View.VISIBLE
        mBackImage.setOnClickListener(listener)
    }

    /**
     * 设置后退图片和点击事件
     * @param resId 图片id
     * */
    fun registerBack(resId: Int, listener: View.OnClickListener) {
        mBackImage.setImageResource(resId)
        mBackImage.visibility = View.VISIBLE
        mBackImage.setOnClickListener(listener)
    }


    /**
     * 设置背景颜色
     * */
    fun setTitleBarBackgroundColor(color: Int) {
        mTitleBarBackground.setBackgroundColor(mContext.resources.getColor(color))

    }

    /**
     * 设置通知图片、消息数量和监听事件
     * @param resId 消息图片
     * @param count 消息数量
     * */
    fun setNotification(resId: Int, count: Int, listener: View.OnClickListener) {
        mLayoutNotification.visibility = View.VISIBLE
        mNotificationImage.setImageResource(resId)
        if (count > 0) {
            mNotificationNumber.visibility = View.VISIBLE
            mNotificationNumber.text = count.toString()
        } else {
            mNotificationNumber.visibility = View.GONE
        }
        mLayoutNotification.setOnClickListener(listener)
    }

    fun setRightImageVisible(visible: Int) {

        mNotificationImage.visibility = visible
    }

    /**
     * 设置消息数量
     * @param count 消息数量
     * */
    fun setNotificationNumber(count: Int) {

        if (count > 0) {
            mNotificationNumber.visibility = View.VISIBLE
            mNotificationNumber.text = count.toString()
        } else {
            mNotificationNumber.visibility = View.GONE
        }
    }

    fun setNotificationVisiable(visibility: Int) {

        mLayoutNotification.visibility = visibility
    }

    fun showLogo(resId: Int) {
        mCenterLogoImage.visibility = View.VISIBLE
        mCenterLogoImage.setImageResource(resId)
    }

    fun setNotificationClickListener(listener: OnClickListener) {

        mLayoutNotification.setOnClickListener(listener)
    }

}