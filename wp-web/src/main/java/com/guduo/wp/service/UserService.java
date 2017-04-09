package com.guduo.wp.service;

import java.sql.Timestamp;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guduo.wp.dao.OrderRepository;
import com.guduo.wp.dao.UserRepository;
import com.guduo.wp.domain.Order;
import com.guduo.wp.domain.User;
import com.guduo.wp.domain.response.AbstractResponse;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private OrderRepository orderRepo;
	
	public void addUser(User user){
		userRepo.save(user);
	}
	
	public void updateUser(User user){
		User modifiedUser = userRepo.findOne(user.getId());
		modifiedUser = user;
		//modifiedUser.setUpdateTime(new Timestamp(System.currentTimeMillis()));
	}
	
//	//检查用户是否已经付费
//	public boolean isUserPaied(User user, String docName){
//		Order order = orderRepo.findByUserIdAndProductName(user.getId(), docName);
//		if(order == null || order.getOrderState()!=0)//0=交易成功
//			return false;
//		else
//			return true;
//	}
}

