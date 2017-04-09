package com.guduo.wp.domain;

public class Extra_AlipayPCDirect {
	String success_url;					//支付成功的回调地址。
	boolean enable_anti_phishing_key;	//optional,是否开启防钓鱼网站的验证参数（如果已申请开通防钓鱼时间戳验证，则此字段必填）
	String exter_invoke_ip;				//optional,客户端 IP ，用户在创建交易时，该用户当前所使用机器的IP（如果商户申请后台开通防钓鱼IP地址检查选项，此字段必填，校验用）。
	String buyer_account;				//支付完成将额外返回付款用户的支付宝账号。
	
	public String getSuccess_url() {
		return success_url;
	}
	public void setSuccess_url(String success_url) {
		this.success_url = success_url;
	}
	public boolean isEnable_anti_phishing_key() {
		return enable_anti_phishing_key;
	}
	public void setEnable_anti_phishing_key(boolean enable_anti_phishing_key) {
		this.enable_anti_phishing_key = enable_anti_phishing_key;
	}
	public String getExter_invoke_ip() {
		return exter_invoke_ip;
	}
	public void setExter_invoke_ip(String exter_invoke_ip) {
		this.exter_invoke_ip = exter_invoke_ip;
	}
	public String getBuyer_account() {
		return buyer_account;
	}
	public void setBuyer_account(String buyer_account) {
		this.buyer_account = buyer_account;
	}
	
}
