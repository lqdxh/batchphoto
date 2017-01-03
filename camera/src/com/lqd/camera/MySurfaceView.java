package com.lqd.camera;

import java.io.IOException;      
import android.content.Context;      
import android.graphics.PixelFormat;      
import android.hardware.Camera;      
import android.util.Log;      
import android.view.SurfaceHolder;      
import android.view.SurfaceView;   

public class MySurfaceView  extends SurfaceView implements SurfaceHolder.Callback{
	SurfaceHolder holder;      
    Camera myCamera;      
    @SuppressWarnings("deprecation")
	public MySurfaceView(Context context)      
    {      
        super(context);      
        holder = getHolder();//���surfaceHolder����      
        holder.addCallback(this);      
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//��������        
    }  
    @Override      
    public void surfaceCreated(SurfaceHolder holder) {      
        if(myCamera == null)      
        {      
            myCamera = Camera.open();//�������,���ܷ��ڹ��캯���У���Ȼ������ʾ����.      
            try {      
                myCamera.setPreviewDisplay(holder);      
            } catch (IOException e) {      
                e.printStackTrace();      
            }      
        }             
    }      
    @Override      
    public void surfaceChanged(SurfaceHolder holder, int format, int width,      
            int height) {      
        myCamera.startPreview();              
    }      
        @Override      
    public void surfaceDestroyed(SurfaceHolder holder) {      
        myCamera.stopPreview();//ֹͣԤ��      
         myCamera.release();//�ͷ������Դ      
         myCamera = null;      
    } 

}
