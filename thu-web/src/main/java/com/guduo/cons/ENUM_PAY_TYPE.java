package com.guduo.cons;

public enum ENUM_PAY_TYPE {		
	//支付宝
	//PAY_TYPE_ALIPAY_PC_DIRECT("alipay_pc_direct",3),//支付宝电脑网站支付
	PAY_TYPE_ALIPAY_QR("alipay_qr", 1),				//支付宝扫码支付
	 
	//微信
	PAY_TYPE_WX_PUB_QR("wx_pub_qr",2);//微信公众号扫码支付

	
	private String name;
	private int index;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	private ENUM_PAY_TYPE(String name, int index) {
		this.name = name;
		this.index = index;
	}
	
	public static String getName(int index) {
		for(ENUM_PAY_TYPE c:ENUM_PAY_TYPE.values()){
			if(c.getIndex() == index){
				return c.name;
			}
		}
		return null;
	}
}
