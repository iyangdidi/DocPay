package com.guduo.wp.domain.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class RequestLogin implements Serializable{

	private String userName;

	private String password;
}
