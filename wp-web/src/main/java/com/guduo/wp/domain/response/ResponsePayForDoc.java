package com.guduo.wp.domain.response;

import com.pingplusplus.model.Charge;

public class ResponsePayForDoc extends AbstractResponse{
	Charge charge;

	public Charge getCharge() {
		return charge;
	}

	public void setCharge(Charge charge) {
		this.charge = charge;
	}
	
}
