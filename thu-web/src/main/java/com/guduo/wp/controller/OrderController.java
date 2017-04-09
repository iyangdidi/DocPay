package com.guduo.wp.controller;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guduo.cons.Constants;
import com.guduo.cons.ResponseStateEnum;
import com.guduo.wp.domain.Order;
import com.guduo.wp.domain.response.AbstractResponse;
import com.guduo.wp.service.OrderService;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.pingplusplus.exception.APIConnectionException;
import com.pingplusplus.exception.APIException;
import com.pingplusplus.exception.AuthenticationException;
import com.pingplusplus.exception.ChannelException;
import com.pingplusplus.exception.InvalidRequestException;
import com.pingplusplus.model.Charge;

@Controller
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@RequestMapping("/paied")
	private void paied(@RequestBody Charge charge){
		//更新订单状态为已经支付
		Order order = orderService.getById(charge.getOrderNo());
		order.setOrderState(Constants.PAY_STATE_PAIED);
		order.setPayTime(new Timestamp(charge.getTimePaid()));
		order.setUpdateTime(new Timestamp((new Date()).getTime()));
		orderService.save(order);
	}
	
	@RequestMapping("/delete")
	private void delete(@RequestBody Charge charge){
		//当轮询一定时间后，还没有支付，则支付失败
		Order order = orderService.getById(charge.getOrderNo());
		orderService.delete(order);
	}
	
	@RequestMapping("/checkOrder")
	@ResponseBody
	private AbstractResponse checkOrder(@RequestParam String chargeId){
		AbstractResponse response = new AbstractResponse();
		try {
			response = orderService.checkOrder(chargeId);
		} catch (Exception e){
			e.printStackTrace();
			response.setState(ResponseStateEnum.FAILED.getIndex());
			response.setContent(ResponseStateEnum.FAILED.getName());
		}
		return response;
	}
//	@RequestMapping("/payFailed")
//	private void payFailed(@RequestBody Charge charge){
//		//当轮询一定时间后，还没有支付，则支付失败
//		Order order = orderService.getById(charge.getOrderNo());
//		//order.setOrderState(Constants.PAY_STATE_NO);
//		//TODO-需要新增一个状态
//		order.setUpdateTime(new Timestamp((new Date()).getTime()));
//		orderService.save(order);
//	}
	

}
