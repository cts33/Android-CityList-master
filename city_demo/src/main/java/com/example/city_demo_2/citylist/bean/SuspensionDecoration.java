package com.example.city_demo_2.citylist.bean;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 分类、悬停的Decoration
 */

public class SuspensionDecoration extends RecyclerView.ItemDecoration {
//    private List<? extends ISuspensionInterface> mDatas;

    private HashMap<String, List<CityBean>> hashMap = new LinkedHashMap<>();
    private Paint mPaint;
    private Rect mBounds;//用于存放测量文字Rect

    private LayoutInflater mInflater;

    private int mTitleHeight;//title的高
    private static int COLOR_TITLE_BG = Color.parseColor("#FFDFDFDF");
    private static int COLOR_TITLE_FONT = Color.parseColor("#FF999999");
    private static int mTitleFontSize;//title字体大小

    private int defaultPaddingLeft = 15;
    private int mHeaderViewCount = 0;


    public SuspensionDecoration(Context context, HashMap<String, List<CityBean>> hashMap) {
        super();
        this.hashMap = hashMap;
        mPaint = new Paint();
        mBounds = new Rect();
        mTitleHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, context.getResources().getDisplayMetrics());
        mTitleFontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, context.getResources().getDisplayMetrics());
        mPaint.setTextSize(mTitleFontSize);
        mPaint.setAntiAlias(true);
        mInflater = LayoutInflater.from(context);
    }


    public SuspensionDecoration setmTitleHeight(int mTitleHeight) {
        this.mTitleHeight = mTitleHeight;
        return this;
    }


    public SuspensionDecoration setColorTitleBg(int colorTitleBg) {
        COLOR_TITLE_BG = colorTitleBg;
        return this;
    }

    public SuspensionDecoration setColorTitleFont(int colorTitleFont) {
        COLOR_TITLE_FONT = colorTitleFont;
        return this;
    }

    public SuspensionDecoration setTitleFontSize(int mTitleFontSize) {
        mPaint.setTextSize(mTitleFontSize);
        return this;
    }

    public SuspensionDecoration setmDatas(HashMap<String, List<CityBean>> hashMap) {
        this.hashMap = hashMap;
        return this;
    }

    public int getHeaderViewCount() {
        return mHeaderViewCount;
    }

    public SuspensionDecoration setHeaderViewCount(int headerViewCount) {
        mHeaderViewCount = headerViewCount;
        return this;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child .getLayoutParams();
            int position = params.getViewLayoutPosition();
            position -= getHeaderViewCount();
            //pos为1，size为1，1>0? true
            if (hashMap == null || hashMap.isEmpty() || position > hashMap.size() - 1 || position < 0 /**|| !hashMap.get(position).isShowSuspension()**/) {
                continue;//越界
            }
            if (position >=0) {
                if (position == 0) {
                    drawTitleArea(c, left, right, child, params, position);

                } else {

                    drawTitleArea(c, left, right, child, params, position);

                }
            }
        }
    }

    /**
     * 绘制Title区域背景和文字的方法
     *
     * @param c
     * @param left
     * @param right
     * @param child
     * @param params
     * @param position
     */
    private void drawTitleArea(Canvas c, int left, int right, View child, RecyclerView.LayoutParams params, int position) {//最先调用，绘制在最下层
        mPaint.setColor(COLOR_TITLE_BG);
        c.drawRect(left, child.getTop() - params.topMargin - mTitleHeight, right, child.getTop() - params.topMargin, mPaint);
        mPaint.setColor(COLOR_TITLE_FONT);

        mPaint.getTextBounds(getSuspensionFirstWord(position), 0, getSuspensionFirstWord(position).length(), mBounds);
        c.drawText(getSuspensionFirstWord(position), child.getPaddingLeft() + defaultPaddingLeft, child.getTop() - params.topMargin - (mTitleHeight / 2 - mBounds.height() / 2), mPaint);
    }


    /**
     * 根据下标 从hashmap取出 每个条目的首字母
     *
     * @param pos
     * @return
     */
    public String getSuspensionFirstWord(int pos) {
        int index = 0;

        Iterator<Map.Entry<String, List<CityBean>>> iterator = hashMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, List<CityBean>> next = iterator.next();
            if (index == pos) {
                String key = next.getKey();
                key = key.equals("0") ?"热门":key;
                return key != null ? key : "";
            }
            index++;
        }
        return "";
    }

    @Override
    public void onDrawOver(Canvas c, final RecyclerView parent, RecyclerView.State state) {//最后调用 绘制在最上层

        int pos = ((LinearLayoutManager) (parent.getLayoutManager())).findFirstVisibleItemPosition();
        pos -= getHeaderViewCount();

        if (hashMap == null || hashMap.isEmpty() || pos > hashMap.size() - 1 || pos < 0) {
            return;//越界
        }

        String tag = getSuspensionFirstWord(pos);

        View child = parent.findViewHolderForLayoutPosition(pos + getHeaderViewCount()).itemView;

        boolean flag = false;//定义一个flag，Canvas是否位移过的标志

        if (null != tag && !tag.equals(getSuspensionFirstWord(pos + 1))) {//当前第一个可见的Item的tag，不等于其后一个item的tag，说明悬浮的View要切换了

            if (child.getHeight() + child.getTop() < mTitleHeight) {
                c.save();
                flag = true;

                c.translate(0, child.getHeight() + child.getTop() - mTitleHeight);
            }
        }

        mPaint.setColor(COLOR_TITLE_BG);
        c.drawRect(parent.getPaddingLeft(), parent.getPaddingTop(), parent.getRight() - parent.getPaddingRight(), parent.getPaddingTop() + mTitleHeight, mPaint);

        mPaint.setColor(COLOR_TITLE_FONT);
        mPaint.getTextBounds(tag, 0, tag.length(), mBounds);
        c.drawText(tag, child.getPaddingLeft() + defaultPaddingLeft, parent.getPaddingTop() + mTitleHeight - (mTitleHeight / 2 - mBounds.height() / 2), mPaint);
        if (flag)
            c.restore();


    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        super.getItemOffsets(outRect, view, parent, state);
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        position -= getHeaderViewCount();

        if (hashMap == null || hashMap.isEmpty() || position > hashMap.size() - 1) {//pos为1，size为1，1>0? true
            return;//越界
        }

        if (position >= 0) {

            String suspensionFirstWord = getSuspensionFirstWord(position);

            if (position == 0) {
                outRect.set(0, mTitleHeight, 0, 0);
            } else if (!suspensionFirstWord.equals(getSuspensionFirstWord(position - 1))) {
                outRect.set(0, mTitleHeight, 0, 0);
            }
        }
    }

}
