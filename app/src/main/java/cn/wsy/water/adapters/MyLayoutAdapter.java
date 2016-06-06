package cn.wsy.water.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.wsy.water.EditFragment;
import cn.wsy.water.MainActivity;
import cn.wsy.water.R;
import cn.wsy.water.app.ViewApplication;
import cn.wsy.water.base.BaseView;
import cn.wsy.water.db.LayoutIndexBean;
import cn.wsy.water.db.ViewDB;

/**
 * Created by Wusy on 2016/4/30.
 */
public class MyLayoutAdapter extends BaseAdapter {

    List<LayoutIndexBean> layoutIDs = new ArrayList<>();
    Context context;

    private ViewDB viewDB;

    public MyLayoutAdapter(List<LayoutIndexBean> layoutIDs, Context context) {
        this.layoutIDs = layoutIDs;
        this.context = context;
        viewDB = ViewDB.getInstance(context);
    }

    @Override
    public int getCount() {
        return layoutIDs.size();
    }

    @Override
    public Object getItem(int position) {
        return layoutIDs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View paramView, ViewGroup parent) {

        ViewHolder holder = null;
        if (paramView == null) {
            paramView = LayoutInflater.from(this.context).inflate(R.layout.my_layout_list_item, parent, false);
            holder = new ViewHolder(paramView);
            paramView.setTag(holder);
        } else {
            holder = (ViewHolder) paramView.getTag();
        }

        final LayoutIndexBean bean = layoutIDs.get(position);
        holder.layoutName.setText(bean.getLayout_name());
        holder.createTime.setText(bean.getCreateTime());


        holder.deleteBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                showDeleteDialog(bean);
            }
        });

        holder.editBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                //缓存中读取layout
                ViewApplication.getInstance().readView(bean.getLayout_id());

                ((Activity)context).finish();

                MainActivity.instance.showAgainEditLayout();
            }
        });

        return paramView;
    }

    private void showDeleteDialog(final LayoutIndexBean bean){
        AlertDialog dialog = new AlertDialog.Builder(context).setTitle("您確定刪除這個佈局?").setIcon(
                android.R.drawable.ic_dialog_info).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewDB.deleteViewConfig
                        (bean.getLayout_id());

                ViewApplication.getInstance().readDataBaseForView();

                layoutIDs = ViewApplication.getInstance().getLayoutIDs();

                notifyDataSetChanged();
            }
        })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    class ViewHolder {
        public TextView layoutName;
        public TextView createTime;

        public ImageButton editBtn;
        public ImageButton deleteBtn;

        public ViewHolder(View view) {
            layoutName = (TextView) view.findViewById(R.id.my_layout_item_name);
            createTime = (TextView) view.findViewById(R.id.my_layout_item_time);
            deleteBtn = (ImageButton) view.findViewById(R.id.my_layout_item_delete);
            editBtn = (ImageButton) view.findViewById(R.id.my_layout_item_edit);
        }
    }

}
