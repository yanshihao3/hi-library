package com.zq.ui.title

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.StringRes
import com.zq.hilibrary.util.DisplayUtil
import com.zq.hilibrary.util.HiRes
import com.zq.ui.R
import com.zq.ui.icfont.IconFontButton
import com.zq.ui.icfont.IconFontTextView

class NavigationBar @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet?,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attributeSet, defStyleAttr) {
    private var navAttrs: Attrs

    private var titleView: IconFontTextView? = null // 主标题
    private var subTittleView: IconFontTextView? = null // 副标题
    private var titleContainer: LinearLayout? = null // 标题容器

    private var mLeftLastViewID = View.NO_ID
    private var mRightLastViewId = View.NO_ID
    private val mLeftViewList = ArrayList<View>()
    private val mRightViewList = ArrayList<View>()

    init {
        navAttrs = parseNavAttrs(context, attributeSet, defStyleAttr)
        if (!TextUtils.isEmpty(navAttrs.navTitle)) {
            setTitle(navAttrs.navTitle!!)
        }

        if (!TextUtils.isEmpty(navAttrs.navSubtitle)) {
            setSubtitle(navAttrs.navSubtitle!!)
        }
        if (navAttrs.lineHeight > 0) {
            addLineView()
        }
    }

    fun setNavListener(listener: OnClickListener) {
        if (!TextUtils.isEmpty(navAttrs.navIconStr)) {
            val back =
                addLeftTextButton(navAttrs.navIconStr!!)
            back.setOnClickListener(listener)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (titleContainer != null) {
            //计算出标题栏左侧已占用空间
            var leftUserSpace = paddingLeft
            for (view in mLeftViewList) {
                leftUserSpace += view.measuredWidth
            }
            var rightUserSpace = paddingRight
            for (view in mRightViewList) {
                rightUserSpace += view.measuredWidth
            }
            val titleContainerWidth = titleContainer!!.measuredWidth
            val remainingSpace = measuredWidth - Math.max(leftUserSpace, rightUserSpace) * 2
            if (titleContainerWidth > remainingSpace) {
                val size =
                    MeasureSpec.makeMeasureSpec(remainingSpace, MeasureSpec.EXACTLY)
                titleContainer!!.measure(size, heightMeasureSpec)
            }
        }
    }

    private fun addLineView() {
        val view = View(context)
        val params = LayoutParams(LayoutParams.MATCH_PARENT, navAttrs.lineHeight)
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        view.layoutParams = params
        view.setBackgroundColor(navAttrs.lineColor)
        addView(view)
    }

    fun setCenterView(view: View) {
        var params = view.layoutParams
        if (params == null) {
            params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        } else if (params !is LayoutParams) {
            params = LayoutParams(params)
        }

        val centerViewParams = params as LayoutParams
        centerViewParams.addRule(RIGHT_OF, mLeftLastViewID)
        centerViewParams.addRule(LEFT_OF, mRightLastViewId)
        params.addRule(CENTER_VERTICAL)
        addView(view, centerViewParams)
    }

    //region 添加左边控件
    fun addLeftTextButton(@StringRes stringRes: Int): Button {
        return addLeftTextButton(HiRes.getString(stringRes))
    }

    fun addLeftTextButton(buttonText: String): Button {
        val button = generateTextButton()
        button.text = buttonText
        button.id = View.generateViewId()

        if (mLeftViewList.isEmpty()) {
            button.setPadding(navAttrs.horPadding * 2, 0, navAttrs.horPadding, 0)
        } else {
            button.setPadding(navAttrs.horPadding, 0, navAttrs.horPadding, 0)
        }
        addLeftView(button, generateTextButtonLayoutParams())
        return button
    }

    fun addLeftView(
        view: View,
        params: LayoutParams
    ) {
        val viewID = view.id
        if (viewID == View.NO_ID) {
            throw IllegalStateException("left view must be has an unique id.")
        }
        if (mLeftLastViewID == View.NO_ID) {
            params.addRule(ALIGN_PARENT_LEFT, viewID)
        } else {
            params.addRule(RIGHT_OF, mLeftLastViewID)
        }
        mLeftLastViewID = viewID
        params.alignWithParent = true
        mLeftViewList.add(view)
        addView(view, params)
    }

    //region 添加右边控件
    fun addRightTextButton(@StringRes stringRes: Int): Button {
        return addRightTextButton(HiRes.getString(stringRes))
    }

    fun addRightTextButton(buttonText: String): Button {
        val button = generateTextButton()
        button.text = buttonText
        button.id = View.generateViewId()
        if (mRightViewList.isEmpty()) {
            button.setPadding(navAttrs.horPadding, 0, navAttrs.horPadding * 2, 0)
        } else {
            button.setPadding(navAttrs.horPadding, 0, navAttrs.horPadding, 0)
        }
        addRightView(button, generateTextButtonLayoutParams())
        return button
    }

    fun addRightView(
        view: View,
        params: LayoutParams
    ) {
        val viewID = view.id
        if (viewID == View.NO_ID) {
            throw IllegalStateException("right view must be has an unique id.")
        }
        if (mRightLastViewId == View.NO_ID) {
            params.addRule(ALIGN_PARENT_RIGHT, viewID)
        } else {
            params.addRule(LEFT_OF, mRightLastViewId)
        }
        mRightLastViewId = viewID
        params.alignWithParent = true
        mRightViewList.add(view)
        addView(view, params)
    }

    //endregion
    private fun generateTextButton(): Button {
        val button = IconFontButton(context)
        button.setBackgroundResource(0)
        button.minWidth = 0
        button.minimumWidth = 0
        button.minHeight = 0
        button.minimumHeight = 0
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, navAttrs.btnTextSize)
        button.setTextColor(navAttrs.btnTextColor)
        button.gravity = Gravity.CENTER
        return button
    }

    private fun generateTextButtonLayoutParams(): LayoutParams {
        return LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
    }

    //region 设置标题
    fun setTitle(title: String) {
        ensureTitleView()
        titleView?.text = title
        titleView?.visibility = if (TextUtils.isEmpty(title)) View.GONE else View.VISIBLE
    }

    fun setSubtitle(subTitle: String) {
        ensureSubtitleView()
        updateTitleViewStyle()
        subTittleView?.text = subTitle
        subTittleView?.visibility = if (TextUtils.isEmpty(subTitle)) View.GONE else View.VISIBLE
    }

    /**
     * 生成主标题控件
     */
    private fun ensureTitleView() {
        if (titleView == null) {
            titleView = IconFontTextView(context)
            titleView?.apply {
                gravity = Gravity.CENTER
                isSingleLine = true
                ellipsize = TextUtils.TruncateAt.END
                setTextColor(navAttrs.titleTextColor)
                updateTitleViewStyle()
                ensureTitleContainer()
                titleContainer?.addView(titleView, 0)
            }
        }
    }

    /**
     * 生成副标题控件
     */
    private fun ensureSubtitleView() {
        if (subTittleView == null) {
            subTittleView = IconFontTextView(context)
            subTittleView?.apply {
                gravity = Gravity.CENTER
                isSingleLine = true
                ellipsize = TextUtils.TruncateAt.END
                setTextColor(navAttrs.subTitleTextColor)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, navAttrs.subTitleSize)
                ensureTitleContainer()
                titleContainer?.addView(subTittleView)
            }
        }
    }

    /**
     * 生成标题容器
     */
    private fun ensureTitleContainer() {
        if (titleContainer == null) {
            titleContainer = LinearLayout(context)
            titleContainer?.apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
                params.addRule(CENTER_IN_PARENT)
                this@NavigationBar.addView(titleContainer, params)
            }
        }
    }

    /**
     * 更新标题字体大小
     */
    private fun updateTitleViewStyle() {
        titleView?.run {
            if (subTittleView == null || TextUtils.isEmpty(subTittleView!!.text)) {
                typeface = Typeface.DEFAULT_BOLD
                setTextSize(TypedValue.COMPLEX_UNIT_PX, navAttrs.titleTextSize)
            } else {
                typeface = Typeface.DEFAULT
                setTextSize(TypedValue.COMPLEX_UNIT_PX, navAttrs.titleTextSizeWithSubTitle)
            }
        }
    }


    private fun parseNavAttrs(
        context: Context,
        attributeSet: AttributeSet?,
        defStyleAttr: Int
    ): Attrs {
        val array = context.obtainStyledAttributes(
            attributeSet,
            R.styleable.NavigationBar,
            defStyleAttr,
            R.style.navigationStyle
        )
        val navIcon = array.getString(R.styleable.NavigationBar_nav_icon)

        val navTitle = array.getString(R.styleable.NavigationBar_nav_title)

        val navSubTitle = array.getString(R.styleable.NavigationBar_nav_subTitle)

        val horPadding = array.getDimensionPixelSize(R.styleable.NavigationBar_hor_padding, 0)

        val btnTextSize = array.getDimensionPixelSize(
            R.styleable.NavigationBar_text_btn_text_size,
            DisplayUtil.sp2px(16f)
        )
        val btnTextColor = array.getColorStateList(R.styleable.NavigationBar_text_btn_text_color)

        val titleTextSize = array.getDimensionPixelSize(
            R.styleable.NavigationBar_title_text_size,
            DisplayUtil.sp2px(18f)
        )

        val titleWithSubTitleTextSize = array.getDimensionPixelSize(
            R.styleable.NavigationBar_title_text_size_with_subTitle,
            DisplayUtil.sp2px(16f)
        )
        val titleTextColor = array.getColor(
            R.styleable.NavigationBar_title_text_color,
            HiRes.getColor(R.color.hi_tabtop_normal_color)
        )

        val subTitleTextSize = array.getDimensionPixelSize(
            R.styleable.NavigationBar_subTitle_text_size,
            DisplayUtil.sp2px(14f)
        )
        val subTitleTextColor = array.getColor(
            R.styleable.NavigationBar_subTitle_text_color,
            HiRes.getColor(R.color.hi_tabtop_normal_color)
        )
        val lineColor =
            array.getColor(R.styleable.NavigationBar_nav_line_color, Color.parseColor("#eeeeee"))
        val lineHeight =
            array.getDimensionPixelOffset(R.styleable.NavigationBar_nav_line_height, 0)

        array.recycle()
        return Attrs(
            navIcon,
            navTitle,
            navSubTitle,
            horPadding,
            btnTextSize.toFloat(),
            btnTextColor,
            titleTextSize.toFloat(),
            titleWithSubTitleTextSize.toFloat(),
            titleTextColor,
            subTitleTextSize.toFloat(),
            subTitleTextColor,
            lineColor,
            lineHeight
        )
    }

    private data class Attrs(
        val navIconStr: String?,
        val navTitle: String?,
        val navSubtitle: String?,
        val horPadding: Int,
        val btnTextSize: Float,
        val btnTextColor: ColorStateList?,
        val titleTextSize: Float,
        val titleTextSizeWithSubTitle: Float,
        val titleTextColor: Int,
        val subTitleSize: Float,
        val subTitleTextColor: Int,
        val lineColor: Int,
        val lineHeight: Int
    )

}