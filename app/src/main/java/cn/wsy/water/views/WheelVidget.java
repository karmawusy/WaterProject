package cn.wsy.water.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import cn.wsy.water.R;
import cn.wsy.water.interfaces.ViewOpenEditPop;

/**
 * Created by wsy on 2016/5/27.
 */
public class WheelVidget extends Button {

    private ViewOpenEditPop popListener;

    private Paint bgPaint = new Paint();
    private Paint inPaint = new Paint();

    private int RECT_WIDTH = 0;//矩形宽度
    private int VIEW_HEIGHT = 0;//view 高度

    private boolean isGet = true;
    private boolean isOnclick = false;
    private boolean isEnableOnclik = true;

    private int defualt_color = Color.BLACK;
    private int press_color = getResources().getColor(R.color.app_main_color);

    public WheelVidget(Context context) {
        super(context);
        init();
    }

    //这里绝对按钮是否需要点击颜色变化
    public WheelVidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        setBackgroundColor(Color.TRANSPARENT);
        int w = MeasureSpec.makeMeasureSpec(0,
                MeasureSpec.UNSPECIFIED);
        int h = MeasureSpec.makeMeasureSpec(0,
                MeasureSpec.UNSPECIFIED);
        this.measure(w, h);
        VIEW_HEIGHT = this.getMeasuredHeight();
        RECT_WIDTH = this.getMeasuredWidth();
        Log.i("wusy","real w is :"+RECT_WIDTH+" h is :"+VIEW_HEIGHT);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
//        RECT_WIDTH = getWidth();
//        VIEW_HEIGHT = getHeight();
//        Log.i("wusy","real w is :"+RECT_WIDTH+" h is :"+VIEW_HEIGHT);
//        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isEnableOnclik){return false;}
        return super.onTouchEvent(event);
    }

    public void setEnableOnclik(boolean enableOnclik) {
        this.isEnableOnclik = enableOnclik;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (RECT_WIDTH > 0) {

            if (RECT_WIDTH < VIEW_HEIGHT) {

                if (!isOnclick) {
                    bgPaint.setColor(defualt_color);
                } else {
                    bgPaint.setColor(press_color);
                }

                //画矩形
                canvas.drawRect(0, 0, RECT_WIDTH, VIEW_HEIGHT * 2 / 5, bgPaint);
                //绘画三角形
                Path path = new Path();
                path.moveTo(RECT_WIDTH / 2, VIEW_HEIGHT);
                path.lineTo(0, VIEW_HEIGHT * 3 / 5);
                path.lineTo
                        (RECT_WIDTH, VIEW_HEIGHT * 3 / 5);
                path.close(); // 使这些点构成封闭的多边形
                canvas.drawPath(path, bgPaint);

                //画内部三角形
                inPaint.setColor(Color.WHITE);
                Path path_in = new Path();
                path_in.moveTo(RECT_WIDTH / 2, VIEW_HEIGHT / 4);
                path_in.lineTo(RECT_WIDTH / 3, VIEW_HEIGHT / 2);
                path_in.lineTo(RECT_WIDTH * 2 / 3, VIEW_HEIGHT / 2);
                path_in.close(); // 使这些点构成封闭的多边形
                canvas.drawPath(path_in, inPaint);

            } else {

                if (!isOnclick) {
                    bgPaint.setColor(defualt_color);
                } else {
                    bgPaint.setColor(press_color);
                }

                canvas.drawRect(0, 0, RECT_WIDTH * 2 / 5, VIEW_HEIGHT, bgPaint);

                //绘画三角形
                Path path = new Path();
                path.moveTo(RECT_WIDTH, VIEW_HEIGHT / 2);
                path.lineTo(RECT_WIDTH * 3 / 5, 0);
                path.lineTo(RECT_WIDTH * 3 / 5, VIEW_HEIGHT);
                path.close(); // 使这些点构成封闭的多边形
                canvas.drawPath(path, bgPaint);

                //画内部三角形
                inPaint.setColor(Color.WHITE);
                Path path_in = new Path();
                path_in.moveTo(RECT_WIDTH / 4, VIEW_HEIGHT / 2);
                path_in.lineTo(RECT_WIDTH / 2, VIEW_HEIGHT / 3);
                path_in.lineTo(RECT_WIDTH / 2, VIEW_HEIGHT * 2 / 3);
                path_in.close(); // 使这些点构成封闭的多边形
                canvas.drawPath(path_in, inPaint);

            }
        }

    }

    public void setIsOnclick(boolean onclick) {
        isOnclick = onclick;
    }

    public void setPopListener(ViewOpenEditPop popListener) {
        this.popListener = popListener;
    }


}
