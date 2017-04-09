package com.guduo.wp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.guduo.wp.domain.SessionUser;

public interface SessionUserRepository extends JpaRepository<SessionUser, Integer>{
	SessionUser findBySessionId(String sessionId);
}
