package com.guduo.wp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.guduo.wp.domain.TradeLog;

public interface TradeLogRepository extends JpaRepository<TradeLog, Integer>{
	TradeLog findById(String id);
}
