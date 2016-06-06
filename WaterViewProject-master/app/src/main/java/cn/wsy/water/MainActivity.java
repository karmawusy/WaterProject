package cn.wsy.water;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.wsy.water.views.MyListview;


public class MainActivity extends Activity {



    private MyListview leftListview;
    private MyListview opreationLv;
    private DrawerLayout drawerLayout;
    private ImageView drawerTogglerBtn;
    private ScrollView slDrawerLayout;
    private Button saveBtn;
    private Button deleteBtn;
    private PopupWindow mPopupWindow;
    private View popupWindowView;

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private MainFragment mainFragment;
    private EditFragment editFragment;
    private AgainEditFragment againEditFragment;

    private TextView titleTv;

    public static MainActivity instance;

    private int fragmentTAG = -1;//0 MAIN 1 EDIT 2 AGINEDIT 用于标识不同的fragment时的区分

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 1000) {

                fragmentTAG = 1;

                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_contaner, editFragment);
                transaction.commit();

                showSaveBtn();

            } else if (msg.what == 1001) {

                fragmentTAG = 2;

                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_contaner, againEditFragment);
                transaction.commit();

                showSaveBtn();

            } else if (msg.what == 1002) {

                fragmentTAG = 0;

                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_contaner, mainFragment);
                transaction.commit();

                hidDeleteBtn();
                hidSaveBtn();
            }

        }
    };


    //test
    private String[] views = new String[]{
            "摇杆", "方向盤", "滑杆", "按鈕", "開關"
            , "三軸加速度傳感器", "量程表", "文本顯示框"
    };

    private String[] opreations = new String[]{
            "創建佈局", "我的佈局"
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //实现全屏效果
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        initView();
        initFragment();
        initListener();
    }

    private void initView() {
        instance = this;
        leftListview = (MyListview) findViewById(R.id.listview_views);
        opreationLv = (MyListview) findViewById(R.id.listview_action);
        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer);
        drawerTogglerBtn = (ImageView) findViewById(R.id.main_drawer_btn);
        slDrawerLayout = (ScrollView) findViewById(R.id.sl_drawer_layout);
        saveBtn = (Button) findViewById(R.id.btn_layout_save);
        deleteBtn = (Button) findViewById(R.id.btn_layout_delete);
        titleTv = (TextView) findViewById(R.id.main_title);
        leftListview.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, views));

        opreationLv.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, opreations));

        leftListview.setEnabled(false);
//
    }

    private void initFragment() {
        mainFragment = new MainFragment();
        editFragment = new EditFragment();
        againEditFragment = new AgainEditFragment();

        fragmentManager = getFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_contaner, mainFragment);
        transaction.commit();
    }

    private void initListener() {
        drawerTogglerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer();
            }
        });

        leftListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                closeDrawer();
                if (!leftListview.isEnabled()) {
                    Toast.makeText(MainActivity.this, "請先創建佈局！", Toast.LENGTH_SHORT).show();
                    return;
                }
                switch (position) {
                    case 0://摇杆

                        EditFragment.instance.createRockerView();
//                        initPopupWindo∂∂w();
                        break;
                    case 1://方向盤
                        EditFragment.instance.createWeelView();
//                        initPopupWindow();
                        break;
                    case 2://滑杆
                        EditFragment.instance.createSeekBar();
//                        initPopupWindow();
                        break;
                    case 3://按鈕
                        EditFragment.instance.createCircleButton();
//                        initPopupWindow();
                        break;
                    case 4://開關
                        EditFragment.instance.createSwitchView();
//                        initPopupWindow();
                        break;
                    case 5://三軸加速度傳感器
                        EditFragment.instance.createThreeView();
//                        initPopupWindow();
                        break;
                    case 6://量程表
                        EditFragment.instance.createKnobView();
//                        initPopupWindow();
                        break;
                    case 7://文本顯示框
                        EditFragment.instance.createOutputView();
//                        initPopupWindow();
                        break;
                }
            }
        });

        opreationLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                closeDrawer();
                if (position == 0) {
                    leftListview.setEnabled(true);
                    showSaveBtn();
                    showEditLayout();
                } else {
                    startActivity(new Intent(MainActivity.this, MyLayoutActivity.class));
                }

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentTAG == 1) {
                    editFragment.deleteCurrentView();
                } else if (fragmentTAG == 2) {
                    againEditFragment.deleteCurrentView();
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("wusy", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("wusy", "onStop");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("wusy", "onDestroy");
    }

    private void openDrawer() {
        drawerLayout.openDrawer(slDrawerLayout);
    }

    private void closeDrawer() {
        drawerLayout.closeDrawer(slDrawerLayout);
    }

    public void showSaveBtn() {
        saveBtn.setVisibility(View.VISIBLE);
    }

    public void hidSaveBtn() {
        saveBtn.setVisibility(View.GONE);
    }

    public void showDeleteBtn() {
        deleteBtn.setVisibility(View.VISIBLE);
    }

    public void hidDeleteBtn() {
        deleteBtn.setVisibility(View.GONE);
    }

    public void setTitle(String title){
        titleTv.setText(title);
    }

    /**
     * 显示mainfragmemt  延时
     */
    public void showMyLayout() {
        Message msg = new Message();
        msg.what = 1002;
        handler.sendMessageDelayed(msg, 200);
    }

    /**
     * 显示editfragmemt  延时
     */
    public void showEditLayout() {
        Message msg = new Message();
        msg.what = 1000;
        handler.sendMessageDelayed(msg, 200);
    }

    /**
     * 显示agaginEditfragmemt  延时
     */
    public void showAgainEditLayout() {
        Message msg = new Message();
        msg.what = 1001;
        handler.sendMessageDelayed(msg, 200);
    }

    public void showDialog() {
        final EditText inputName = new EditText(this);

        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("請輸入佈局名稱").setIcon(
                android.R.drawable.ic_dialog_info).setView(
                inputName).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日    HH:mm:ss     ");
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                String time = formatter.format(curDate);

                String name = inputName.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(MainActivity.this, "輸入名稱不能為空！！", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    if (fragmentTAG == 1) {
                        editFragment.saveAllViewsToDB(name, time);
                    } else if (fragmentTAG == 2) {
                        againEditFragment.saveAllViewsToDB(name, time);
                    }
                    dialog.dismiss();
                }
            }
        })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();


    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }



    class popupDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mPopupWindow != null && mPopupWindow.isShowing()) {

            mPopupWindow.dismiss();

            mPopupWindow = null;

        }
        return super.onTouchEvent(event);
    }
}



