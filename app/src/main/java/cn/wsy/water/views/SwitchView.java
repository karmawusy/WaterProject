package cn.wsy.water.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.sevenheaven.iosswitch.ShSwitchView;

import cn.wsy.water.R;
import cn.wsy.water.app.Contacts;
import cn.wsy.water.interfaces.ViewOpenEditPop;

/**
 * Created by Kevin on 2016/5/2.
 */
public class SwitchView extends FrameLayout {
    private View view;
    private ShSwitchView switchView;
    private Context context;
    private ViewOpenEditPop popListener;
    public SwitchView(Context context) {
        super(context);
        init(context);
    }
    public SwitchView(Context context,ViewOpenEditPop popListener){
        this(context);
      this.popListener=popListener;
        init(context);
        if (popListener!=null){
            popListener.showEditPopWindow(Contacts.SWTCHVIEW_TYPE,this,R.layout.switch_layout);
        }


    }

    public void setPopListener(ViewOpenEditPop popListener) {
        this.popListener = popListener;
    }

    private void init(Context context) {
        this.context=context;
        view= LayoutInflater.from(context).inflate(R.layout.view_switch_layout,this);
        switchView= (ShSwitchView) view.findViewById(R.id.switch_view);
        switchView.setOn(true);

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
                    popListener.showEditPopWindow(Contacts.SWTCHVIEW_TYPE, this,R.layout.switch_layout);
            }
        }
        return true;
    }
}
