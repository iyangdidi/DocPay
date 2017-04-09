package com.guduo.cons;

public class Constants {
	
	//Charge相关参数
	static String test_api_key = "sk_test_P0SqDCfrP4KG5SWrbTuHyHmD";
	static String live_api_key = "sk_live_z1ePyLXTuXH09GKmr5SOeLKC";
	static String test_app_id = "app_e1WbbHCu5C4CPynX";
	
	public static final String API_KEY = test_api_key;
	public static final String APP_ID = test_app_id;
	public static final String CLIENT_IP = "127.0.0.1";//"183.172.46.248";
	public static final String HOST_IP = "127.0.0.1";//"183.172.46.248";
	public static final String SUCCESS_URL = "http://"+HOST_IP+":8085/doc/paySuccess";
	public static final String REQUEST_BASE_URL = "http://9997.stage.srv.guduomedia.com/api";
	
	public static final int PAY_STATE_NO = -1;
	public static final int PAY_STATE_ON = 0;
	public static final int PAY_STATE_PAIED = 1;
	public static final int PAY_STATE_DELETE = 2;
	
	public static final int Order_check_time = 2;
}

