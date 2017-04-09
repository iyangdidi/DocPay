package com.guduo.wp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.guduo.wp.domain.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	
	User findUByPhone(String phone);
}
