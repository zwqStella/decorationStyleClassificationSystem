package pictureA;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GLCM{
	
	public static int counter = 1;
	
	public static final String midsea = "D:\\Homework\\2016实训\\家装图片集\\地中海\\1-";
	public static final String europe = "D:\\Homework\\2016实训\\家装图片集\\欧式古典\\2-";
	public static final String china = "D:\\Homework\\2016实训\\家装图片集\\中式古典\\3-";
	public static final String modern = "D:\\Homework\\2016实训\\家装图片集\\现代简约\\4-";
	public static final String garden = "D:\\Homework\\2016实训\\家装图片集\\田园风\\5-";
	public static final String midseaC = "D:\\Homework\\2016实训\\家装图片集\\地中海\\cuts\\";
	public static final String europeC = "D:\\Homework\\2016实训\\家装图片集\\欧式古典\\cuts\\";
	public static final String chinaC = "D:\\Homework\\2016实训\\家装图片集\\中式古典\\cuts\\";
	public static final String modernC = "D:\\Homework\\2016实训\\家装图片集\\现代简约\\cuts\\";
	public static final String gardenC = "D:\\Homework\\2016实训\\家装图片集\\田园风\\cuts\\";

	public static void initial() throws IOException{
		cut(midsea,midseaC);
		cut(europe,europeC);
		cut(china,chinaC);
		cut(modern,modernC);
		cut(garden,gardenC);
		/*
		scan1(midseaC);
		scan1(europeC);
		scan1(chinaC);
		scan1(modernC);
		scan1(gardenC);
		*/
		System.out.println("finished");
	}
	
	public static double[] picAna(String path) throws IOException{
		double[] resultList = new double[5];
		cut3(path,"temp\\",100,100);
		 File file=new File("temp\\");
		 File[] tempList = file.listFiles();
		  for (int i = 0; i < tempList.length; i++) {
			  if (tempList[i].isFile()) {
				  double [][]temp = getMatrix(tempList[i]);
				  if(temp == null){
					  continue;
				  }
				  	resultList[0] = getDirectory(midseaC,temp,tempList[i].getPath());
					resultList[1] = getDirectory(europeC,temp,tempList[i].getPath());
					resultList[2] = getDirectory(chinaC,temp,tempList[i].getPath());
					resultList[3] = getDirectory(modernC,temp,tempList[i].getPath());
					resultList[4] = getDirectory(gardenC,temp,tempList[i].getPath());
			  }
		   }
		/*
		  for (int i = 0; i < tempList.length; i++) {
			  if (tempList[i].isFile()) {
				  tempList[i].delete();
			  }
		   }
		*/
		return resultList;
	}
	
	
	public static double getDirectory(String path,double [][]m1,String source) throws IOException{
		  double goodNum = 0;
		  File file=new File(path);
		  File[] tempList = file.listFiles();
		  for (int i = 0; i < tempList.length; i++) {
			  if (tempList[i].isFile()) {
				  double [][]m2 = getMatrix(tempList[i]);
				  if(m2 == null){
					  continue;
				  }
				  double result = Main.GetSimilarity2(m1,m2);
				  goodNum += result;
			  }
		   }
		return goodNum/tempList.length;
	}
	
	public static void scan1(String path) throws IOException{
		String tail = ".jpg";
		for(int i = 1; i < 15 ; i++){
			for(int j = 0 ; j < 6 ; j++){
				for(int k = 0; k < 4 ; k++){
					File file = new File(path+i+"_"+j+"_"+k+tail);
					if(!file.exists()){
						continue;
					}else{
						double [][]temp = getMatrix(file);
						if(temp == null){
							//System.out.println(i+"_"+j+"_"+k);
							file.delete();
						}else{
							if(scan2(path,i,temp) < 3){
								file.delete();
							}
						}
					}
				}
			}
		}
	}
	public static int scan2(String path,int order,double [][]com) throws IOException{
		int c = 0;
		String tail = ".jpg";
		for(int i = 1; i < 15 ; i++){
			if(order == i){
				continue;
			}
			for(int j = 0 ; j < 6 ; j++){
				for(int k = 0; k < 4 ; k++){
					File file = new File(path+i+"_"+j+"_"+k+tail);
					if(!file.exists()){
						continue;
					}else {
						double [][]temp = getMatrix(file);
						if(temp == null){
							file.delete();
						}else{
							double simi = Main.GetSimilarity2(temp, com) ;
							if(simi < 90){
								c++;
							}
						}
					}						
				}
			}
		}
		return c;
	}
	
	public static double[][] getMatrix(File file) throws IOException{
		BufferedImage img = ImageIO.read(file);
	    int r,g,b;//记录R、G、B的值 
	    int grey;
	    int width=img.getWidth();//图片宽度  
	    int height=img.getHeight();//图片高度  
	    int pix[]= new int [width*height];//像素个数  
	    pix = img.getRGB(0, 0, width, height, pix, 0, width);//将图片的像素值存到数组里 
	    int max=0,min=255;
	    for(int i=0; i<width*height; i++)   
	       {    
	           r = pix[i]>>16 & 0xff; 
	           g = pix[i]>>8 & 0xff;    
	           b = pix[i] & 0xff;
	           pix[i] = (r*30+g*59+b*11)/100;
	           if(pix[i] > max){
	        	   max = pix[i];
	           }
	           if(pix[i] < min){
	        	   min = pix[i];
	           }
	       }
	    if(max - min < 32){
	    	System.out.println(file.getPath()+"it's a bad picture");
	    	return null;
	    }
	    double[][] result = new double[4][4];
	    matrix0(height,width,pix,max,min,result);
	    matrix1(height,width,pix,max,min,result);
	    matrix2(height,width,pix,max,min,result);
	    matrix3(height,width,pix,max,min,result);
	    
	    return result;
	}
	
	public static void matrix0(int height, int width,int [] pix,int max, int min,double [][] result){ //135度角
		double[][] matrix = new double[32][32];
	    int gap = (max-min)/32 + 1;
	    for(int i=0; i<width*(height-1); i++){    
	    	int temp1,temp2;
	    		if((i+1)%width == 0){
	    			continue;
	    		}else{
	    			temp1 = (pix[i]-min)/gap;
	    			temp2 = (pix[i+width+1]-min)/gap;
	    			if(temp1 == temp2){
	    				matrix[temp1][temp2]++;
	    			}else{
	    				matrix[temp1][temp2]++;
		    			matrix[temp2][temp1]++;
	    			}
	    		}	
	       }
	    for(int i = 0 ; i < 32 ; i++){
	    	for(int j = 0; j < 32 ; j++){
	    		matrix[i][j] /= (pix.length - 1)*(pix.length - 1);
	    	}
	    }
	    result[0][0] = getASM(matrix);
	    result[0][1] = getCON(matrix);
	    result[0][2] = getENT(matrix);
	    result[0][3] = getCOR(matrix);
	}
	
	public static void matrix1(int height, int width,int [] pix,int max, int min,double [][] result){ //0度角
		double[][] matrix = new double[32][32];
	    int gap = (max-min)/32 + 1;
	    for(int i=0; i<width*height; i++)   {    
	    	if((i+1)%width == 0){
	    		continue;
	    	}else{
	    		int temp1,temp2;
	    		temp1 = (pix[i]-min)/gap;
	    		temp2 = (pix[i+1]-min)/gap;
	    		if(temp1 == temp2){
	    			matrix[temp1][temp2]++;
	    		}else{
	    			matrix[temp1][temp2]++;
	    			matrix[temp2][temp1]++;
	    		}
	    	}
	     }
	    for(int i = 0 ; i < 32 ; i++){
	    	for(int j = 0; j < 32 ; j++){
	    		matrix[i][j] /= (pix.length - 1)*pix.length;
	    	}
	    }
	    result[1][0] = getASM(matrix);
	    result[1][1] = getCON(matrix);
	    result[1][2] = getENT(matrix);
	    result[1][3] = getCOR(matrix);
	}
	
	public static void matrix2(int height, int width,int [] pix,int max, int min,double [][] result){ //90度角
		double[][] matrix = new double[32][32];
	    int gap = (max-min)/32 + 1;
	    for(int i=0; i<width*(height-1); i++)   {    
	    	int temp1,temp2;
	    	temp1 = (pix[i]-min)/gap;
	    	temp2 = (pix[i+width]-min)/gap;
	    	if(temp1 == temp2){
	    			matrix[temp1][temp2]++;
	    	}else{
	    			matrix[temp1][temp2]++;
		    		matrix[temp2][temp1]++;
	    	}
	    }	

	    for(int i = 0 ; i < 32 ; i++){
	    	for(int j = 0; j < 32 ; j++){
	    		matrix[i][j] /= (pix.length - 1)*pix.length;
	    	}
	    }
	    result[2][0] = getASM(matrix);
	    result[2][1] = getCON(matrix);
	    result[2][2] = getENT(matrix);
	    result[2][3] = getCOR(matrix);
	}
	
	public static void matrix3(int height, int width,int [] pix,int max, int min,double [][] result){ //45度角
		double[][] matrix = new double[32][32];
	    int gap = (max-min)/32 + 1;
	    for(int i=0; i<width*(height-1); i++)   
	       {    
	    	int temp1,temp2;
	    		if(i%width == 0){
	    			continue;
	    		}else{
	    			temp1 = (pix[i]-min)/gap;
	    			temp2 = (pix[i+width-1]-min)/gap;
	    			if(temp1 == temp2){
	    				matrix[temp1][temp2]++;
	    			}else{
	    				matrix[temp1][temp2]++;
		    			matrix[temp2][temp1]++;
	    			}
	    		}	
	       }
	    for(int i = 0 ; i < 32 ; i++){
	    	for(int j = 0; j < 32 ; j++){
	    		matrix[i][j] /= (pix.length - 1)*(pix.length - 1);
	    	}
	    }
	    result[3][0] = getASM(matrix);
	    result[3][1] = getCON(matrix);
	    result[3][2] = getENT(matrix);
	    result[3][3] = getCOR(matrix);
	}
	
	public static double getASM(double [][] matrix){
		double asm = 0;
	    for(int i = 0 ; i < 32 ; i++){
	    	for(int j = 0; j < 32 ; j++){
	    		asm += matrix[i][j]*matrix[i][j];
	    	}
	    }
		return asm*100000000;
	}
	
	public static double getCON(double [][] matrix){
		double con = 0;
	    for(int i = 0 ; i < 32 ; i++){
	    	for(int j = 0; j < 32; j++){
	    		con += matrix[i][j]*(i-j)*(i-j);
	    	}
	    }
		return con*1000;
	}
	
	public static double getENT(double [][] matrix){
		double ent = 0;
	    for(int i = 0 ; i < 32 ; i++){
	    	for(int j = 0; j < 32 ; j++){
	    		if(matrix[i][j] == 0){
	    			continue;
	    		}
	    		ent += -matrix[i][j]*Math.log(matrix[i][j]);
	    	}
	    }
		return ent*1000;
	}
	
	public static double getCOR(double [][] matrix){
		double cor = 0;
		double ui = 0,uj = 0,si = 0,sj = 0;
	    for(int i = 0 ; i < 32 ; i++){
	    	for(int j = 0; j < 32 ; j++){
	    		ui += matrix[i][j]*i;
	    		uj += matrix[i][j]*j;
	    	}
	    }
	    for(int i = 0 ; i < 32 ; i++){
	    	for(int j = 0; j < 32 ; j++){
	    		si += matrix[i][j]*(1-ui)*(1-ui);
	    		sj += matrix[i][j]*(1-uj)*(1-uj);
	    	}
	    }
	    si = Math.sqrt(Math.abs(si));
	    sj = Math.sqrt(Math.abs(sj));
	    for(int i = 0 ; i < 32 ; i++){
	    	for(int j = 0; j < 32 ; j++){
	    		cor +=(i*j*matrix[i][j]-ui*uj)/(si*sj+1);
	    	}
	    }
		return cor*0.01;
	}
	
	public static double[][] getMatrix(String path) throws IOException{
		return getMatrix(new File(path));
	}
	
	public static void cut(String cla,String claC) throws IOException{
		String tail = ".jpg";
		int i = 1;
		double re = (double)0.0;
		while(i<15){
			cut3(cla+i+tail,claC,100,100);
            counter ++;
			i++;
		}
	}
	
	public static void cut3(String srcImageFile, String descDir,
            int destWidth, int destHeight) {
        try {
            if(destWidth<=0) destWidth = 100; // 切片宽度
            if(destHeight<=0) destHeight = 100; // 切片高度
            // 读取源图像
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            int srcWidth = bi.getHeight(); // 源图宽度
            int srcHeight = bi.getWidth(); // 源图高度
            if (srcWidth > destWidth && srcHeight > destHeight) {
                Image img;
                ImageFilter cropFilter;
                Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
                int cols = 0; // 切片横向数量
                int rows = 0; // 切片纵向数量
                // 计算切片的横向和纵向数量
                if (srcWidth % destWidth == 0) {
                    cols = srcWidth / destWidth;
                } else {
                    cols = (int) Math.floor(srcWidth / destWidth);
                }
                if (srcHeight % destHeight == 0) {
                    rows = srcHeight / destHeight;
                } else {
                    rows = (int) Math.floor(srcHeight / destHeight);
                }
                // 循环建立切片
                // 改进的想法:是否可用多线程加快切割速度
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        // 四个参数分别为图像起点坐标和宽高
                        // 即: CropImageFilter(int x,int y,int width,int height)
                        cropFilter = new CropImageFilter(j * destWidth, i * destHeight,
                                destWidth, destHeight);
                        img = Toolkit.getDefaultToolkit().createImage(
                                new FilteredImageSource(image.getSource(),
                                        cropFilter));
                        BufferedImage tag = new BufferedImage(destWidth,
                                destHeight, BufferedImage.TYPE_BYTE_GRAY );
                        Graphics g = tag.getGraphics();
                        g.drawImage(img, 0, 0, null); // 绘制缩小后的图
                        g.dispose();
                        // 输出为文件
                        ImageIO.write(tag, "JPEG", new File(descDir
                                +counter+ "_" + i + "_" + j + ".jpg"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}