package com.guduo.cons;

public enum ResponseStateEnum {
	FAILED("失败",-1),
	USER_NOT_LOGIN("用户未登录",1),
	USER_NOT_PAIED("用戶未支付",2),
	USER_WRONG_PAY_TYPE("支付方式错误",3),
	USER_TOKEN_EXPIRE("Token过期",4),
	PAY_CHARGE_ERROR("支付凭据错误",5),
	PAY_STATE_ON("支付中",6),
	PAY_STATE_NO("未支付",7),
	PAY_STATE_PAIED("已支付",8),
	PAY_STATE_DELETE("订单失效",12),
	CHARGE_INVALID("错误支付Charge",9),
	ORDER_INVALID("订单无效",10),	
	ORDER_PAIED("订单已经支付",11)
	;
	
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

	private ResponseStateEnum(String name, int index) {
		this.name = name;
		this.index = index;
	}
	
	public static String getName(int index) {
		for(ResponseStateEnum c:ResponseStateEnum.values()){
			if(c.getIndex() == index){
				return c.name;
			}
		}
		return null;
	}
}
