package com.lqd.camera;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.SurfaceView;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SetupActivity extends ListActivity {

	private static final int BUFFER_SIZE = 1024;
	private List<Map<String,Object>> data;
	private SharedPreferences xmlSP;
	private SimpleAdapter adapter;
	private List<Map<String,Object>> list ;
	private int width;
	private int height;
	private int photoQuality;
	int activeState;
	String activeSIM;
	String activePhone;
	boolean genBigPhoto;
	boolean hidePhotoed;
	int devType;
	private View view = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 
		 xmlSP = getSharedPreferences("SP", MODE_PRIVATE);
		 list =  new ArrayList<Map<String,Object>>();
		
		data = getData();
		adapter = new SimpleAdapter(this,data,R.layout.setup,
				new String[]{"title","info"},
				new int[]{R.id.setup_titile,R.id.setup_info});
		
		setListAdapter(adapter);
		
		ListView lv = getListView();
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,  
                    int arg2, long arg3) {  

                return false;  
            } 
			
		});
	}
	
	/**
	* 获取版本号
	* @return 当前应用的版本号
	*/
	public String getVersion() {
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			String version = info.versionName;
			return this.getString(R.string.version_name) + version;
		} catch (Exception e) {
			e.printStackTrace();
			return this.getString(R.string.version_name);
		}
	}

	private List<Map<String,Object>> getData() {
		list.clear();
		Map<String,Object> map = new HashMap<String,Object>();
		width = xmlSP.getInt("sizeWidth", ((MainActivity) MainActivity.mainActivity).getPhotoWidth()) ;
		height = xmlSP.getInt("sizeHeight",  ((MainActivity) MainActivity.mainActivity).getPhotoHeight());
		photoQuality = xmlSP.getInt("photoQuality",  ((MainActivity) MainActivity.mainActivity).getPhotoQuality());
		activeState = xmlSP.getInt("activeState",  ((MainActivity) MainActivity.mainActivity).getActiveState());
		activePhone = xmlSP.getString("activePhone",  ((MainActivity) MainActivity.mainActivity).getActivePhone());
		activeSIM = xmlSP.getString("activeSIM",  ((MainActivity) MainActivity.mainActivity).getActiveSIM());
		genBigPhoto = xmlSP.getBoolean("bigPhoto",  ((MainActivity) MainActivity.mainActivity).isGenBigPhoto());
		devType = xmlSP.getInt("devType",  ((MainActivity) MainActivity.mainActivity).getDevType());
		hidePhotoed = MainActivity.getInstance().getHidePhotoed();
		
		map.put("title",  AppResource.savePhotoSizeTitle);
		map.put("info",  String.format(AppResource.savePhotoSizeMsg, ((MainActivity) MainActivity.mainActivity).getMaxWHRate(),  width, height ));
		list.add(map);
		
		map = new HashMap<String,Object>();
		map.put("title", AppResource.savePhotoQualityTitle);
		map.put("info", String.format("%s:%d",AppResource.savePhotoQualityMsg,  photoQuality));
		list.add(map);
		
		map = new HashMap<String,Object>();
		map.put("title",  AppResource.saveBigPhotoTitle);
		map.put("info", String.format("%s:%s", AppResource.saveBigPhotoMsg,(genBigPhoto?AppResource.Yes:AppResource.No)));
		list.add(map);
		
		map = new HashMap<String,Object>();
		map.put("title",  AppResource.hidePhotoedTitle);
		map.put("info", String.format("%s:%s", AppResource.hidePhotoedMsg,(hidePhotoed?AppResource.Yes:AppResource.No)));
		list.add(map);
		
		map = new HashMap<String,Object>();
		map.put("title",  AppResource.devTypeTitle);
		map.put("info", String.format("%s:%s", AppResource.devType,(devType==1?AppResource.devPhone:AppResource.devTablet)));
		list.add(map);
		
		map = new HashMap<String,Object>();
		map.put("title",  AppResource.appValidTitle);
		map.put("info", String.format("%s:%s  %s",AppResource.appValidMsg, activePhone ,(ValidUtils.valid()?AppResource.appValided:AppResource.appInvalided)));
		list.add(map);
		
		map = new HashMap<String,Object>();
		map.put("title",  AppResource.helpTitle + "(" + getVersion() + ")");
		map.put("info", AppResource.helpMsg);
		list.add(map);
		
		map = new HashMap<String,Object>();
		map.put("title",  AppResource.suguestTitle);
		map.put("info", AppResource.suguestMsg);
		list.add(map);
		
		return list;
	}
	
	private boolean networkCheck(){
		ConnectivityManager manager = (ConnectivityManager) MainActivity.mainActivity.getSystemService(MainActivity.mainActivity.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo(); 
		if (info != null && info.isAvailable()){
			return true;
		}else{
			return false;
		}
		
	}
	
	
	private void refreshData(){
		data = getData();
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		//图像尺寸设置
		if (position == 0) {
			_setPhotoSize();
		}else if (position == 1){
			//图像质量设置
			_setPhotoQuality();
		}else if (position == 2){
			//是否保存大图
			_setSaveBigPhoto();
		}else if (position == 3){
			//是否隐藏已拍对象
			_setHidePhotoed();
		}else if (position == 4){
			//设备选择
			_setDevType();
		}else if (position == 5){
			//软件激活
			if (!networkCheck()){
				Toast.makeText(this, AppResource.noConnectTis, Toast.LENGTH_LONG).show();
			}else{
				_validSoft();
			}
		}else if (position == 6){
			//使用说明
			_showHelp();
		}else if (position == 7){
			//建议反馈
			if (!networkCheck()){
				Toast.makeText(this, AppResource.noConnectTis, Toast.LENGTH_LONG).show();
			}else{
				_suguest();
			}
		}
	}
	
	private void _setDevType() {
		view = View.inflate(this, R.layout.devtype, null);
		if (1==devType){
			((RadioButton)view.findViewById(R.id.radioPhone)).setChecked(true);
		}else{
			((RadioButton)view.findViewById(R.id.radioTablet)).setChecked(true);
		}
		new AlertDialog.Builder(this)
				.setTitle(AppResource.devTypeTitle)
				.setIcon(null)
				.setView(view)
				.setPositiveButton(AppResource.buttonEnter,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								int chooseID = ((RadioGroup)view.findViewById(R.id.radioGroup)).getCheckedRadioButtonId();
								if (chooseID == R.id.radioPhone){
									devType = 1;
								}else{
									devType = 2;
								}
								Editor editor = xmlSP.edit();
								editor.putInt("devType", devType);
								editor.commit();
								((MainActivity) MainActivity.mainActivity).setDevType(devType);
								refreshData();
							}
						})
				.setNegativeButton(AppResource.buttonCancel, null)
				.show();
		
	}

	private void _setPhotoSize(){
		view = View.inflate(this, R.layout.photosize, null);
		EditText et = (EditText)view.findViewById(R.id.potosize_e_width);
		et.setText(String.valueOf(width));
		et = (EditText)view.findViewById(R.id.potosize_e_height);
		et.setText(String.valueOf(height));
		new AlertDialog.Builder(this)
				.setTitle(AppResource.photoSizeDlgTitle)
				.setIcon(null)
				.setView(view)
				.setPositiveButton(AppResource.buttonEnter,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								width = Integer.parseInt(((EditText) view
										.findViewById(R.id.potosize_e_width))
										.getText().toString());
								height = Integer.parseInt(((EditText) view
										.findViewById(R.id.potosize_e_height))
										.getText().toString());
								if(height/(float)width >((MainActivity) MainActivity.mainActivity).getMaxWHRate()){
									height = (int)(width*((MainActivity) MainActivity.mainActivity).getMaxWHRate());
								}
								
								Editor editor = xmlSP.edit();
								editor.putInt("sizeWidth", width);
								editor.putInt("sizeHeight", height);
								editor.commit();
								
								//改变红框大小
								((MainActivity) MainActivity.mainActivity).setPhotoHeight(height);
								((MainActivity) MainActivity.mainActivity).setPhotoWidth(width);
								((MainActivity) MainActivity.mainActivity).setWhRatio((width/(float)height));
								SurfaceView surfaceView = (SurfaceView)((MainActivity) MainActivity.mainActivity).findViewById(R.id.surefaceView);
				            	DrawRectImageView.rectWidth = surfaceView.getWidth();
								DrawRectImageView.rectHeight = (int) (surfaceView.getWidth() / (width/(float)height)) ;
				            	//((DrawRectImageView)((MainActivity) MainActivity.mainActivity).findViewById(R.id.drawRect)).invalidate();
								refreshData();
							}
						})
				.setNegativeButton(AppResource.buttonCancel, null)
				.show();
	}
	
	private void _setPhotoQuality(){
		view = View.inflate(this, R.layout.photoquality, null);
		((EditText)view.findViewById(R.id.poto_e_quality)).setText(String.valueOf(photoQuality));
		new AlertDialog.Builder(this)
				.setTitle(AppResource.photoQualityDlgTitle)
				.setIcon(null)
				.setView(view)
				.setPositiveButton(AppResource.buttonEnter,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								photoQuality = Integer.parseInt(((EditText) view
										.findViewById(R.id.poto_e_quality))
										.getText().toString());
								if (photoQuality<10){
									photoQuality = 10;
								}else if (photoQuality>100){
									photoQuality = 100;
								}
								Editor editor = xmlSP.edit();
								editor.putInt("photoQuality", photoQuality);
								editor.commit();
								((MainActivity) MainActivity.mainActivity).setPhotoQuality(photoQuality);
								refreshData();
							}
						})
				.setNegativeButton(AppResource.buttonCancel, null)
				.show();
	}
	
	private void _setSaveBigPhoto(){
		view = View.inflate(this, R.layout.bigphoto, null);
		((CheckBox)view.findViewById(R.id.chkBigPhoto)).setChecked(genBigPhoto);
		new AlertDialog.Builder(this)
				.setTitle(AppResource.bigPhotoDlgTitle)
				.setIcon(null)
				.setView(view)
				.setPositiveButton(AppResource.buttonEnter,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								genBigPhoto = ((CheckBox)view.findViewById(R.id.chkBigPhoto)).isChecked();
								Editor editor = xmlSP.edit();
								editor.putBoolean("bigPhoto", genBigPhoto);
								editor.commit();
								((MainActivity) MainActivity.mainActivity).setGenBigPhoto(genBigPhoto);
								refreshData();
							}
						})
				.setNegativeButton(AppResource.buttonCancel, null)
				.show();
	}
	
	private void _setHidePhotoed(){
		view = View.inflate(this, R.layout.hidephotoed, null);
		((CheckBox)view.findViewById(R.id.hidepotoed_ck)).setChecked(hidePhotoed);
		new AlertDialog.Builder(this)
				.setTitle(AppResource.hidePhotoedTitle)
				.setIcon(null)
				.setView(view)
				.setPositiveButton(AppResource.buttonEnter,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								hidePhotoed = ((CheckBox)view.findViewById(R.id.hidepotoed_ck)).isChecked();
								Editor editor = xmlSP.edit();
								editor.putBoolean("hidePhotoed", hidePhotoed);
								editor.commit();
								MainActivity.getInstance().setHidePhotoed(hidePhotoed);
								refreshData();
							}
						})
				.setNegativeButton(AppResource.buttonCancel, null)
				.show();
	}
	
	private void _validSoft(){
		if (ValidUtils.valid()) return;
		activeSIM = ((MainActivity) MainActivity.mainActivity).getCurSIM();
		view = View.inflate(this, R.layout.validsoft, null);
		((EditText)view.findViewById(R.id.valid_phone_num_e)).setText(String.valueOf(activePhone));
		EditText simED = ((EditText)view.findViewById(R.id.valid_phone_sim_e));
		simED.setText(String.valueOf(activeSIM));
		simED.setCursorVisible(false);      //设置输入框中的光标不可见  
		simED.setFocusable(false);           //无焦点  
		simED.setFocusableInTouchMode(false);     //触摸时也得不到焦点  
		new AlertDialog.Builder(this)
				.setTitle(AppResource.validAppDlgTitle)
				.setIcon(null)
				.setView(view)
				.setPositiveButton(AppResource.buttonEnter,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								activePhone = ((EditText) view
										.findViewById(R.id.valid_phone_num_e))
										.getText().toString();
								
								//服务器验证信息
								try {
									activeSIM = MD5.getMD5(activeSIM);
								} catch (NoSuchAlgorithmException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								String valid = ValidUtils.validSoft(activePhone,activeSIM);
								if (valid==null) valid = AppResource.webServerErr;
								if (valid.equals("1")){
									activeState = 1;
									Editor editor = xmlSP.edit();
									editor.putInt("activeState", activeState);
									editor.putString("activePhone", activePhone);
									editor.putString("activeSIM", activeSIM);
									editor.commit();
									
									
									((MainActivity) MainActivity.mainActivity).setActiveState(activeState);
									((MainActivity) MainActivity.mainActivity).setActivePhone(activePhone);
									((MainActivity) MainActivity.mainActivity).setActiveSIM(activeSIM);
									((MainActivity) MainActivity.mainActivity).showData(((MainActivity) MainActivity.mainActivity).getPath() + "/" + ((MainActivity) MainActivity.mainActivity).getSelectFile()+".txt");
									((MainActivity) MainActivity.mainActivity).getMyAdapter().setCurSelect(0);
									((MainActivity) MainActivity.mainActivity).selectChanged();
									((MainActivity) MainActivity.mainActivity).setValidated(true);
									refreshData();
									Toast.makeText(SetupActivity.this, AppResource.validAppDlgSuccess, Toast.LENGTH_LONG).show();
									
								}else if (valid.equals("0")){
									Toast.makeText(SetupActivity.this, AppResource.validAppDlgFail, Toast.LENGTH_LONG).show();
								}else{
									Toast.makeText(SetupActivity.this, String.format("%s:%s",AppResource.validAppDlgErrTis, valid), Toast.LENGTH_LONG).show();
								}
								
								
							}
						})
				.setNegativeButton(AppResource.buttonCancel, null)
				.show();
	}
	
	private void _showHelp(){
		Intent intent = new Intent();
		intent.setClass(SetupActivity.this, HelpActivity.class);
		startActivity(intent);
	}
	
	private void _suguest(){
		view = View.inflate(this, R.layout.suguest, null);
		new AlertDialog.Builder(this)
				.setTitle(AppResource.suguestDlgTitle)
				.setIcon(null)
				.setView(view)
				.setPositiveButton(AppResource.buttonEnter,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								String suguest = ((EditText)view.findViewById(R.id.suguest_e)).getText().toString();
								if (suguest.equalsIgnoreCase("")){
									Toast.makeText(SetupActivity.this, AppResource.suguestDlgTis, Toast.LENGTH_LONG).show();
								}
								String result = ValidUtils.sendSuguest(suguest);
								if (result==null) result = AppResource.webServerErr;
								if (result.equals("1")){
									Toast.makeText(SetupActivity.this, AppResource.suguestDlgSuccess, Toast.LENGTH_LONG).show();
								}else{
									Toast.makeText(SetupActivity.this, String.format("%s:%s", AppResource.suguestDlgFail, result), Toast.LENGTH_LONG).show();
								}
							}
						})
				.setNegativeButton(AppResource.buttonCancel, null)
				.show();
	}
	
	



}
