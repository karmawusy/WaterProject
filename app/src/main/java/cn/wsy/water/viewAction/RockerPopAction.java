package cn.wsy.water.viewAction;

import android.content.Context;
import android.view.View;

import cn.wsy.water.interfaces.ViewOpenEditPop;
import cn.wsy.water.views.RockerView;

/**
 * 遥杆
 * Created by Wusy on 2016/5/11.
 */
public class RockerPopAction extends BasePopAction<RockerView> {

    public RockerPopAction(RockerView widget, View parentLayout, Context context,ViewOpenEditPop popListener) {
        super(widget, parentLayout, context,popListener);
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initListener() {
        super.initListener();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    public void getEditColorCallBack(int tag, int color) {

    }
}
