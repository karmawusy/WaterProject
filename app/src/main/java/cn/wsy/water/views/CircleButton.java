package cn.wsy.water.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;

import cn.wsy.water.R;
import cn.wsy.water.app.Contacts;
import cn.wsy.water.base.BaseButton;
import cn.wsy.water.interfaces.ViewOpenEditPop;

/**
 * 圆形按钮
 * Created by wsy on 2016/4/14.
 */
public class CircleButton extends BaseButton {

    private Context context;
    private int pressColor = -1;

    private ViewOpenEditPop popListener;

    public CircleButton(Context context) {
        super(context);
        init(context, null);
    }


    public CircleButton(Context context, ViewOpenEditPop popListener) {
        super(context);
        this.popListener = popListener;
        init(context, null);
    }

    public void setPopListener(ViewOpenEditPop popListener) {
        this.popListener = popListener;
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        defaultStyle();
        setText("B");
        setTextSize(35);

        if (popListener !=null){
            popListener.showEditPopWindow(Contacts.CIRCLEBUTTON_TYPE,this,R.layout.button_layout);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void defaultStyle() {
        setTextColor(getResources().getColor(R.color.rocker_main_none));
        setBackground(getResources().getDrawable(R.drawable.circle_btn_bg_nor));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void changeStyle() {
        setTextColor(getResources().getColor(R.color.rocker_main_press));
        setBackground(getResources().getDrawable(R.drawable.circle_btn_bg_press));
        if (this.pressColor != -1) {
            GradientDrawable bgShape = (GradientDrawable) this.getBackground();
            bgShape.setColor(this.pressColor );
        }
    }

    float dx = -1;
    float dy = -1;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (isNotMove()) {
            if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                changeStyle();
            }

            if (event.getAction() == MotionEvent.ACTION_UP) {
                defaultStyle();
            }
        }else{/***这是在编辑模式先，如果在编辑模式下，移动的时候必须屏蔽自身的点击事件*/
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                dx = event.getX();
                dy = event.getY();

            }else if (event.getAction() == MotionEvent.ACTION_UP){
                /**以松开手移动距离作为判定标准！！！！**/
                float moveX = event.getX() - dx;
                float moveY = event.getY() - dy;

                if (moveX < 0){moveX = -moveX;}
                if (moveY < 0){moveY = -moveY;}

                if ((popListener !=null &&moveX>5)||(popListener !=null &&moveY>5)){
                    popListener.dimissPopWindow();
                }else{
                    if (popListener !=null)
                    popListener.showEditPopWindow(Contacts.CIRCLEBUTTON_TYPE, this,R.layout.button_layout);
                }
            }
        }
        return true;
    }



//    //edit
//    public void setLabel(String label){
//        setText(label);
//        setTextSize(35);
//    }

    public void setBtnBackgroundColor(int color){
        GradientDrawable bgShape = (GradientDrawable) this.getBackground();
        bgShape.setColor(color);
    }

    public void setBtnPressBackgroundColor(int color){
        this.pressColor = color;
    }

    public void setBtnText(String label){
        if (label.length() > 1){
            this.setText(label.substring(0,1));
        }
    }

    public void finishDraw(){
        this.invalidate();
    }

}
