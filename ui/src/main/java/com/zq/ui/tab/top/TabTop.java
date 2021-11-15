package com.zq.ui.tab.top;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zq.ui.R;
import com.zq.ui.tab.common.ITab;

import org.jetbrains.annotations.NotNull;

/**
 * @program: hi-library
 * @description:
 * @author: 闫世豪
 * @create: 2021-09-16 11:22
 **/
public class TabTop extends RelativeLayout implements ITab<TabTopInfo> {

    private TabTopInfo<?> tabInfo;
    private ImageView tabImageView;
    private View indicator;
    private TextView tabNameView;

    public TabTop(Context context) {
        this(context, null);
    }

    public TabTop(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabTop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.tab_top, this);
        tabImageView = findViewById(R.id.iv_image);
        tabNameView = findViewById(R.id.tv_name);
        indicator = findViewById(R.id.tab_top_indicator);

    }

    @Override
    public void setTabInfo(@NonNull TabTopInfo data) {
        this.tabInfo = data;
        inflateInfo();
    }

    public TabTopInfo getHiTabInfo() {
        return tabInfo;
    }

    public ImageView getTabImageView() {
        return tabImageView;
    }


    public TextView getTabNameView() {
        return tabNameView;
    }


    @Override
    public void resetHeight(int height) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = height;
        setLayoutParams(layoutParams);
        getTabNameView().setVisibility(View.GONE);
    }

    /**
     * 初始化控件
     */
    private void inflateInfo() {
        if (tabInfo.tabType == TabTopInfo.TabType.TEXT) {
            tabImageView.setVisibility(View.GONE);
            tabNameView.setVisibility(VISIBLE);
            if (!TextUtils.isEmpty(tabInfo.name)) {
                tabNameView.setText(tabInfo.name);
            }
        } else if (tabInfo.tabType == TabTopInfo.TabType.BITMAP) {
            tabImageView.setVisibility(VISIBLE);
            tabNameView.setVisibility(GONE);

        }
        select(false);
    }

    private int getTextColor(Object color) {
        if (color instanceof String) {
            return Color.parseColor((String) color);
        } else {
            return (int) color;
        }
    }

    /**
     * 选中
     *
     * @param selected 选中状态
     */
    private void select(boolean selected) {
        if (tabInfo.tabType == TabTopInfo.TabType.TEXT) {
            if (selected) {
                indicator.setVisibility(VISIBLE);
                tabNameView.setTextColor(getTextColor(tabInfo.tintColor));
            } else {
                indicator.setVisibility(GONE);
                tabNameView.setTextColor(getTextColor(tabInfo.defaultColor));
            }

        } else if (tabInfo.tabType == TabTopInfo.TabType.BITMAP) {
            if (selected) {
                indicator.setVisibility(VISIBLE);
                tabImageView.setImageBitmap(tabInfo.selectedBitmap);
            } else {
                indicator.setVisibility(GONE);
                tabImageView.setImageBitmap(tabInfo.defaultBitmap);
            }
        }
    }

    @Override
    public void onTabSelectedChange(int index, @Nullable Object prevInfo, @NotNull Object nextInfo) {
        if (prevInfo != tabInfo && nextInfo != tabInfo || prevInfo == nextInfo) {
            return;
        }
        if (prevInfo == tabInfo) {
            select(false);
        } else {
            select(true);
        }
    }
}
