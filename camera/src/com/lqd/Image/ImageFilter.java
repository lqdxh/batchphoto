package com.lqd.Image;


import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Point;

public class ImageFilter {

	
	
    /**
     * ����ͼ�����ȡ��Աȶ�
     */
	   public static int clamp(int value)
	    {
	    	return value > 255 ? 255 :(value < 0 ? 0 : value);  
	    }
		public  static Bitmap greyFilter(Bitmap src, double brightness,double contrast)
		{
			if (src==null) return null;
			//���ԴͼƬ���ȺͿ��
			int width=src.getWidth();
			int height=src.getHeight();
			int[] inPixels=new int[width*height];
			int[] outPixels=new int[width*height];
			src.getPixels(inPixels, 0, width, 0, 0, width, height);
			
			//���ȶԱȶȵ���ֵ
			//float brightness = 1.5f;
			//float contrast = 1.0f;
			
			//����һ�����صĺ죬�̣�������
			int index=0;
			int[] rgbmeans=new int[3];
			double redSum=0,greenSum=0,blueSum=0;
			double total=height*width;
			for(int row=0;row<height;row++)
			{
				int ta=0,tr=0,tg=0,tb=0;
				for(int col=0;col<width;col++)
				{
					index=row*width+col;
					ta=(inPixels[index] >> 24) & 0xff;
					tr=(inPixels[index] >> 16) & 0xff;
					tg=(inPixels[index] >> 8) & 0xff;
					tb=inPixels[index] & 0xff;
					redSum+=tr;
					greenSum+=tg;
					blueSum+=tb;
				}
			}
			//���ͼ������ƽ��ֵ
			rgbmeans[0]=(int)(redSum/total);
			rgbmeans[1]=(int)(greenSum/total);
			rgbmeans[2]=(int)(blueSum/total);
			
			//�����Աȶȣ�����
			index = 0;
			for(int row=0;row<height;row++)
			{
				int ta=0,tr=0,tg=0,tb=0;
				for(int col=0;col<width;col++)
				{
					ta=(inPixels[index] >> 24) & 0xff;
					tr=(inPixels[index] >> 16) & 0xff;
					tg=(inPixels[index] >> 8) & 0xff;
					tb=inPixels[index] & 0xff;
					
					//��ȥƽ��ֵ
					tr -=rgbmeans[0];
					tg -=rgbmeans[1];
					tb -=rgbmeans[2];
					
					
					//�����Աȶ�
					tr=(int)(tr * contrast);
					tg=(int)(tg * contrast);
					tb=(int)(tb * contrast);
					
					
					//��������
					tr=(int)((tr+rgbmeans[0])*brightness);
					tg=(int)((tg+rgbmeans[1])*brightness);
					tb=(int)((tb+rgbmeans[2])*brightness); //end;
					/*tr +=(int)(rgbmeans[0] * brightness);
					tg +=(int)(rgbmeans[1] * brightness);
					tb +=(int)(rgbmeans[2] * brightness);  //end;*/
					
					outPixels[index] = (ta << 24) | (clamp(tr) << 16) | (clamp(tg) << 8) | clamp(tb);
					index++;
					
				}
			}
			Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);  
	        bitmap.setPixels(outPixels, 0, width, 0, 0, width, height);
			return bitmap;
		}
		
		
	
	
    /** 
     * �ֲ���ֵ��ͼ����С 
     * @param srcImg Ҫ��С��ͼ����� 
     * @param destWidth ��С��ͼ��Ŀ� 
     * @paramdestHeightn ��С��ͼ��ĸ� 
     * @return ���ش�����ͼ����� 
     */  
    public static Bitmap shrink(Bitmap srcImg, int destWidth, int destHeight) {  
        float scaleW = (float)destWidth/srcImg.getWidth();  
        float scaleH = (float)destHeight/srcImg.getHeight();          
        return shrink(srcImg, scaleW, scaleH);  
    }  
      
    /** 
      * �ֲ���ֵ��ͼ����С 
     * @param srcImg Ҫ��С��ͼ����� 
     * @param scaleW Ҫ��С�Ŀ���� 
     * @param scaleH Ҫ��С�ĸ߱��� 
     * @return ���ش�����ͼ����� 
     */  
    public static Bitmap shrink(Bitmap srcImg, float scaleW, float scaleH) {  
        if(scaleW >1 || scaleH>1) {//���k1 >1 || k2>1����ͼƬ�Ŵ󣬲�����С  
            System.err.println("this is shrink image funcation, please set k1<=1 and k2<=1��");  
            return null;  
        }   
        float ii = 1/scaleW;    //�������м��  
        float jj = 1/scaleH; //�������м��         
        int dd = (int)(ii*jj);   
        //int m=0 , n=0;                  
        int w = srcImg.getWidth();  
        int h = srcImg.getHeight();  
        int m = (int) (scaleW*w);  
        int n = (int) (scaleH*h);  
        int[] pix = new int[w*h];  
        srcImg.getPixels(pix, 0, w, 0, 0, w, h);
        int[] newpix = new int[m*n];  
        for(int j=0; j<n; j++) {  
            for(int i=0; i<m; i++) {  
                int r = 0, g=0, b=0;  
                for(int k=0; k<(int)jj; k++) {  
                    for(int l=0; l<(int)ii; l++) {  
                        r = r + getRComponent(pix[(int)(jj*j+k)*w + (int)(ii*i+l)]);  
                        g = g + getGComponent(pix[(int)(jj*j+k)*w + (int)(ii*i+l)]);  
                        b = b + getBComponent(pix[(int)(jj*j+k)*w + (int)(ii*i+l)]);  
                    }  
                }  
                r = r/dd;  
                g = g/dd;  
                b = b/dd;  
                newpix[j*m + i] = 255<<24 | r<<16 | g<<8 | b;  
            }  
        }  
        Bitmap bitmap = Bitmap.createBitmap(m, n, Config.ARGB_8888);  
        bitmap.setPixels(newpix, 0, m, 0, 0, m, n);
        return bitmap;  
    }
    
    
    
    /** 
     * ˫���Բ�ֵ��ͼ��ķŴ� 
     * @param img Ҫ��С��ͼ����� 
     * @param k1 Ҫ��С���б��� 
     * @param k2 Ҫ��С���б��� 
     * @return ���ش�����ͼ����� 
     */  
    public static Bitmap amplify(Bitmap img, float k1, float k2) {  
        if(k1 <1 || k2<1) {//���k1 <1 || k2<1����ͼƬ��С�����ǷŴ�  
            System.err.println("this is shrink image funcation, please set k1<=1 and k2<=1��");  
            return null;  
        }   
        float ii = 1/k1;    //�������м��  
        float jj = (1/k2); //�������м��       
        int dd = (int)(ii*jj);   
        //int m=0 , n=0;  
        int w = img.getWidth();     //ԭͼƬ�Ŀ�  
        int h = img.getHeight();    //ԭͼƬ�Ŀ�  
        int m = Math.round(k1*w);   //�Ŵ��ͼƬ�Ŀ�  
        int n = Math.round(k2*h);   //�Ŵ��ͼƬ�Ŀ�  
        int[] pix = new int[w*h];  
        img.getPixels(pix, 0, w, 0, 0, w, h);
        /*System.out.println(w + " * " + h); 
        System.out.println(m + " * " + n);*/  
        int[] newpix = new int[m*n];  
          
        for(int j=0; j<h-1; j++){  
            for(int i=0; i<w-1; i++) {  
                int x0 = Math.round(i*k1);  
                int y0 = Math.round(j*k2);  
                int x1, y1;  
                if(i == w-2) {  
                    x1 = m-1;  
                } else {  
                    x1 = Math.round((i+1)*k1);  
                }                 
                if(j == h-2) {  
                    y1 = n-1;  
                } else {  
                    y1 = Math.round((j+1)*k2);  
                }                 
                int d1 = x1 - x0;  
                int d2 = y1 - y0;  
                if(0 == newpix[y0*m + x0]) {  
                    newpix[y0*m + x0] =  pix[j*w+i];  
                }  
                if(0 == newpix[y0*m + x1]) {  
                    if(i == w-2) {  
                        newpix[y0*m + x1] = pix[j*w+w-1];  
                    } else {  
                        newpix[y0*m + x1] =  pix[j*w+i+1];  
                    }                     
                }  
                if(0 == newpix[y1*m + x0]){  
                    if(j == h-2) {  
                        newpix[y1*m + x0] = pix[(h-1)*w+i];  
                    } else {  
                        newpix[y1*m + x0] =  pix[(j+1)*w+i];  
                    }                     
                }  
                if(0 == newpix[y1*m + x1]) {  
                    if(i==w-2 && j==h-2) {  
                        newpix[y1*m + x1] = pix[(h-1)*w+w-1];  
                    } else {  
                        newpix[y1*m + x1] = pix[(j+1)*w+i+1];  
                    }                     
                }  
                int r, g, b;  
                float c;  
                for(int l=0; l<d2; l++) {  
                    for(int k=0; k<d1; k++) {  
                        if(0 == l) {  
                            //f(x,0) = f(0,0) + c1*(f(1,0)-f(0,0))  
                            if(j<h-1 && newpix[y0*m + x0 + k] == 0) {  
                                c = (float)k/d1;  
                                 r = getRComponent(newpix[y0*m + x0]) + (int)(c*(getRComponent(newpix[y0*m + x1]) - getRComponent(newpix[y0*m + x0])));//newpix[(y0+l)*m + k]  
                                 g = getGComponent(newpix[y0*m + x0]) + (int)(c*(getGComponent(newpix[y0*m + x1]) - getGComponent(newpix[y0*m + x0])));  
                                 b = getBComponent(newpix[y0*m + x0]) + (int)(c*(getBComponent(newpix[y0*m + x1]) - getBComponent(newpix[y0*m + x0])));  
                                 newpix[y0*m + x0 + k] = 255<<24 | r<<16 | g<<8 | b;  
                            }  
                            if(j+1<h && newpix[y1*m + x0 + k] == 0) {  
                                 c = (float)k/d1;  
                                    r = getRComponent(newpix[y1*m + x0]) + (int)(c*(getRComponent(newpix[y1*m + x1]) - getRComponent(newpix[y1*m + x0])));  
                                    g = getGComponent(newpix[y1*m + x0]) + (int)(c*(getGComponent(newpix[y1*m + x1]) - getGComponent(newpix[y1*m + x0])));  
                                    b = getBComponent(newpix[y1*m + x0]) + (int)(c*(getBComponent(newpix[y1*m + x1]) - getBComponent(newpix[y1*m + x0])));  
                                    newpix[y1*m + x0 + k] = 255<<24 | r<<16 | g<<8 | b;  
                             }  
                            //System.out.println(c);  
                        } else {  
                            //f(x,y) = f(x,0) + c2*f(f(x,1)-f(x,0))  
                            c = (float)l/d2;  
                            r = getRComponent(newpix[y0*m + x0+k]) + (int)(c*(getRComponent(newpix[y1*m + x0+k]) - getRComponent(newpix[y0*m + x0+k])));  
                            g = getGComponent(newpix[y0*m + x0+k]) + (int)(c*(getGComponent(newpix[y1*m + x0+k]) - getGComponent(newpix[y0*m + x0+k])));  
                            b = getBComponent(newpix[y0*m + x0+k]) + (int)(c*(getBComponent(newpix[y1*m + x0+k]) - getBComponent(newpix[y0*m + x0+k])));  
                            newpix[(y0+l)*m + x0 + k] = 255<<24 | r<<16 | g<<8 | b;  
                            //System.out.println((int)(c*(cm.getRed(newpix[y1*m + x0+k]) - cm.getRed(newpix[y0*m + x0+k]))));  
                        }                 
                    }                     
                    if(i==w-2 || l==d2-1) { //���һ�еļ���  
                        //f(1,y) = f(1,0) + c2*f(f(1,1)-f(1,0))  
                        c = (float)l/d2;  
                        r = getRComponent(newpix[y0*m + x1]) + (int)(c*(getRComponent(newpix[y1*m + x1]) - getRComponent(newpix[y0*m + x1])));  
                        g = getGComponent(newpix[y0*m + x1]) + (int)(c*(getGComponent(newpix[y1*m + x1]) - getGComponent(newpix[y0*m + x1])));  
                        b = getBComponent(newpix[y0*m + x1]) + (int)(c*(getBComponent(newpix[y1*m + x1]) - getBComponent(newpix[y0*m + x1])));  
                        newpix[(y0+l)*m + x1] = 255<<24 | r<<16 | g<<8 | b;  
                    }  
                }  
            }  
        }  
        /* 
        for(int j=0; j<50; j++){ 
            for(int i=0; i<50; i++) { 
                System.out.print(new Color(newpix[j*m + i]).getRed() + "\t");                
            } 
            System.out.println(); 
        } 
        */  
        Bitmap bitmap = Bitmap.createBitmap(m, n, Config.ARGB_8888);  
        bitmap.setPixels(newpix, 0, m, 0, 0, m, n);
        return bitmap;  
    }  
    
       
    
    // ������ص��͸���� A
    private static  int getAComponent(int dot) {
            return (dot & 0xFF000000) >>> 24;
    }

    // ������ص�ĺ�ɫֵ R
    private static int getRComponent(int dot) {
            return (dot & 0x00FF0000) >>> 16;
    }

    // ������ص����ɫֵ G
    private static  int getGComponent(int dot) {
            return (dot & 0x0000FF00) >>> 8;
    }

    // ������ص����ɫֵ B
    private static  int getBComponent(int dot) {
            return (dot & 0x000000FF);
    }
}  
