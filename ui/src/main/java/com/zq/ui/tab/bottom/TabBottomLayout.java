package com.zq.ui.tab.bottom;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.zq.hilibrary.util.DisplayUtil;
import com.zq.hilibrary.util.ViewUtil;
import com.zq.ui.tab.common.ITabLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TabBottomLayout extends FrameLayout implements ITabLayout<TabBottom, TabBottomInfo<?>> {
    private List<ITabLayout.OnTabSelectedListener<TabBottomInfo<?>>> tabSelectedChangeListeners = new ArrayList<>();
    private TabBottomInfo<?> selectedInfo;
    private float bottomAlpha = 1f;
    //TabBottom高度
    private static float tabBottomHeight = 50;
    //TabBottom的头部线条高度
    private float bottomLineHeight = 0.5f;
    //TabBottom的头部线条颜色
    private String bottomLineColor = "#dfe0e1";
    private List<TabBottomInfo<?>> infoList;

    public TabBottomLayout(@NonNull Context context) {
        this(context, null);
    }

    public TabBottomLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabBottomLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void inflateInfo(@NonNull List<TabBottomInfo<?>> infoList) {
        if (infoList.isEmpty()) {
            return;
        }
        this.infoList = infoList;
        // 移除之前已经添加的View
        for (int i = getChildCount() - 1; i > 0; i--) {
            removeViewAt(i);
        }
        selectedInfo = null;
        addBackground();
        //清除之前添加的TabBottom listener，Tips：Java foreach remove问题
        Iterator<ITabLayout.OnTabSelectedListener<TabBottomInfo<?>>> iterator = tabSelectedChangeListeners.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() instanceof TabBottomInfo) {
                iterator.remove();
            }
        }
        int height = DisplayUtil.dp2px(tabBottomHeight, getResources());
        FrameLayout ll = new FrameLayout(getContext());
        int width = DisplayUtil.getDisplayWidthInPx(getContext()) / infoList.size();
        ll.setTag(TAG_TAB_BOTTOM);
        for (int i = 0; i < infoList.size(); i++) {
            final TabBottomInfo<?> info = infoList.get(i);
            //Tips：为何不用LinearLayout：当动态改变child大小后Gravity.BOTTOM会失效
            LayoutParams params = new LayoutParams(width, height);
            params.gravity = Gravity.BOTTOM;
            params.leftMargin = i * width;

            TabBottom tabBottom = new TabBottom(getContext());
            tabSelectedChangeListeners.add(tabBottom);
            tabBottom.setTabInfo(info);
            ll.addView(tabBottom, params);
            tabBottom.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSelected(info);
                }
            });
        }
        LayoutParams flPrams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        flPrams.gravity = Gravity.BOTTOM;
        addBottomLine();
        addView(ll, flPrams);

        fixContentView();
    }

    @Override
    public void addTabSelectedChangeListener(ITabLayout.OnTabSelectedListener<TabBottomInfo<?>> listener) {
        tabSelectedChangeListeners.add(listener);
    }

    public void setTabAlpha(float alpha) {
        this.bottomAlpha = alpha;
    }

    public void setTabHeight(float tabHeight) {
        this.tabBottomHeight = tabHeight;
    }

    public void setBottomLineHeight(float bottomLineHeight) {
        this.bottomLineHeight = bottomLineHeight;
    }

    public void setBottomLineColor(String bottomLineColor) {
        this.bottomLineColor = bottomLineColor;
    }

    @Nullable
    @Override
    public TabBottom findTab(@NonNull TabBottomInfo<?> info) {
        ViewGroup ll = findViewWithTag(TAG_TAB_BOTTOM);
        for (int i = 0; i < ll.getChildCount(); i++) {
            View child = ll.getChildAt(i);
            if (child instanceof TabBottom) {
                TabBottom tab = (TabBottom) child;
                if (tab.getHiTabInfo() == info) {
                    return tab;
                }
            }
        }
        return null;
    }

    @Override
    public void defaultSelected(@NonNull TabBottomInfo<?> defaultInfo) {
        onSelected(defaultInfo);
    }

    private void onSelected(@NonNull TabBottomInfo<?> nextInfo) {
        for (ITabLayout.OnTabSelectedListener<TabBottomInfo<?>> listener : tabSelectedChangeListeners) {
            listener.onTabSelectedChange(infoList.indexOf(nextInfo), selectedInfo, nextInfo);
        }
        this.selectedInfo = nextInfo;
    }


    private void addBottomLine() {
        View bottomLine = new View(getContext());
        bottomLine.setBackgroundColor(Color.parseColor(bottomLineColor));

        LayoutParams bottomLineParams =
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dp2px(bottomLineHeight, getResources()));
        bottomLineParams.gravity = Gravity.BOTTOM;
        bottomLineParams.bottomMargin = DisplayUtil.dp2px(tabBottomHeight - bottomLineHeight, getResources());
        addView(bottomLine, bottomLineParams);
        bottomLine.setAlpha(bottomAlpha);
    }

    private void addBackground() {
        View view = new View(getContext());
        view.setBackgroundColor(Color.WHITE);
        LayoutParams params =
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dp2px(tabBottomHeight, getResources()));
        params.gravity = Gravity.BOTTOM;
        addView(view, params);
        view.setAlpha(bottomAlpha);
    }

    private static final String TAG_TAB_BOTTOM = "TAG_TAB_BOTTOM";

    /**
     * 修复内容区域的底部Padding
     */
    private void fixContentView() {
        if (!(getChildAt(0) instanceof ViewGroup)) {
            return;
        }
        ViewGroup rootView = (ViewGroup) getChildAt(0);
        ViewGroup targetView = ViewUtil.findTypeView(rootView, RecyclerView.class);
        if (targetView == null) {
            targetView = ViewUtil.findTypeView(rootView, ScrollView.class);
        }
        if (targetView == null) {
            targetView = ViewUtil.findTypeView(rootView, NestedScrollView.class);
        }
        if (targetView == null) {
            targetView = ViewUtil.findTypeView(rootView, AbsListView.class);
        }
        if (targetView != null) {
            targetView.setPadding(0, 0, 0, DisplayUtil.dp2px(tabBottomHeight, getResources()));
            targetView.setClipToPadding(false);
        }
    }

    public static void clipBottomPadding(ViewGroup targetView) {
        if (targetView != null) {
            targetView.setPadding(0, 0, 0, DisplayUtil.dp2px(tabBottomHeight));
            targetView.setClipToPadding(false);
        }
    }
}