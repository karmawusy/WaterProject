package cn.wsy.water.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;

import cn.wsy.water.R;
import cn.wsy.water.app.Contacts;
import cn.wsy.water.base.BaseView;
import cn.wsy.water.interfaces.ViewOpenEditPop;

/**
 * Created by Kevin on 2016/4/25.
 */
public class OutputView extends BaseView {
    private Paint mPaint;
    private ViewOpenEditPop popListenr;




    public OutputView(Context context) {
        super(context);
    }

    public OutputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void setPopListenr(ViewOpenEditPop popListenr) {
        this.popListenr = popListenr;
    }

    public OutputView(Context context, ViewOpenEditPop popListener) {
        this(context);
        this.popListenr = popListener;
        if (popListenr != null) {
            popListenr.showEditPopWindow(Contacts.OUTPUTVIEW_TYPE, this, R.layout.graphic_layout);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint = new Paint();

        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        canvas.drawRect(new Rect(0, 90, 650, 450), mPaint);
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(3);
        canvas.drawLine(0, 180, 650, 180, mPaint);
        canvas.drawLine(0, 270, 650, 270, mPaint);
        canvas.drawLine(0, 360, 650, 360, mPaint);
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(1);
        mPaint.setTextSize(35);
        canvas.drawText("Graphic",250,50,mPaint);
    }
    float dx=0;
    float dy=0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            dx = event.getX();
            dy = event.getY();

        }else if (event.getAction() == MotionEvent.ACTION_UP){
            /**以松开手移动距离作为判定标准！！！！**/
            float moveX = event.getX() - dx;
            float moveY = event.getY() - dy;

            if (moveX < 0){moveX = -moveX;}
            if (moveY < 0){moveY = -moveY;}

            if ((popListenr !=null &&moveX>5)||(popListenr !=null &&moveY>5)){
                popListenr.dimissPopWindow();
            }else{
                if(popListenr!=null)
                    popListenr.showEditPopWindow(Contacts.OUTPUTVIEW_TYPE,this, R.layout.graphic_layout);
            }
        }
        return true;
    }
}
