package com.bala.nosql.mongodb.springmongocloud.entity;

import org.springframework.data.mongodb.core.mapping.Field;

public class Subject {
	
	@Field("subject_id")
	private String subjectId;
	
	@Field("subject_name")
	private String subjectName;
	
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	
}
