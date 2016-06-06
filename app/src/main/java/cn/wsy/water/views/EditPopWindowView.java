package cn.wsy.water.views;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import cn.wsy.water.R;
import cn.wsy.water.utils.MathUtils;

/**
 * pop
 * Created by Wusy on 2016/5/10.
 */
public class EditPopWindowView extends PopupWindow {

    private Context context;
    private View view;

    public EditPopWindowView(Context context,int layoutID) {
        super(context);
        this.context = context;
        view = LayoutInflater.from(context).inflate(layoutID, null);
        initView(context);
    }

    public EditPopWindowView(Context context) {
        super(context);
    }

    public void show(){


    }

    public View getView() {
        return view;
    }

    private void initView(final Context context) {
        this.setContentView(view);
        final RelativeLayout popView = (RelativeLayout) view.findViewById(R.id.edit_pop_layout);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(MathUtils.getScreeWidth(this.context) / 2, RelativeLayout.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        popView.setLayoutParams(params);

        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                int left = popView.getLeft();
                int right = popView.getRight();
                int bottom = popView.getBottom();
                int y = (int) event.getY();
                int x = (int) event.getX();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (left > x) {
                        dismiss();
                    }
                }
                return true;
            }
        });

    }
}
