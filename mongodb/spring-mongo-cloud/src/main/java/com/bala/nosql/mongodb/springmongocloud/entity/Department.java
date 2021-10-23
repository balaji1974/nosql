package com.bala.nosql.mongodb.springmongocloud.entity;

import org.springframework.data.mongodb.core.mapping.Field;

public class Department {
	
	@Field("department_name")
	private String departmentName;
	
	@Field("location_id")
	private String locationId;
	
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	
	
	
}
