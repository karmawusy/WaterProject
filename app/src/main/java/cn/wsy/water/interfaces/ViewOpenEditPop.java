package cn.wsy.water.interfaces;

import android.view.View;

/**
 * 每个组件如果监听了这个接口，默认可以打开pop
 * Created by Wusy on 2016/5/10.
 */
public interface ViewOpenEditPop {

    /**
     * 注入是控件类型 与 控件操作对象
     * @param type
     * @param widget 组件
     */
    public void showEditPopWindow(int type,View widget,int layoutID);

    public void dimissPopWindow();

}
