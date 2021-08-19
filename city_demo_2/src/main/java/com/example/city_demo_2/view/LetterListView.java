package com.example.city_demo_2.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.TextView;


import androidx.annotation.RequiresApi;

import com.example.city_demo_2.PingYinUtil;
import com.example.city_demo_2.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * 用于展示城市列表的字母索引列表
 * Created by next on 2016/3/24.
 */
public class LetterListView extends View {

    private String[] defaultLets = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;

    private List<String> letterList = new ArrayList<>();
    private int choose = 0;

    private Context context;
    private int textSize = 15;
    private float width, height;

    private int textDefaultColor = Color.parseColor("#333333");
    private int textFocusColor = Color.RED;

    private HashMap<String, Float> wordXY = new LinkedHashMap();

    //每个字母占位的高度值 px
    private float singleHeight = 50;
    //默认选中的第一个元素的y坐标
    private float selectY = 0;
    private boolean isClickEvent;
    private OverlayThread overlayThread;
    private boolean isOverlayReady =false;
    private TextView letterOverlay;
    private Handler handler;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public LetterListView(Context context) {
        this(context, null);
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public LetterListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public LetterListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.context = context;

        setBackgroundColor(Color.GRAY);
        initConfig();
        initOverlay();

    }
    private class OverlayThread implements Runnable {
        @Override
        public void run() {
            letterOverlay.setVisibility(View.GONE);
        }
    }
    // 初始化汉语拼音首字母弹出提示框
    private void initOverlay() {
        handler = new Handler();
        overlayThread = new OverlayThread();
        isOverlayReady = true;

        letterOverlay = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.v_letter_overlay, null);
        letterOverlay.setVisibility(View.INVISIBLE);

        int width = dp2px(  65);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                width, width,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(letterOverlay, lp);
    }
   private int dp2px(float dp) {
        return (int) (getResources().getDisplayMetrics().density * dp);
    }


    private void initConfig() {

        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();


        int hhh = dm.heightPixels;

        Log.d(TAG, "initConfig: " + hhh);

        width = dp2px(context, 50);

        letterList.addAll(Arrays.asList(defaultLets));

        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    height = ((View) getParent()).getHeight();
                    Log.d(TAG, "initConfig: 测量当前view高度=" + height);

                    float hh = height / letterList.size();

                    singleHeight = Math.min(hh, singleHeight);
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
        this.letterList.clear();

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


        setMeasuredDimension((int) width, getMeasuredHeight());
    }

    private static final String TAG = "LetterListView";

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        drawLetterList(canvas);

    }

    private long lastClickTime = 0L;
    // 两次点击间隔不能少于1000ms
    private static final int FAST_CLICK_DELAY_TIME = 500;


    public void updateSelectIndex(String word) {
        visibleOverLay(word);

        //如果是滑动事件，不处理
        if (isClickEvent)
            return;

        if (System.currentTimeMillis() - lastClickTime < FAST_CLICK_DELAY_TIME) {

            lastClickTime = System.currentTimeMillis();
            return;
        }
        if (wordXY.containsKey(word)) {
            selectY = wordXY.get(word);

            invalidate();
        }
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
            float bottom = top + singleHeight;
            RectF rect = new RectF(0, top, width, bottom);//画一个矩形

            wordXY.put(word, bottom);


            Paint mPaint = new Paint();

            top += singleHeight;
            mPaint.setColor(Color.BLACK);
            mPaint.setTextSize(22);

            mPaint.setStyle(Paint.Style.FILL);
            //该方法即为设置基线上那个点究竟是left,center,还是right  这里我设置为center
            mPaint.setTextAlign(Paint.Align.CENTER);

//            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
//            float t = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
//            float b = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom

//            int baseLineY = (int) (rect.centerY() - t / 2 - b / 2);//基线中间点的y轴计算公式

//            Log.d(TAG, "drawLetterList: "+baseLineY);
            if (selectY == bottom) {
                mPaint.setColor(Color.RED);

            }

            canvas.drawText(word, rect.centerX(), bottom, mPaint);
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        //计算事件在哪个字母的下标index
//        final int currenIndex = (int) (y / getHeight() * letterList.size());
        int currenIndex = getCurrIndex(y);
        if (currenIndex < 0) {
            currenIndex = 0;
        }

        switch (action) {

            case MotionEvent.ACTION_DOWN:
                isClickEvent = true;
                updateLetterByEvent(oldChoose, currenIndex);
                break;
            case MotionEvent.ACTION_MOVE:
                isOverlayReady = true;
                updateLetterByEvent(oldChoose, currenIndex);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                String w = letterList.get(currenIndex);
                choose = currenIndex;
                //根据字母取出坐标
                selectY = wordXY.get(w);
                invalidate();
                isClickEvent = false;
                isOverlayReady = false;
                break;
        }
        return true;
    }

    /**
     * 通过记录的坐标集合，计算出点击是第几个位置
     *
     * @param y
     * @return
     */
    private int getCurrIndex(float y) {
        Iterator<Map.Entry<String, Float>> iterator = wordXY.entrySet().iterator();
        float pre = 0;
        int pos = 0;
        while (iterator.hasNext()) {
            Map.Entry<String, Float> next = iterator.next();
            Float value = next.getValue();
            Log.d(TAG, "getCurrIndex: 通过坐标，计算位置=pre "+pre +"  y "+y+" value "+value);
            if (y >= pre && y <= value) {
                return pos;
            }
            pre = value;
            pos++;
        }


        return -1;
    }

    private void updateLetterByEvent(int oldChoose, int currenIndex) {
        if (oldChoose != currenIndex && onTouchingLetterChangedListener != null) {
            if (currenIndex >= 0 && currenIndex < letterList.size()) {
                String w = letterList.get(currenIndex);
                onTouchingLetterChangedListener.onTouchingLetterChanged(w);
                choose = currenIndex;
                //根据字母取出坐标
                selectY = wordXY.get(w);


                handler.removeCallbacks(overlayThread);
                handler.postDelayed(overlayThread,1000);
            }
        }
    }

    public void visibleOverLay(String firstword){
        if (isOverlayReady) {

            Pattern pattern = Pattern.compile("[A-Z]");
            if (pattern.matcher(firstword).matches()) {
                letterOverlay.setTextSize(40);
            } else {
                letterOverlay.setTextSize(20);
            }
            letterOverlay.setText(firstword);
            letterOverlay.setVisibility(View.VISIBLE);
            handler.removeCallbacks(overlayThread);
            // 延迟一秒后执行，让overlay为不可见
            handler.postDelayed(overlayThread, 1000);
        }
    }

    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener listener) {
        this.onTouchingLetterChangedListener = listener;
    }


    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s);
    }

    private static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources()
                .getDisplayMetrics());
    }
}
