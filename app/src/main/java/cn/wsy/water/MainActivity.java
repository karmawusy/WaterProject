package cn.wsy.water;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cn.wsy.water.adapters.BlueItemAdapter;
import cn.wsy.water.adapters.MyLayoutAdapter;
import cn.wsy.water.app.BlueDevice;
import cn.wsy.water.app.ViewApplication;
import cn.wsy.water.db.LayoutIndexBean;
import cn.wsy.water.views.MyListview;


public class MainActivity extends Activity implements SensorEventListener, AdapterView.OnItemClickListener {


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

    private EditText titleTv;

    public static MainActivity instance;

    private int fragmentTAG = -1;//0 MAIN 1 EDIT 2 AGINEDIT 用于标识不同的fragment时的区分

    //傳感器
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private Bundle bundle;

    //藍牙
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice btd;
    private TextView change_device;
    private boolean hasregister = false;
    private List<BlueDevice> deviceList;
    private TextView btserch;
    private DeviceReceiver mydevice = new DeviceReceiver();
    private ListView devicelistView;
    private BlueItemAdapter adapter;
    private BlueDevice mBlueDevice;
    private TextView device_name;
    private TextView device_address;

    //mylayout
    private MyListview layoutListview;
    List<LayoutIndexBean> layoutIDs = new ArrayList<>();
    private MyLayoutAdapter layoutAdapter;


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 1000) {

                fragmentTAG = 1;

                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_contaner, editFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                titleTv.setEnabled(false);
                showSaveBtn();

            } else if (msg.what == 1001) {

                fragmentTAG = 2;

                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_contaner, againEditFragment);
                transaction.commit();
                leftListview.setEnabled(true);
                titleTv.setEnabled(true);
                showSaveBtn();

            } else if (msg.what == 1002) {

                fragmentTAG = 0;

                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_contaner, mainFragment);
                transaction.commit();
                leftListview.setEnabled(false);
                titleTv.setEnabled(false);
                hidDeleteBtn();
                hidSaveBtn();
            } else if (msg.what == 1) {
                editFragment.updateData(bundle);
            } else if (msg.what == 2) {
                mainFragment.updateMainData(bundle);
            } else if (msg.what == 3) {
                againEditFragment.updateAgainData(bundle);
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
        Log.i("bluetooth", "-----1");

        //实现全屏效果
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        bundle = new Bundle();
        popupWindowView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bluetootn_list, null, false);
        devicelistView = (ListView) popupWindowView.findViewById(R.id.bluetooth_device);
        btserch = (TextView) popupWindowView.findViewById(R.id.start_seach);
        mBlueDevice = new BlueDevice();
        mBlueDevice.setConnect("Available");
        deviceList = new ArrayList<BlueDevice>();
        adapter = new BlueItemAdapter(getApplicationContext(), deviceList);
        devicelistView.setAdapter(adapter);
        btserch.setOnClickListener(new ClinckMonitor());
        devicelistView.setOnItemClickListener(this);


        initView();
        initFragment();
        initListener();
        setBluetooth();

        change_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPopupWindow = new PopupWindow(popupWindowView, LinearLayout.LayoutParams.WRAP_CONTENT, 700, true);
                mPopupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
                closeDrawer();
                // 点击其他地方消失
                popupWindowView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        // TODO Auto-generated method stub
                        if (mPopupWindow != null && mPopupWindow.isShowing()) {
                            mPopupWindow.dismiss();
                            mPopupWindow = null;
                        }
                        return false;
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        Log.i("bluetooth", "-----2");
        //注册蓝牙接收广播
        if (!hasregister) {
            hasregister = true;
            IntentFilter filterStart = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            IntentFilter filterEnd = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            registerReceiver(mydevice, filterStart);
            registerReceiver(mydevice, filterEnd);
        }
        super.onStart();
    }


    private void setBluetooth() {
        Log.i("bluetooth", "-----1--1");
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null) {
            //請求用戶開啟藍牙
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, RESULT_FIRST_USER);
            //設置設備為可見狀態
            Intent in = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            in.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 200);
            //直接開啟
            mBluetoothAdapter.enable();
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("No bluetooth devices");
            dialog.setMessage("Your equipment does not support bluetooth, please change device");
            dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.show();
        }

    }

    /**
     * Finding Devices
     */
    private void findAvalibleDevice() {
        Log.i("bluetooth", "-----3");
        //获取可配对蓝牙设备
        Set<BluetoothDevice> device = mBluetoothAdapter.getBondedDevices();

        if (mBluetoothAdapter != null && mBluetoothAdapter.isDiscovering()) {
            deviceList.clear();
            adapter.notifyDataSetChanged();
        }
        if (device.size() > 0) { //存在已经配对过的蓝牙设备
            for (Iterator<BluetoothDevice> it = device.iterator(); it.hasNext(); ) {
                BluetoothDevice btd = it.next();
                mBlueDevice.setDevice_name(btd.getName());
                mBlueDevice.setDevice_address(btd.getAddress());
                deviceList.add(mBlueDevice);
                adapter.notifyDataSetChanged();
            }
        } else {  //不存在已经配对过的蓝牙设备
            //deviceList.add("No can be matched to use bluetooth");
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (resultCode) {
            case RESULT_OK:
                findAvalibleDevice();
                break;
            case RESULT_CANCELED:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class ClinckMonitor implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (mBluetoothAdapter.isDiscovering()) {
//                mBluetoothAdapter.cancelDiscovery();
//                btserch.setText("find");
//                btserch.setBackgroundColor(getResources().getColor(R.color.green));
            } else {
                findAvalibleDevice();
                mBluetoothAdapter.startDiscovery();
                btserch.setText("Finding....");
                btserch.setBackgroundColor(getResources().getColor(R.color.grey));
            }
        }
    }

    /**
     * 蓝牙搜索状态广播监听
     *
     * @author Andy
     */
    private class DeviceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {    //搜索到新设备
                btd = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //搜索没有配过对的蓝牙设备
                if (btd.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mBlueDevice.setDevice_name(btd.getName());
                    mBlueDevice.setDevice_address(btd.getAddress());
                    deviceList.add(mBlueDevice);
                    //  Log.i("bluetooth", "數據----》" +mBlueDevice.getDevice_name());
                    adapter.notifyDataSetChanged();
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {   //搜索结束

                if (devicelistView.getCount() == 0) {
                    //deviceList.add("No can be matched to use bluetooth");
                    adapter.notifyDataSetChanged();
                }
                btserch.setText("Find");
                btserch.setBackgroundColor(getResources().getColor(R.color.green));
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {

        Log.e("msgParent", "Parent= " + arg0);
        Log.e("msgView", "View= " + arg1);
        Log.e("msgChildView", "ChildView= " + arg0.getChildAt(pos - arg0.getFirstVisiblePosition()));

        //final String msg = mBlueDevice.setMsg(deviceList.get(pos));
        mBlueDevice = deviceList.get(pos);
        String msg = null;
        Field[] fields = mBlueDevice.getClass().getFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);//忽略访问权限，私有的也可以访问
            msg = field.getName();
        }


        if (mBluetoothAdapter != null && mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
            btserch.setText("Find");
            mBlueDevice.setConnect("Connecting....");
            btserch.setBackgroundColor(getResources().getColor(R.color.green));
        }

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);// 定义一个弹出框对象
        dialog.setTitle("Confirmed connecting device");
        dialog.setMessage(msg);
        dialog.setPositiveButton("connect",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       /* BluetoothMsg.BlueToothAddress=msg.substring(msg.length()-17);

                        if(BluetoothMsg.lastblueToothAddress!=BluetoothMsg.BlueToothAddress){
                            BluetoothMsg.lastblueToothAddress=BluetoothMsg.BlueToothAddress;
                        }

                        Intent in=new Intent(SearchDeviceActivity.this,BluetoothActivity.class);
                        startActivity(in);*/


                    }
                });
        dialog.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       /* BluetoothMsg.BlueToothAddress = null;*/
                    }
                });
        dialog.show();
    }


    private void initView() {
        instance = this;
        leftListview = (MyListview) findViewById(R.id.listview_views);
        layoutListview = (MyListview) findViewById(R.id.listview_layout);
        opreationLv = (MyListview) findViewById(R.id.listview_action);
        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer);
        drawerTogglerBtn = (ImageView) findViewById(R.id.main_drawer_btn);
        slDrawerLayout = (ScrollView) findViewById(R.id.sl_drawer_layout);
        saveBtn = (Button) findViewById(R.id.btn_layout_save);
        deleteBtn = (Button) findViewById(R.id.btn_layout_delete);
        titleTv = (EditText) findViewById(R.id.main_title);
        titleTv.setEnabled(false);
        change_device = (TextView) findViewById(R.id.tv_change_deivice);
        device_name = (TextView) findViewById(R.id.device_name);
        device_address = (TextView) findViewById(R.id.device_address);
        //btserch.setOnClickListener(new ClinckMonitor());
        leftListview.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, views));

        opreationLv.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, opreations));


        layoutIDs = ViewApplication.getInstance().getLayoutIDs();

        layoutAdapter = new MyLayoutAdapter(layoutIDs, this, false);

        layoutListview.setAdapter(layoutAdapter);

    }

    private void initFragment() {
        editFragment = new EditFragment();
        editFragment.setHandler(handler);

        mainFragment = new MainFragment();
        mainFragment.setMainHandler(handler);

        againEditFragment = new AgainEditFragment();
        againEditFragment.setAgainHandler(handler);

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

        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                ViewApplication.getInstance().readDataBaseForView();
                layoutIDs = ViewApplication.getInstance().getLayoutIDs();
                layoutAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        layoutListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ViewApplication.getInstance().readView(layoutIDs.get(position).getLayout_id());

                MainActivity.instance.showMyLayout();

                closeDrawer();
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
                if (fragmentTAG == 1) {
                    switch (position) {
                        case 0://摇杆
                            EditFragment.instance.createRockerView();
                            break;
                        case 1://方向盤
                            EditFragment.instance.createWeelView();

                            break;
                        case 2://滑杆
                            EditFragment.instance.createSeekBar();

                            break;
                        case 3://按鈕
                            EditFragment.instance.createCircleButton();

                            break;
                        case 4://開關
                            EditFragment.instance.createSwitchView();

                            break;
                        case 5://三軸加速度傳感器
                            EditFragment.instance.createThreeView();

                            break;
                        case 6://量程表
                            EditFragment.instance.createKnobView();

                            break;
                        case 7://文本顯示框
                            EditFragment.instance.createOutputView();

                            break;
                    }
                } else {
                    againEditFragment.createView(position);
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
                if (fragmentTAG == 1) {
                    showDialog();
                } else if (fragmentTAG == 2) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日    HH:mm:ss     ");
                    Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                    String time = formatter.format(curDate);
                    String name = titleTv.getText().toString();
                    againEditFragment.saveAllViewsToDB(name, time);
                }
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
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
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
        mSensorManager.unregisterListener(this);
        if (mBluetoothAdapter != null && mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        if (hasregister) {
            hasregister = false;
            unregisterReceiver(mydevice);
        }
        Log.i("wusy", "onDestroy");
        Log.i("kevin", "生命周期结束");
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

    public void setTitle(String title) {
        titleTv.setText(title);
        if (title.length() > 0){
            titleTv.setSelection(title.length());
        }
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        int x = (int) event.values[0];
        int y = (int) event.values[1];
        int z = (int) event.values[2];

        bundle.putInt("x", x);
        bundle.putInt("y", y);
        bundle.putInt("z", z);

        // Log.i("Senor----->","x"+x+"y"+y+"z"+z);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}



