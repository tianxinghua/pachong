package com.shangpin.api.airshop.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

/**
 * 
 * @author hubiao
 * @dateTime 2014-06-07 本工具对实现对IMG与PDF相互转换。 运行测试需要导入以下2个jar包 itext-2.0.2.jar
 *           PDFRenderer.jar
 *
 */
@SuppressWarnings("unused")
public class PdfToImgUtils {
	public static void main(String[] args) throws Exception {
		 pdfToJpg(new File("C://iog//DHL//2138337180.pdf"),"C://iog//DHL//","100000");
	}

	private static void listOrder() {

		File[] listFiles = new File("C://iog//DHL//test").listFiles();
		TreeMap<Integer, File> tree = new TreeMap<Integer, File>();
		for (File f : listFiles) {
			tree.put(Integer.parseInt(f.getName().replaceAll(".jpg$", "")), f);
		}
		for (Entry<Integer, File> eif : tree.entrySet()) {
			System.out.println(eif.getKey() + "=" + eif.getValue().toString());
		}
	}

	/**
	 * @param list
	 *            图片集合
	 * @param file
	 *            保存路径
	 * @return true,合并完成 如果文件名不是1.jpg，2.jpg，3.jpg，4.jpg这样的。则需要自己重写TreeMap的排序方式！
	 */
	public static boolean imgMerageToPdf(File[] list, File file)
			throws Exception {
		// 1：对图片文件通过TreeMap以名称进行自然排序
		Map<Integer, File> mif = new TreeMap<Integer, File>();
		for (File f : list)
			mif.put(Integer.parseInt(f.getName().replaceAll(".jpg$", "")), f);

		// 2：获取第一个Img的宽、高做为PDF文档标准
		ByteArrayOutputStream baos = new ByteArrayOutputStream(2048 * 3);
		InputStream is = new FileInputStream(mif.get(1));
		for (int len; (len = is.read()) != -1;)
			baos.write(len);

		baos.flush();
		Image image = Image.getInstance(baos.toByteArray());
		float width = image.getWidth();
		float height = image.getHeight();
		baos.close();

		// 3:通过宽高 ，实例化PDF文档对象。
		Document document = new Document(new Rectangle(width, height));
		PdfWriter pdfWr = PdfWriter.getInstance(document, new FileOutputStream(
				file));
		document.open();

		// 4：获取每一个图片文件，转为IMG对象。装载到Document对象中
		for (Entry<Integer, File> eif : mif.entrySet()) {
			// 4.1:读取到内存中
			baos = new ByteArrayOutputStream(2048 * 3);
			is = new FileInputStream(eif.getValue());
			for (int len; (len = is.read()) != -1;)
				baos.write(len);
			baos.flush();

			// 4.2通过byte字节生成IMG对象
			image = Image.getInstance(baos.toByteArray());
			Image.getInstance(baos.toByteArray());
			image.setAbsolutePosition(0.0f, 0.0f);

			// 4.3：添加到document中
			document.add(image);
			document.newPage();
			baos.close();
		}

		// 5：释放资源
		document.close();
		pdfWr.close();

		return true;
	}

	/**
	 * 
	 * @param source
	 *            源文件
	 * @param target
	 *            目标文件
	 * @param x
	 *            读取源文件中的第几页
	 */
	public static String pdfToJpg(File file, String target, String number)
			throws Exception {

		List<String> list = new ArrayList<String>();
		StringBuffer str = new StringBuffer();
		RandomAccessFile rea = new RandomAccessFile(file, "r");
		// 将流读取到内存中，然后还映射一个PDF对象
		FileChannel channel = rea.getChannel();
		ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0,
				channel.size());
		PDFFile pdfFile = new PDFFile(buf);
		File fil = new File(target+"//"+number);
		if (!fil.exists()) {
			fil.mkdirs();
		}
//		List<BufferedOutputStream> list = new ArrayList<BufferedOutputStream>();
		for (int i = 1; i <= pdfFile.getNumPages(); i++) {
			PDFPage page = pdfFile.getPage(i);
			if (page == null) {
				break;
			}
			// get the width and height for the doc at the default zoom
//			java.awt.Rectangle rect = new java.awt.Rectangle(0, 0, 800, 800);
			java.awt.Rectangle rect = new java.awt.Rectangle(0, 0, (int) page
					.getBBox().getWidth(), (int) page.getBBox().getHeight());
			
			java.awt.Image img = page.getImage(rect.width, rect.height, // width
																		// &
					rect, // clip rect
					null, // null for the ImageObserver
					true, // fill background with white
					true // block until drawing is done
					);
			BufferedImage tag = new BufferedImage(rect.width, rect.height,
					BufferedImage.TYPE_INT_RGB);
			System.out.println(rect.width + "  == "+rect.height);
			tag.getGraphics().drawImage(img, 0,0, rect.width, rect.height,
					null);
			FileOutputStream out = new FileOutputStream(target + "//"+number+"//"
					+ (number + "_" + i) + ".jpg"); // 输出到文件流
			list.add(number+"/"
					+ (number + "_" + i) + ".jpg");
			str.append(",");
			str.append(number+"/"
					+ (number + "_" + i) + ".jpg");
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(tag); // JPEG编码
			out.close();
		}
		channel.close();
		rea.close();
		unmap(buf);
		return str.toString().substring(1);
	}

	@SuppressWarnings("unchecked")
	private static void unmap(final Object buffer) {
		AccessController.doPrivileged(new PrivilegedAction() {
			public Object run() {
				try {
					Method getCleanerMethod = buffer.getClass().getMethod(
							"cleaner", new Class[0]);
					getCleanerMethod.setAccessible(true);
					sun.misc.Cleaner cleaner = (sun.misc.Cleaner) getCleanerMethod
							.invoke(buffer, new Object[0]);
					cleaner.clean();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});
	}

	/**
	 * @param source
	 *            源PDF文件路径
	 * @param target
	 *            保存PDF文件路径
	 * @param pageNum
	 *            提取PDF中第pageNum页
	 * @throws Exception
	 */
	private static void pdfExtraction(String source, String target, int pageNum)
			throws Exception {
		// 1：创建PDF读取对象
		PdfReader pr = new PdfReader(source);
		System.out.println("this document " + pr.getNumberOfPages() + " page");

		// 2：将第page页转为提取，创建document对象
		Document doc = new Document(pr.getPageSize(pageNum));

		// 3：通过PdfCopy转其单独存储
		PdfCopy copy = new PdfCopy(doc, new FileOutputStream(new File(target)));
		doc.open();
		doc.newPage();

		// 4：获取第1页，装载到document中。
		PdfImportedPage page = copy.getImportedPage(pr, pageNum);
		copy.addPage(page);

		// 5：释放资源
		copy.close();
		doc.close();
		pr.close();
	}

	/**
	 * @param pdfFile
	 *            源PDF文件
	 * @param imgFile
	 *            图片文件
	 */
	private static void jpgToPdf(File pdfFile, File imgFile) throws Exception {
		// 文件转img
		InputStream is = new FileInputStream(pdfFile);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (int i; (i = is.read()) != -1;) {
			baos.write(i);
		}
		baos.flush();

		// 取得图像的宽和高。
		Image img = Image.getInstance(baos.toByteArray());
		float width = img.getWidth();
		float height = img.getHeight();
		img.setAbsolutePosition(0.0F, 0.0F);// 取消偏移
		System.out.println("width = " + width + "\theight" + height);

		// img转pdf
		Document doc = new Document(new Rectangle(width, height));
		PdfWriter pw = PdfWriter
				.getInstance(doc, new FileOutputStream(imgFile));
		doc.open();
		doc.add(img);

		// 释放资源
		System.out.println(doc.newPage());
		pw.flush();
		baos.close();
		doc.close();
		pw.close();
	}

}
