package com.zq.ui.tab.bottom;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
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
public class TabBottom extends RelativeLayout implements ITab<TabBottomInfo> {

    private TabBottomInfo<?> tabInfo;
    private ImageView tabImageView;
    private TextView tabIconView;
    private TextView tabNameView;

    public TabBottom(Context context) {
        this(context, null);
    }

    public TabBottom(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabBottom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.tab_bottom, this);
        tabImageView = findViewById(R.id.iv_image);
        tabIconView = findViewById(R.id.tv_icon);
        tabNameView = findViewById(R.id.tv_name); 
    }

    @Override
    public void setTabInfo(@NonNull TabBottomInfo data) {
        this.tabInfo = data;
        inflateInfo();
    }

    public TabBottomInfo getHiTabInfo() {
        return tabInfo;
    }

    public ImageView getTabImageView() {
        return tabImageView;
    }

    public TextView getTabIconView() {
        return tabIconView;
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
        if (tabInfo.tabType == TabBottomInfo.TabType.ICON) {
            tabImageView.setVisibility(View.GONE);
            tabIconView.setVisibility(VISIBLE);
            //设置字体
            Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), tabInfo.iconFont);
            tabIconView.setTypeface(typeface);
            if (!TextUtils.isEmpty(tabInfo.name)) {
                tabNameView.setText(tabInfo.name);
            }
        } else if (tabInfo.tabType == TabBottomInfo.TabType.BITMAP) {
            tabImageView.setVisibility(VISIBLE);
            tabIconView.setVisibility(GONE);
            if (!TextUtils.isEmpty(tabInfo.name)) {
                tabNameView.setText(tabInfo.name);
            }
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
        if (tabInfo.tabType == TabBottomInfo.TabType.ICON) {
            if (selected) {
                tabIconView.setText(TextUtils.isEmpty(tabInfo.selectedIconName) ? tabInfo.defaultIconName : tabInfo.selectedIconName);
                tabIconView.setTextColor(getTextColor(tabInfo.tintColor));
                tabNameView.setTextColor(getTextColor(tabInfo.tintColor));
            } else {
                tabIconView.setText(tabInfo.defaultIconName);
                tabIconView.setTextColor(getTextColor(tabInfo.defaultColor));
                tabNameView.setTextColor(getTextColor(tabInfo.defaultColor));
            }

        } else if (tabInfo.tabType == TabBottomInfo.TabType.BITMAP) {
            if (selected) {
                tabImageView.setImageBitmap(tabInfo.selectedBitmap);
            } else {
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
