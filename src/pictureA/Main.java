package pictureA;

import java.io.BufferedReader;
import pic.algorithm.sift.*;
import pic.utility.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.awt.List;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;


public class Main {

	public static final String midsea = "D:\\Homework\\2016实训\\家装图片集\\地中海\\1-";
	public static final String europe = "D:\\Homework\\2016实训\\家装图片集\\欧式古典\\2-";
	public static final String china = "D:\\Homework\\2016实训\\家装图片集\\中式古典\\3-";
	public static final String modern = "D:\\Homework\\2016实训\\家装图片集\\现代简约\\4-";
	public static final String garden = "D:\\Homework\\2016实训\\家装图片集\\田园风\\5-";
	
	public static final String midsea1 = "D:\\Homework\\2016实训\\家装图片集\\测试集\\地中海\\1-";
	public static final String europe1 = "D:\\Homework\\2016实训\\家装图片集\\测试集\\\\欧式古典\\2-";
	public static final String china1 = "D:\\Homework\\2016实训\\家装图片集\\测试集\\\\中式古典\\3-";
	public static final String modern1 = "D:\\Homework\\2016实训\\家装图片集\\测试集\\\\现代简约\\4-";
	public static final String garden1 = "D:\\Homework\\2016实训\\家装图片集\\测试集\\\\田园风\\5-";
	
	
	public static final String trainPath = "trainfile\\test_train.txt";
	public static final String predictPath = "trainfile\\test_predict.txt";
	
	public static void main(String []args) throws IOException{

		//ArrayList< java.util.List<MyPoint>> myList = new ArrayList< java.util.List<MyPoint>>();
		//prepareTrainModel(myList);
		//preparePredictModel(myList);
		colorAna();

		//GLCM.initial();
		
	}
	
	public static double siftCompare(BufferedImage source,String cla) throws IOException{
		String tail = ".jpg";
		int i = 1;
		double re = (double)0.0;
		while(i<15){
			BufferedImage target = ImageIO.read(new File(cla+i+tail));
			i++;
			double a = sift(source,target);
			re += a;
		}
		return re/14;
	}
	
	
	public static double sift(BufferedImage sourceImage,BufferedImage targetImage){
	     HashMap<Integer,double[][]> result=ImageTransform.getGaussPyramid(Image_Utility.imageToDoubleArray(sourceImage), 20, 3,1.6);
	     HashMap<Integer,double[][]> dog=ImageTransform.gaussToDog(result, 6);
	     HashMap<Integer,java.util.List<MyPoint>> keyPoints=ImageTransform.getRoughKeyPoint(dog,6);
	     keyPoints=ImageTransform.filterPoints(dog, keyPoints, 10,0.03);
	     java.util.List<MyPoint> v1=ImageTransform.getCharacterVectors(sourceImage);
	     java.util.List<MyPoint> v2=ImageTransform.getCharacterVectors(targetImage);
	     int num=ImageTransform.getSimilarPointsNum(v1, v2);
	     double a = (double)num/v1.size(); 
	     System.out.println("关键点数目"+v1.size()+"&"+v2.size()+" 相似关键点数目"+num+"	"+a);
	    
	     return a;
	}
	
	public static void colorAna(){
		try {
			String [] arg = {trainPath,"trainfile\\model_r1.txt"};
			String []parg = {predictPath,"trainfile\\model_r1.txt","trainfile\\out_r1.txt"};
			svm_train t = new svm_train();
			svm_predict p = new svm_predict();
			t.main(arg);
			p.main(parg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static java.util.List<MyPoint> getPoints(BufferedImage sourceImage){
		 HashMap<Integer,double[][]> result=ImageTransform.getGaussPyramid(Image_Utility.imageToDoubleArray(sourceImage), 20, 3,1.6);
	     HashMap<Integer,double[][]> dog=ImageTransform.gaussToDog(result, 6);
	     HashMap<Integer,java.util.List<MyPoint>> keyPoints=ImageTransform.getRoughKeyPoint(dog,6);
	     keyPoints=ImageTransform.filterPoints(dog, keyPoints, 10,0.03);
	     java.util.List<MyPoint> v=ImageTransform.getCharacterVectors(sourceImage);
	     return v;
	}
	
	public static double[][] siftResult(ArrayList< java.util.List<MyPoint>> myList) throws IOException{
		
		double [][] result = new double[70][5];
		
		for(int i = 1; i < 15 ;i++){
			BufferedImage src = ImageIO.read(new File(midsea+i+".jpg"));
			myList.add(getPoints(src));
		}
		
		for(int i = 1; i < 15 ;i++){
			BufferedImage src = ImageIO.read(new File(europe+i+".jpg"));
			myList.add(getPoints(src));
		}
		for(int i = 1; i < 15 ;i++){
			BufferedImage src = ImageIO.read(new File(china+i+".jpg"));
			myList.add(getPoints(src));
		}
		for(int i = 1; i < 15 ;i++){
			BufferedImage src = ImageIO.read(new File(modern+i+".jpg"));
			myList.add(getPoints(src));
		}
		for(int i = 1; i < 15 ;i++){
			BufferedImage src = ImageIO.read(new File(garden+i+".jpg"));
			myList.add(getPoints(src));
		}
		
		for(int i = 0; i < 70 ;i++){
			int cla0 = i/14;
			for(int j = 0; j < 70 ; j++){
				int cla1 = j/14;
				double temp = (double)ImageTransform.getSimilarPointsNum(myList.get(i), myList.get(j))/myList.get(i).size();
				if(temp>result[i][cla1] && temp < 0.99){
					result[i][cla1] = temp;
				}
			}
		}
		
		return result;
		
	}
	
	public static double[][] siftResult1(ArrayList< java.util.List<MyPoint>> compareList) throws IOException{
		
		double [][] result = new double[30][5];
		
		ArrayList< java.util.List<MyPoint>> myList = new ArrayList< java.util.List<MyPoint>>();
		for(int i = 15; i < 21 ;i++){
			BufferedImage src = ImageIO.read(new File(midsea1+i+".jpg"));
			myList.add(getPoints(src));
		}
		for(int i = 15; i < 21 ;i++){
			BufferedImage src = ImageIO.read(new File(europe1+i+".jpg"));
			myList.add(getPoints(src));
		}
		for(int i = 15; i < 21 ;i++){
			BufferedImage src = ImageIO.read(new File(china1+i+".jpg"));
			myList.add(getPoints(src));
		}
		for(int i = 15; i < 21 ;i++){
			BufferedImage src = ImageIO.read(new File(modern1+i+".jpg"));
			myList.add(getPoints(src));
		}
		for(int i = 15; i <21 ;i++){
			BufferedImage src = ImageIO.read(new File(garden1+i+".jpg"));
			myList.add(getPoints(src));
		}
		
		for(int i = 0; i < 30 ;i++){
			int cla0 = i/14;
			for(int j = 0; j < 70 ; j++){
				int cla1 = j/14;
				double temp = (double) ImageTransform.getSimilarPointsNum(myList.get(i), compareList.get(j))/(14*myList.get(i).size());
				if(temp>result[i][cla1]){
					result[i][cla1] = temp;
				}
			}
		}
		
		return result;
		
	}
	
	public static void prepareTrainModel(ArrayList< java.util.List<MyPoint>> myList){
		try {
			clear(trainPath);
			double [][] sift = siftResult(myList);
			int card = 0;
			for(int i = 1; i < 15 ;i++){
				File src = new File(midsea+i+".jpg");
				double [] a;
				a = getResult(src);
				write(trainPath,+1+"  "+"1:"+a[0]+" "+"2:"+a[1]+" "+"3:"+a[2]+" "+"4:"+a[3]+" "+"5:"+a[4]
						+" "+"6:"+sift[card][0]+" "+"7:"+sift[card][1]+" "+"8:"+sift[card][2]+" "+"9:"+sift[card][3]+" "+"10:"+sift[card][4]);
				card++;
			}
			for(int i = 1; i < 15 ;i++){
				File src = new File(europe+i+".jpg");
				double [] a;
				a = getResult(src);
				write(trainPath,+2+"  "+"1:"+a[0]+" "+"2:"+a[1]+" "+"3:"+a[2]+" "+"4:"+a[3]+" "+"5:"+a[4]
						+" "+"6:"+sift[card][0]+" "+"7:"+sift[card][1]+" "+"8:"+sift[card][2]+" "+"9:"+sift[card][3]+" "+"10:"+sift[card][4]);
				card++;
			}
			for(int i = 1; i < 15 ;i++){
				File src = new File(china+i+".jpg");
				double [] a;
				a = getResult(src);
				write(trainPath,+3+"  "+"1:"+a[0]+" "+"2:"+a[1]+" "+"3:"+a[2]+" "+"4:"+a[3]+" "+"5:"+a[4]
						+" "+"6:"+sift[card][0]+" "+"7:"+sift[card][1]+" "+"8:"+sift[card][2]+" "+"9:"+sift[card][3]+" "+"10:"+sift[card][4]);
				card++;
			}
			for(int i = 1; i < 15 ;i++){
				File src = new File(modern+i+".jpg");
				double [] a;
				a = getResult(src);
				write(trainPath,+4+"  "+"1:"+a[0]+" "+"2:"+a[1]+" "+"3:"+a[2]+" "+"4:"+a[3]+" "+"5:"+a[4]
						+" "+"6:"+sift[card][0]+" "+"7:"+sift[card][1]+" "+"8:"+sift[card][2]+" "+"9:"+sift[card][3]+" "+"10:"+sift[card][4]);
				card++;
			}
			for(int i = 1; i < 15 ;i++){
				File src = new File(garden+i+".jpg");
				double [] a;
				a = getResult(src);
				write(trainPath,+5+"  "+"1:"+a[0]+" "+"2:"+a[1]+" "+"3:"+a[2]+" "+"4:"+a[3]+" "+"5:"+a[4]
						+" "+"6:"+sift[card][0]+" "+"7:"+sift[card][1]+" "+"8:"+sift[card][2]+" "+"9:"+sift[card][3]+" "+"10:"+sift[card][4]);
				card++;
			}
			System.out.println("finished!");
		} catch (IOException e) {
			System.out.println("未找到文件");
			e.printStackTrace();
		} 
	}
	
	public static void preparePredictModel(ArrayList< java.util.List<MyPoint>> compareList){
		try {
			clear(predictPath);
			double [][] sift = siftResult1(compareList);
			int card = 0;
			for(int i = 15; i < 21 ;i++){
				File src = new File(midsea1+i+".jpg");
				double [] a;
				a = getResult(src);
				write(predictPath,+1+"  "+"1:"+a[0]+" "+"2:"+a[1]+" "+"3:"+a[2]+" "+"4:"+a[3]+" "+"5:"+a[4]
						+" "+"6:"+sift[card][0]+" "+"7:"+sift[card][1]+" "+"8:"+sift[card][2]+" "+"9:"+sift[card][3]+" "+"10:"+sift[card][4]);
				card++;
			}
			for(int i = 15; i < 21 ;i++){
				File src = new File(europe1+i+".jpg");
				double [] a;
				a = getResult(src);
				write(predictPath,+2+"  "+"1:"+a[0]+" "+"2:"+a[1]+" "+"3:"+a[2]+" "+"4:"+a[3]+" "+"5:"+a[4]
						+" "+"6:"+sift[card][0]+" "+"7:"+sift[card][1]+" "+"8:"+sift[card][2]+" "+"9:"+sift[card][3]+" "+"10:"+sift[card][4]);
				card++;
			}
			for(int i = 15; i < 21 ;i++){
				File src = new File(china1+i+".jpg");
				double [] a;
				a = getResult(src);
				write(predictPath,+3+"  "+"1:"+a[0]+" "+"2:"+a[1]+" "+"3:"+a[2]+" "+"4:"+a[3]+" "+"5:"+a[4]
						+" "+"6:"+sift[card][0]+" "+"7:"+sift[card][1]+" "+"8:"+sift[card][2]+" "+"9:"+sift[card][3]+" "+"10:"+sift[card][4]);
				card++;
			}
			for(int i = 15; i < 21 ;i++){
				File src = new File(modern1+i+".jpg");
				double [] a;
				a = getResult(src);
				write(predictPath,+4+"  "+"1:"+a[0]+" "+"2:"+a[1]+" "+"3:"+a[2]+" "+"4:"+a[3]+" "+"5:"+a[4]
						+" "+"6:"+sift[card][0]+" "+"7:"+sift[card][1]+" "+"8:"+sift[card][2]+" "+"9:"+sift[card][3]+" "+"10:"+sift[card][4]);
				card++;
			}
			for(int i = 15; i < 21 ;i++){
				File src = new File(garden1+i+".jpg");
				double [] a;
				a = getResult(src);
				write(predictPath,+5+"  "+"1:"+a[0]+" "+"2:"+a[1]+" "+"3:"+a[2]+" "+"4:"+a[3]+" "+"5:"+a[4]
						+" "+"6:"+sift[card][0]+" "+"7:"+sift[card][1]+" "+"8:"+sift[card][2]+" "+"9:"+sift[card][3]+" "+"10:"+sift[card][4]);
				card++;
			}
			System.out.println("finished!");
		} catch (IOException e) {
			System.out.println("未找到文件");
			e.printStackTrace();
		} 
	}
	
	public static String result(int re){
		switch(re){
		case 1: return "地中海";
		case 2: return "欧式古典";
		case 3: return "中式古典";
		case 4: return "现代简约";
		case 5: return "田园风";
		}
		return null;
	}
	
	public static double[] getResult(File src1) throws IOException{
		BufferedImage src = ImageIO.read(src1);
		
		//double [] nums = new double[5];
		double [] order = new double[5];
		//int [] result = new int[5];
		double[][] srcDist = GetHistogram(src);
		
		order[0] = compare(srcDist,midsea);
		order[1] = compare(srcDist,europe);
		order[2] = compare(srcDist,china);
		order[3] = compare(srcDist,modern);
		order[4] = compare(srcDist,garden);
		
		return order;
		/*
		for(int i = 0; i < 5; i++){
			nums[i] = order[i];
			//System.out.println(nums[i]);
		}
		for(int i = 0; i < 5 ;i++){
			for(int j = i+1; j < 5 ; j++){
				if(nums[i] < nums[j]){
					double temp = nums[i];
					nums[i] = nums[j];
					nums[j] = temp;
				}
			}
		}
		for(int i = 0; i < 5 ; i++){
			for(int j = 0; j < 5 ; j++){
				if(nums[i] == order[j]){
					result[i] = j+1;
					break;
				}
			}
		}
		
		double [][] glcm = GLCM.getMatrix(src1);
		for(int i = 0; i < 4 ; i++){
			for(int j = 0; j < 4; j++){
				System.out.print(glcm[i][j] + "	");
				BigDecimal bg = new BigDecimal(glcm[i][j]);  
				glcm[i][j] = bg.setScale(16, BigDecimal.ROUND_HALF_UP).doubleValue(); 
			}
			System.out.println();
		}
		*/
		//write(path,+cla+"  "+"1:"+order[0]+" "+"2:"+order[1]+" "+"3:"+order[2]+" "+"4:"+order[3]+" "+"5:"+order[4]
				//+" "+"6:"+a1+" "+"7:"+a2+" "+"8:"+a3+" "+"9:"+a4
						//+" "+"10:"+glcm[1][0]+" "+"11:"+glcm[1][1]+" "+"12:"+glcm[1][2]+" "+"13:"+glcm[1][3]
								//+" "+"14:"+glcm[2][0]+" "+"15:"+glcm[2][1]+" "+"16:"+glcm[2][2]+" "+"17:"+glcm[2][3]
										//+" "+"18:"+glcm[3][0]+" "+"19:"+glcm[3][1]+" "+"20:"+glcm[3][2]+" "+"21:"+glcm[3][3]
												//);
	}
	
	public static double compare(double[][] srcDist,String cla) throws IOException{
		String tail = ".jpg";
		int i = 1;
		double re = (double)0.0;
		while(i<15){
			BufferedImage src1 = ImageIO.read(new File(cla+i+tail));
			i++;
			double a = GetSimilarity2(GetHistogram(src1),srcDist);
			if(a > re && a != 1){
				re = a;
			}
		}
		return re;
	}
	
	public static double [][] GetHistogram(BufferedImage img)  
    {  
		   double [][] histgram=new double [3][20];  			////////////////////////////
		   /*
		   int [] hHist = new int[360];
		   int [] sHist = new int[100];
		   int [] vHist = new int[100];
		   */
	       int width=img.getWidth();//图片宽度  
	       int height=img.getHeight();//图片高度  
	       int pix[]= new int [width*height];//像素个数  
	       int r,g,b;//记录R、G、B的值 
	       float h,s,v;//记录HSV值
	       pix = img.getRGB(0, 0, width, height, pix, 0, width);//将图片的像素值存到数组里  
	       //int smax=0,smin=100,vmax=0,vmin=100;
	       for(int i=0; i<width*height; i++)   
	       {    
	           r = pix[i]>>16 & 0xff; //提取R   
	           g = pix[i]>>8 & 0xff;    
	           b = pix[i] & 0xff;     
	           float min, max, delta;  
	           min = Math.min( r, Math.min( g, b ));  
	           max = Math.max( r, Math.max( g, b ));  
	           v = (float) (max/2.55);               // v  
	           delta = (max - min)*100;  
	           if( max != 0 ){
	               s =delta / max;       // s  
	               if( r == max )  
	            	   h = ( g - b ) / delta;     // between yellow & magenta  
	               else if( g == max )  
	            	   h = 2 + ( b - r ) / delta; // between cyan & yellow  
	               else  
	            	   h = 4 + ( r - g ) / delta; // between magenta & cyan  
	              
	               h = h*60;               // degrees  
	               if( h < 0 )  
	            	   h += 360;  
	               
	               int hi,si,vi;
	               hi = (int)h;
	               si = (int)s;
	               vi = (int)v;
	               if(hi == 360){
	            	   hi = 0;
	               }
	               if(si == 100){
	            	   si = 99;
	               }
	               if(vi == 100){
	            	   vi = 99;
	               }
	             /*
	               if(si >smax){
	            	   smax = si;
	               }
	               if(si < smin){
	            	   smin = si;
	               }
	               if(vi >vmax){
	            	   vmax = vi;
	               }
	               if(vi < vmin){
	            	   vmin = vi;
	               }
	               
	               hHist[hi]++;
	               sHist[si]++;
	               vHist[vi]++;
	               */
	               histgram[0][hi/18]++;
	               histgram[1][si/5]++;
	               histgram[2][si/5]++;
	           }else {  
	        	   /*
	        	   hHist[0]++;
	               sHist[0]++;
	               vHist[0]++;
	               */
	        	   histgram[0][0]++;
	               histgram[1][0]++;
	               histgram[2][0]++;
	           }       
	       } 

	       int hn =0,sn=0,vn=0;  
	       for(int j=0;j<20;j++){  							/////////////////////////////////////
	    	   /*
	           hn+=hHist[j];  
	           sn+=sHist[j];  
	           vn+=vHist[j];  
	           */
	    	   hn+=histgram[0][j];  
	           sn+=histgram[1][j];  
	           vn+=histgram[2][j]; 
	       }  
	       /*
	       int sgap = (smax-smin)/10 +1;
	       int vgap = (vmax-vmin)/10 +1;
	       for(int i = 0 ; i < 360 ; i++){
	    	   histgram[0][i/36] += hHist[i];
	       }
	       for(int i = smin ; i <= smax ; i++){
	    	   histgram[1][(i-smin)/sgap] += sHist[i];
	       }
	       for(int i = vmin ; i <= vmax ; i++){
	    	   histgram[2][(i-vmin)/vgap] += vHist[i];
	       }
	       */
	       for(int j=0;j<20;j++)//将直方图每个像素值的总个数进行量化  	//此处有bin大小
	       {  
	           histgram[0][j]/=hn;  
	           histgram[1][j]/=sn;  
	           histgram[2][j]/=vn;  
	       }  
	       /*
	       //输出颜色矩阵
	       for(int i = 0; i < 3; i++){
	    	   for(int j = 0; j < 10; j++){
	    		   System.out.print(histgram[i][j]+"	");
	    	   }
	    	   System.out.println();
	       }
	       System.out.println("-------------------------------------------------------------------");
			*/
	       
	       return histgram;  
    }  
	  
    public static double GetSimilarity2(double [][] Rhistgram,double  [][] Dhistgram)  
    {  
    	double similar=(double)0.0;//相似度  
    	int length = Rhistgram.length;
        for(int i=0;i<length;i++)  
        {  
            for(int j=0;j<Rhistgram[i].length;j++)  
            {  
                similar+=Math.sqrt(Rhistgram[i][j]*Dhistgram[i][j]);  
            }  
        }  
        similar=similar/length;  
        return similar; 

    }  
    public static void write(String filePath, String content) {  
        String str = new String(); //原有txt内容  
        String s1 = new String();//内容更新  
        try {  
            File f = new File(filePath);  
            if (f.exists()) {  
            } else {  
                f.createNewFile();// 不存在则创建  
            }  
            BufferedReader input = new BufferedReader(new FileReader(f));  
  
            while ((str = input.readLine()) != null) {  
                s1 += str + "\n";  
            }  
            input.close();  
            s1 += content;  
  
            BufferedWriter output = new BufferedWriter(new FileWriter(f));  
            output.write(s1);  
            output.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
    public static void clear(String path){
    	  try 
    	   {    
    		   File f5 = new File(path);
    	       FileWriter fw5 = new FileWriter(f5);
    	       BufferedWriter bw1 = new BufferedWriter(fw5);
    	       bw1.write("");
    	   }
    	   catch (Exception e)
    	   {}
    }
    private static BufferedImage grayTran(String imagePath){

        BufferedImage bimg=null;
        try {
            bimg = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int width=bimg.getWidth();
        int height=bimg.getHeight();
	/*	double sx = (double) width / bimg.getWidth();///姝ゅ蹇呴』涓篸ouble 锛屽惁鍒欏緱鍒扮殑sx涓�0锛屽鑷寸粨鏋滀负鍏ㄩ粦鍥�
		double sy = (double) height / bimg.getHeight();

	 	int type = bimg.getType();*/
        BufferedImage targetImage = null;

		/*if (type == BufferedImage.TYPE_CUSTOM) { // handmade
			ColorModel cm = bimg.getColorModel();
			WritableRaster raster = cm.createCompatibleWritableRaster(width,
					height);
			boolean alphaPremultiplied = cm.isAlphaPremultiplied();
			targetImage = new BufferedImage(cm, raster, alphaPremultiplied, null);
		} else*/
        targetImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);//BufferedImage.TYPE_BYTE_BINARY);
		/*Graphics2D g = targetImage.createGraphics();
		// smoother than exlax:
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g.drawRenderedImage(bimg, AffineTransform.getScaleInstance(sx, sy));
		g.dispose();
		*/


        for(int j=0;j<height;j++){
            for(int i=0;i<width;i++){
                int rgb=bimg.getRGB(i, j);
				/*if(i>=1&&j>=1){
				rgb=bimg.getRGB(i-1, j-1)+bimg.getRGB(i-1, j)+bimg.getRGB(i, j-1);
				rgb=rgb/4;}*/
                int c_red=(rgb>>16)&0xFF;
                int c_green=(rgb>>8)&0xFF;
                int c_blue=rgb&0xFF;
                int grayRGB=(int) (0.3 * c_red + 0.59 * c_green + 0.11 * c_blue);////鐏板害鍖�



                rgb=(255<<24)|(grayRGB<<16)|(grayRGB<<8)|grayRGB;///鐏板害鍖栨仮澶�

                targetImage.setRGB(i, j, rgb);

				/*if(grayRGB<180){
					grayRGB=0;
				}else{
					grayRGB=255;
				}/////浜屽�煎寲鍥惧儚
				rgb=(255<<24)|(grayRGB<<16)|(grayRGB<<8)|grayRGB;///鐏板害鍖栧鐞�

				targetImage.setRGB(i, j, rgb);*/

			/*	if(i>0&&j>0&&j<height-1&&(Math.abs(bimg.getRGB(i, j)-bimg.getRGB(i, j-1))+Math.abs(bimg.getRGB(i, j)-bimg.getRGB(i, j+1))>20000)){
					rgb=rgb+20000;
				}

				*/
				/*if(i>0&&j>0&&j<height-1&i<width-1){
					grayRGB=Math.abs(bimg.getRGB(i, j)-bimg.getRGB(i, j-1))+Math.abs(bimg.getRGB(i, j)-bimg.getRGB(i+1, j));
					if(grayRGB+120<255){
						if(grayRGB>=125){
							grayRGB+=125;
						}
					}else{
						grayRGB=0xFFFFFF;
					}
				}
				rgb=(grayRGB<<16)|(grayRGB<<8)|grayRGB;///鐏板�煎寲澶勭悊
*/
//				rgb=255-rgb;////鍙嶈壊

            }
        }


        //Canvas



		/* for (int i=0;i<targetImage.getWidth();i++){
	    	  for(int j=0;j<targetImage.getHeight();j++){
	    		  System.out.print(""+targetImage.getRGB(i,j));
	    	  }

	      }
		 */

        return targetImage;

    }


}
