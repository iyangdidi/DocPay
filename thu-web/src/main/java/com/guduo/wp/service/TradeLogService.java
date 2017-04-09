package com.guduo.wp.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import scala.Char;

import com.guduo.cons.Constants;
import com.guduo.cons.ENUM_PAY_TYPE;
import com.guduo.cons.ResponseStateEnum;
import com.guduo.wp.dao.OrderRepository;
import com.guduo.wp.dao.TradeLogRepository;
import com.guduo.wp.dao.UserRepository;
import com.guduo.wp.domain.Order;
import com.guduo.wp.domain.TradeLog;
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
public class TradeLogService {
	@Autowired
	private TradeLogRepository tradeLogRepo;

	public TradeLog findById(String id){ return tradeLogRepo.findById(id);}
	public void save(TradeLog tradeLog){tradeLogRepo.save(tradeLog);}
}

