package com.example.autocollaspseheader.auto;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChild2;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParent2;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.autocollaspseheader.R;

import static android.content.ContentValues.TAG;

/**
 * 触发顺序onStartNestedScroll->onStopNestedScroll->onStartNestedScroll->|onNestedPreScroll->onScrollChanged|->...->onStopNestedScroll
 * 使用注意事项:
 * 1.AutoCollapseHeaderRl布局必须要拥有一个待隐藏控件和他的父控件
 * 2.如果下方有ViewPager或者ScrollView可滑动布局, 必须对此布局设置一个与待隐藏控件高度相同的negative bottomMargin来适应. 不然会出现滑动下方有空白的现象.
 */
public class AutoCollapseHeaderRl extends RelativeLayout implements NestedScrollingParent {
    private CollapseListener mListener; // 展开和关闭监听器
    private View mCollapseView;         // 可以折叠的view
    private View mHideView;             // 折叠之后会隐藏的view
    private int mBgColor;               // 折叠view的背景颜色


    public AutoCollapseHeaderRl(Context context) {
        this(context, null);
    }

    public AutoCollapseHeaderRl(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoCollapseHeaderRl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        
        mBgColor = R.color.sky_blue;
    }

    /**
     * @param dx 你滑动x方向的待消耗事件
     * @param dy 滑动y方向的待消耗的事件 手指向下屏幕向上<0   手指向上屏幕向下>0
     */
    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed) {
        if (null == mCollapseView || null == mHideView) {
            return;
        }
        int consumedY;
        if (dy > 0 && getScrollY() <= mHideView.getMeasuredHeight()) {
            consumedY = Math.min(dy, mHideView.getMeasuredHeight() - getScrollY());
            scrollBy(0, consumedY);
            consumed[1] = consumedY;
        } else if (dy < 0 && getScrollY() > 0) {
            consumedY = (int) Math.max(dy, -getScrollY());
            scrollBy(0, consumedY);
            consumed[1] = consumedY;
        }
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int nestedScrollAxes) {
        if (null == mCollapseView || null == mHideView) {
            return false;
        }

        int collapseViewHeight = mHideView.getMeasuredHeight();
        int scrollY = getScrollY();
        return 0 <= scrollY && collapseViewHeight >= scrollY;
    }

    @Override
    public void onStopNestedScroll(@NonNull View child) {
        if (null == mCollapseView || null == mHideView) {
            super.onStopNestedScroll(child);
        }
        int collapseViewHeight = mHideView.getMeasuredHeight();
        int scrollY = getScrollY();
        if (scrollY >= 0 && collapseViewHeight >= scrollY) {
            if (scrollY >= collapseViewHeight / 2) {
                // 缩回去
                scrollBy(0, collapseViewHeight - scrollY);
            } else {
                // 展开
                scrollBy(0, -scrollY);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (null == mCollapseView || null == mHideView) {
            return;
        }
        // 如果滑动前后为y位置不一样的话
        if (t != oldt) {
            if (getScrollY() >= mHideView.getMeasuredHeight()) {
                mHideView.setVisibility(INVISIBLE);
                mHideView.setEnabled(false);
                mCollapseView.setBackground(null);
                if (mListener != null) {
                    mListener.onCollapse();
                }
            } else if (getScrollY() <= 0) {
                mHideView.setVisibility(VISIBLE);
                mHideView.setEnabled(true);
                mCollapseView.setBackgroundResource(mBgColor);
                if (mListener != null) {
                    mListener.onExpand();
                }
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mCollapseView = (View) findViewById(R.id.collapse_view);
        mHideView = (View) findViewById(R.id.will_hide_view);
        mCollapseView.setBackgroundResource(mBgColor);
    }

    public interface CollapseListener {
        void onCollapse();

        void onExpand();
    }
}
