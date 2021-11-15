package com.zq.ui.banner.indicator;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.zq.hilibrary.util.DisplayUtil;
import com.zq.ui.R;

/**
 * @program: hi-library
 * @description:
 * @author: 闫世豪
 * @create: 2021-09-17 12:02
 **/
public class CircleIndicator extends FrameLayout implements Indicator<FrameLayout> {

    /**
     * 指示点左右内间距
     */
    private int mPointLeftRightPadding = 0;

    /**
     * 指示点上下内间距
     */
    private int mPointTopBottomPadding = 0;


    public CircleIndicator(@NonNull Context context) {
        super(context);
        mPointLeftRightPadding = DisplayUtil.dp2px(5);
        mPointTopBottomPadding = DisplayUtil.dp2px(15);
    }

    @Override
    public FrameLayout get() {
        return this;
    }

    @Override
    public void onInflate(int count) {
        removeAllViews();
        if (count <= 0) {
            return;
        }
        LinearLayout groupView = new LinearLayout(getContext());
        groupView.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        imageViewParams.gravity = Gravity.CENTER_VERTICAL;
        imageViewParams.setMargins(
                mPointLeftRightPadding,
                mPointTopBottomPadding,
                mPointLeftRightPadding,
                mPointTopBottomPadding
        );
        for (int i = 0; i < count; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(imageViewParams);
            if (i == 0) {
                imageView.setImageResource(R.drawable.shape_point_select);
            } else {
                imageView.setImageResource(R.drawable.shape_point_normal);
            }
            groupView.addView(imageView);
        }
        LayoutParams groupViewParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        groupViewParams.gravity =
                Gravity.CENTER | Gravity.BOTTOM;
        addView(groupView, groupViewParams);
    }

    @Override
    public void onPointChange(int current, int count) {
        ViewGroup viewGroup = (ViewGroup) getChildAt(0);
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            ImageView imageView = (ImageView) viewGroup.getChildAt(i);
            if (i == current) {
                imageView.setImageResource(R.drawable.shape_point_select);
            } else {
                imageView.setImageResource(R.drawable.shape_point_normal);
            }
            imageView.requestLayout();
        }
    }
}
