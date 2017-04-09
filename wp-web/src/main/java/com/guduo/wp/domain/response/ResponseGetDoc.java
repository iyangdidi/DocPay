package com.guduo.wp.domain.response;

import org.springframework.http.ResponseEntity;


public class ResponseGetDoc extends AbstractResponse{
	private ResponseEntity<byte[]> responseEntity;

	public ResponseEntity<byte[]> getResponseEntity() {
		return responseEntity;
	}

	public void setResponseEntity(ResponseEntity<byte[]> responseEntity) {
		this.responseEntity = responseEntity;
	}
	
}
