package cn.wsy.water.views;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import cn.wsy.water.R;
import cn.wsy.water.app.Contacts;
import cn.wsy.water.interfaces.ViewOpenEditPop;

/**
 * Created by Wusy on 2016/4/30.
 */
public class KnobTableView extends FrameLayout{

    private Context context;
    private View view;
    private KnobView knobView;
    //声明接口
    private ViewOpenEditPop popListener;

    public KnobTableView(Context context){
        super(context);
        init(context);
    }

    public KnobTableView(Context context,ViewOpenEditPop popListener) {
        super(context);
        this.popListener=popListener;
        init(context);

    }

    public void setPopListener(ViewOpenEditPop popListener) {
        this.popListener = popListener;
    }

    private void init(Context context){
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.view_knob_layout,this);
        knobView =(KnobView)view.findViewById(R.id.knob_5);
        DisplayMetrics dm = getResources().getDisplayMetrics();//获取设备屏幕的大小
        if(dm.widthPixels>dm.heightPixels) {

            knobView.setLayoutParams(new LinearLayout.LayoutParams(dm.heightPixels/2,dm.heightPixels/2));

        } else {

            knobView.setLayoutParams(new LinearLayout.LayoutParams(dm.widthPixels/2,dm.widthPixels/2));

        }
        if (popListener!=null){
            //注入pop，回调显示方法
            popListener.showEditPopWindow(Contacts.KNOBTABLE_TYPE,this, R.layout.knob_layout);
        }
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
                if(popListener!=null)
                popListener.showEditPopWindow(Contacts.KNOBTABLE_TYPE, this,R.layout.knob_layout);
            }
        }
        return true;
    }

}
