package cn.wsy.water.viewAction;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import cn.wsy.water.R;
import cn.wsy.water.interfaces.EditColorCallBack;
import cn.wsy.water.interfaces.ViewOpenEditPop;
import cn.wsy.water.views.EditColorPopView;

/**
 * Created by Wusy on 2016/5/11.
 */
public abstract class BasePopAction<T> implements View.OnClickListener, EditColorCallBack {

    private T widget;//行为操作对象
    private View parentLayout;//这是pop布局容器
    private Context context;

    private ImageButton delteBtn;
    private ImageButton confirmBtn;

    private ViewOpenEditPop popListener;



    public BasePopAction(T widget, View parentLayout, Context context,ViewOpenEditPop popListener) {
        this.widget = widget;
        this.parentLayout = parentLayout;
        this.context = context;
        this.popListener=popListener;
        initView();
        initListener();
    }


    public void initView() {
        delteBtn = (ImageButton) this.parentLayout.findViewById(R.id.delete_btn);
        confirmBtn = (ImageButton) this.parentLayout.findViewById(R.id.confirm_btn);
    }

    public void initListener() {
        if (delteBtn != null) {
            delteBtn.setOnClickListener(this);
        }

        if (confirmBtn != null) {
            confirmBtn.setOnClickListener(this);
        }
    }

    public View getViewById(int viewId) {
        return this.parentLayout.findViewById(viewId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete_btn:
                EditColorPopView popView = new EditColorPopView(this.context);
                ColorDrawable colorDrawable = (ColorDrawable) delteBtn.getBackground();
                popView.setCallBack(this, 100);
                popView.show(colorDrawable.getColor());
                break;

            case R.id.confirm_btn:
                //Toast.makeText(context, "点击确认按钮！", Toast.LENGTH_SHORT).show();
                //窗口消失
               popListener.dimissPopWindow();
                break;
        }
    }

    @Override
    public void callBackGetColor(int tag, int color) {
        getEditColorCallBack(tag, color);
        Toast.makeText(context, "tag is " + Integer.toString(tag) + "color is " + Integer.toHexString(color), Toast.LENGTH_SHORT).show();
    }

    public abstract void getEditColorCallBack(int tag, int color);
}
