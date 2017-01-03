package com.lqd.facedetect;

  
import com.lqd.camera.R;

import android.content.Context;  
import android.graphics.Canvas;  
import android.graphics.Color;  
import android.graphics.Matrix;  
import android.graphics.Paint;  
import android.graphics.Paint.Style;  
import android.graphics.Rect;
import android.graphics.RectF;  
import android.graphics.drawable.Drawable;  
import android.hardware.Camera.CameraInfo;  
import android.hardware.Camera.Face;  
import android.util.AttributeSet;  
import android.widget.ImageView;  
  
public class FaceView extends ImageView {  
    private static final String TAG = "bmCamera";  
    private Context mContext;  
    private Paint mLinePaint;  
    private Face[] mFaces;  
    private Matrix mMatrix = new Matrix();  
    private RectF mRect = new RectF();  
    private static Drawable mFaceIndicator = null;  
    
    
    
    
    
    
    
    public Rect getFaceRect() {
		return new Rect(Math.round(mRect.left), Math.round(mRect.top),  
                Math.round(mRect.right), Math.round(mRect.bottom));
	}


	public FaceView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        // TODO Auto-generated constructor stub  
        //initPaint();  
        mContext = context;  
        if (null == mFaceIndicator) mFaceIndicator = getResources().getDrawable(R.drawable.jujiaokuang);  
    }  
  
  
    public void setFaces(Face[] faces){  
        this.mFaces = faces;  
        invalidate();  
    }  
    public void clearFaces(){  
        mFaces = null;  
        invalidate();  
    }  
      
  
    @Override  
    protected void onDraw(Canvas canvas) {  
        // TODO Auto-generated method stub  
        if(mFaces == null || mFaces.length < 1){  
            return;  
        }  
        boolean isMirror = false;  
//        int Id = CameraInterface.getInstance().getCameraId();  
        int Id = CameraInfo.CAMERA_FACING_BACK;
        if(Id == CameraInfo.CAMERA_FACING_BACK){  
            isMirror = false; //后置Camera无需mirror  
        }else if(Id == CameraInfo.CAMERA_FACING_FRONT){  
            isMirror = true;  //前置Camera需要mirror  
        }  
        
        Util.prepareMatrix(mMatrix, isMirror, 90, getWidth(), getHeight());  
        canvas.save();  
        mMatrix.postRotate(0); //Matrix.postRotate默认是顺时针  
        canvas.rotate(-0);   //Canvas.rotate()默认是逆时针   
        for(int i = 0; i< mFaces.length; i++){  
            mRect.set(mFaces[i].rect);  
            mMatrix.mapRect(mRect);  
            mFaceIndicator.setBounds(Math.round(mRect.left), Math.round(mRect.top),  
                    Math.round(mRect.right), Math.round(mRect.bottom));  
            mFaceIndicator.draw(canvas);  
//          canvas.drawRect(mRect, mLinePaint);  
        }  
        canvas.restore();  
        super.onDraw(canvas);  
    }  
  
    private void initPaint(){  
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);  
//      int color = Color.rgb(0, 150, 255);  
        int color = Color.rgb(98, 212, 68);  
//      mLinePaint.setColor(Color.RED);  
        mLinePaint.setColor(color);  
        mLinePaint.setStyle(Style.STROKE);  
        mLinePaint.setStrokeWidth(2f);  
        mLinePaint.setAlpha(220);  
    }  
} 