package com.zq.ui.banner.indicator;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.zq.hilibrary.util.DisplayUtil;

/**
 * @program: ASProj
 * @description:
 * @author: 闫世豪
 * @create: 2021-09-17 14:27
 **/
public class NumberIndicator extends FrameLayout implements Indicator<FrameLayout> {

    private static final int VWC = ViewGroup.LayoutParams.WRAP_CONTENT;
    /**
     * 指示点左右内间距
     */
    private int mPointLeftRightPadding;

    /**
     * 指示点上下内间距
     */
    private int mPointTopBottomPadding;


    public NumberIndicator(@NonNull Context context) {
        super(context);
        mPointLeftRightPadding = DisplayUtil.dp2px(10, getContext().getResources());
        mPointTopBottomPadding = DisplayUtil.dp2px(10, getContext().getResources());
    }

    @Override
    public FrameLayout get() {
        return this;
    }

    @Override
    public void onInflate(int count) {
        removeAllViews();
        if (count < 0) {
            return;
        }
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setPadding(0, 0, mPointLeftRightPadding, mPointTopBottomPadding);
        TextView indexTv = new TextView(getContext());
        indexTv.setText("1");
        indexTv.setTextColor(Color.WHITE);
        linearLayout.addView(indexTv);

        TextView symbolTv = new TextView(getContext());
        symbolTv.setText(" / ");
        symbolTv.setTextColor(Color.WHITE);
        linearLayout.addView(symbolTv);

        TextView countTv = new TextView(getContext());
        countTv.setText(String.valueOf(count));
        countTv.setTextColor(Color.WHITE);
        linearLayout.addView(countTv);

        LayoutParams groupViewParams = new LayoutParams(VWC, VWC);
        groupViewParams.gravity = Gravity.END | Gravity.BOTTOM;
        addView(linearLayout, groupViewParams);
    }

    @Override
    public void onPointChange(int current, int count) {
        ViewGroup viewGroup = (ViewGroup) getChildAt(0);
        TextView indexTv = (TextView) viewGroup.getChildAt(0);
        TextView countTv = (TextView) viewGroup.getChildAt(2);
        indexTv.setText(String.valueOf(current + 1));
        countTv.setText(String.valueOf(count));
    }
}
