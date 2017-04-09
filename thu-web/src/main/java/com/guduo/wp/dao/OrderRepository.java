package com.guduo.wp.dao;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.guduo.wp.domain.Order;


public interface OrderRepository extends JpaRepository<Order, Integer>{
	
	Order findById(String id);
	Order findByChargeId(String chargeId);
	List findByUserId(int userId);
	List<Order> findByUserIdAndProductName(int userId, String productName);
	List<Order> findByOrderState(int orderSate);
	@Query(value="select o from Order o where o.orderState=?1 and o.updateTime<?2")
	List<Order> findByOrderStateSetTime(int orderSate, Timestamp time);
	
}
