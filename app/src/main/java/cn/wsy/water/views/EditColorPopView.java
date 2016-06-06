package cn.wsy.water.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import cn.wsy.water.R;
import cn.wsy.water.interfaces.EditColorCallBack;
import cn.wsy.water.utils.MathUtils;

/**
 * 色彩调整 pop
 * Created by Wusy on 2016/5/13.
 */
public class EditColorPopView extends PopupWindow implements SeekBar.OnSeekBarChangeListener{

    private Context context;
    private View view;

    private SeekBar colorSeekBarR,colorSeekBarG,colorSeekBarB;
    private TextView colorTextVauleR,colorTextVauleG,colorTextVauleB;
    private TextView showView,showText;
    private ImageButton confirmBtn;
    private int valueR,valueG,valueB;
    private int currentColor;

    private EditColorCallBack callBack;
    private int tag = -1;//为了区分

    public EditColorPopView(Context context) {
        super(context);
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.edit_color_pop_layout, null);
        initView(context);
    }

    public void show(int color){
        this.currentColor = color;
        showView.setBackgroundColor(color);
        showText.setText(Integer.toHexString(color));

        valueR = Color.red(color);
        valueG = Color.green(color);
        valueB = Color.blue(color);

        colorSeekBarB.setProgress(valueB);
        colorSeekBarG.setProgress(valueG);
        colorSeekBarR.setProgress(valueR);

        this.showAtLocation(LayoutInflater.from(context).inflate(R.layout.fragment_edit, null), Gravity.RIGHT, 0, 0);
    }

    public View getView() {
        return view;
    }

    public void setCallBack(EditColorCallBack callBack,int tag) {
        this.callBack = callBack;
        this.tag = tag;
    }

    private void initView(final Context context) {
        this.setContentView(view);
        final RelativeLayout popView = (RelativeLayout) view.findViewById(R.id.color_pop_view);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(MathUtils.getScreeWidth(this.context) / 2, RelativeLayout.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        popView.setLayoutParams(params);

        colorSeekBarB = (SeekBar) view.findViewById(R.id.color_b_seekbar);
        colorSeekBarR = (SeekBar) view.findViewById(R.id.color_r_seekbar);
        colorSeekBarG = (SeekBar) view.findViewById(R.id.color_g_seekbar);

        colorTextVauleR = (TextView) view.findViewById(R.id.color_r_value);
        colorTextVauleG = (TextView) view.findViewById(R.id.color_g_value);
        colorTextVauleB = (TextView) view.findViewById(R.id.color_b_value);

        showView = (TextView) view.findViewById(R.id.color_show_view);
        showText = (TextView) view.findViewById(R.id.color_show_text);

        confirmBtn = (ImageButton) view.findViewById(R.id.color_confirm_btn);

        initListener();

        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
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

    private void initListener(){

        colorSeekBarB.setOnSeekBarChangeListener(this);
        colorSeekBarG.setOnSeekBarChangeListener(this);
        colorSeekBarR.setOnSeekBarChangeListener(this);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack !=null){
                    callBack.callBackGetColor(tag,currentColor);
                }
                dismiss();
            }
        });
    }

    private void changeColorForView(){
        currentColor = Color.rgb(valueR, valueG, valueB);
        showText.setText(Integer.toHexString(currentColor));
        showView.setBackgroundColor(currentColor);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == colorSeekBarB){
            colorTextVauleB.setText(String.valueOf(progress));
            valueB = progress;
            changeColorForView();
        }
        if (seekBar == colorSeekBarG){
            colorTextVauleG.setText(String.valueOf(progress));
            valueG = progress;
            changeColorForView();
        }
        if (seekBar == colorSeekBarR){
            colorTextVauleR.setText(String.valueOf(progress));
            valueR = progress;
            changeColorForView();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
