package com.guduo.wp.domain.response;

public class AbstractResponse {
	private int state=1;
	private String content = "成功";
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
