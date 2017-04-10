package com.bugdb.domain;

import java.io.Serializable;
import java.sql.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Bug implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer bugNo;
	private Integer filedBy;
	private Timestamp filed;
	private Integer assigned;
	private String customer;
	private String bugType;
	private Integer statusId;
	private Integer fixedBy;
	private String fixedVer;
	private Integer severityId;
	private Integer productId;
	private Integer componentId;
	private Integer osId;
	private String tags;
	private String subject;
	public Integer getBugNo() {
		return bugNo;
	}
	public void setBugNo(Integer bugNo) {
		this.bugNo = bugNo;
	}
	public Integer getFiledBy() {
		return filedBy;
	}
	public void setFiledBy(Integer filedBy) {
		this.filedBy = filedBy;
	}
	public Timestamp getFiled() {
		return filed;
	}
	public void setFiled(Timestamp filed) {
		this.filed = filed;
	}
	public Integer getAssigned() {
		return assigned;
	}
	public void setAssigned(Integer assigned) {
		this.assigned = assigned;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getBugType() {
		return bugType;
	}

	public void setBugType(String bugType) {
		this.bugType = bugType;
	}

	public Integer getStatusId() {
		return statusId;
	}
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	public Integer getFixedBy() {
		return fixedBy;
	}
	public void setFixedBy(Integer fixedBy) {
		this.fixedBy = fixedBy;
	}
	public String getFixedVer() {
		return fixedVer;
	}
	public void setFixedVer(String fixedVer) {
		this.fixedVer = fixedVer;
	}
	public Integer getSeverityId() {
		return severityId;
	}
	public void setSeverityId(Integer severityId) {
		this.severityId = severityId;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getComponentId() {
		return componentId;
	}
	public void setComponentId(Integer componentId) {
		this.componentId = componentId;
	}
	public Integer getOsId() {
		return osId;
	}
	public void setOsId(Integer osId) {
		this.osId = osId;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	
	
	
}
