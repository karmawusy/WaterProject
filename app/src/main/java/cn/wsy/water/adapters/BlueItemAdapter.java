package cn.wsy.water.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.wsy.water.R;
import cn.wsy.water.app.BlueDevice;

/**
 * Created by Kevin on 2016/6/3.
 * Email：1026karma@gmail.com
 * Github：karmalove
 */
public class BlueItemAdapter extends BaseAdapter {
    private List<BlueDevice> listDatas;
    private LayoutInflater mInflater;
    private BlueDevice mBlueDevice;

    public BlueItemAdapter(Context context, List<BlueDevice> listDatas) {
        mInflater = LayoutInflater.from(context);
        this.listDatas = listDatas;
    }

    @Override
    public int getCount() {
        return listDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return listDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.bluetooth_item, null);
            viewHolder.device_name = (TextView) convertView.findViewById(R.id.devicelist_name);
            viewHolder.device_address = (TextView) convertView.findViewById(R.id.devicelist_address);
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.connect = (TextView) convertView.findViewById(R.id.connect);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        mBlueDevice=listDatas.get(position);
        viewHolder.device_name.setText(mBlueDevice.getDevice_name());
        viewHolder.device_address.setText("("+mBlueDevice.getDevice_address()+")");
        viewHolder.connect.setText(mBlueDevice.getConnect());
        Log.i("bluetooth", "數據----》" + mBlueDevice.getDevice_name());
        return convertView;
    }

    class ViewHolder {
        TextView device_name;
        TextView device_address;
        TextView connect;
        ImageView mImageView;
    }
}
