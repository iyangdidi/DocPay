package com.guduo.wp.domain.response;

import org.apache.http.protocol.ResponseDate;

public class ResponseGet {
	int code;
	String message;
	ResponseUserInfo data;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ResponseUserInfo getData() {
		return data;
	}
	public void setData(ResponseUserInfo data) {
		this.data = data;
	}
	
}
