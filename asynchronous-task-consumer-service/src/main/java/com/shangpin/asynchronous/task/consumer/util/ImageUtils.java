package com.shangpin.asynchronous.task.consumer.util;


import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import lombok.extern.slf4j.Slf4j;

/**
 * 图片处理工具类：<br>
 * 功能：缩放图像、切割图像、图像类型转换、彩色转黑白、文字水印、图片水印等
 * @author Administrator
 */
@Slf4j
public class ImageUtils {
	private static Logger loggerInfo = Logger.getLogger("info");
	/**
     * 几种常见的图片格式
     */
    public static String IMAGE_TYPE_GIF = "gif";// 图形交换格式
    public static String IMAGE_TYPE_JPG = "jpg";// 联合照片专家组
    public static String IMAGE_TYPE_JPEG = "jpeg";// 联合照片专家组
    public static String IMAGE_TYPE_BMP = "bmp";// 英文Bitmap（位图）的简写，它是Windows操作系统中的标准图像文件格式
    public static String IMAGE_TYPE_PNG = "png";// 可移植网络图形
    public static String IMAGE_TYPE_PSD = "psd";// Photoshop的专用格式Photoshop
    
    /**
     * 程序入口：用于测试
     * @param args
     */
    public static void main(String[] args){
    	 // 1-缩放图像：
        // 方法一：按比例缩放
//        ImageUtils.scale("E:\\处理好的图片\\2015-12-09\\", "E:\\TTTTT\\", 2, false);//测试OK
        // 方法二：按高度和宽度缩放
//        ImageUtils.scale2("E:\\处理好的图片\\2015-12-09\\", "E:\\TTTTT\\", 500, 300, false);//测试OK
    	
//    	ImageUtils.checkImageSize("E:\\abc.jpg");
//    	String replaceSpecialChar = ImageUtils.replaceSpecialChar("http://dynamic.forzieri.com/is/image/Forzieri/confezione+manieri?scl=1");
//    	System.out.println(replaceSpecialChar);
//    	ImageUtils.downImage("http://dynamic.forzieri.com/is/image/Forzieri/confezione+manieri?scl=1", "E://aaa//", "test.jpg");
    	ImageUtils.batchZoomScale("E:\\other\\image\\", "E:\\other\\tmp\\", 10, false);
    }
    /**
     * 下载图片
     * @param url
     * @param filepath  /usr/local/app/supplierName/picture/
     * @param filename
     * @return 成功返回图片绝对路径,失败返回""
     */
    public static String downImage(String url,String filepath,String filename){
    	System.out.println(url);
    	System.out.println("下载"+filepath+filename);
    	// 创建文件对象  
        File f = new File(filepath+filename);  
        if (f.exists()) {
        	System.out.println("image has been download");
			return "";
		}
    	HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient httpClient = httpClientBuilder.build();
		HttpClientContext context = HttpClientContext.create();
		context.setRequestConfig(RequestConfig.custom()
				.setConnectionRequestTimeout(1000*60*5)
				.setConnectTimeout(1000*60*5)
				.setSocketTimeout(1000*60*5)
				.build());
		String string = replaceSpecialChar(url);
		HttpGet get = new HttpGet(string);
		try {
			CloseableHttpResponse response = httpClient.execute(get,context);
			if (response.getStatusLine().getStatusCode() == 200) {  
                byte[] result = EntityUtils.toByteArray(response.getEntity());  
                BufferedOutputStream bw = null;  
                try {  
					
                    // 创建文件路径  
                    if (!f.getParentFile().exists())  
                        f.getParentFile().mkdirs();  
                    // 写入文件  
                    bw = new BufferedOutputStream(new FileOutputStream(filepath+filename));  
                    bw.write(result);  
                } catch (Exception e) {  
                	System.out.println("保存图片出错");
                	return "";
                } finally {  
                    try {  
                        if (bw != null)  
                            bw.close();  
                    } catch (Exception e) {  
                    	System.out.println("关闭出错");
                    }  
                }  
            }  
		} catch (Exception e) {
			loggerInfo.info("图片未知错误"+e.getMessage());
			return "";
		}
    	return filepath+filename;
    }
    /**
     * 检查图片尺寸和大小。
     * @return "",图片>1M,图片尺寸>800
     */
    public static String checkImageSize(String filePath){
    	String memo = "";
    	if (StringUtils.isNotEmpty(filePath)) {
    		FileInputStream fis = null;
    		
    		try {
    			File file = new File(filePath);
    			fis = new FileInputStream(file);
    			int available = fis.available();
    			fis.close();
    			if (available>1048576) {
    				memo = "图片>1M ";
    			}
    			BufferedImage src = ImageIO.read(file);
    			int width = src.getWidth(); // 得到源图宽
    			int height = src.getHeight(); // 得到源图长
    			
    			if (width>800||height>800) {
    				memo = memo + "图片尺寸>800 ";
    			}
    		} catch (Exception e) {
    			loggerInfo.info("检查图片出错"+filePath);
    		}
		}
    	return memo;
    }
    /**
     * 单个文件 缩放图像（按比例缩放）
     * @param file
     * @param scale
     * @param flag
     * @return
     * @throws Exception
     */
    public static BufferedImage zoomScaleOfFile(File file,int scale, boolean flag) throws Exception{
   	 	BufferedImage src = ImageIO.read(file); // 读入文件
        return zoomScale(src,scale,flag);
   }
    /**
     * 二进制流 缩放图像（按比例缩放）
     * @param input
     * @param scale 缩放比例
     * @param flag 缩放选择:true 放大; false 缩小;
     * @return
     * @throws Exception
     */
    public static BufferedImage zoomScaleOfByteArrayInputStream(ByteArrayInputStream input,int scale, boolean flag) throws Exception{
    	 BufferedImage src = ImageIO.read(input);
         return zoomScale(src,scale,flag);
    }
    /**
     * 缩放图像（按比例缩放）
     * @param src
     * @param scale
     * @param flag
     * @return
     * @throws Exception
     */
    private static BufferedImage zoomScale(BufferedImage src,int scale, boolean flag) throws Exception{
    	int width = src.getWidth(); // 得到源图宽
        int height = src.getHeight(); // 得到源图长
        if (flag) {// 放大
            width = width * scale;
            height = height * scale;
        } else {// 缩小
            width = width / scale;
            height = height / scale;
        }
        Image image = src.getScaledInstance(width, height,
                Image.SCALE_DEFAULT);
        BufferedImage tag = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics g = tag.getGraphics();
        g.drawImage(image, 0, 0, null); // 绘制缩小后的图
        g.dispose();
        return tag;
    }
    
    /**
     * 缩放图像（按比例缩放）
     * @param srcImageFile 存放源图像文件的文件夹 如 E:\\image\\
     * @param result 存放缩放后的图像的文件夹 如 E:\\newimage\\
     * @param scale 缩放比例
     * @param flag 缩放选择:true 放大; false 缩小;
     */
    public static void batchZoomScale(String srcImageFile, String result,
            int scale, boolean flag) {
    	File folder = new File(srcImageFile);
    	for(File file : folder.listFiles()){
    		try {
    			String fileName = file.getName();
                BufferedImage tag = zoomScaleOfFile(file,scale,flag);
                new File(result).mkdir();
                String newFileName = result+fileName;
                ImageIO.write(tag, "JPG", new File(newFileName));// 输出到文件流
            } catch (Exception e) {
                e.printStackTrace();
                log.error("批量按比例缩放图片异常："+e.getMessage(),e); 
            }
    	}
        
    }
    /**
     * 单个文件缩放图像（按高度和宽度缩放）
     * @param file
     * @param height 缩放后的高度
     * @param width 缩放后的宽度
     * @param bb 比例不对时是否需要补白：true为补白; false为不补白;
     * @return
     * @throws Exception
     */
    public static BufferedImage singleScale2OfFile(File file,int height, int width, boolean bb) throws Exception{
    	BufferedImage bi = ImageIO.read(file);
    	return singleScale2(bi,height,width,bb);
    }
    /**
     * 二进制流缩放图像（按高度和宽度缩放）
     * @param input
     * @param height 缩放后的高度
     * @param width 缩放后的宽度
     * @param bb 比例不对时是否需要补白：true为补白; false为不补白;
     * @return
     * @throws Exception
     */
    public static BufferedImage singleScale2OfByteArrayInputStream(ByteArrayInputStream input,int height, int width, boolean bb) throws Exception{
    	BufferedImage bi = ImageIO.read(input);
    	return singleScale2(bi,height,width,bb);
    }
    /**
     * 缩放图像（按高度和宽度缩放）
     * @param bi 缩放的对象
     * @param height 缩放后的高度
     * @param width 缩放后的宽度
     * @param bb 比例不对时是否需要补白：true为补白; false为不补白;
     * @return
     * @throws Exception
     */
    private static BufferedImage singleScale2(BufferedImage bi,int height, int width, boolean bb) throws Exception{
    	double ratio = 0.0; // 缩放比例                
        Image itemp = bi.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
        // 计算比例
        if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
            if (bi.getHeight() > bi.getWidth()) {
                ratio = (new Integer(height)).doubleValue()
                        / bi.getHeight();
            } else {
                ratio = (new Integer(width)).doubleValue() / bi.getWidth();
            }
            AffineTransformOp op = new AffineTransformOp(AffineTransform
                    .getScaleInstance(ratio, ratio), null);
            itemp = op.filter(bi, null);
        }
        if (bb) {//补白
            BufferedImage image = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.setColor(Color.white);
            g.fillRect(0, 0, width, height);
            if (width == itemp.getWidth(null))
                g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2,
                        itemp.getWidth(null), itemp.getHeight(null),
                        Color.white, null);
            else
                g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0,
                        itemp.getWidth(null), itemp.getHeight(null),
                        Color.white, null);
            g.dispose();
            itemp = image;
        }
        return (BufferedImage) itemp;
    }
    
    /**
     * 批量缩放图像（按高度和宽度缩放）
     * @param srcImageFile 存放源图像文件的文件夹 如 E:\\image\\
     * @param result 存放缩放后的图像的文件夹 如 E:\\newimage\\
     * @param height 缩放后的高度
     * @param width 缩放后的宽度
     * @param bb 比例不对时是否需要补白：true为补白; false为不补白;
     */
    public static void scale2(String srcImageFile, String result, int height, int width, boolean bb) {
    	File folder = new File(srcImageFile);
    	for(File file : folder.listFiles()){
    		try {
    			String fileName = file.getName();
    			BufferedImage itemp = singleScale2OfFile(file,height,width,bb);
                new File(result).mkdir();
                String newFileName = result+fileName;
                ImageIO.write(itemp, "JPG", new File(newFileName));
            } catch (Exception e) {
            	e.printStackTrace(); 
                log.error("压缩图片异常："+e.getMessage(),e);
            }
    	}   	
    }
    
    /**
     * 图像切割(按指定起点坐标和宽高切割)
     * @param srcImageFile 源图像地址
     * @param result 切片后的图像地址
     * @param x 目标切片起点坐标X
     * @param y 目标切片起点坐标Y
     * @param width 目标切片宽度
     * @param height 目标切片高度
     */
    public final static void cut(String srcImageFile, String result,
            int x, int y, int width, int height) {
        try {
            // 读取源图像
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            int srcWidth = bi.getHeight(); // 源图宽度
            int srcHeight = bi.getWidth(); // 源图高度
            if (srcWidth > 0 && srcHeight > 0) {
                Image image = bi.getScaledInstance(srcWidth, srcHeight,
                        Image.SCALE_DEFAULT);
                // 四个参数分别为图像起点坐标和宽高
                // 即: CropImageFilter(int x,int y,int width,int height)
                ImageFilter cropFilter = new CropImageFilter(x, y, width, height);
                Image img = Toolkit.getDefaultToolkit().createImage(
                        new FilteredImageSource(image.getSource(),
                                cropFilter));
                BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics g = tag.getGraphics();
                g.drawImage(img, 0, 0, width, height, null); // 绘制切割后的图
                g.dispose();
                // 输出为文件
                ImageIO.write(tag, "JPEG", new File(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 图像切割（指定切片的行数和列数）
     * @param srcImageFile 源图像地址
     * @param descDir 切片目标文件夹
     * @param rows 目标切片行数。默认2，必须是范围 [1, 20] 之内
     * @param cols 目标切片列数。默认2，必须是范围 [1, 20] 之内
     */
    public final static void cut2(String srcImageFile, String descDir,
            int rows, int cols) {
        try {
            if(rows<=0||rows>20) rows = 2; // 切片行数
            if(cols<=0||cols>20) cols = 2; // 切片列数
            // 读取源图像
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            int srcWidth = bi.getHeight(); // 源图宽度
            int srcHeight = bi.getWidth(); // 源图高度
            if (srcWidth > 0 && srcHeight > 0) {
                Image img;
                ImageFilter cropFilter;
                Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
                int destWidth = srcWidth; // 每张切片的宽度
                int destHeight = srcHeight; // 每张切片的高度
                // 计算切片的宽度和高度
                if (srcWidth % cols == 0) {
                    destWidth = srcWidth / cols;
                } else {
                    destWidth = (int) Math.floor(srcWidth / cols) + 1;
                }
                if (srcHeight % rows == 0) {
                    destHeight = srcHeight / rows;
                } else {
                    destHeight = (int) Math.floor(srcWidth / rows) + 1;
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
                                destHeight, BufferedImage.TYPE_INT_RGB);
                        Graphics g = tag.getGraphics();
                        g.drawImage(img, 0, 0, null); // 绘制缩小后的图
                        g.dispose();
                        // 输出为文件
                        ImageIO.write(tag, "JPEG", new File(descDir
                                + "_r" + i + "_c" + j + ".jpg"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 图像切割（指定切片的宽度和高度）
     * @param srcImageFile 源图像地址
     * @param descDir 切片目标文件夹
     * @param destWidth 目标切片宽度。默认200
     * @param destHeight 目标切片高度。默认150
     */
    public final static void cut3(String srcImageFile, String descDir,
            int destWidth, int destHeight) {
        try {
            if(destWidth<=0) destWidth = 200; // 切片宽度
            if(destHeight<=0) destHeight = 150; // 切片高度
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
                    cols = (int) Math.floor(srcWidth / destWidth) + 1;
                }
                if (srcHeight % destHeight == 0) {
                    rows = srcHeight / destHeight;
                } else {
                    rows = (int) Math.floor(srcHeight / destHeight) + 1;
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
                                destHeight, BufferedImage.TYPE_INT_RGB);
                        Graphics g = tag.getGraphics();
                        g.drawImage(img, 0, 0, null); // 绘制缩小后的图
                        g.dispose();
                        // 输出为文件
                        ImageIO.write(tag, "JPEG", new File(descDir
                                + "_r" + i + "_c" + j + ".jpg"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 图像类型转换：GIF->JPG、GIF->PNG、PNG->JPG、PNG->GIF(X)、BMP->PNG
     * @param srcImageFile 源图像地址
     * @param formatName 包含格式非正式名称的 String：如JPG、JPEG、GIF等
     * @param destImageFile 目标图像地址
     */
    public final static void convert(String srcImageFile, String formatName, String destImageFile) {
        try {
            File f = new File(srcImageFile);
            f.canRead();
            f.canWrite();
            BufferedImage src = ImageIO.read(f);
            ImageIO.write(src, formatName, new File(destImageFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 彩色转为黑白 
     * @param srcImageFile 源图像地址
     * @param destImageFile 目标图像地址
     */
    public final static void gray(String srcImageFile, String destImageFile) {
        try {
            BufferedImage src = ImageIO.read(new File(srcImageFile));
            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
            ColorConvertOp op = new ColorConvertOp(cs, null);
            src = op.filter(src, null);
            ImageIO.write(src, "JPEG", new File(destImageFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 给图片添加文字水印
     * @param pressText 水印文字
     * @param srcImageFile 源图像地址
     * @param destImageFile 目标图像地址
     * @param fontName 水印的字体名称
     * @param fontStyle 水印的字体样式
     * @param color 水印的字体颜色
     * @param fontSize 水印的字体大小
     * @param x 修正值
     * @param y 修正值
     * @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
     */
    public final static void pressText(String pressText,
            String srcImageFile, String destImageFile, String fontName,
            int fontStyle, Color color, int fontSize,int x,
            int y, float alpha) {
        try {
            File img = new File(srcImageFile);
            Image src = ImageIO.read(img);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);
            g.setColor(color);
            g.setFont(new Font(fontName, fontStyle, fontSize));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    alpha));
            // 在指定坐标绘制水印文字
            g.drawString(pressText, (width - (getLength(pressText) * fontSize))
                    / 2 + x, (height - fontSize) / 2 + y);
            g.dispose();
            ImageIO.write((BufferedImage) image, "JPEG", new File(destImageFile));// 输出到文件流
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 给图片添加文字水印
     * @param pressText 水印文字
     * @param srcImageFile 源图像地址
     * @param destImageFile 目标图像地址
     * @param fontName 字体名称
     * @param fontStyle 字体样式
     * @param color 字体颜色
     * @param fontSize 字体大小
     * @param x 修正值
     * @param y 修正值
     * @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
     */
    public final static void pressText2(String pressText, String srcImageFile,String destImageFile,
            String fontName, int fontStyle, Color color, int fontSize, int x,
            int y, float alpha) {
        try {
            File img = new File(srcImageFile);
            Image src = ImageIO.read(img);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);
            g.setColor(color);
            g.setFont(new Font(fontName, fontStyle, fontSize));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    alpha));
            // 在指定坐标绘制水印文字
            g.drawString(pressText, (width - (getLength(pressText) * fontSize))
                    / 2 + x, (height - fontSize) / 2 + y);
            g.dispose();
            ImageIO.write((BufferedImage) image, "JPEG", new File(destImageFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 给图片添加图片水印
     * @param pressImg 水印图片
     * @param srcImageFile 源图像地址
     * @param destImageFile 目标图像地址
     * @param x 修正值。 默认在中间
     * @param y 修正值。 默认在中间
     * @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
     */
    public final static void pressImage(String pressImg, String srcImageFile,String destImageFile,
            int x, int y, float alpha) {
        try {
            File img = new File(srcImageFile);
            Image src = ImageIO.read(img);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(wideth, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, wideth, height, null);
            // 水印文件
            Image src_biao = ImageIO.read(new File(pressImg));
            int wideth_biao = src_biao.getWidth(null);
            int height_biao = src_biao.getHeight(null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    alpha));
            g.drawImage(src_biao, (wideth - wideth_biao) / 2,
                    (height - height_biao) / 2, wideth_biao, height_biao, null);
            // 水印文件结束
            g.dispose();
            ImageIO.write((BufferedImage) image,  "JPEG", new File(destImageFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 计算text的长度（一个中文算两个字符）
     * @param text
     * @return
     */
    public final static int getLength(String text) {
        int length = 0;
        for (int i = 0; i < text.length(); i++) {
            if (new String(text.charAt(i) + "").getBytes().length > 1) {
                length += 2;
            } else {
                length += 1;
            }
        }
        return length / 2;
    }
    /**
     * 替换url中的特殊字符  空格
     */
    private static String replaceSpecialChar(String url){
    	return url.replace(" ", "%20");
//    	http://188.217.192.104/foto/P16/M%20MISSONI/KD0KM02Z2300NO_7_P.JPG
//    	http://188.217.192.104/foto/P16/COACH%20NEW%20YORK/36600DK()FOG.JPG
//    	http://188.217.192.104/foto/P16/COACH%20NEW%20YORK/36600DK()FOG_3_D.JPG
    }
}
