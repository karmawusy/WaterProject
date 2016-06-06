package cn.wsy.water.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import cn.wsy.water.R;


public class KnobView extends LinearLayout {

    public static final int GRAD_TYPE5 = 5;
    public static final int KNOB_TYPE5 = 5;



    LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View view = inflater.inflate(R.layout.activity_knob, this, true);

    private RelativeLayout Layout_BG = (RelativeLayout) view.findViewById(R.id.Layout_BG);

    private ImageView IV_Knob = (ImageView) view.findViewById(R.id.IV_Knob);

    private Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(), R.drawable.knob_0);

    private int soundID;
    private boolean loaded = false;
    private int old_Value = 0;
    private int current_Value = 0;
    private int total_Count = 10;
    /*
     * Absolutely HP is right side
     * CW is + , CCW is -
    */
    private float start_Angle = 90f;
    private float low_Angle = 120f;
    private float high_Angle = 420f;
    private float angle = low_Angle - start_Angle;


    private KnobListener listener;

    public interface KnobListener {
        public void onKnobChanged(int position);

        public void onKnobChangedComplete(int position);
    }

    public void setKnobListener(KnobListener l) {
        listener = l;
    }

    public KnobView(Context context) {
        super(context);

    }

    public KnobView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);  // Make onDraw occure
        initialize();

    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = Layout_BG.getWidth();
        int height = Layout_BG.getHeight();

        Log.v("Width", String.valueOf(width));
        Log.v("Height", String.valueOf(height));

		/*
		if(width>0 && height>0)
		{
			if(width > height)
			{
				Layout_BG.setLayoutParams(new android.widget.LinearLayout.LayoutParams(height,height));
			}
			else
			{
				Layout_BG.setLayoutParams(new android.widget.LinearLayout.LayoutParams(width,width));
			}
		}*/

        drawKnob();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Layout_BG.setBackgroundResource(R.drawable.grad_0);
        IV_Knob.setImageResource(R.drawable.knob_0);
        BitmapFactory.decodeResource(getResources(), R.drawable.knob_0);
        setMax(15);
        drawKnob();
    }


    public boolean setValue(int value) {
        if (value < total_Count) {
            current_Value = value;
            if (value == total_Count - 1)
                angle = high_Angle - start_Angle;
            else
                angle = ((current_Value * (high_Angle - low_Angle)) / total_Count) + low_Angle - start_Angle;

            drawKnob();

            return true;
        } else
            return false;
    }

    public int getValue() {
        return current_Value;
    }

    public void setMax(int max_Value) {
        total_Count = max_Value + 1;
    }


    // Class routine =================================================================
    public void initialize() {
        IV_Knob.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int action = event.getAction();
                int actionCode = action & MotionEvent.ACTION_MASK;

                if (actionCode == MotionEvent.ACTION_POINTER_DOWN) {
                    float x = event.getX(0);
                    float y = event.getY(0);

                    float theta = (getTheta(x, y) < 90 ? getTheta(x, y) + 360 : getTheta(x, y));
                    current_Value = (int) (total_Count * ((angle + start_Angle) - low_Angle) / (high_Angle - low_Angle));
                } else if (actionCode == MotionEvent.ACTION_MOVE) {
                    float x = event.getX(0);
                    float y = event.getY(0);

                    float theta = (getTheta(x, y) < 90 ? getTheta(x, y) + 360 : getTheta(x, y));
                    current_Value = (int) (total_Count * ((angle + start_Angle) - low_Angle) / (high_Angle - low_Angle));

                    if ((theta - start_Angle) > (low_Angle - start_Angle) && (theta - start_Angle) < (high_Angle - start_Angle)) {
                        angle = theta - start_Angle;
                        if (null != listener)
                            listener.onKnobChanged(current_Value);
                    }

                    if (loaded && old_Value != current_Value) {

                        old_Value = current_Value;
                    }

                    drawKnob();

                } else if (actionCode == MotionEvent.ACTION_UP) {
                    if (null != listener)
                        listener.onKnobChangedComplete(current_Value);
                }

                return true;
            }
        });
    }


    // Sub routine ===================================================================
    private float getTheta(float x, float y) {
        float sx = x - (IV_Knob.getWidth() / 2.0f);
        float sy = y - (IV_Knob.getHeight() / 2.0f);

        float length = (float) Math.sqrt(sx * sx + sy * sy);
        float nx = sx / length;
        float ny = sy / length;
        float theta = (float) Math.atan2(ny, nx);

        final float rad2deg = (float) (180.0 / Math.PI);
        float theta2 = theta * rad2deg;

        return (theta2 < 0) ? theta2 + 360.0f : theta2;
    }

    private void drawKnob() {
        Matrix matrix = new Matrix();

        float scale_Width = (float) IV_Knob.getWidth() / (float) bitmapOrg.getWidth();
        float scale_Height = (float) IV_Knob.getHeight() / (float) bitmapOrg.getHeight();


        matrix.postScale(scale_Width, scale_Height);
        matrix.postRotate((float) angle, IV_Knob.getWidth() / 2, IV_Knob.getHeight() / 2);

        IV_Knob.setScaleType(ScaleType.MATRIX);
        IV_Knob.setImageMatrix(matrix);
    }


}
