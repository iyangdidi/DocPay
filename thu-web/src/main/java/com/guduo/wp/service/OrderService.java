package com.guduo.wp.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.net.ntp.TimeStamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import scala.Char;


import com.alibaba.fastjson.JSON;
import com.couchbase.client.java.document.json.JsonObject;
import com.guduo.cons.Constants;
import com.guduo.cons.ENUM_PAY_TYPE;
import com.guduo.cons.ResponseStateEnum;
import com.guduo.wp.dao.OrderRepository;
import com.guduo.wp.dao.UserRepository;
import com.guduo.wp.domain.Order;
import com.guduo.wp.domain.User;
import com.guduo.wp.domain.request.RequestPayForDoc;
import com.guduo.wp.domain.response.AbstractResponse;
import com.guduo.wp.domain.response.ResponsePayForDoc;
import com.guduo.wp.util.OrderUtil;
import com.guduo.wp.util.UnicodeUtil;
import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.APIConnectionException;
import com.pingplusplus.exception.APIException;
import com.pingplusplus.exception.AuthenticationException;
import com.pingplusplus.exception.ChannelException;
import com.pingplusplus.exception.InvalidRequestException;
import com.pingplusplus.model.Charge;

@Service
public class OrderService {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private OrderRepository orderRepo;
	
	public void delete(Order order){
		orderRepo.delete(order);
	}
	
	public int getPayState(int userId, String docName){
		
		List<Order> orderList = orderRepo.findByUserIdAndProductName(userId, docName);
		if(orderList.size()<1){
			//用户没有发起过订单
			return Constants.PAY_STATE_NO;
		}else{
			//检查最近一条的支付状态,数据库默认是按照id递增的，检验最后一条
			Order order = orderList.get(orderList.size()-1);
			return order.getOrderState();
		}
	}
	
	public Order getById(String id){
		return orderRepo.findById(id);
	}
	
	public void save(Order order){
		orderRepo.save(order);
	}
	
	public ResponsePayForDoc pay(int userId, RequestPayForDoc data, ResponsePayForDoc responsePayForDoc){		
		
		String order_no = OrderUtil.generateOrderId();
		
		//调用 Ping++ Server SDK 发起支付请求
		Pingpp.apiKey = Constants.API_KEY;		
		Pingpp.privateKeyPath = System.getProperty("user.dir")+"/src/main/resources/keys/privkey.pem";
		
		//配置请求内容
		Map<String, Object> chargeParams = new HashMap<String, Object>();
		chargeParams.put("order_no", order_no);		 				//订单号
		chargeParams.put("amount", data.getAmount());				//订单总金额, 人民币单位：分（如订单总金额为 1 元，此处请填 100）
		Map<String, String> app = new HashMap<String, String>();
		app.put("id", Constants.APP_ID);			 				//支付使用的  app 对象的  id
		chargeParams.put("app", app);
		chargeParams.put("channel", ENUM_PAY_TYPE.getName(data.getPayType()));	//支付渠道
		chargeParams.put("currency", "cny");									//目前仅支持人民币  cny
		chargeParams.put("client_ip", Constants.CLIENT_IP);						//发起支付请求客户端的 IPv4 地址
		//chargeParams.put("subject", UnicodeUtil.cnToUnicode(data.getDocName()));//商品的标题，该参数最长为 32 个 Unicode 字符
		//chargeParams.put("body", UnicodeUtil.cnToUnicode(data.getDocDesc()));	//商品的描述信息，该参数最长为 128 个 Unicode 字符
		chargeParams.put("subject", data.getDocName());//商品的标题，该参数最长为 32 个 Unicode 字符
		chargeParams.put("body", data.getDocDesc());	//商品的描述信息，该参数最长为 128 个 Unicode 字符
		//根据不同channel配置extra
		Map<String, Object> extra = new HashMap<String, Object>();
		//支付宝-扫码
		if(data.getPayType() == ENUM_PAY_TYPE.PAY_TYPE_ALIPAY_QR.getIndex()){
			//不需要extra参数		
		}//微信-公众号-扫码-支付
		else if(data.getPayType() == ENUM_PAY_TYPE.PAY_TYPE_WX_PUB_QR.getIndex()){
			//extra.put("limit_pay",);	//string	optional,指定支付方式，指定不能使用信用卡支付可设置为  no_credit
			extra.put("product_id", order_no);	//string	required,商品 ID，1-32 位字符串。此 id 为二维码中包含的商品 ID，商户自行维护。
			//extra.put("goods_tag",);	//string	optional,商品标记，代金券或立减优惠功能的参数。
			//extra.put("open_id",);		//string	response-only,支付完成后额外返回付款用户的微信  open_id 
			//extra.put("bank_type",);	//string	response-only,支付完成后额外返回付款用户的付款银行类型  bank_type 。
		}else{
			responsePayForDoc.setState(ResponseStateEnum.USER_WRONG_PAY_TYPE.getIndex());
			responsePayForDoc.setContent(ResponseStateEnum.USER_WRONG_PAY_TYPE.getName());
		}
		chargeParams.put("extra", extra);
		//ping++发起支付请求，获得支付凭据
		String chargeId;
		try {
			Charge charge = Charge.create(chargeParams);
			responsePayForDoc.setCharge(charge);
			chargeId = charge.getId();
			//System.out.println("Create Charge");
		} catch (Exception e) {			
			e.printStackTrace();
			
			responsePayForDoc.setState(ResponseStateEnum.PAY_CHARGE_ERROR.getIndex());
			responsePayForDoc.setContent(ResponseStateEnum.PAY_CHARGE_ERROR.getName());				
			return responsePayForDoc;
		}		
		
		//本地插入订单信息
		Order order = new Order();
		order.setId(order_no);
		order.setChargeId(chargeId);
		order.setAmount(data.getAmount());
		order.setOrderState(Constants.PAY_STATE_ON);
		order.setPayType(data.getPayType());
		order.setProductName(data.getDocName());
		order.setUserId(userId);
		order.setCreateTime(new Timestamp((new Date()).getTime()));
		order.setUpdateTime(new Timestamp((new Date()).getTime()));
		orderRepo.save(order);		
		
		return responsePayForDoc;
	}
	
	public void checkAndUpdateOrDeleteOrder() throws AuthenticationException, InvalidRequestException, APIConnectionException, APIException, ChannelException{
		//获得支付中的Order信息
		Timestamp time = new Timestamp((new Date()).getTime()-Constants.Order_check_time*60*1000);
		List<Order> orderList = orderRepo.findByOrderStateSetTime(Constants.PAY_STATE_ON, time);		
		Pingpp.apiKey = Constants.API_KEY;		
		Pingpp.privateKeyPath = System.getProperty("user.dir")+"/src/main/resources/keys/privkey.pem";
		for(Order order:orderList){
			//查询Ping++看是否已经支付
			Charge ch = Charge.retrieve(order.getChargeId());
			if(ch.getPaid()){
				//已经支付，更新Order
				order.setOrderState(Constants.PAY_STATE_PAIED);
				order.setPayTime(new Timestamp(ch.getTimePaid()*1000));
				order.setUpdateTime(new Timestamp((new Date()).getTime()));
				orderRepo.save(order);
			}else{
				//还没有支付，设置Order无效
				String jStr = JSON.toJSONString(order);
				System.out.println("set invalid order: "+jStr);
				order.setOrderState(Constants.PAY_STATE_DELETE);
				order.setUpdateTime(new Timestamp((new Date()).getTime()));
				orderRepo.save(order);
				//orderRepo.delete(order);
			}			
		}
	}
	
	public AbstractResponse checkOrder(String chargeId) throws AuthenticationException, InvalidRequestException, APIConnectionException, APIException, ChannelException{
		AbstractResponse response = new AbstractResponse();
		Pingpp.apiKey = Constants.API_KEY;		
		Pingpp.privateKeyPath = System.getProperty("user.dir")+"/src/main/resources/keys/privkey.pem";
		//查询Ping++看是否已经支付
		Charge ch = Charge.retrieve(chargeId);
		if(null == ch){
			response.setState(ResponseStateEnum.CHARGE_INVALID.getIndex());
			response.setContent(ResponseStateEnum.CHARGE_INVALID.getName());
			return response;
		}
		//支付宝二维码是每分钟更新的，不会存在数据库的无效订单被清理了，却已经支付的情况
		//检查订单 
		if(ch.getPaid()){//已经支付
			Order order = orderRepo.findByChargeId(chargeId);
			//检查订单有效性
			if(null == order){//订单无效
				response.setState(ResponseStateEnum.ORDER_INVALID.getIndex());
				response.setContent(ResponseStateEnum.ORDER_INVALID.getName());
			}else{//订单有效
				//检查有效订单状态
				if(order.getOrderState()==Constants.PAY_STATE_ON){
					//更新订单状态
					order.setOrderState(Constants.PAY_STATE_PAIED);
					order.setPayTime(new Timestamp(ch.getTimePaid()*1000));
					orderRepo.save(order);
					System.out.println("now:"+(new Date()).getTime()+"	pay:"+ch.getTimePaid());
				}
				response.setState(ResponseStateEnum.ORDER_PAIED.getIndex());
				response.setContent(ResponseStateEnum.ORDER_PAIED.getName());
			}
		}else{//没有支付
			response.setState(ResponseStateEnum.USER_NOT_PAIED.getIndex());
			response.setContent(ResponseStateEnum.USER_NOT_PAIED.getName());
		}
		return response;
	}
	
}

