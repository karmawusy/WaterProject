package cn.wsy.water.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import cn.wsy.water.R;
import cn.wsy.water.app.Contacts;
import cn.wsy.water.interfaces.ViewOpenEditPop;

/**
 * Created by Kevin on 2016/4/20.
 * 三轴加速度传感器
 */
public class ThreeView extends BaseView {
    private ViewOpenEditPop popListenr;
    private int x1;
    private int y1;
    private int z1;

    /*
      * 自定义控件一般写两个构造方法 CoordinatesView(Context context)用于java硬编码创建控件
      * 如果想要让自己的控件能够通过xml来产生就必须有第2个构造方法 CoordinatesView(Context context,
      * AttributeSet attrs) 因为框架会自动调用具有AttributeSet参数的这个构造方法来创建继承自View的控件
      */
    public ThreeView(Context context) {
        super(context, null);
    }

    public ThreeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ThreeView(Context context, ViewOpenEditPop popListener) {
        this(context);
        this.popListenr = popListener;
        if (popListenr != null) {
            popListenr.showEditPopWindow(Contacts.THREEVIEW_TYPE, this, R.layout.accelerometer_layout);
        }

    }

    public ThreeView(Context context, Bundle bundle) {
        this(context);
        x1 = bundle.getInt("x");
        y1 = bundle.getInt("y");
        z1 = bundle.getInt("z");
        Log.i("threeview","x"+x1+"y"+y1+"z"+z1);


    }

    public void setPopListenr(ViewOpenEditPop popListenr) {
        this.popListenr = popListenr;
    }

    /*
         * 两个外部数据
         */
    Point pa = new Point(10, 10);
    Point pb = new Point(20, 40);

    /*
     * 圆心（坐标值是相对与控件的左上角的）
     */
    Point po = new Point();
    /*
     * 控件的中心点
     */
    int centerX, centerY;

    /*
     * 控件创建完成之后，在显示之前都会调用这个方法，此时可以获取控件的大小 并得到中心坐标和坐标轴圆心所在的点。
     */
    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        centerX = w / 2 - 100;
        centerY = h / 2 + 30;
        po.set(centerX, centerY);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /*
     * 自定义控件一般都会重载onDraw(Canvas canvas)方法，来绘制自己想要的图形
     */
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStrokeWidth(6);
        paint.setColor(Color.BLACK);

        // 画坐标轴
        if (canvas != null) {
            // 画直线
            canvas.drawLine(centerX, centerY, centerX + 150, centerY, paint);
            canvas.drawLine(centerX, centerY, centerX, centerY - 150, paint);
            // 画X轴箭头
            int x = 155 + centerX, y = centerY;
            drawTriangle(canvas, new Point(x, y), new Point(x - 40, y - 30),
                    new Point(x - 40, y + 30));
            //canvas.drawText("X", x - 15, y + 18, paint);
            // 画Y轴箭头
            x = centerX;
            y = centerY - 155;
            drawTriangle(canvas, new Point(x, y), new Point(x - 30, y + 40),
                    new Point(x + 30, y + 40));
            //canvas.drawText("Y", x + 12, y + 15, paint);
            // 画中心点坐标
            // 先计算出来当前中心点坐标的值
            //String centerString = "(" + (centerX - po.x) / 2 + ","+ (po.y - centerY) / 2 + ")";
            paint.setStrokeWidth(6);
            paint.setTextSize(40);
            paint.setColor(Color.GRAY);
            //Log.i("threeview", "x" + x1 + "y" + y1 + "z" + z1);
            // 然后显示坐标
            canvas.drawText("X: " + x1 + " Y: " + y1 + " Z: " + z1, 0, centerY + 80, paint);
            postInvalidate();
        }

        if (canvas != null) {
            /*
             * TODO 画数据 所有外部需要在坐标轴上画的数据，都在这里进行绘制
             */
            //canvas.drawCircle(po.x + 2 * pa.x, po.y - 2 * pa.y, 2, paint);
            //canvas.drawCircle(po.x + 2 * pb.x, po.y - 2 * pb.y, 2, paint);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(3);
            canvas.drawCircle(centerX + 50, centerY - 50, 10, paint);

            paint.setStyle(Paint.Style.STROKE);
            paint.setAntiAlias(true);
            paint.setStrokeWidth(6);
            canvas.drawCircle(centerX + 50, centerY - 50, 30, paint);
            //canvas.drawLine(po.x + 2 * pa.x, po.y - 2 * pa.y, po.x + 2 * pb.x,po.y - 2 * pb.y, paint);
            // canvas.drawPoint(pa.x+po.x, po.y-pa.y, paint);
        }

    }

    /**
     * 画三角形 用于画坐标轴的箭头
     */
    private void drawTriangle(Canvas canvas, Point p1, Point p2, Point p3) {
        Path path = new Path();
        path.moveTo(p1.x, p1.y);
        path.lineTo(p2.x, p2.y);
        path.lineTo(p3.x, p3.y);
        path.close();

        Paint paint = new Paint();
        paint.setStrokeWidth(4);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        // 绘制这个多边形
        canvas.drawPath(path, paint);
    }

    /*
     * 用于保存拖动时的上一个点的位置
     */
//    int x0, y0;

    /*
     * 拖动事件监听
     */
    /*@Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (getIsNotMove()) {
        *//*
         * (x,y)点为发生事件时的点，它的坐标值为相对于该控件左上角的距离
         *//*
            int x = (int) event.getX();
            int y = (int) event.getY();
            switch (action) {

                case MotionEvent.ACTION_DOWN: // 按下
                    x0 = x;
                    y0 = y;
                    Log.i("down", "(" + x0 + "," + y0 + ")");
                    break;
                case MotionEvent.ACTION_MOVE: // 拖动
            *//*
             * 拖动时圆心坐标相对运动 (x0,y0)保存先前一次事件发生的坐标
             * 拖动的时候只要计算两个坐标的delta值，然后加到圆心中，即移动了圆心。
             * 然后调用invalidate()让系统调用onDraw()刷新以下屏幕，即实现了坐标移动。
             *//*
                    po.x += x - x0;
                    po.y += y - y0;
                    x0 = x;
                    y0 = y;
                    Log.i("move", "(" + x0 + "," + y0 + ")");
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP: // 弹起
                    break;
            }
        }*/
        /*
         * 注意：这里一定要返回true
         * 返回false和super.onTouchEvent(event)都会本监听只能检测到按下消息
         * 这是因为false和super.onTouchEvent(event)的处理都是告诉系统该控件不能处理这样的消息，
         * 最终系统会将这些事件交给它的父容器处理。

        return true;

    }*/

    float dx = 0, dy = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            dx = event.getX();
            dy = event.getY();

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            /**以松开手移动距离作为判定标准！！！！**/
            float moveX = event.getX() - dx;
            float moveY = event.getY() - dy;

            if (moveX < 0) {
                moveX = -moveX;
            }
            if (moveY < 0) {
                moveY = -moveY;
            }

            if ((popListenr != null && moveX > 5) || (popListenr != null && moveY > 5)) {
                popListenr.dimissPopWindow();
            } else {
                if (popListenr != null)
                    popListenr.showEditPopWindow(Contacts.THREEVIEW_TYPE, this, R.layout.accelerometer_layout);
            }
        }
        return true;
    }

   /* public void xyz(Bundle bundle){
       x1=bundle.getInt("x");
        y1=bundle.getInt("y");
        z1=bundle.getInt("z");
        //Log.i("threeview","x"+x1+"y"+y1+"z"+z1);
    }*/

}
