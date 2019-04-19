package com.shanshui.musicapp.mvp.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author mashanshui
 * @date 2018-11-03
 * @desc TODO
 */
public class MyTableLayout extends HorizontalScrollView {
    private static final String TAG = "MyTableLayout";
    private LinearLayout tabsContainer;
    private LinearLayout.LayoutParams defaultTabLayoutParams;
    private LinearLayout.LayoutParams expandedTabLayoutParams;
    private final PageListener pageListener = new PageListener();
    private ViewPager viewPager;
    private int tabPadding = 24;
    private boolean shouldExpand = true;
    private int tabTextSize = 16;
    private int tabTextColor = Color.argb(150, 255, 255, 255);
    private int tabSelectTextSize = 20;
    private int tabSelectTextColor = Color.argb(255, 255, 255, 255);
    private int currentPosition = 0;
    private float currentPositionOffset = 0f;
    private int tabCount;

    public MyTableLayout(Context context) {
        this(context, null);
    }

    public MyTableLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTableLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFillViewport(true);
        setWillNotDraw(false);
        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(tabsContainer);
        defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        expandedTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
    }

    public void addTab(String[] title) {
        if (title == null) {
            return;
        }
        for (int i = 0; i < title.length; i++) {
            String s = title[i];
            View tab = createTextView(s);
            tab.setFocusable(true);
            int finalI = i;
            tab.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(finalI);
                }
            });
            tab.setPadding(tabPadding, 0, tabPadding, 0);
            tabsContainer.addView(tab, i, shouldExpand ? expandedTabLayoutParams : defaultTabLayoutParams);
        }
    }

    private View createTextView(String s) {
        TextView textView = new TextView(getContext());
        textView.setText(s);
        textView.setGravity(Gravity.CENTER);
        textView.setSingleLine();
        textView.setTextColor(tabTextColor);
        textView.setTextSize(tabTextSize);
        return textView;
    }

    public void setUpWithViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        viewPager.addOnPageChangeListener(pageListener);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
//                resumeTabStatus(0);
            }
        });
    }

    /**
     * 重置tab的状态
     *
     * @param position 默认选中的项目
     */
    private void resumeTabStatus(int position) {
        tabCount = viewPager.getAdapter().getCount();
        for (int i = 0; i < tabCount; i++) {
            TextView tab = (TextView) tabsContainer.getChildAt(i);
            tab.setTextColor(tabTextColor);
            tab.setTextSize(tabTextSize);
        }
        //默认选中第一项
        TextView tab = (TextView) tabsContainer.getChildAt(position);
        tab.setTextSize(tabSelectTextSize);
        tab.setTextColor(tabSelectTextColor);
    }


    private class PageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            Log.e(TAG, "onPageScrolled: " + position + "positionOffset=" + positionOffset);
            scrollToChild(position, positionOffset);
            currentPositionOffset = positionOffset;
//            requestLayout();
            invalidate();
        }

        @Override
        public void onPageSelected(int position) {
//            Log.e(TAG, "onPageSelected: " + position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
//            Log.e(TAG, "onPageScrollStateChanged: " + state);
        }
    }

    private void scrollToChild(int position, float offset) {
        if (offset < 0) {
            return;
        }
        if (offset == 0) {
            currentPosition = position;
            resumeTabStatus(currentPosition);
            return;
        }
//        TextView currentTab = (TextView) tabsContainer.getChildAt(position);
//
//        if (offset > currentPositionOffset) {
//            TextView futureTab = (TextView) tabsContainer.getChildAt(position + 1);
//            futureTab.setTextColor(MyTableLayout.argb(offset, 1f, 1f, 1f));
//            futureTab.getPaint().setTextSize(14f + 4f * offset);
//            currentTab.setTextColor(MyTableLayout.argb(1f - offset, 1f, 1f, 1f));
//            currentTab.getPaint().setTextSize(14f + 4f * (1f - offset));
//        } else if (offset < currentPositionOffset) {
//            TextView futureTab = (TextView) tabsContainer.getChildAt(position - 1);
//            futureTab.setTextColor(MyTableLayout.argb(offset, 1f, 1f, 1f));
//            futureTab.getPaint().setTextSize(14f + 4f * offset);
//            currentTab.setTextColor(MyTableLayout.argb(1f - offset, 1f, 1f, 1f));
//            currentTab.getPaint().setTextSize(14f + 4f * (1f - offset));
//        }
    }

    public static int argb(float alpha, float red, float green, float blue) {
        return ((int) (alpha * 255.0f + 0.5f) << 24) |
                ((int) (red * 255.0f + 0.5f) << 16) |
                ((int) (green * 255.0f + 0.5f) << 8) |
                (int) (blue * 255.0f + 0.5f);
    }

}
