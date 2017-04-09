package com.guduo.wp.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.guduo.cons.Constants;
import com.guduo.wp.domain.request.RequestGetUserByToken;
import com.guduo.wp.domain.request.RequestLogin;
import com.guduo.wp.domain.response.ResponseGet;
import com.guduo.wp.domain.response.ResponseGetUserByToken;
import com.guduo.wp.domain.response.ResponseLogin;
import com.guduo.wp.domain.response.ResponseUserInfo;

public class HttpUtil extends RestTemplate{
	
    HttpHeaders headers =new HttpHeaders();    
    HttpEntity request;
    
    public HttpUtil(){
    	headers.setContentType(MediaType.APPLICATION_JSON);
    }
    
    public ResponseGetUserByToken getUserByToken(RequestGetUserByToken data){
    	
    	request = new HttpEntity(data, headers);
    	//TODO
    	String urlTail = "/user/";
    	ResponseGetUserByToken response = this.postForObject(Constants.REQUEST_BASE_URL+urlTail, request, ResponseGetUserByToken.class);
    	
    	return response;
    }
    
    public String getUserByToken2(RequestGetUserByToken data){
    	
//    	request = new HttpEntity(data, headers);
//    	String urlTail = "http://127.0.0.1:8085/user/get?token='qwertyufg'";
//    	String response = this.postForObject(urlTail, null, String.class);
    	
    	//TEST
    	ResponseGet responseGet = new ResponseGet();    	
    	responseGet.setCode(0);
    	ResponseUserInfo info = new ResponseUserInfo();
    	info.setId(1);
    	responseGet.setData(info);
    	String response = JSONObject.toJSONString(responseGet);
    	return response;
    }
    
    public ResponseLogin login(RequestLogin data){
    	String urlTail = "/user/logIn";
    	request = new HttpEntity(data, headers);    	
    	ResponseLogin response = this.postForObject(Constants.REQUEST_BASE_URL+urlTail, request, ResponseLogin.class);  
    	
    	return response;
    }
    
    public String testPost(String data){
    	String urlTail = "/user/test/post";
    	request = new HttpEntity(data, headers);   
    	String  response = this.postForObject("http://127.0.0.1:8085"+urlTail, request, String.class);     	 
    	return response;
    }
}
