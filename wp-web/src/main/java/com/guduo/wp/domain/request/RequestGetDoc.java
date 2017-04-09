package com.guduo.wp.domain.request;

public class RequestGetDoc {
	String docName;
	String phone;
	//int docType;//白皮書還是週報月報
	int requestType;//下載還是查看
	
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
//	public int getDocType() {
//		return docType;
//	}
//	public void setDocType(int docType) {
//		this.docType = docType;
//	}
	public int getRequestType() {
		return requestType;
	}
	public void setRequestType(int requestType) {
		this.requestType = requestType;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
}
