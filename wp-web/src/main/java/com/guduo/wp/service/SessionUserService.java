package com.guduo.wp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guduo.wp.dao.SessionUserRepository;
import com.guduo.wp.domain.SessionUser;

@Service
public class SessionUserService {

	@Autowired
	private SessionUserRepository sessionUserRepo;
	
	public SessionUser findBySessionId(String sessionId){
		return sessionUserRepo.findBySessionId(sessionId);
	}
}
