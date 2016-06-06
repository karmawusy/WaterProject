package cn.wsy.water.views;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import cn.wsy.water.R;
import cn.wsy.water.app.Contacts;
import cn.wsy.water.interfaces.ViewOpenEditPop;

/**
 * 方向盘View
 * Created by wsy on 2016/4/13.
 */
public class WheelView extends RelativeLayout {

    private WheelVidget leftBtn, rightBtn, upBtn, downBtn;
    private LinearLayout parentLayout;

    private Context context;
    private View view;

    private ViewOpenEditPop popListener;

    private boolean isPause = false;

    public WheelView(Context context) {
        super(context);
        init(context);
    }

    public WheelView(Context context, ViewOpenEditPop popListener) {
        this(context);
        this.popListener = popListener;
        init(context);
    }

    public WheelView(Context context, ViewOpenEditPop popListener, boolean isPause) {
        super(context);
        this.popListener = popListener;
        this.isPause = isPause;
        init(context);
    }

    public void setPopListener(ViewOpenEditPop popListener) {
        this.popListener = popListener;
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }

    private void init(Context context) {
        this.context = context;
        view = LayoutInflater.from(this.context).inflate(R.layout.view_wheel_layout, this);

        leftBtn = (WheelVidget) findViewById(R.id.wheel_left_btn);
        rightBtn = (WheelVidget) findViewById(R.id.wheel_right_btn);
        upBtn = (WheelVidget) findViewById(R.id.wheel_top_btn);
        downBtn = (WheelVidget) findViewById(R.id.wheel_bottom_btn);
        parentLayout = (LinearLayout) findViewById(R.id.wheel_layout);

        if (popListener != null) {
            popListener.showEditPopWindow(Contacts.WHELLVIEW_TYPE, this, R.layout.dpad_layout);
            setOnClickAction(leftBtn);
            setOnClickAction(rightBtn);
            setOnClickAction(upBtn);
            setOnClickAction(downBtn);
            parentLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    popListener.showEditPopWindow(Contacts.WHELLVIEW_TYPE, view, R.layout.dpad_layout);
                }
            });
        } else {
//            parentLayout.setVisibility(GONE);
            setOntouchAction(leftBtn);
            setOntouchAction(rightBtn);
            setOntouchAction(upBtn);
            setOntouchAction(downBtn);
        }
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        //遍历获取view触摸点
//        if (popListener != null) {
//            for (int i = 0; i < getChildCount(); i++) {
//                if (getChildAt(i) == parentLayout && getChildAt(i).dispatchTouchEvent(ev)) {
//                    return true;//拦截
//                }
//            }
//        }
//        return super.dispatchTouchEvent(ev);
//    }
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return super.onInterceptTouchEvent(ev);
//    }


    private void initOnClick(OnClickListener leftListener, OnClickListener rightListener, OnClickListener upListerner, OnClickListener downListener) {

        leftBtn.setOnClickListener(leftListener);
        rightBtn.setOnClickListener(rightListener);
        upBtn.setOnClickListener(upListerner);
        downBtn.setOnClickListener(downListener);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

    }

    float dx = 0, dy = 0;

    private void setOntouchAction(final WheelVidget wheelVidget) {

        wheelVidget.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    wheelVidget.setEnableOnclik(false);
                    wheelVidget.setIsOnclick(true);
                    wheelVidget.invalidate();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    wheelVidget.setEnableOnclik(false);
                    wheelVidget.setIsOnclick(false);
                    wheelVidget.invalidate();
                }

                return false;
            }
        });

    }

    private void setOnClickAction(final WheelVidget wheelVidget) {
        Log.i("wusy", "setOnClickAction");
        wheelVidget.setPopListener(popListener);
//        wheelVidget.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i("wusy", "wheel btn onclick");
//                popListener.showEditPopWindow(Contacts.WHELLVIEW_TYPE, view, R.layout.dpad_layout);
//            }
//        });



    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isPause) {

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
                if ((popListener != null && moveX > 5) || (popListener != null && moveY > 5)) {
                    popListener.dimissPopWindow();
                } else {
                    if (popListener != null)
                        popListener.showEditPopWindow(Contacts.WHELLVIEW_TYPE, this, R.layout.dpad_layout);
                }

                return false;
            }
        }
        return false;
    }


}
