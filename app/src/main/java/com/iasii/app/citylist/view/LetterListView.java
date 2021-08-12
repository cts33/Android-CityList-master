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
    private int width;

    private int textDefaultColor = Color.parseColor("#333333");
    private int textFocusColor = Color.RED;

    int paddingTop, paddingDown;

    //每个字母占位的高度值 px
    private int singleHeight = 24;
    //默认选中的第一个元素的y坐标
    private int selectY =0;

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
        paddingTop = paddingDown = DensityUtil.dp2px(context, 10);

        selectY = singleHeight;

        letterList.addAll(Arrays.asList(defaultLets));

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

        Log.d(TAG, "onMeasure: " + getMeasuredHeight());
        singleHeight = getMeasuredHeight() / letterList.size();
//        int height = 0;
//        for (int i = 0; i < letterList.size(); i++) {
//            height += singleHeight;
//        }
        setMeasuredDimension(width, getMeasuredHeight());
    }

    private static final String TAG = "LetterListView";

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.d(TAG, "onDraw: ");

        xxx(canvas);

//        int letSize = letterList.size();
//
//
//        for (int i = 0; i < letSize; i++) {
//
//            paint.setColor(textDefaultColor);
//            paint.setTextSize(textSize);
//
//            paint.setAntiAlias(true);
//
//            float xPos = width / 2 - paint.measureText(letterList.get(i)) / 2;
//            float yPos = singleHeight * i + singleHeight;
//
//            if (i == choose) {
//                paint.setColor(textFocusColor);
//
//                paint.setFakeBoldText(true);
//
//            }
//            String s = letterList.get(i);
//            Log.d(TAG, "choose: ="+choose);
//            Log.d(TAG, "onDraw: x="+xPos+"  y="+yPos+"--s="+s);
//            canvas.drawText(s, xPos, yPos, paint);
//
//
//            paint.reset();
//        }

    }

    HashMap<String, Integer> wordXY = new HashMap();

    public void updateSelectIndex(String word) {

        selectY = wordXY.get(word);

    }

    public void xxx(Canvas canvas) {
        int ytop = 0;
        String con;
        int letSize = letterList.size();

        for (int i = 0; i < letSize; i++) {

            con = letterList.get(i);
            int y = ytop + singleHeight;
            RectF rect = new RectF(0, ytop, width, y);//画一个矩形

            wordXY.put(con, ytop + singleHeight);

            ytop += singleHeight;
            Paint mPaint = new Paint();


            canvas.drawRect(rect, mPaint);

            //设置画笔的样式，空心STROKE
//            mPaint.setStyle(Paint.Style.FILL);
            //设置抗锯齿
            mPaint.setAntiAlias(true);
            if (selectY == y) {
                mPaint.setColor(Color.RED);

            } else {

            }
            canvas.drawCircle(rect.centerX(), rect.centerY(), singleHeight / 2, mPaint);

            mPaint.setColor(Color.WHITE);
            mPaint.setTextSize(22);
            mPaint.setStyle(Paint.Style.FILL);
            //该方法即为设置基线上那个点究竟是left,center,还是right  这里我设置为center
            mPaint.setTextAlign(Paint.Align.CENTER);

            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
            float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom

            int baseLineY = (int) (rect.centerY() - top / 2 - bottom / 2);//基线中间点的y轴计算公式

            canvas.drawText(con, rect.centerX(), baseLineY, mPaint);
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        //计算事件在哪个字母的下标index
        final int currenIndex = (int) (y / getHeight() * letterList.size());
        Log.d(TAG, "dispatchTouchEvent: y  = " + y + " getHeight()=" + getHeight());
        Log.d(TAG, "dispatchTouchEvent: c  = " + currenIndex);
        switch (action) {

            case MotionEvent.ACTION_DOWN:
                updateLetterByEvent(oldChoose, currenIndex);
                break;
            case MotionEvent.ACTION_MOVE:
                updateLetterByEvent(oldChoose, currenIndex);
                break;
            case MotionEvent.ACTION_UP:

                invalidate();

                break;
        }
        return true;
    }

    public void updateLetterByEvent(int oldChoose, int currenIndex) {
        if (oldChoose != currenIndex && onTouchingLetterChangedListener != null) {
            if (currenIndex >= 0 && currenIndex < letterList.size()) {
                onTouchingLetterChangedListener.onTouchingLetterChanged(letterList.get(currenIndex));
                choose = currenIndex;
                invalidate();
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
