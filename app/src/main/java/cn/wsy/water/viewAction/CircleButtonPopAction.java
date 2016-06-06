package cn.wsy.water.viewAction;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import cn.wsy.water.R;
import cn.wsy.water.interfaces.ViewOpenEditPop;
import cn.wsy.water.views.CircleButton;

/**
 * 圆形按钮pop事件
 * Created by Wusy on 2016/5/11.
 */
public class CircleButtonPopAction extends BasePopAction<CircleButton>{

    private EditText bt_label;

    public CircleButtonPopAction(CircleButton widget, View parentLayout, Context context, ViewOpenEditPop popListener) {
        super(widget, parentLayout, context,popListener);
    }

    @Override
    public void initView() {
        super.initView();
        /**举例find id**/
        bt_label = (EditText) getViewById(R.id.bt_label);
    }

    @Override
    public void initListener() {
        super.initListener();
        /**举例注册**/
        bt_label.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.bt_label:
                //do...
                break;
        }
    }

    @Override
    public void getEditColorCallBack(int tag, int color) {

    }


}
