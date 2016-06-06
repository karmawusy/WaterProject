package cn.wsy.water.viewAction;

import android.content.Context;
import android.view.View;

import cn.wsy.water.interfaces.ViewOpenEditPop;
import cn.wsy.water.views.SeekBarView;

/**
 * 进度条
 * Created by Wusy on 2016/5/11.
 */
public class SeekBarPopAction extends BasePopAction<SeekBarView> {

    public SeekBarPopAction(SeekBarView widget, View parentLayout, Context context,ViewOpenEditPop popListener) {
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
    public void getEditColorCallBack(int tag, int color) {

    }
}
