package com.guduo.wp.controller;

import antlr.collections.List;

import com.alibaba.fastjson.JSON;
import com.guduo.cons.Constants;
import com.guduo.wp.domain.Order;
import com.guduo.wp.domain.TradeLog;
import com.guduo.wp.service.OrderService;
import com.guduo.wp.service.TradeLogService;
import com.guduo.wp.util.PingppUtil;
import com.pingplusplus.exception.APIConnectionException;
import com.pingplusplus.exception.APIException;
import com.pingplusplus.exception.AuthenticationException;
import com.pingplusplus.exception.ChannelException;
import com.pingplusplus.exception.InvalidRequestException;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Event;
import com.pingplusplus.model.EventData;
import com.pingplusplus.model.Webhooks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.security.PublicKey;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Enumeration;

@Controller
public class MainController {
	
	@RequestMapping("/index")
    public String index() {
        return "/index";
    }	
	
	@Autowired
	private OrderService orderService;	
	@Autowired
	private TradeLogService tradeLogService;	
	
	@RequestMapping("/webhooks")
	public void paySuccess(HttpServletRequest request, HttpServletResponse response){
		try{
 			request.setCharacterEncoding("UTF8");
	        //获取头部所有信息
	        Enumeration headerNames = request.getHeaderNames();
	        while (headerNames.hasMoreElements()) {
	            String key = (String) headerNames.nextElement();
	            String value = request.getHeader(key);
	            System.out.println(key+" "+value);
	        }
	        // 获得 http body 内容
	        BufferedReader reader = request.getReader();
	        StringBuffer buffer = new StringBuffer();
	        String string;
	        while ((string = reader.readLine()) != null) {
	            buffer.append(string);
	        }
	        reader.close();
	        
	        //验证签名
	        try {
 	        	String signatureString = request.getHeader("x-pingplusplus-signature");
		        String dataString = buffer.toString();
		        PublicKey publicKey = PingppUtil.getPubKey();	        
		        boolean verify = PingppUtil.verifyData(dataString, signatureString, publicKey);
		        if(!verify){
		        	System.out.print("ERROR: 验证webhooks签名失败，非法通知");
		        	return;
		        }
		        
			} catch (Exception e) {
				e.printStackTrace();				
				return;
			}        
	        
	        
	        // 解析异步通知数据
	        Event event = Webhooks.eventParse(buffer.toString());
	        if ("charge.succeeded".equals(event.getType())) {
	        	
	        	//检查是否已经接受此Event
	        	TradeLog tmpLog = tradeLogService.findById(event.getId());
	        	//已经接收到过此信息，通知webhooks不用再发
	        	if(null != tmpLog){
	        		response.setStatus(200);
	        		return;
	        	}	        	
	        	
	        	//没有接收过此次Event
	        	EventData data = event.getData();
	        	Charge charge = (Charge) data.getObject();
	        	
	        	//更新订单状态
	        	Order order = orderService.getById(charge.getOrderNo());
	        	order.setUpdateTime(new Timestamp((new Date()).getTime()));
	        	order.setPayTime(new Timestamp(charge.getTimePaid()));
	        	order.setOrderState(Constants.PAY_STATE_PAIED);
	        	orderService.save(order);
	        	
	        	//将通知存入本地数据库
	        	TradeLog tradeLog = new TradeLog();
	        	tradeLog.setId(event.getId());
	        	tradeLog.setChargeId(charge.getId());
	        	String content = buffer.toString();
	        	tradeLog.setContent(JSON.toJSONString(content));
	        	tradeLog.setCreatTime(new Timestamp((new Date()).getTime()));
	        	tradeLogService.save(tradeLog);
	        	
	            response.setStatus(200);
	        } else if ("refund.succeeded".equals(event.getType())) {
	            response.setStatus(200);
	        } else {
	            response.setStatus(500);
	        }
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Scheduled(fixedRate=Constants.Order_check_time*60000)//n分钟检查一次
	public void checkAndUpdateOrDeleteOrder(){		
		try {
			System.out.println("定时更新or删除无效订单");
			//检查n分钟前的订单
			orderService.checkAndUpdateOrDeleteOrder();			
			return;
		} catch (AuthenticationException e) {
			e.printStackTrace();
		} catch (InvalidRequestException e) {
			e.printStackTrace();
		} catch (APIConnectionException e) {
			e.printStackTrace();
		} catch (APIException e) {
			e.printStackTrace();
		} catch (ChannelException e) {
			e.printStackTrace();
		}
		
		System.out.println("定时更新or删除失败");
	}
}
