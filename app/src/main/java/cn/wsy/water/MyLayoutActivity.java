package cn.wsy.water;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.wsy.water.adapters.MyLayoutAdapter;
import cn.wsy.water.app.ViewApplication;
import cn.wsy.water.db.LayoutIndexBean;

public class MyLayoutActivity extends Activity {

    private TextView noLayoutTip;
    private ListView listView;
    private ImageButton backBtn;


    List<LayoutIndexBean> layoutIDs = new ArrayList<>();
    private MyLayoutAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_layout);
        initView();
    }

    private void initView(){
        noLayoutTip = (TextView) findViewById(R.id.mylayout_noview_tip);
        listView = (ListView) findViewById(R.id.lv_mylayout_list);
        backBtn = (ImageButton) findViewById(R.id.my_layout_back);
        layoutIDs = ViewApplication.getInstance().getLayoutIDs();

        adapter = new MyLayoutAdapter(layoutIDs,this);

        listView.setAdapter(adapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ViewApplication.getInstance().readView(layoutIDs.get(position).getLayout_id());

                finish();

                MainActivity.instance.showMyLayout();
            }
        });
    }


}
