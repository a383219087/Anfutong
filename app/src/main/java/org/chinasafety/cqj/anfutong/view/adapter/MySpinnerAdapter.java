package org.chinasafety.cqj.anfutong.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.chinasafety.cqj.anfutong.R;
import org.chinasafety.cqj.anfutong.model.IChooseItem;

import java.util.ArrayList;
import java.util.List;

public class MySpinnerAdapter<T extends IChooseItem> extends BaseAdapter {

	private Context context;
	private List<T> data;
	private List<T> copyData;

	public MySpinnerAdapter(List<T> data,
							Context context) {
		this.context = context;
		this.data = data;
		copyData = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public T getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.choose_item, null);
			vHolder = new ViewHolder();
			vHolder.content = (TextView) convertView
					.findViewById(R.id.choose_tv);
			convertView.setTag(vHolder);
		} else {
			vHolder = (ViewHolder) convertView.getTag();
		}
		final T map = data.get(position);
		if (map != null) {
			vHolder.content.setText(map.getItemName());
		}
		return convertView;
	}

	public void filterSb(String csId){
		List<T> sbData = new ArrayList<T>();
		for (T item:data){
			if(item.getCsId()!=null && csId.equals(item.getCsId())){
				sbData.add(item);
			}
		}
		data.clear();
		data.addAll(sbData);
		notifyDataSetChanged();
	}

	public void resetData(){
		data.clear();
		data.addAll(copyData);
		notifyDataSetChanged();
	}

	public int selectItemById(String id){
		int position =-1;
		for (int i=0;i<data.size();i++){
			if(data.get(i).getItemId().equals(id)){
				position =i;
			}
		}
		if(-1==position){
			Toast.makeText(context, "不存在的ID", Toast.LENGTH_SHORT).show();
		}
		return position;
	}

	class ViewHolder {
		TextView content;
	}

}
