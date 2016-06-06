package cn.wsy.water.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import cn.wsy.water.R;
import cn.wsy.water.app.Contacts;
import cn.wsy.water.interfaces.ViewOpenEditPop;

/**
 * 方向盘View
 * Created by wsy on 2016/4/13.
 */
public class WheelView extends RelativeLayout {

    private Button leftBtn,rightBtn,upBtn,downBtn;

    private Context context;
    private View view;

    private ViewOpenEditPop popListener;

    public WheelView(Context context) {
        super(context);
        init(context);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public WheelView(Context context,ViewOpenEditPop popListener){
        this(context);
        this.popListener=popListener;
        init(context);
        if (popListener!=null){
            popListener.showEditPopWindow(Contacts.WHELLVIEW_TYPE,this, R.layout.dpad_layout);
        }
    }

    public void setPopListener(ViewOpenEditPop popListener) {
        this.popListener = popListener;
    }

    private void init(Context context){
        this.context = context;
        view = LayoutInflater.from(this.context).inflate(R.layout.view_wheel_layout, this);

        leftBtn = (Button) findViewById(R.id.wheel_left_btn);
        rightBtn = (Button) findViewById(R.id.wheel_right_btn);
        upBtn = (Button) findViewById(R.id.wheel_up_btn);
        downBtn = (Button) findViewById(R.id.wheel_down_btn);
    }

    private void initOnClick(OnClickListener leftListener,OnClickListener rightListener,OnClickListener upListerner,OnClickListener downListener){

        leftBtn.setOnClickListener(leftListener);
        rightBtn.setOnClickListener(rightListener);
        upBtn.setOnClickListener(upListerner);
        downBtn.setOnClickListener(downListener);
    }
    float dx=0,dy=0;
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

            if ((popListener !=null &&moveX>5)||(popListener !=null &&moveY>5)){
                popListener.dimissPopWindow();
            }else{
                if (popListener !=null)
                    popListener.showEditPopWindow(Contacts.WHELLVIEW_TYPE, this,R.layout.dpad_layout);
            }
        }
        return true;
    }


}
