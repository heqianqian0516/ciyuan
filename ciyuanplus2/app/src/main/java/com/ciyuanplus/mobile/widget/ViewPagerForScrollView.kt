package com.ciyuanplus.mobile.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import java.util.*

/**
 * Created by Alen on 2018/1/26.
 *
 *
 * 这里需要自适应高度，所以会导致不同的pager都是一个高度， 需要在onPageSelected方法中使用resetHeight 来进行处理
 */

class ViewPagerForScrollView : ViewPager {
    private var current: Int = 0
    private var totalHeight  = 0

    /**
     * 保存position与对于的View
     */
    private val mChildrenViews = LinkedHashMap<Int, View>()
    var isScrollable = true


    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec

        if (mChildrenViews.size > current) {
            val child = mChildrenViews[current]
            if (child != null) {
                child.measure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                totalHeight = child.measuredHeight
            }
        }
        heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(totalHeight, View.MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    fun resetHeight(current: Int) {

        this.current = current
        if (mChildrenViews.size > current) {

            var layoutParams: ViewGroup.LayoutParams? = layoutParams
            if (layoutParams == null) {
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, totalHeight)
            } else {
                layoutParams.height = totalHeight
            }
            setLayoutParams(layoutParams)
        }
    }


    /**
     * 保存position与对于的View
     */
    fun setObjectForPosition(view: View, position: Int) {
        mChildrenViews[position] = view
    }


    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return if (!isScrollable) {
            true
        } else super.onTouchEvent(ev)
    }
}