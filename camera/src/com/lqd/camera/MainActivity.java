package com.lqd.camera;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.lqd.Image.ImageFilter;
import com.lqd.facedetect.*;
import com.testin.agent.TestinAgent;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.StrictMode;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.hardware.Camera.Area;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Face;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.SurfaceHolder.Callback;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class MainActivity extends Activity {

	protected static final String TAG = "bmCamera";
	protected static final long TIME = 1000;
	protected static final int MAXPERSONS = 5;    //���δ���ֻ����ʾ5������
	private String path;
	private List<String> spinnerList = new ArrayList<String>();    
    private TextView txtInfo;    
    private Spinner mySpinner;    
    private ArrayAdapter<String> adapter;    
    private String selectFile;
    private MyAdapter myAdapter;
    private List<Person> oldData = new ArrayList<Person>();
    boolean bBegin = true;
    private int enterCount=0;
    public static MainActivity mainActivity;
    private Person curPerson;
    private int curPersonPos;
    
    //private int surfaceViewWidth, surfaceViewHeight;
    //photoWidth, photoHeightĿ�걣��ͼ��Ĵ�С
    private int photoWidth, photoHeight;
    private float whRatio;
    private float maxWHRate;
    private int photoQuality;
    private boolean genBigPhoto;	//�Ƿ����ɴ�ͼ��
    
    //previewSize,pictureSize���Ԥ��������ͼ���С
    private Size previewSize ;
    private Size pictureSize;
//    public static Size originalPictrueSize;
    
    //����ͷ��ת�Ƕ�
    int cameraRt;
    //�豸����  1���ֻ�	2��ƽ��
    private int devType;  
    
    
    boolean showPhoto;
    
    private Point screenPoint;
    
    private Camera camera;
    
    //��֤��Ϣ
	private int activeState;
	private String activePhone;
    private String activeSIM;
    private String curSIM;
    private Boolean validated;
    
    //�������
    FaceView faceView;
    private SurfaceView surfaceView;
	private GoogleFaceDetect googleFaceDetect = null;
	public MainHandler mMainHandler;
	private Rect preRect;
	
	Handler handler = new Handler();  
	
	PowerManager.WakeLock mWakeLock;
	
	//surfaceView ���Ʊ���
	private static final int NONE = 0;// ԭʼ  
    private static final int DRAG = 1;// �϶�  
    private static final int ZOOM = 2;// �Ŵ�  
    private int zoomValue;
    private PointF mStartPoint = new PointF();  
    private float mStartDistance = 0f;
	private int brightness;
	private int contrast;  
    private Bitmap previewBitmap;
    private Boolean savingPhoto=false;
    private Boolean hidePhotoed=false;
	
	
    public static MainActivity getInstance(){
    	return mainActivity;
    }
    
    

	public Boolean getHidePhotoed() {
		return hidePhotoed;
	}



	public void setHidePhotoed(Boolean hidePhotoed) {
		this.hidePhotoed = hidePhotoed;
	}



	public Boolean getSavingPhoto() {
		return savingPhoto;
	}


	public void setSavingPhoto(Boolean savingPhoto) {
		this.savingPhoto = savingPhoto;
	}


	public Bitmap getPreviewBitmap() {
		return previewBitmap;
//		return null;
	}


	public void setPreviewBitmap(Bitmap previewBitmap) {
		this.previewBitmap = previewBitmap;
	}


	public int getBrightness() {
		return brightness;
	}


	public void setBrightness(int brightness) {
		this.brightness = brightness;
	}


	public int getContrast() {
		return contrast;
	}


	public void setContrast(int contrast) {
		this.contrast = contrast;
	}


	public Boolean getValidated() {
		return validated;
	}


	public void setValidated(Boolean validated) {
		this.validated = validated;
	}


	public int getDevType() {
		return devType;
	}


	public void setDevType(int devType) {
		this.devType = devType;
	}


	public float getMaxWHRate() {
		return maxWHRate;
	}


	public void setMaxWHRate(float maxWHRate) {
		this.maxWHRate = maxWHRate;
	}


	public boolean isShowPhoto() {
		return showPhoto;
	}


	public void setbShowPhoto(boolean showPhoto) {
		this.showPhoto = showPhoto;
	}


	public MyAdapter getMyAdapter() {
		return myAdapter;
	}


	public void setMyAdapter(MyAdapter myAdapter) {
		this.myAdapter = myAdapter;
	}


	public String getCurSIM() {
		return curSIM;
	}


	public void setCurSIM(String curSIM) {
		this.curSIM = curSIM;
	}


	public boolean isGenBigPhoto() {
		return genBigPhoto;
	}


	public void setGenBigPhoto(boolean genBigPhoto) {
		this.genBigPhoto = genBigPhoto;
	}


	public int getActiveState() {
		return activeState;
	}


	public void setActiveState(int activeState) {
		this.activeState = activeState;
	}


	public String getActivePhone() {
		return activePhone;
	}


	public void setActivePhone(String activePhone) {
		this.activePhone = activePhone;
	}


	public String getActiveSIM() {
		return activeSIM;
	}


	public void setActiveSIM(String activeSIM) {
		this.activeSIM = activeSIM;
	}


	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}


	public String getSelectFile() {
		return selectFile;
	}


	public void setSelectFile(String selectFile) {
		this.selectFile = selectFile;
	}


	public int getPhotoWidth() {
		return photoWidth;
	}


	public void setPhotoWidth(int photoWidth) {
		this.photoWidth = photoWidth;
	}


	public int getPhotoHeight() {
		return photoHeight;
	}


	public void setPhotoHeight(int photoHeight) {
		this.photoHeight = photoHeight;
	}


	public float getWhRatio() {
		return whRatio;
	}


	public void setWhRatio(float whRatio) {
		this.whRatio = whRatio;
	}


	public int getPhotoQuality() {
		return photoQuality;
	}


	public void setPhotoQuality(int photoQuality) {
		this.photoQuality = photoQuality;
	}


	public Size getPreviewSize() {
		return previewSize;
	}


	public void setPreviewSize(Size previewSize) {
		this.previewSize = previewSize;
	}


	public Size getPictureSize() {
		return pictureSize;
	}


	public void setPictureSize(Size pictureSize) {
		this.pictureSize = pictureSize;
	}


	public Point getScreenPoint() {
		return screenPoint;
	}


	public void setScreenPoint(Point screenPoint) {
		this.screenPoint = screenPoint;
	}


	@Override
	protected void onResume() {
		
		super.onResume();
		mWakeLock.acquire(); 
	}


	@Override
	protected void onPause() {
		
		super.onPause();
		mWakeLock.release();
	}
	
	
	


	@SuppressWarnings({ "deprecation", "unused" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		TestinAgent.init(this);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
        .detectDiskReads()
        .detectDiskWrites()
        .detectAll()   // or .detectAll() for all detectable problems
        .penaltyLog()
        .build());
     StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
        .detectLeakedSqlLiteObjects()
        .detectLeakedClosableObjects()
        .penaltyLog()
        .penaltyDeath()
        .build());
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		new AppResource(this);
		mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag"); 
		//����Ƿ������
				if (!checkCamera(this)){
					new AlertDialog.Builder(this)
					.setMessage(AppResource.noCamera)
					.show();
					finish();
				}
				
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		
		txtInfo = (TextView)findViewById(R.id.txtInfo);
		mainActivity = this;
		TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);  
//		if (telephonyManager.getSimState()==TelephonyManager.SIM_STATE_READY){
			try{
				curSIM = telephonyManager.getDeviceId();		//ʹ���豸IMEI�����
			}catch(Exception e){
				curSIM = "0123456789ABCDE";
			}
//		}else{
//			curSIM = "-1";
//		}
		if (null == curSIM) curSIM = "0123456789ABCDE";
		
		
		
		//���SD���Ƿ����
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory().getPath()+"/bmCamera";
        }else{
            Toast.makeText(this, "û��SD��", Toast.LENGTH_LONG).show();
            finish();
        }
        //Ϊ�����һ���������ɲ�������
      	File file = new File(path);
      	if (null == file){
      		new AlertDialog.Builder(this)
			.setMessage(AppResource.noWriteFileAuthority)
			.setPositiveButton(AppResource.buttonEnter,new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog,
						int whichButton) {
						finish();
					}
			})
			.show();
			return;
      	}
      	if (!file.exists()){
      		file.mkdir();
      		genTestFile(String.format("%s/%s.txt",   path,AppResource.testFileName));
      	}
        //������Ŀ¼��bmCameraĿ¼�����TXT�ļ�����spinnerList�б���
        getTxtFiles(path);
        //���spinnerList���ݵ���������
        fillSpinner();
        
        //ȡ������
        SharedPreferences xmlSP = getSharedPreferences("SP", MODE_PRIVATE);
        photoWidth = xmlSP.getInt("sizeWidth", 168) ;
        photoHeight = xmlSP.getInt("sizeHeight", 240);
		photoQuality = xmlSP.getInt("photoQuality", 100);
		whRatio = (float)photoWidth/photoHeight;
		genBigPhoto = xmlSP.getBoolean("bigPhoto", false);
		activeState = xmlSP.getInt("activeState", 0);
		activePhone = xmlSP.getString("activePhone", "");
		activeSIM = xmlSP.getString("activeSIM", "");
		devType = xmlSP.getInt("devType", 1);
		enterCount = xmlSP.getInt("enterCount", 0);
		brightness = xmlSP.getInt("brightness", 10);
		contrast = xmlSP.getInt("contrast", 10);
		hidePhotoed = xmlSP.getBoolean("hidePhotoed", false);
		
		validated = ValidUtils.valid();
		
		//��ȡ��Ļ�ֱ���
		Display mDisplay = getWindowManager().getDefaultDisplay();
		screenPoint = new Point() ;
		mDisplay.getSize(screenPoint);
		
		//����ʶ��
		mMainHandler = new MainHandler();
		googleFaceDetect = new GoogleFaceDetect(getApplicationContext(), mMainHandler);
        
        //�����ʾ
        surfaceView = (SurfaceView)this.findViewById(R.id.surefaceView);
		surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceView.getHolder().setKeepScreenOn(true);
		surfaceView.getHolder().addCallback(new SurfaceCallback());
		
		
//		faceView = (FaceView)findViewById(R.id.face_view);
//		mMainHandler.sendEmptyMessageDelayed(EventUtil.CAMERA_HAS_STARTED_PREVIEW, 800);
//		handler.postDelayed(runnable, TIME);
//		
		//���˲���
		EditText findName = (EditText) this.findViewById(R.id.find_name);
		findName.addTextChangedListener(new watcher());
		
		final LinearLayout ly = (LinearLayout)this.findViewById(R.id.item_left);
		ly.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				if (bBegin){
					int width = ly.getWidth();
					int height = (int) (width / ((float) screenPoint.x / screenPoint.y));
					// ����surfaceview��С

					LayoutParams params = surfaceView.getLayoutParams();
					params.width = width;
					params.height = height;
					surfaceView.setLayoutParams(params);
					
					DrawRectImageView.rectWidth = width;
					DrawRectImageView.rectHeight = (int) (width / whRatio) ;
					bBegin = false;
				}
				return true;
			}
		});
		
		//���surfaceview�¼�
//        surfaceView.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				try{
//					camera.autoFocus(null);
//				}catch(Exception e){
//					
//				}
//				
//			}
//		});
		
		//�����Ļ������
        surfaceView.setOnTouchListener(new OnTouchListener() {
        	
        	private float spacing(MotionEvent event) {  
                float x = event.getX(0) - event.getX(1);  
                float y = event.getY(0) - event.getY(1);  
                return (float) Math.sqrt(x * x + y * y);  
            }
        	
        	private void zoomAcition(MotionEvent event) {  
                    float newDist = spacing(event);  
                    float scale = newDist / mStartDistance;  
                    if (newDist>mStartDistance+5){
                    	zoomValue += 1;
                    	if (zoomValue>40) zoomValue = 40;
                    }else if(newDist <mStartDistance-5 ){
                    	zoomValue -= 1;
                    	if (zoomValue<0) zoomValue = 0;
                    }
                   mStartDistance = newDist;   
                    try{
                    	Parameters params = camera.getParameters();
                    	if (!params.isZoomSupported()) return;
//                    	Log.i("ZOOM", zoomValue+"");
                        params.setZoom(zoomValue);
                        camera.setParameters(params);
                       // Log.d(TAG, "Is support Zoom " + params.isZoomSupported());
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
            }  
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction() & MotionEvent.ACTION_MASK) {  
		        case MotionEvent.ACTION_DOWN:  
		        	try{
						camera.autoFocus(null);
						Log.i("auto", "down");
					}catch(Exception e){
						
					}
		        	break;
		        case MotionEvent.ACTION_POINTER_DOWN:  
		            float distance = spacing(event);  
		            if (distance > 10f) {  
//		                mStatus = ZOOM;  
		                mStartDistance = distance;  
		            }  
		            break;  
		  
		        case MotionEvent.ACTION_MOVE:  
		           // if (mStatus == DRAG) {  
		                //dragAction(event);  
		           // } else {  
		                if (event.getPointerCount() == 1)  
		                    return true;  
		                zoomAcition(event);  
		            //}  
		            break;  
		        case MotionEvent.ACTION_UP:  
		        case MotionEvent.ACTION_POINTER_UP:  
//		            mStatus = NONE;  
		            break;  
		        default:  
		            break;  
		        }  
		  
		        return true; 
			}
		});
	}
	
	
	private boolean checkCamera(Context context){
		if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
			if(Camera.getNumberOfCameras()>0){
				return true;
			}else{
				return false;
			}
			
		}else{
			return false;
		}
	}
	
//	/**
//	 * �ж��豸���ֻ�����ƽ��
//	 * @param context
//	 * @return DevType.TABLET:ƽ�� 	DevType.PHONE:�ֻ�
//	 */
//	private DevType checkDevType(Context context) {
//		if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE){
//			return DevType.TABLET;
//		}else{
//			return DevType.PHONE;
//		}
//	}
	
	
	/*
	 * ������⴦��
	 */
	private  class MainHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what){
			case EventUtil.UPDATE_FACE_RECT:
				Face[] faces = (Face[]) msg.obj;
				faceView.setFaces(faces);
				break;
			case EventUtil.CAMERA_HAS_STARTED_PREVIEW:
				startGoogleFaceDetect();
				break;
			case EventUtil.GETPHOTOREADING:
				((Button)MainActivity.this.findViewById(R.id.getPhoto)).setClickable(true);
				showPhoto = false;
				selectChanged();
				MainActivity.this.findViewById(R.id.drawRect).invalidate();
				camera.startPreview();
				startGoogleFaceDetect();
				break;
			case EventUtil.REDRAWRECT:
				MainActivity.this.findViewById(R.id.drawRect).invalidate();
				break;
			case EventUtil.FILENAMEEDITFOCUS:
				setEnterFileFocus();
				break;
			}
			super.handleMessage(msg);
		}

	}
	
	private void startGoogleFaceDetect(){
		if (null == camera) return;
		Camera.Parameters params = camera.getParameters();
		if(params.getMaxNumDetectedFaces() > 0){
			if(faceView != null){
				faceView.clearFaces();
				faceView.setVisibility(View.VISIBLE);
			}
			camera.setFaceDetectionListener(googleFaceDetect);
			camera.startFaceDetection();
		}
	}
	private void stopGoogleFaceDetect(){
		if (null == camera) return;
		Camera.Parameters params = camera.getParameters();
		if(params.getMaxNumDetectedFaces() > 0){
			camera.setFaceDetectionListener(null);
			camera.stopFaceDetection();
			faceView.clearFaces();
		}
	}
	
	Runnable runnable = new Runnable(){
		@Override
		public void run() {
			Rect rect = faceView.getFaceRect();
			if (null == preRect) preRect = rect;
			if (Math.abs(rect.left - preRect.left)>50 || Math.abs(rect.top-preRect.top)>50){
				try{
					Parameters parameters = camera.getParameters();
					parameters.setFocusMode(Parameters.FOCUS_MODE_AUTO);
					List<Area> focusAreas = new ArrayList<Area>();
					focusAreas.add(new Area(rect, 500));
					parameters.setFocusAreas(focusAreas);
					camera.setParameters(parameters);
					camera.autoFocus(null);
				}catch(Exception e){
					
				}
				preRect = rect;
	        }
			handler.postDelayed(this, TIME);
		}
	};
	
	

	/*
	 * �����仯�۲���
	 */
	class watcher implements TextWatcher{

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			String pinyin = s.toString();
			List<Person> find = new ArrayList<Person>();
			for(Person person: oldData){
				if (person.getPinyin().indexOf(pinyin.toLowerCase()) != -1){
					find.add(person);
				}
			}
			myAdapter = new MyAdapter(MainActivity.this, LayoutInflater.from(MainActivity.this), find);
			ListView lv = (ListView) MainActivity.this.findViewById(R.id.lvStudentList) ;
			lv.setAdapter(myAdapter);
			selectChanged();
		}

		@Override
		public void afterTextChanged(Editable s) {
			
			
		}
		
	}
	
	private void genTestFile(String fileName){
		File f = new File(fileName);
		try {
			if (!f.exists()) {
				f.createNewFile();
			}
			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f),"utf-8");
			BufferedWriter writer=new BufferedWriter(write);   
			for(int i=1; i<10; i++){
				writer.write(String.format("%s%d  20150100%d",AppResource.testItemName,i,i));
				writer.write("\r\n");
			}
			writer.flush();
			writer.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setRectSize(int width, int height){
		DrawRectImageView.rectWidth = width;
		DrawRectImageView.rectHeight = height;
		
	}
	
	
	// ������Ŀ¼��bmCameraĿ¼�����TXT�ļ���
	void getTxtFiles(String filePath){  
		File file = new File(filePath);
        File files[] = file.listFiles();  
        String fileName="";
        if(files != null){  
            for (File f : files){  
                if(f.isFile()){  
                	fileName = f.getName();
                	try{
                		spinnerList.add(fileName.substring(0,fileName.lastIndexOf(".")));
                	}catch(Exception e){
                		
                	}
                }
            }  
        }  
    } 
	
	//���SPINNER����
	private void fillSpinner(){
		mySpinner = (Spinner)findViewById(R.id.spinner_unit);    
        //�ڶ�����Ϊ�����б���һ����������������õ���ǰ�涨���list��    
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, spinnerList);    
        //��������Ϊ���������������б�����ʱ�Ĳ˵���ʽ��    
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
        //���Ĳ�������������ӵ������б���    
        mySpinner.setAdapter(adapter);    
        //���岽��Ϊ�����б����ø����¼�����Ӧ���������Ӧ�˵���ѡ��    
        mySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){    
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {    
                // TODO Auto-generated method stub    
                selectFile = adapter.getItem(arg2);
                showData(path + "/" + selectFile+".txt");
                myAdapter.setCurSelect(0);
                selectChanged();
                
                /* ��mySpinner ��ʾ*/    
                arg0.setVisibility(View.VISIBLE);    
            }    
            public void onNothingSelected(AdapterView<?> arg0) {    
                // TODO Auto-generated method stub    
                arg0.setVisibility(View.VISIBLE);    
            }    
        });    
        /*�����˵�����������ѡ����¼�����*/    
        mySpinner.setOnTouchListener(new Spinner.OnTouchListener(){    
            public boolean onTouch(View v, MotionEvent event) {    
                // TODO Auto-generated method stub    
                /** 
                 *  
                 */  
                return false;    
            }  
        });    
        /*�����˵�����������ѡ���ı��¼�����*/    
        mySpinner.setOnFocusChangeListener(new Spinner.OnFocusChangeListener(){    
        public void onFocusChange(View v, boolean hasFocus) {    
            // TODO Auto-generated method stub    
  
        }    
        }); 
	}
	
	private void setPreviewBmp(){
		String file = path + '/' + selectFile + '/' + curPerson.getPhotoName()+ ".jpg";
		Bitmap bmp = DrawRectImageView.getLoacalBitmap(file);
		setPreviewBitmap(bmp);
	}
	
	//��һ����ť�¼�
	public void btnPriorClick(View v){
		if (myAdapter == null || myAdapter.getCount()==0) return;
		int cur = myAdapter.getCurSelect();
		if (cur>0){
			myAdapter.setCurSelect(cur-1);
			selectChanged();
		}
		setEnterFileVisible(false);
		
	}
	
	//��һ����ť�¼�
	public void btnNextClick(View v){
		if (myAdapter == null || myAdapter.getCount()==0) return;
		int cur = myAdapter.getCurSelect();
		if (cur<myAdapter.getCount()-1){
			myAdapter.setCurSelect(cur+1);
			selectChanged();
		}
		setEnterFileVisible(false);
		
	}
	
	//ѡ������б䣬֪ͨ����
	public void selectChanged(){
		if (myAdapter.isEmpty()){
			myAdapter.notifyDataSetChanged();
			return;
		}
		curPersonPos = myAdapter.getCurSelect();
		myAdapter.notifyDataSetChanged();
		curPerson = myAdapter.getPersonList().get(curPersonPos);
		txtInfo.setText(curPerson.getName() + "    " + curPerson.getPhotoName());
		if (showPhoto){
			setPreviewBmp();
			MainActivity.this.findViewById(R.id.drawRect).invalidate();
		}
	}
	
	//���ð�ť�¼�
	public void btnSetupClick(View v){
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, SetupActivity.class);
		startActivity(intent);
	}
	
	//����ƿ���
	public void sgdClick(View v){
		Camera.Parameters parameters = camera.getParameters();
		CheckBox cb = (CheckBox)MainActivity.this.findViewById(R.id.sgd);
		if (cb.isChecked()){
			parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);   
		}else{
			parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);   
		}
		camera.setParameters(parameters);
	}
	
	//Ԥ������
	public void eyeClick(View v) {
		if (myAdapter == null || myAdapter.getCount()==0) return;
		CheckBox cb = (CheckBox) MainActivity.this.findViewById(R.id.eye);
		if (cb.isChecked()) {
			showPhoto = true;
			setPreviewBmp();
			MainActivity.this.findViewById(R.id.drawRect).invalidate();
			camera.stopPreview();
			faceView.clearFaces();
			faceView.setVisibility(View.INVISIBLE);
		} else {
			showPhoto = false;
			MainActivity.this.findViewById(R.id.drawRect).invalidate();
			camera.startPreview();
			faceView.setVisibility(View.VISIBLE);
		}
	}
	
	//
	private void delPhoto(){
		if (myAdapter.isEmpty()) return;
		int cur = myAdapter.getCurSelect();
		myAdapter.notifyDataSetChanged();
		final Person person = myAdapter.getPersonList().get(cur);
		new AlertDialog.Builder(this)
		.setMessage(person.getName())
		.setTitle(AppResource.delPhotoConfirm)
		.setPositiveButton(AppResource.buttonEnter,new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog,
					int whichButton) {
				String file = path + '/' + selectFile + '/' + person.photoName + ".jpg";
				//Log.i("delfile", file);
				File f = new File(file);
				try {
					if (f.exists()) {
						f.delete();
						myAdapter.notifyDataSetChanged();
					}
				}catch(Exception e){}
			}
		})
		.setNegativeButton(AppResource.buttonCancel, null)
		.show();
	
	}
	
	//��ʾTXT�ļ����������
	public void showData(String file){
		oldData = new GenData(file).getPersonList();
		myAdapter = new MyAdapter(this, LayoutInflater.from(this), oldData);
		ListView lv = (ListView) this.findViewById(R.id.lvStudentList) ;
		lv.setAdapter(myAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
//				showPhoto = false;
				myAdapter.setCurSelect(position);
				selectChanged();
				setEnterFileVisible(false);
			}
			
		});
		
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				myAdapter.setCurSelect(position);
				selectChanged();
				delPhoto();
				return false;
			}
		});

		//Toast.makeText(this, selectFile, Toast.LENGTH_LONG).show();
	}
	
	
	public void showMessage(String msg){
		new AlertDialog.Builder(this)
		.setMessage(msg)
		.setPositiveButton(AppResource.buttonEnter,null)
		.show();
	}

	/* 
	 *  �����ش���     
	 */
	private int getRotation() {  
        int rotation = this.getWindowManager().getDefaultDisplay()  
                .getRotation();  
        int degrees = 0;  
        switch (rotation) {  
        case Surface.ROTATION_0:  
            degrees = 0;  
            break;  
        case Surface.ROTATION_90:  
            degrees = 90;  
            break;  
        case Surface.ROTATION_180:  
            degrees = 180;  
            break;  
        case Surface.ROTATION_270:  
            degrees = 270;  
            break;  
        }  
        return degrees;  
    }  
	
	
	 public Bitmap getScaleBitmap(Bitmap b, int w, int h){
			float scaleW = w/(float)b.getWidth();
			float scaleH = h/(float)b.getHeight();
	        Matrix matrix = new Matrix();
	        matrix.postScale(scaleW, scaleH);
	        Bitmap resizedBitmap = Bitmap.createBitmap(b, 0, 0,
	        b.getWidth(), b.getHeight(), matrix, true);
	        return resizedBitmap;
		}
	

	 private class SavePhotoThread extends Thread { 
		 
	        private Handler handler; 
	        private Bitmap mBitmap; 
	 
	        public SavePhotoThread(Handler handler, Bitmap bmp) { 
	            this.handler = handler; 
	            this.mBitmap = bmp; 
	        } 
	 
	        @Override 
	        public void run() { 
	        	//���ͼ��δ��ת90������ת
				Matrix matrix = null;
				int h;
				int w;
				w = mBitmap.getWidth();
				h = (int) Math.rint(w/(float)whRatio);
				if (h > mBitmap.getHeight()){
					h = mBitmap.getHeight();
				}
				if (1==devType){
					if (mBitmap.getWidth()>mBitmap.getHeight()){
						matrix = new Matrix();
						matrix.setRotate(90);
						h = mBitmap.getHeight();
						w = (int)(mBitmap.getHeight()/(float)whRatio);
						if (w > mBitmap.getWidth()){
							w = mBitmap.getWidth();
						}
					}
				}
				
				Bitmap rectBitmap = null;
				if (null!=matrix){
					rectBitmap = Bitmap.createBitmap(mBitmap, 0, 0, w, h, matrix, true);
				}else{
					rectBitmap = Bitmap.createBitmap(mBitmap, 0, 0, w, h);
				}
			
	            Bitmap saveBitmap = ImageFilter.greyFilter(ImageFilter.shrink(rectBitmap, photoWidth, photoHeight), brightness/10.0, contrast/10.0);
	            setPreviewBitmap(saveBitmap);
	            if(null != saveBitmap)  
	            {  
	            	saveJpeg(saveBitmap);  
	            	if (genBigPhoto){
	            		saveBigJpeg(rectBitmap);  
	            	}
	            	mMainHandler.sendEmptyMessage(EventUtil.FILENAMEEDITFOCUS);
	            	
	            }  
	            if(mBitmap != null && !mBitmap.isRecycled()){
	            	mBitmap.recycle();
	            	mBitmap = null;
	            }
	            if(rectBitmap != null && !rectBitmap.isRecycled()){
	            	rectBitmap.recycle();
	            	rectBitmap = null;
	            }
	            System.gc();
	            setPreviewBitmap(saveBitmap);
	            //�������Ĵ������
	            if (hidePhotoed){
	            	int pos = myAdapter.getPersonList().indexOf(curPerson);
	            	myAdapter.getPersonList().remove(pos);
	            	oldData.remove(curPerson);
	            	if (pos>=myAdapter.getPersonList().size()-1){
	            		pos = myAdapter.getPersonList().size()-1;
	            	}
	            	if (myAdapter.getPersonList().size()>0){
	            		myAdapter.setCurSelect(pos);
	            	}else{
//	            		myAdapter = new MyAdapter(MainActivity.this, LayoutInflater.from(MainActivity.this), myAdapter.getPersonList());
//	        			ListView lv = (ListView) MainActivity.this.findViewById(R.id.lvStudentList) ;
//	        			lv.setAdapter(myAdapter);
//	        			lv.setAdapter(null);
//	            		myAdapter.notifyDataSetChanged();
	            	}
	            }
	            //
	            
	            savingPhoto = false;
	            showPhoto = true;
	            mMainHandler.sendEmptyMessage(EventUtil.REDRAWRECT);
	            mMainHandler.sendEmptyMessageDelayed(EventUtil.GETPHOTOREADING, 1500);
	        } 
	         
	    } 
	 
	//������Ƭ
	 
	private final class MyPictureCallback implements PictureCallback{

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			Bitmap mBitmap = null;
			if(null != data){  
				//stopGoogleFaceDetect();
				camera.stopPreview(); 
				faceView.clearFaces();
				MainActivity.this.findViewById(R.id.drawRect).invalidate();
                mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);//data���ֽ����ݣ����������λͼ  
                new SavePhotoThread(handler, mBitmap).start(); 
            }  

			
//            ((Button)MainActivity.this.findViewById(R.id.getPhoto)).setClickable(true);
		}

		

		
	}
	
	private void saveJpeg(Bitmap rectBitmap) {
		File file = null;
		File jpgFile = null;
		String fileName;
		try {
			file = new File(path + '/' + selectFile);
			if (!file.exists()){
				file.mkdir();
			}
			fileName = getEnterFileName();
			if (fileName.equals("")){
				jpgFile = new File(path + '/' + selectFile+ '/' +myAdapter.getPersonList().get(myAdapter.getCurSelect()).getPhotoName() + ".jpg");
			}else{
				jpgFile = new File(path + '/' + selectFile+ '/' + fileName + ".jpg");
				enterCount++;
			}
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(jpgFile));
			rectBitmap.compress(Bitmap.CompressFormat.JPEG, photoQuality, bos);
			bos.flush();
			bos.close();
			bos = null;
			
		} catch (IOException e) {	
			e.printStackTrace();
		}
		file = null;
		jpgFile = null;
		
	}
	
	private void saveBigJpeg(Bitmap rectBitmap) {
		File file = null;
		File jpgFile = null;
		try {
			file = new File(path + '/' + selectFile + 'L');
			if (!file.exists()){
				file.mkdir();
			}
			String fileName = getEnterFileName();
			if (fileName.equals("")){
				jpgFile = new File(path + '/' + selectFile + 'L' + '/' +myAdapter.getPersonList().get(myAdapter.getCurSelect()).getPhotoName() + ".jpg");
			}else{
				jpgFile = new File(path + '/' + selectFile + 'L' + '/' + fileName + ".jpg");
			}
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(jpgFile));
			rectBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
			bos = null;
			
		} catch (IOException e) {	
			e.printStackTrace();
		}
		file = null;
		jpgFile = null;
	}
	
	private final class SurfaceCallback implements Callback{

		@Override
		public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2,
				int arg3) {
			
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			if(null == camera){
				int flag=0;
				try{
					camera = Camera.open();
					flag=1;
					
					
				}catch(Exception e){
					
				}
				if (flag==0){
					new AlertDialog.Builder(MainActivity.this)
					.setMessage(AppResource.noCameraAuthority)
					.setPositiveButton(AppResource.buttonEnter,new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
								finish();
							}
					})
					.show();
					return;
				}
				
			}

			
			cameraRt = (360+(90-getRotation()))%360;
			Log.i("cameraRt", cameraRt+"");
			Camera.Parameters parameters = camera.getParameters();
			if(1 == devType){
				camera.setDisplayOrientation(cameraRt);
				parameters.setRotation(cameraRt);
			}
			
			
			//���ú����ĶԽ���������
//			List<String> allFocus = parameters.getSupportedFocusModes();  
//            for(String ff:allFocus){  
//                Log.i("FOCUSMODEL:", ff + "...FOCUS...");  
//            }  
//            if(allFocus.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)){  
//            	parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);  // FOCUS_MODE_CONTINUOUS_PICTURE FOCUS_MODE_AUTO  
//            	Log.i("FOCUSMODEL:", "FOCUS_MODE_CONTINUOUS_PICTURE");  
//            }  
            
            
//			parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
//			parameters.setPreviewSize(surfaceViewHeight,surfaceViewWidth);
//			parameters.setPictureSize(photoHeight,photoWidth);
			//��ȡ����ߴ����
			List<Size> pictureSizes = camera.getParameters().getSupportedPictureSizes();
			pictureSize = MyCamPara.getInstance().getPictureSize(pictureSizes, 2000);  
			List<Size> previewSizes = camera.getParameters().getSupportedPreviewSizes();
			if(1 == devType){
				previewSize = MyCamPara.getInstance().getPreviewSize(previewSizes, screenPoint.y);  
			}else{
				previewSize = MyCamPara.getInstance().getPreviewSize(previewSizes, screenPoint.x);  
			}
			
			
			parameters.setPictureSize(pictureSize.width, pictureSize.height);  
			parameters.setPreviewSize(previewSize.width, previewSize.height);
			
//			parameters.setPictureSize(pictureSize.height, pictureSize.width);  
//			parameters.setPreviewSize(previewSize.height,previewSize.width);
			
			
			parameters.setJpegQuality(100);
			camera.setParameters(parameters);
			try {
				camera.setPreviewDisplay(holder);
			} catch (IOException e) {
				e.printStackTrace();
			}
			camera.startPreview();
			faceView = (FaceView)findViewById(R.id.face_view);
			mMainHandler.sendEmptyMessageDelayed(EventUtil.CAMERA_HAS_STARTED_PREVIEW, 800);
			handler.postDelayed(runnable, TIME);
			
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder arg0) {
			// TODO Auto-generated method stub
			if (camera != null){
				camera.stopPreview();
				camera.release();
				camera = null;
			}
		}


	}
	
	public void btnGetPhotoClick(View v){
		if (myAdapter == null || myAdapter.getCount()==0) return;
		if (!getValidated() && enterCount>=MAXPERSONS){
			SharedPreferences xmlSP = getSharedPreferences("SP", MODE_PRIVATE);
			Editor editor = xmlSP.edit();
			editor.putInt("enterCount", enterCount);
			editor.commit();
			Toast.makeText(this, AppResource.validTip, Toast.LENGTH_LONG).show();
			return;
		}
		if (camera!=null && !showPhoto){
				((Button)this.findViewById(R.id.getPhoto)).setClickable(false);
				savingPhoto = true;
//				MainActivity.this.findViewById(R.id.drawRect).invalidate();
				camera.takePicture(null, null, new MyPictureCallback());
				
		}
	}
	
	
	
	
	
	
	
	//����������
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	//����˵�����
	public boolean onOptionsItemSelected(MenuItem item){
		int item_id = item.getItemId();  
		  
        switch (item_id)  
        {  
            case R.id.action_settings:  
            	btnSetupClick(null);
                break;  
            case R.id.action_photo:  
            	btnCameraSetup(null);
                break; 
            case R.id.action_edittxt:
            	btnEditText(null);
            	break;
        }  
        return true;  
	}

	/*����һ������ʱ���ڲ���*/  
	LinearLayout layout_tip;
	TextView tv_tip;
    class MyCount extends CountDownTimer {     
        public MyCount(long millisInFuture, long countDownInterval) {     
            super(millisInFuture, countDownInterval);     
        }     
        @Override     
        public void onFinish() {     
        	takeHandlePhoto(); 
        	layout_tip.setVisibility(View.GONE);
        }     
        @Override     
        public void onTick(long millisUntilFinished) {     
            tv_tip.setText(millisUntilFinished / 1000 + AppResource.setCameraTip);     
        }  
    }
    
    private void takeHandlePhoto(){
    	camera.takePicture(null, null, new PictureCallback() {
			
			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
				Bitmap mBitmap = null;
				if(null != data){  
					camera.stopPreview(); 
					faceView.clearFaces();
					faceView.setVisibility(View.INVISIBLE);
	                mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);//data���ֽ����ݣ����������λͼ
					//���ͼ��δ��ת90������ת
					Matrix matrix = null;
					if (1==devType){
						if (mBitmap.getWidth()>mBitmap.getHeight()){
							matrix = new Matrix();
							matrix.setRotate(90);
						}
					}
					
		//            //��ȡͼ��
					int h = (int)(mBitmap.getWidth()/(float)whRatio);
					if (h > mBitmap.getHeight()){
						h = mBitmap.getHeight();
					}
					Bitmap rectBitmap = null;
					if (null!=matrix){
						rectBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), h, matrix, true);
					}else{
						rectBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), h);
					}
		            Bitmap saveBitmap = ImageFilter.shrink(rectBitmap, photoWidth, photoHeight);
		            setPreviewBitmap(saveBitmap);
		            PhotoParameterClass cameraSetup = new PhotoParameterClass(MainActivity.this, saveBitmap);
					cameraSetup.init();
					findViewById(R.id.drawRect).invalidate();
		            if(mBitmap != null && !mBitmap.isRecycled()){
		            	mBitmap.recycle();
		            	mBitmap = null;
		            }
		            if(rectBitmap != null && !rectBitmap.isRecycled()){
		            	rectBitmap.recycle();
		            	rectBitmap = null;
		            }
		            System.gc();
		            
				}
			}
		});
    }

	private void btnCameraSetup(View v) {
		if (camera!=null && !showPhoto){
			layout_tip = (LinearLayout) this.findViewById(R.id.layout_tip);
			tv_tip = (TextView) this.findViewById(R.id.tv_tip);
			layout_tip.setVisibility(View.VISIBLE);
			MyCount  mc = new MyCount(5000, 1000);  
	        mc.start(); 
		}
		
	}


	private void btnEditText(View v) {
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, EditTextActivity.class);
		startActivity(intent);
		
	}
	
	public void showEdit(View v){
		setEnterFileVisible(true);
	}
	
	private String getEnterFileName(){
		EditText et = (EditText) this.findViewById(R.id.enterfilename);
		if (et.getVisibility()==View.VISIBLE){
			return et.getText().toString();
		}else{
			return "";
		}
	}
	
	private void setEnterFileVisible(Boolean visible){
		EditText et = (EditText) this.findViewById(R.id.enterfilename);
		if (visible){
			et.setVisibility(View.VISIBLE);
		}else{
			et.setVisibility(View.GONE);
		}
	}
	
	private void setEnterFileFocus(){
		EditText et = (EditText) this.findViewById(R.id.enterfilename);
		if (et.getVisibility()==View.VISIBLE){
			//et.setText("");
			et.selectAll();
			et.requestFocus();
		}
	}
	
	public void initCameraStatus(){
		findViewById(R.id.drawRect).invalidate();
		camera.startPreview();
		faceView.setVisibility(View.VISIBLE);
		//mMainHandler.sendEmptyMessageDelayed(EventUtil.GETPHOTOREADING, 10);
	}


}
