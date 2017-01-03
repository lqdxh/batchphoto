package com.lqd.camera;

import java.io.File;
import java.util.List;

import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater layoutInflater;
	private List<Person> personList;
	public int curSelect = 0;
	
	public List<Person> getPersonList() {
		return personList;
	}

	public int getCurSelect() {
		return curSelect;
	}

	public void setCurSelect(int curSelect) {
		this.curSelect = curSelect;
	}

	public MyAdapter(Context context, LayoutInflater layoutInflater,
			List<Person> personList) {
		super();
		this.context = context;
		this.layoutInflater = layoutInflater;
		this.personList = personList;
	}

	@Override
	public int getCount() {
		return personList.size();
	}

	@Override
	public Object getItem(int position) {
//		if (position>=0 && position<getCount()){
			return personList.get(position);
//		}else{
//			return null;
//		}
	}
	

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
//		if (personList.size()==0){
//			return null;
//		}
		if (convertView == null){
			convertView = layoutInflater.inflate(R.layout.vlist, null);
		}
		ImageView img = (ImageView)convertView.findViewById(R.id.lv_img);
		TextView name = (TextView)convertView.findViewById(R.id.lv_name);
		
		String photoFileName = personList.get(position).getPhotoName();
		File file = new File(((MainActivity) MainActivity.mainActivity).getPath() + '/' +( (MainActivity) MainActivity.mainActivity).getSelectFile()+ '/' +photoFileName + ".jpg");
		if (file.exists()){
			img.setImageResource(R.drawable.touxiang);
		}else{
			img.setImageResource(R.drawable.touxiang0);
		}
		name.setText(personList.get(position).getName());
		View layout  = convertView.findViewById(R.id.lv_layout);
		if (curSelect == position){
			layout.setBackgroundResource(R.color.listselected);
		}else{
			layout.setBackgroundResource(color.background_light);
		}
		return convertView;
	}

}
