package com.lqd.camera;

import com.lqd.Image.ImageFilter;
import com.lqd.facedetect.EventUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class PhotoParameterClass {
	private MainActivity context;
	private SeekBar sb_b;
	private SeekBar sb_c;
	private TextView tv_b;
	private String curBrightness;
	private String curContrast;
	private TextView tv_c;
	private int brightness;
	private int contrast;
	private Bitmap bmp;
	private RelativeLayout layout;
	private Button btnOk;
	private Button btnCancel;

	public PhotoParameterClass(MainActivity context, Bitmap bmp) {
		super();
		this.context = context;
		this.bmp = bmp;
	}
	
	public void init(){
		layout = (RelativeLayout)context.findViewById(R.id.photoparameter);
		layout.setVisibility(View.VISIBLE);
		brightness = context.getBrightness();
		contrast = context.getContrast();
		curBrightness = AppResource.curBrightness;
		curContrast = AppResource.curContrast;
		sb_b = (SeekBar) context.findViewById(R.id.liangdu);
		sb_b.setProgress(brightness);
		sb_b.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				tv_b.setText(curBrightness+(progress/10.0));
				
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				if (seekBar.getProgress()!=brightness){
					brightness = seekBar.getProgress();
					refreshPhoto();
				}
				
			}
			
		});
		sb_c = (SeekBar) context.findViewById(R.id.duibidu);
		sb_c.setProgress(contrast);
		sb_c.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				tv_c.setText(curContrast+(progress/10.0));
				
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				if (seekBar.getProgress()!=contrast){
					contrast = seekBar.getProgress();
					refreshPhoto();
				}
				
			}
			
		});
		
		tv_b = (TextView)context.findViewById(R.id.liangdu_l);
		tv_c = (TextView)context.findViewById(R.id.duibidu_l);
		tv_b.setText(curBrightness+(brightness/10.0));
		tv_c.setText(curContrast+(contrast/10.0));
		
		btnOk = (Button)context.findViewById(R.id.btn_pp_Ok);
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SharedPreferences xmlSP = context.getSharedPreferences("SP", context.MODE_PRIVATE);
				Editor editor = xmlSP.edit();
				editor.putInt("brightness", brightness);
				editor.putInt("contrast", contrast);
				editor.commit();
				context.setBrightness(brightness);
				context.setContrast(contrast);
				context.setbShowPhoto(false);
				layout.setVisibility(View.GONE);
				context.initCameraStatus();
				
			}
		});
		
		btnCancel = (Button)context.findViewById(R.id.btn_pp_cancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				context.setbShowPhoto(false);
				layout.setVisibility(View.GONE);
				context.initCameraStatus();
				
			}
		});
		context.setbShowPhoto(true);
		refreshPhoto();
		
	}
	
	
	private void refreshPhoto() {
		
		
		Bitmap _bmp = ImageFilter.greyFilter(bmp, brightness/10.0, contrast/10.0);
		context.setPreviewBitmap(_bmp);
		context.findViewById(R.id.drawRect).invalidate();
	}
}
