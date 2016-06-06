package cn.wsy.water.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import cn.wsy.water.R;
import cn.wsy.water.app.Contacts;
import cn.wsy.water.interfaces.ViewOpenEditPop;
import cn.wsy.water.utils.DensityUtil;
import cn.wsy.water.utils.MathUtils;

/**
 * 虚拟摇杆 支持屏蔽开关
 * Created by wsy on 2016/4/13.
 */
public class RockerView extends SurfaceView implements Runnable, SurfaceHolder.Callback {

    private Context context;
    private SurfaceHolder mHolder;
    private boolean isStop = false;
    private boolean isPause = false;
    private boolean isOnclick = false;
    private boolean isMove = false;
    private Thread mThread;
    private Paint mPaint;

    private Point mRockerPosition;                  //摇杆位置
    private Point mCtrlPoint = new Point(80, 80);   //摇杆起始位置
    private int mRudderRadius = 40;                 //摇杆半径
    private int mWheelRadius = 70;                  //摇杆活动范围半径

    Bitmap rocker_bg, rocker_ctrl;

    private RudderListener listener = null; //事件回调接口
    private ViewOpenEditPop popListener;
    public static final int ACTION_RUDDER = 1;

    private int defaultColor = -1;//默认颜色
    private int pressColor = -1;//按下颜色
    private String label = "";

    public RockerView(Context context) {
        super(context);
        init(context);
    }

    public RockerView(Context context, ViewOpenEditPop popListener) {
        super(context);
        this.popListener = popListener;
        init(context);
    }

    public void setPopListener(ViewOpenEditPop popListener) {
        this.popListener = popListener;
    }

    private void init(Context context) {
        this.context = context;
        //数值修正
        mWheelRadius = DensityUtil.dip2px((ContextThemeWrapper) context, mWheelRadius);
        mRudderRadius = DensityUtil.dip2px((ContextThemeWrapper) context, mRudderRadius);

        this.setKeepScreenOn(true);
        mHolder = getHolder();
        mHolder.addCallback(this);
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setAntiAlias(true);                  //抗锯齿
        mRockerPosition = new Point(mCtrlPoint);    //设置起始位置
        setFocusable(true);                         //可获得焦点（键盘）
        setFocusableInTouchMode(true);              //可获得焦点（触摸）
        setZOrderOnTop(true);                       //保持在界面最上层
        mHolder.setFormat(PixelFormat.TRANSPARENT); //设置背景透明
        if (popListener!=null){
            popListener.showEditPopWindow(Contacts.ROCKERVIEW_TYPE,this, R.layout.analogstick_layout);
        }
    }


    /**
     * 设置摇杆背景图片
     *
     * @param bitmap
     */
    public void setRockerBg(Bitmap bitmap) {
        rocker_bg = Bitmap.createScaledBitmap(bitmap, mWheelRadius * 2, mWheelRadius * 2, true);
    }

    /**
     * 设置摇杆图片
     *
     * @param bitmap
     */
    public void setRockerCtrl(Bitmap bitmap) {
        rocker_ctrl = Bitmap.createScaledBitmap(bitmap, mRudderRadius * 2, mRudderRadius * 2, true);
    }

    /**
     * 设置摇杆活动半径
     *
     * @param radius
     */
    public void setmWheelRadius(int radius) {
        mWheelRadius = DensityUtil.dip2px((ContextThemeWrapper) context, radius);
    }

    /**
     * 设置摇杆半径
     *
     * @param radius
     */
    public void setmRudderRadius(int radius) {
        mRudderRadius = DensityUtil.dip2px((ContextThemeWrapper) context, radius);
    }

    @Override
    public void run() {
        Canvas canvas = null;

        while (!isStop) {
            try {
                canvas = mHolder.lockCanvas();
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//清除屏幕
                if (rocker_bg != null) {
                    mPaint.setColor(Color.WHITE);
                    mPaint.setStyle(Paint.Style.FILL);
                    // 绘制一个矩形
//                    canvas.drawRect(new Rect(0, 0, getWidth(), getHeight()), mPaint);
                    canvas.drawBitmap(rocker_bg, mCtrlPoint.x - mWheelRadius, mCtrlPoint.y - mWheelRadius, mPaint);//这里的60px是最外围的图片的半径
                    if (isOnclick) {
                        mPaint.setColor(getResources().getColor(R.color.rocker_bg_press));
                        canvas.drawCircle(mCtrlPoint.x, mCtrlPoint.y, mWheelRadius * 5 / 7, mPaint);//绘制范围
                    }

                    if (isMove){
                        mPaint.setColor(Color.YELLOW);
                        canvas.drawCircle(mCtrlPoint.x, mCtrlPoint.y, mWheelRadius * 5 / 7, mPaint);//绘制范围
                    }

                } else {
                    mPaint.setColor(Color.YELLOW);
                    canvas.drawCircle(mCtrlPoint.x, mCtrlPoint.y, mWheelRadius, mPaint);//绘制范围
                }

                if (rocker_ctrl != null) {
                    canvas.drawBitmap(rocker_ctrl, mRockerPosition.x - mRudderRadius, mRockerPosition.y - mRudderRadius, mPaint);//这里的20px是最里面的图片的半径
                } else {
                    if (defaultColor != -1){
                        mPaint.setColor(defaultColor);
                    }else {
                        mPaint.setColor(getResources().getColor(R.color.rocker_main_none));
                    }
                    canvas.drawCircle(mRockerPosition.x, mRockerPosition.y, mRudderRadius, mPaint);//绘制摇杆

                    if (isOnclick) {
                        if (defaultColor != -1){
                            mPaint.setColor(pressColor);
                        }else {
                            mPaint.setColor(getResources().getColor(R.color.rocker_main_press));
                        }
                        canvas.drawCircle(mRockerPosition.x, mRockerPosition.y, mRudderRadius * 9 / 10, mPaint);//绘制摇杆焦点
                    }

                    if (isMove){
                        mPaint.setColor(Color.YELLOW);
                        canvas.drawCircle(mRockerPosition.x, mRockerPosition.y, mRudderRadius * 9 / 10, mPaint);//绘制摇杆焦点
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    mHolder.unlockCanvasAndPost(canvas);
                }
            }
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //确定中心点
        int width = this.getWidth();
        int height = this.getHeight();
        mCtrlPoint = new Point(width / 2, height / 2);
        mRockerPosition = new Point(mCtrlPoint);
        isStop = false;
        mThread = new Thread(this);
        mThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isStop = true;
    }

    public void setIsPause(boolean isPause) {
        this.isPause = isPause;
    }

    float dx=0,dy=0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isPause) {
            int len = MathUtils.getLength(mCtrlPoint.x, mCtrlPoint.y, event.getX(), event.getY());
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                //如果屏幕接触点不在摇杆挥动范围内,则不处理
                if (len > mWheelRadius) {
                    return true;
                }
                dx = event.getX();
                dy = event.getY();
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                isOnclick = false;
                if (listener != null) {
                    listener.onSteeringWheelChanged(ACTION_RUDDER, 0);
                }
                float moveX = event.getX() - dx;
                float moveY = event.getY() - dy;

                if (moveX < 0){moveX = -moveX;}
                if (moveY < 0){moveY = -moveY;}

                if ((popListener !=null &&moveX>5)||(popListener !=null &&moveY>5)){
                    popListener.dimissPopWindow();
                }else{
                    if(popListener !=null){
                        popListener.showEditPopWindow(Contacts.ROCKERVIEW_TYPE, this,R.layout.analogstick_layout);
                    }
                }
            }

            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                isOnclick = true;
                if (len <= mWheelRadius / 4) {
                    //如果手指在摇杆活动范围内，则摇杆处于手指触摸位置
                    mRockerPosition.set((int) event.getX(), (int) event.getY());

                } else {
                    //设置摇杆位置，使其处于手指触摸方向的 摇杆活动范围边缘
                    mRockerPosition = MathUtils.getBorderPoint(mCtrlPoint,
                            new Point((int) event.getX(), (int) event.getY()), mWheelRadius / 4);
                }
                if (listener != null) {
                    float radian = MathUtils.getRadian(mCtrlPoint, new Point((int) event.getX(), (int) event.getY()));
                    listener.onSteeringWheelChanged(ACTION_RUDDER, RockerView.this.getAngleCouvert(radian));
                }
            }
            //如果手指离开屏幕，则摇杆返回初始位置
            if (event.getAction() == MotionEvent.ACTION_UP) {
                mRockerPosition = new Point(mCtrlPoint);
            }
        } else {
            mRockerPosition = new Point(mCtrlPoint);
//            if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE){
//                isMove = true;
//            }else if (event.getAction() == MotionEvent.ACTION_UP){
//                isMove = false;
//            }else{
//                isMove = true;
//            }
        }
        return true;

    }

    //获取摇杆偏移角度 0-360°
    private int getAngleCouvert(float radian) {
        int tmp = (int) Math.round(radian / Math.PI * 180);
        if (tmp < 0) {
            return -tmp;
        } else {
            return 180 + (180 - tmp);
        }
    }

    //回调接口
    public interface RudderListener {
        void onSteeringWheelChanged(int action, int angle);
    }

    //设置回调接口
    public void setRudderListener(RudderListener rockerListener) {
        listener = rockerListener;
    }

    public void setPressColor(int pressColor) {
        this.pressColor = pressColor;
    }

    public void setDefaultColor(int defaultColor) {
        this.defaultColor = defaultColor;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}



