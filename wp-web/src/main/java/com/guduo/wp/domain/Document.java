package com.guduo.wp.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_document")
public class Document extends BaseDomain{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	@Column(nullable=false, columnDefinition="varchar(100) COMMENT \"文件名称\"")
	private String name;
	@Column(nullable=false, columnDefinition="varchar(100) COMMENT \"文件描述\"")
	private String docDesc;	
	@Column(nullable=false, columnDefinition="int COMMENT \"文件类型：0=白皮书；1=周报；2=月报\"")
	private int docType;
	@Column(nullable=false, columnDefinition="varchar(200) COMMENT \"文件位置。白皮书在本地不用填；周报月为url\"")
	private String location;
	
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
	public String getDocDesc() {
		return docDesc;
	}
	public void setDocDesc(String docDesc) {
		this.docDesc = docDesc;
	}
	public int getDocType() {
		return docType;
	}
	public void setDocType(int docType) {
		this.docType = docType;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}	
}
