package com.guduo.wp.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.guduo.wp.domain.User;
import com.guduo.wp.domain.response.ResponseUserInfo;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class MyFileUtil {

	public static final ResponseEntity<byte[]> download(String fileName, HttpServletRequest request) throws IOException{
		
			//TEST init
			//String path="D:\\guduo_work\\GuDuo-ReportDownload\\wp-web\\doc\\whitePaper\\2014年网络剧产业发展研究报告.pdf"; 
		
			fileName = fileName+".pdf";
			String path = System.getProperty("user.dir")+"/doc/whitePaper/"+fileName;
		 	
	        File file=new File(path);  
	        HttpHeaders headers = new HttpHeaders();    
	        
	        fileName=new String(fileName.getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题  
	        headers.setContentDispositionFormData("attachment", fileName);   
	        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);   
	        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),    
	                                          headers, HttpStatus.CREATED);    
	}
	
	public static final ResponseEntity<byte[]> readPdfTmp(String fileName, HttpServletRequest request) throws IOException{
		
		//TEST init
		//String path="D:\\guduo_work\\GuDuo-ReportDownload\\wp-web\\doc\\whitePaper\\2014年网络剧产业发展研究报告.pdf"; 
	
		fileName = fileName+".pdf";
		String path = System.getProperty("user.dir")+"/doc/tmp/"+fileName;
	 	
        File file=new File(path);  
        HttpHeaders headers = new HttpHeaders();    
        
        fileName=new String(fileName.getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题  
        headers.setContentDispositionFormData("attachment", fileName);   
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);   
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),    
                                          headers, HttpStatus.CREATED);    
	}
	public static final ResponseEntity<byte[]> readPdfTmpByUrl(String url, String docName, HttpServletRequest request) throws IOException{
		
		//TEST init
		//String path="D:\\guduo_work\\GuDuo-ReportDownload\\wp-web\\doc\\whitePaper\\2014年网络剧产业发展研究报告.pdf"; 
//	
//		fileName = fileName+".pdf";
//		String path = System.getProperty("user.dir")+"/doc/tmp/"+fileName;
	 	
        File file=new File(url);  
        HttpHeaders headers = new HttpHeaders();  
        
        docName = docName+".pdf";
        docName=new String(docName.getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题  
        headers.setContentDispositionFormData("attachment", docName);   
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);   
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),    
                                          headers, HttpStatus.CREATED);    
	}
	public static boolean deletePdfTmp(String sPath) {  
	    boolean flag = false;  
	    File file = new File(sPath);  
	    // 路径为文件且不为空则进行删除  
	    if (file.isFile() && file.exists()) {  
	        file.delete();  
	        flag = true;  
	    }  
	    return flag;  
	} 
	
	public static final String getPdfPath(String fileName, HttpServletRequest request){
		fileName = fileName+".pdf";
		String dir = System.getProperty("user.dir")+"/doc/whitePaper/"+fileName;
		String path = request.getRequestURL() + dir.substring(dir.lastIndexOf('\\'));
		String retString = path.replaceAll("\\\\","/");
		
		return retString;
	}
	
	//添加文本水印
	public static void addTextMart(String inPdfFile, String outPdfFile, String maskInfo) throws IOException, DocumentException{
		// 待加水印的文件  
        PdfReader reader = new PdfReader(inPdfFile);  
        // 加完水印的文件  
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outPdfFile));  
        
        int total = reader.getNumberOfPages() + 1;  
        
        PdfContentByte content;  
        // 设置字体  
        BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",BaseFont.EMBEDDED);  
        // 水印文字  
        String waterText = "用户："+maskInfo;  
        int j = waterText.length(); // 文字长度  
        char c = 0;  
        int high = 0;// 高度  
        // 循环对每页插入水印  
        for (int i = 1; i < total; i++) {  
            // 水印的起始  
            high = 500;  
            content = stamper.getUnderContent(i);  
            // 开始  
            content.beginText();  
            // 设置颜色  
            content.setColorFill(BaseColor.GRAY);  
            // 设置字体及字号  
            content.setFontAndSize(base, 18);  
            // 设置起始位置  
            content.setTextMatrix(10, 1);  
            // 开始写入水印  
            for (int k = 0; k < j; k++) {  
                content.setTextRise(14);  
                c = waterText.charAt(k);  
                // 将char转成字符串  
                content.showText(c + "");  
                high -= 5;  
            }  
            content.endText();  
  
        }  
        stamper.close();       
	}	
	
//	//添加圖片水印
//	public static void addPdfMark(String InPdfFile, String outPdfFile, String markImagePath, int pageSize) throws Exception {  
//
//		PdfReader reader = new PdfReader(InPdfFile, "PDF".getBytes());
//		PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(outPdfFile));
//		
//		Image img = Image.getInstance(markImagePath);// 插入水印
//
//		img.setAbsolutePosition(150, 100);
//
//		for (int i = 1; i <= pageSize; i++) {
//
//			PdfContentByte under = stamp.getUnderContent(i);
//			under.addImage(img);
//
//		}
//
//		stamp.close();// 关闭
//
//		File tempfile = new File(InPdfFile);
//
//		if (tempfile.exists()) {
//
//			tempfile.delete();
//		}
//
//	} 
	

}
