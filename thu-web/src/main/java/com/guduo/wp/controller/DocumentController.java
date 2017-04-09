package com.guduo.wp.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import net.sf.json.JSONObject;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tools.ant.types.FileList.FileName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import scala.collection.generic.BitOperations.Int;

import ch.qos.logback.core.util.FileUtil;

import com.guduo.cons.Constants;
import com.guduo.cons.ResponseStateEnum;
import com.guduo.wp.domain.Document;
import com.guduo.wp.domain.Report;
import com.guduo.wp.domain.SessionUser;
import com.guduo.wp.domain.User;
import com.guduo.wp.domain.request.RequestGetDoc;
import com.guduo.wp.domain.request.RequestGetUserByToken;
import com.guduo.wp.domain.request.RequestPayForDoc;
import com.guduo.wp.domain.response.ResponseGet;
import com.guduo.wp.domain.response.ResponseGetDoc;
import com.guduo.wp.domain.response.ResponseGetUserByToken;
import com.guduo.wp.domain.response.ResponsePayForDoc;
import com.guduo.wp.domain.response.ResponseUserInfo;
import com.guduo.wp.service.DocumentService;
import com.guduo.wp.service.OrderService;
import com.guduo.wp.service.SessionUserService;
import com.guduo.wp.service.UserService;
import com.guduo.wp.util.HttpUtil;
import com.guduo.wp.util.MyFileUtil;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Event;
import com.pingplusplus.model.Webhooks;

@Controller
@RequestMapping("/doc")
public class DocumentController {

	@Autowired
	private DocumentService docService;
	@Autowired
	private UserService userService;
	@Autowired
	private OrderService orderService;	
	@Autowired
	private SessionUserService sessionUserService;
	
	private HttpUtil httpUtil = new HttpUtil();

	
	//获得周报、月报列表
	@RequestMapping("/getReportList")
	public String getReportList(@RequestParam int reportType, Model mode){
		
		List<Report> reportList = docService.getReportList(reportType);
		mode.addAttribute("reportList", reportList);
		
		//返回不同的报告视图
		if(reportType==0){
			return "view/week_report";
		}else if((reportType==1)){
			return "view/month_report";
		}
		return "";
	}
	
	//获得白皮书
	@RequestMapping("/getDoc")
	public String getDoc(String docName, HttpServletRequest request, HttpServletResponse response){
		ResponseGetDoc responseGetDoc = new ResponseGetDoc();
		
		RequestGetUserByToken requestGetUserByToken = null;
		ResponseGetUserByToken responseGetUserByToken = null;
		
		//检查用户登录情况
		//获得Session Id与token的对应
		String sessionId = request.getSession().getId();
		//TEST--------
		sessionId = "C4B9631D76ECB1B3C2E659D708969A98";
		//TEST--------
		SessionUser sessionUser = sessionUserService.findBySessionId(sessionId);
		ResponseGet responseGet = new ResponseGet();
		if(null==sessionUser){//用户未登录
			responseGetDoc.setState(ResponseStateEnum.USER_NOT_LOGIN.getIndex());
			responseGetDoc.setContent(ResponseStateEnum.USER_NOT_LOGIN.getName());
			//跳转到登录页面
			return "redirect:/login";
		}else{
			//通过Token获得User信息
			requestGetUserByToken = new RequestGetUserByToken();
			String tmp = httpUtil.getUserByToken2(requestGetUserByToken);
			JSONObject jsonObject = JSONObject.fromObject(tmp);
			responseGet = (ResponseGet) JSONObject.toBean(jsonObject, ResponseGet.class);
			if(responseGet.getCode() != 0){//登录过期
				//获取失败
				responseGetDoc.setState(ResponseStateEnum.USER_TOKEN_EXPIRE.getIndex());
				responseGetDoc.setContent(ResponseStateEnum.USER_TOKEN_EXPIRE.getName());
				return "redirect:/login";
			}
		}
		
		//获取用户成功
		int userId = responseGet.getData().getId();		
		ResponseUserInfo user = responseGet.getData();		
		
		//判断用户的支付情况
		int payState = orderService.getPayState(userId, docName);		
		if(payState == Constants.PAY_STATE_NO || payState == Constants.PAY_STATE_DELETE){//没有支付，需要跳转到支付页
			responseGetDoc.setState(ResponseStateEnum.PAY_STATE_NO.getIndex());
			responseGetDoc.setContent(ResponseStateEnum.PAY_STATE_NO.getName());
			return "redirect:/pay";
		}else if(payState == Constants.PAY_STATE_ON){//支付中，跳转到首页
			responseGetDoc.setState(ResponseStateEnum.PAY_STATE_ON.getIndex());
			responseGetDoc.setContent(ResponseStateEnum.PAY_STATE_ON.getName());
			return "redirect:/detail";
		}else if(payState == Constants.PAY_STATE_PAIED){
			//支付完成，下载
			try {
				download2(request, response, docName, user.getPhoneNumber());
			} catch (Exception e) {
				e.printStackTrace();
			}
//			responseGetDoc.setState(ResponseStateEnum.PAY_STATE_PAIED.getIndex());
//			responseGetDoc.setContent(ResponseStateEnum.PAY_STATE_PAIED.getName());
//			//打水印，下载
//			String inPdfFile = System.getProperty("user.dir")+"/doc/whitePaper/"+data.getDocName()+".pdf";
//			String tmpPdfName = data.getDocName()+"-"+user.getPhoneNumber();
//			String outPdfFile = System.getProperty("user.dir")+"/doc/tmp/"+tmpPdfName+".pdf";
//			ResponseEntity<byte[]> pdfDataByte;
//			try {	
//				//添加水印
//				MyFileUtil.addTextMart(inPdfFile, outPdfFile, user);
//				//读取添加水印后的pdf
//				pdfDataByte = MyFileUtil.readPdfTmp(tmpPdfName, request);
//				
//				//删除水印本地pdf
//				MyFileUtil.deletePdfTmp(outPdfFile);
//				responseGetDoc.setResponseEntity(pdfDataByte);
//			} catch (Exception e){
//				e.printStackTrace();
//			}
		}	
		return "detail";
	}
	
    //文件下载 主要方法
    public static void download2(HttpServletRequest request,HttpServletResponse response, String docName, String maskInfo) throws Exception {  
    	
		//打水印，下载
		String inPdfFile = System.getProperty("user.dir")+"/doc/whitePaper/"+docName+".pdf";
		String tmpPdfName = docName+"-"+maskInfo;
		String outPdfFile = System.getProperty("user.dir")+"/doc/tmp/"+tmpPdfName+".pdf";
		try {	
			//添加水印
			MyFileUtil.addTextMart(inPdfFile, outPdfFile, maskInfo);//用手机号打水印
			//读取添加水印后的pdf
			request.setCharacterEncoding("UTF-8");  
	        BufferedInputStream bis = null;  
	        BufferedOutputStream bos = null;  
	        long fileLength = new File(outPdfFile).length(); 
	        //设置文件输出类型
	        response.setContentType("application/octet-stream");  
	        response.setHeader("Content-disposition", "attachment; filename="+ new String(tmpPdfName.getBytes("utf-8"), "ISO8859-1")+".pdf"); 
	        //设置输出长度
	        response.setHeader("Content-Length", String.valueOf(fileLength)); 
	        //获取输入流
	        bis = new BufferedInputStream(new FileInputStream(outPdfFile));  
	        //输出流
	        bos = new BufferedOutputStream(response.getOutputStream());  
	        byte[] buff = new byte[2048];  
	        int bytesRead;  
	        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {  
	            bos.write(buff, 0, bytesRead);  
	        }  
	        //关闭流
	        bis.close();  
	        bos.close(); 
			//删除水印本地pdf
			MyFileUtil.deletePdfTmp(outPdfFile);
		} catch (Exception e){
			e.printStackTrace();
		}       
 
    }
	
    @RequestMapping("/download")
    public void download(HttpServletRequest request,HttpServletResponse response, @RequestParam String docName, @RequestParam String url) throws Exception {  
    	
		try {	
			request.setCharacterEncoding("UTF-8");  
	        BufferedInputStream bis = null;  
	        BufferedOutputStream bos = null;  
	        long fileLength = new File(url).length(); 
	        //设置文件输出类型
	        response.setContentType("application/octet-stream");  
	        response.setHeader("Content-disposition", "attachment; filename="+ new String((docName+"").getBytes("utf-8"), "ISO8859-1")+".pdf"); 
	        //设置输出长度
	        response.setHeader("Content-Length", String.valueOf(fileLength)); 
	        //获取输入流
	        bis = new BufferedInputStream(new FileInputStream(url));  
	        //输出流
	        bos = new BufferedOutputStream(response.getOutputStream());  
	        byte[] buff = new byte[2048];  
	        int bytesRead;  
	        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {  
	            bos.write(buff, 0, bytesRead);  
	        }  
	        //关闭流
	        bis.close();  
	        bos.close(); 
		} catch (Exception e){
			e.printStackTrace();
		}       
 
    }
//	public ResponseEntity<byte[]> download(RequestGetDoc data, HttpServletRequest request){
//		//打水印，下载
//		String inPdfFile = System.getProperty("user.dir")+"/doc/whitePaper/"+data.getDocName()+".pdf";
//		String tmpPdfName = data.getDocName()+"-"+data.getPhone();
//		String outPdfFile = System.getProperty("user.dir")+"/doc/tmp/"+tmpPdfName+".pdf";
//		ResponseEntity<byte[]> pdfDataByte = null;
//		try {	
//			//添加水印
//			ResponseUserInfo user = new ResponseUserInfo();
//			user.setPhoneNumber(data.getPhone());
//			MyFileUtil.addTextMart(inPdfFile, outPdfFile, user);//用手机号打水印
//			//读取添加水印后的pdf
//			pdfDataByte = MyFileUtil.readPdfTmp(tmpPdfName, request);
//			//删除水印本地pdf
//			MyFileUtil.deletePdfTmp(outPdfFile);
//		} catch (Exception e){
//			e.printStackTrace();
//		}
//		return pdfDataByte;
//	}
	
	
//			//已经支付
//			if(data.getRequestType() == 0){//查看
//				ResponseEntity<byte[]> pdfDataByte;
//				try {
//					pdfDataByte = MyFileUtil.download(data.getDocName(), request);
//					responseGetDoc.setResponseEntity(pdfDataByte);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}else{//下载
//				//打水印，下载
//				String inPdfFile = System.getProperty("user.dir")+"/doc/whitePaper/"+data.getDocName()+".pdf";
//				String tmpPdfName = data.getDocName()+"-"+user.getPhone();
//				String outPdfFile = System.getProperty("user.dir")+"/doc/tmp/"+tmpPdfName+".pdf";
//				ResponseEntity<byte[]> pdfDataByte;
//				try {					
//					//添加水印
//					MyFileUtil.addTextMart(inPdfFile, outPdfFile, user);
//					//读取添加水印后的pdf
//					pdfDataByte = MyFileUtil.readPdfTmp(tmpPdfName, request);
//					//删除水印本地pdf
//					MyFileUtil.deletePdfTmp(outPdfFile);
//					responseGetDoc.setResponseEntity(pdfDataByte);
//				} catch (Exception e){
//					e.printStackTrace();
//				}
//			}


	
	//支付
	@RequestMapping("/payForDoc")
	@ResponseBody
	public ResponsePayForDoc payForDoc(@RequestBody RequestPayForDoc data,  HttpServletRequest request){
		ResponsePayForDoc responsePayForDoc = new ResponsePayForDoc();
		RequestGetUserByToken requestGetUserByToken = null;
		ResponseGetUserByToken responseGetUserByToken = null;
	
		//获得Session Id与token的对应
		String sessionId = request.getSession().getId();
		//TEST--------
		sessionId = "C4B9631D76ECB1B3C2E659D708969A98";
		//TEST--------		
		SessionUser sessionUser = sessionUserService.findBySessionId(sessionId);
		ResponseGet responseGet = new ResponseGet();
		if(null==sessionUser){
			responsePayForDoc.setState(ResponseStateEnum.USER_NOT_LOGIN.getIndex());
			responsePayForDoc.setContent(ResponseStateEnum.USER_NOT_LOGIN.getName());
			//跳转到登录页面
			return responsePayForDoc;
		}else{
			//通过Token获得User信息
			requestGetUserByToken = new RequestGetUserByToken();
			String tmp = httpUtil.getUserByToken2(requestGetUserByToken);
			JSONObject jsonObject = JSONObject.fromObject(tmp);
			responseGet = (ResponseGet) JSONObject.toBean(jsonObject, ResponseGet.class);
			if(responseGet.getCode() != 0){
				//获取失败
				responsePayForDoc.setState(ResponseStateEnum.USER_TOKEN_EXPIRE.getIndex());
				responsePayForDoc.setContent(ResponseStateEnum.USER_TOKEN_EXPIRE.getName());
				return responsePayForDoc;
			}
		}
		
		//获取用户成功
		int userId = responseGet.getData().getId();
		
		//判断用户的支付情况
		int payState = orderService.getPayState(userId, data.getDocName());
		if(payState == Constants.PAY_STATE_NO || payState == Constants.PAY_STATE_DELETE){
			//case1:没有发起过订单，新建支付凭据，获得支付凭据
			//case2:发起过订单，但是订单失效
			responsePayForDoc = orderService.pay(userId, data, responsePayForDoc);
		}else if(payState == Constants.PAY_STATE_ON){
			//支付中，请用户耐心等待
			Charge charge = new Charge();
			responsePayForDoc.setCharge(charge);
			responsePayForDoc.setState(ResponseStateEnum.PAY_STATE_ON.getIndex());
			responsePayForDoc.setContent(ResponseStateEnum.PAY_STATE_ON.getName());
		}else if(payState == Constants.PAY_STATE_PAIED){
			//已经支付，不再需要支付
			responsePayForDoc.setState(ResponseStateEnum.PAY_STATE_PAIED.getIndex());
			responsePayForDoc.setContent(ResponseStateEnum.PAY_STATE_PAIED.getName());
		}		
		
		return responsePayForDoc;
	}	
	
//	//默认页面为白皮书信息列表
//	@RequestMapping("/index")
//	public String initIndex(Model mo){		
//		int resType = 0;//默认显示白皮书索引
//		List<Document> docs = docService.getDocumentListByDocType(resType);
//		
//		mo.addAttribute("docs", docs);		
//		
//		return "/view/白皮书";
//	}
	
//	//根据请求分类，返回文档信息列表
//	@RequestMapping("/getDocumentListByDocType")
//	public List<Document> getDocumentListByDocType(int docType){
//		return docService.getDocumentListByDocType(docType);
//	}	
	

//	//下载白皮书
//	@RequestMapping("/downloadWhitePaper")
//	public ResponseEntity<byte[]> downloadWhitePaper( String docName,  HttpServletRequest request, HttpServletResponse response){
//		User user = (User) request.getSession().getAttribute("user");
//		//TEST init
//		user = new User();
//		user.setId(1);
//		docName = "2016年网络剧产业发展研究报告";		
//		
//		if(null == user){
//			//跳转到登录页面
//			//TODO
//		}else{
//			//检查是否已经支付
//			if(userService.isUserPaied(user, docName)){
//				//已经支付，返回pdf下载数据
//				try {
//					return MyFileUtil.download2(docName, request);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}else{
//				//没有支付，跳转到支付页面
//				//TODO
//			}
//		}
//		
//		return null;
//	}
	
//	//下载白皮书
//	@RequestMapping("/downloadWhitePaper")
//	public ResponseEntity<byte[]> downloadWhitePaper( String docName,  HttpServletRequest request, HttpServletResponse response){
//		User user = (User) request.getSession().getAttribute("user");
//		//TEST init
//		user = new User();
//		user.setId(1);
//		docName = "2016年网络剧产业发展研究报告";		
//		
//		if(null == user){
//			//跳转到登录页面
//			//TODO
//		}else{
//			//检查是否已经支付
//			if(userService.isUserPaied(user, docName)){
//				//已经支付，返回pdf下载数据
//				try {
//					return MyFileUtil.download(docName, request);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}else{
//				//没有支付，跳转到支付页面
//				//TODO
//			}
//		}
//		
//		return null;
//	}	

//	//下载白皮书
//	@RequestMapping("/downloadWhitePaper")
//	@ResponseBody
//	public ResponseGetDoc downloadWhitePaper(@RequestBody RequestGetDoc,  HttpServletRequest request, HttpServletResponse response){
//		ResponseGetDoc responseGetDoc = new ResponseGetDoc();
//		
//		User user = (User) request.getSession().getAttribute("user");
////		//TEST init
////		user = new User();
////		user.setId(1);
////		docName = "2016年网络剧产业发展研究报告";		
//		
//		if(null == user){
//			//跳转到登录页面
//			//TODO
//		}else{
//			//检查是否已经支付
//			if(userService.isUserPaied(user, docName)){
//				//已经支付，返回pdf下载数据
//				try {
//					return MyFileUtil.download(docName, request);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}else{
//				//没有支付，跳转到支付页面
//				//TODO
//			}
//		}
//		
//		return null;
//	}
	
//	//查看文件内容
//	@RequestMapping("/getDoc")
//	@ResponseBody
//	public ResponseGetDoc getDoc(@RequestBody RequestGetDoc data, HttpServletRequest request){
//		ResponseGetDoc responseGetDoc = new ResponseGetDoc();
//		
//		User user = (User) request.getSession().getAttribute("user");
//		if(null == request.getSession().getAttribute("user")){
//			//用户未登录，返回登录页面
//			responseGetDoc.setState(ResponseStateEnum.USER_NOT_LOGIN.getIndex());
//			responseGetDoc.setContent(ResponseStateEnum.USER_NOT_LOGIN.getName());			
//		}else{
//			//检查用户是否已经付费
//			boolean isPaied = userService.isUserPaied(user, data.getDocName());		
//			int requestType = data.getRequestType();
//			
//			if((requestType==0 && isPaied)){
//				//付费用户可以查看完整文档
//				ResponseEntity<byte[]> pdfDataByte;
//				try {
//					pdfDataByte = MyFileUtil.download(data.getDocName(), request);
//					responseGetDoc.setResponseEntity(pdfDataByte);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}								
//			}else if(requestType==1 && isPaied){
//				//付费用户可以下载完整文档，带上水印
//				//TODO
//			}else if(requestType==0 && !isPaied){
//				//未付费用户请求查看文档，返回目录信息
//				//TODO
//			}else if(requestType==1 && !isPaied){
//				//未付费用户请求下载文档，跳转到付费页面
//				responseGetDoc.setState(ResponseStateEnum.USER_NOT_PAIED.getIndex());
//				responseGetDoc.setContent(ResponseStateEnum.USER_NOT_PAIED.getName());
//			}
//		}
//		return responseGetDoc;
//	}	
	
//	//支付
//	@RequestMapping("/payForDoc")
//	@ResponseBody
//	public ResponsePayForDoc payForDoc(@RequestBody RequestPayForDoc data,  HttpServletRequest request){
//		ResponsePayForDoc responsePayForDoc = new ResponsePayForDoc();
//		
//		User user = (User) request.getSession().getAttribute("user");
//		
//		//TEST INIT
//		user = new User();	
//		data.setDocName("2016年网络大电影产业发展研究报告");
//		data.setDocDesc("这是一个权威的白皮书");
//		
//		if(null==user){
//			//用户未登录，返回登录页面
//			responsePayForDoc.setState(ResponseStateEnum.USER_NOT_LOGIN.getIndex());
//			responsePayForDoc.setContent(ResponseStateEnum.USER_NOT_LOGIN.getName());
//		}else{
//			//生成订单，获得支付凭据
//			responsePayForDoc = orderService.pay(data, responsePayForDoc);
//		}
//		
//		return responsePayForDoc;
//	}
}
