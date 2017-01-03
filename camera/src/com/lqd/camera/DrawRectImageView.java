package com.lqd.camera;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;  
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;  
import android.graphics.Color;  
import android.graphics.Matrix;
import android.graphics.Paint;  
import android.graphics.Paint.Style;  
import android.graphics.Rect;  
import android.util.AttributeSet;  
import android.util.Log;
import android.view.SurfaceView;
import android.widget.ImageView;  
import android.widget.LinearLayout;

public class DrawRectImageView extends ImageView {
	public static int rectWidth;
	public static int rectHeight;
	
	public DrawRectImageView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        // TODO Auto-generated constructor stub  
    }  
      
    Paint paint = new Paint();  
    {  
        paint.setAntiAlias(true);  
        paint.setColor(Color.BLACK);  
        paint.setStyle(Style.FILL);  
        paint.setStrokeWidth(0.0f);//设置线宽  
        paint.setAlpha(255);  
    };  
    Paint paint2 = new Paint();  
    {  
    	paint2.setAntiAlias(true);  
    	paint2.setColor(Color.WHITE);  
    	paint2.setStyle(Style.FILL);  
    	paint2.setStrokeWidth(1.0f);//设置线宽  
    	paint2.setAlpha(255);  
    };
    Paint paint3 = new Paint();  
    {  
    	paint2.setAntiAlias(true);  
    	paint2.setColor(Color.BLACK);  
    	paint2.setStyle(Style.FILL);  
    	paint2.setStrokeWidth(0.0f);//设置线宽  
    	paint2.setAlpha(255);  
    };  

    public static Bitmap getLoacalBitmap(String url) {
    	Bitmap bitmap = null;
    	try {
             FileInputStream fis = new FileInputStream(url);
             bitmap = BitmapFactory.decodeStream(fis);
             fis.close();
             
        } catch (FileNotFoundException e) {
             e.printStackTrace();
             return null;
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return bitmap;
   }
      
    @Override  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  
        if (((MainActivity) MainActivity.mainActivity).getSavingPhoto())
    	{
    		//正在处理相片，显示黑色
    		canvas.drawRect(new Rect(0, 0, rectWidth, rectHeight), paint3);//绘制矩形  
    	}else if (((MainActivity) MainActivity.mainActivity).isShowPhoto()){
        		Bitmap bmp = ((MainActivity) MainActivity.mainActivity).getPreviewBitmap();
				if (bmp == null){
					canvas.drawRect(new Rect(0, 0, rectWidth, rectHeight), paint2);//绘制矩形  
				}else{
					Matrix matrix = new Matrix();
					float r = ((LinearLayout) ((MainActivity) MainActivity.mainActivity)
							.findViewById(R.id.item_left)).getWidth()
							/ (float) bmp.getWidth();
					matrix.postScale(r, r);
					canvas.drawBitmap(bmp, matrix, paint2);
				}
				if(bmp != null && !bmp.isRecycled()){
					bmp.recycle();
					bmp = null;
	            }
	            System.gc();
        }
        //画白线
        canvas.drawLine(0, rectHeight, rectWidth, rectHeight, paint2);
        //画剩余区域黑块
        canvas.drawRect(new Rect(0, rectHeight, rectWidth, 5000), paint);//绘制矩形  
    }  
}
