package com.guduo.wp.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.hadoop.mapred.gethistory_jsp;

@Entity
@Table(name="t_order")
public class Order extends BaseDomain{

	@Id
	private String id;
	@Column(nullable=false)
	private String chargeId;
	@Column(nullable=false)
	private int userId;
	@Column(nullable=false,columnDefinition="decimal COMMENT \"交易金额\"")
	private BigDecimal amount;
	@Column(nullable=false,columnDefinition="varchar(100) COMMENT \"白皮书名称\"")
	private String productName;
	@Column(nullable=false,columnDefinition="int COMMENT \"订单状态\"")
	private int orderState;
	@Column(nullable=false,columnDefinition="int COMMENT \"支付类型\"")
	private int payType;
	@Column(nullable=false)
	private Timestamp createTime;
	@Column(nullable=false)
	private Timestamp updateTime;
	@Column(nullable=true)
	private Timestamp payTime;
	
	public String getChargeId() {
		return chargeId;
	}
	public void setChargeId(String chargeId) {
		this.chargeId = chargeId;
	}
	public Timestamp getPayTime() {
		return payTime;
	}
	public void setPayTime(Timestamp payTime) {
		this.payTime = payTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public int getOrderState() {
		return orderState;
	}
	public void setOrderState(int orderState) {
		this.orderState = orderState;
	}
	public int getPayType() {
		return payType;
	}
	public void setPayType(int payType) {
		this.payType = payType;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	
}
