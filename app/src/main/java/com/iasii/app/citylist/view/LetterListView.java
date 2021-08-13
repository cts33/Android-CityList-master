package com.iasii.app.citylist.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import com.iasii.app.citylist.R;
import com.iasii.app.citylist.utils.DensityUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * Created by next on 2016/3/24.
 */
public class LetterListView extends View {

    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    private String[] defaultLets = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private List<String> letterList = new ArrayList<>();
    int choose = 0;
    private Paint paint = new Paint();

    private Context context;
    private int textSize = 15;
    private int width, height;

    private int textDefaultColor = Color.parseColor("#333333");
    private int textFocusColor = Color.RED;

    int paddingTop, paddingDown;

    //每个字母占位的高度值 px
    private int singleHeight;
    //默认选中的第一个元素的y坐标
    private int selectY = 0;

    public LetterListView(Context context) {
        this(context, null);
        this.context = context;
    }

    public LetterListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;

    }

    public LetterListView(Context context,
                          AttributeSet attrs,
                          int defStyle) {

        super(context, attrs, defStyle);

        this.context = context;


        initConfig();
    }


    private void initConfig() {
        width = DensityUtil.dp2px(context, 50);
//        paddingTop = paddingDown = DensityUtil.dp2px(context, 10);

        letterList.addAll(Arrays.asList(defaultLets));

        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                    width = ((View) getParent()).getWidth();
                    height = ((View) getParent()).getHeight();

                    Log.d(TAG, "viewTreeObserver: " + height);
                    singleHeight = height / letterList.size();
                    //默认选中第一个
                    selectY = singleHeight;
                    if (singleHeight > 0)
                        invalidate();
                }
            });
        }

    }

    public void setLetters(List<String> letters) {

        if (letters.isEmpty()) {
            return;
        }
        letterList.addAll(letters);

        invalidate();
    }

    /**
     * 设置字母字体大小
     *
     * @param textSize
     */
    public void setTextSize(int textSize, int textDefaultColor) {
        this.textSize = textSize;
        this.textDefaultColor = textDefaultColor;
        invalidate();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int size = MeasureSpec.getSize(heightMeasureSpec);

        Log.d(TAG, "onMeasure: "+size);

        setMeasuredDimension(width, getMeasuredHeight());
    }

    private static final String TAG = "LetterListView";

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.d(TAG, "onDraw: ");

        drawLetterList(canvas);

    }

    HashMap<String, Integer> wordXY = new HashMap();

    public void updateSelectIndex(String word) {

        selectY = wordXY.get(word);
        invalidate();

    }

    public void drawLetterList(Canvas canvas) {
        int top = 0;
        String word;
        int letSize = letterList.size();
        if (singleHeight == 0) {
            return;
        }

        for (int i = 0; i < letSize; i++) {

            word = letterList.get(i);
            int bottom = top + singleHeight;
            RectF rect = new RectF(0, top, width, bottom);//画一个矩形

            wordXY.put(word, bottom);


            Paint mPaint = new Paint();


            canvas.drawRect(rect, mPaint);

            //设置画笔的样式，空心STROKE
//            mPaint.setStyle(Paint.Style.FILL);
            //设置抗锯齿
            mPaint.setAntiAlias(true);
            if (selectY == bottom) {
                mPaint.setColor(Color.RED);
            }
            top += singleHeight;
            canvas.drawCircle(rect.centerX(), rect.centerY(), singleHeight / 2, mPaint);

            mPaint.setColor(Color.WHITE);
            mPaint.setTextSize(22);
            mPaint.setStyle(Paint.Style.FILL);
            //该方法即为设置基线上那个点究竟是left,center,还是right  这里我设置为center
            mPaint.setTextAlign(Paint.Align.CENTER);

            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            float t = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
            float b = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom

            int baseLineY = (int) (rect.centerY() - t / 2 - b / 2);//基线中间点的y轴计算公式

            canvas.drawText(word, rect.centerX(), baseLineY, mPaint);
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        //计算事件在哪个字母的下标index
        final int currenIndex = (int) (y / getHeight() * letterList.size());

        Log.d(TAG, "dispatchTouchEvent: "+event.getAction());
        switch (action) {

            case MotionEvent.ACTION_DOWN:
                updateLetterByEvent(oldChoose, currenIndex);
                break;
            case MotionEvent.ACTION_MOVE:
                updateLetterByEvent(oldChoose, currenIndex);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                String w = letterList.get(currenIndex);
                choose = currenIndex;
                //根据字母取出坐标
                selectY = wordXY.get(w);
                invalidate();

                break;
        }
        return true;
    }

    public void updateLetterByEvent(int oldChoose, int currenIndex) {
        if (oldChoose != currenIndex && onTouchingLetterChangedListener != null) {
            if (currenIndex >= 0 && currenIndex < letterList.size()) {
                String w = letterList.get(currenIndex);
                onTouchingLetterChangedListener.onTouchingLetterChanged(w);
                choose = currenIndex;
                //根据字母取出坐标
                selectY = wordXY.get(w);

            }
        }
    }

    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener listener) {
        this.onTouchingLetterChangedListener = listener;
    }


    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s);
    }
}
