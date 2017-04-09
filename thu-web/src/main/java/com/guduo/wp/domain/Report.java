package com.guduo.wp.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.*;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_report")
public class Report extends BaseDomain{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	@Column(nullable=false, columnDefinition="varchar(256) COMMENT \"报告名称\"")
	private String name;
	@Column(nullable=false, columnDefinition="varchar(256) COMMENT \"报告描述\"")
	private String descibe;
	@Column(nullable=false, columnDefinition="varchar(256) COMMENT \"报告Url\"")
	private String url;
	@Column(nullable=false, columnDefinition="int COMMENT \"报告类型\"")
	private int reportType;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescibe() {
		return descibe;
	}
	public void setDescibe(String descibe) {
		this.descibe = descibe;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getReportType() {
		return reportType;
	}
	public void setReportType(int reportType) {
		this.reportType = reportType;
	}
	
}
